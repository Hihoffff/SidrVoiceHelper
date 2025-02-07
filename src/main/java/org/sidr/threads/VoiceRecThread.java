package org.sidr.threads;

import org.sidr.Sidr;
import org.sidr.picovoice.PicovoiceManager;
import org.sidr.vosk.VoskManager;


import javax.sound.sampled.TargetDataLine;
import java.io.IOException;

public class VoiceRecThread implements Runnable{
    private final Sidr sidr;
    private final TargetDataLine microphone;
    private final byte[] buffer = new byte[4096];
    private PicovoiceManager picovoiceManager;
    private VoskManager voskManager;


    public VoiceRecThread(Sidr sidr, TargetDataLine microphone){
        this.sidr = sidr;
        this.microphone = microphone;
        this.picovoiceManager = sidr.getPicovoiceManager();
        this.voskManager = sidr.getVoskManager();
    }
    @Override
    public void run() {
        sidr.setWakeUp(false);
        while (true){
            if(microphone == null){
                System.err.println("Error with microphone loading!");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                continue;
            }
            if(!sidr.isWakeUp()){
                if(isPicoVoiceDetected()){
                    sidr.setWakeUp(true);
                }
            }

            if(sidr.isWakeUp()){
                if(isVoskWaveAccepted()){
                    sidr.setWakeUp(false);
                }
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private boolean isVoskWaveAccepted(){
        int startIndex=0;
        int endIndex=0;
        String text;
        try {
            int bytesRead = microphone.read(buffer, 0, buffer.length);

            if (sidr.getVoskManager().getRecognizer().acceptWaveForm(buffer, bytesRead)) {
                text = sidr.getVoskManager().getRecognizer().getResult();
                startIndex = text.indexOf(":") + 3;  // После символа ": и пробела
                endIndex = text.lastIndexOf("\"");
                text = text.substring(startIndex, endIndex);
                if(!text.isEmpty()){
                    sidr.getCommandManager().onText(text);
                    sidr.setWakeUp(false);
                    return true;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    private boolean isPicoVoiceDetected(){

        try{
            int numBytesRead = microphone.read(picovoiceManager.getCaptureBuffer().array(), 0, picovoiceManager.getCaptureBuffer().capacity());
            if (numBytesRead !=  picovoiceManager.getPicovoice().getFrameLength() * 2) {
                return false;
            }
            picovoiceManager.getCaptureBuffer().asShortBuffer().get(picovoiceManager.getPicoVoiceBuffer());
            int detected = picovoiceManager.getPicovoice().process(picovoiceManager.getPicoVoiceBuffer());

            if(detected==0){
                System.out.println("Ключевое слово распознано!");
                sidr.setWakeUp(true);
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

}
