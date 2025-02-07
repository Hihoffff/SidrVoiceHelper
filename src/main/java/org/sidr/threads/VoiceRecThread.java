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
    private boolean wakeUp = false;
    private long startTime=System.nanoTime();
    private boolean isSpeaking = false;

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
                    Thread.sleep(5000); //делает перерыв дабы система успела захватить микрофон
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                continue;
            }
            if(!wakeUp){ //запускает модель для распознования ключевого слова
                if(isPicoVoiceDetected()){
                    wakeUp = true;
                    startTime = System.nanoTime();
                }
            }

            if(wakeUp){    //запускает модель для обширного распознования слов
                if(isVoskWaveAccepted()){
                    wakeUp = false;
                }
            }

            if(wakeUp && isSpeaking && ((System.nanoTime() - startTime)>10000)){ //если человек будет говорить дольше 10 секунд ,то останавливаем распознование
                wakeUp = false;
                isSpeaking = false;
            }

            if (wakeUp && !isSpeaking && ((System.nanoTime()-startTime)>5000)) { //если молчит больше 5 секунд ,то останаливаем распознование
                wakeUp=false;
            }
            try {
                Thread.sleep(1); //ограничиваем бессконеный цикл
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private boolean isVoskWaveAccepted(){
        String text;
        try {
            int bytesRead = microphone.read(buffer, 0, buffer.length);

            if (sidr.getVoskManager().getRecognizer().acceptWaveForm(buffer, bytesRead)) {
                text = sidr.getVoskManager().getRecognizer().getResult();
                text = convertJsonToText(text);
                if(!text.isEmpty()){
                    sidr.getCommandManager().onText(text);
                    sidr.setWakeUp(false);
                    isSpeaking = false;
                    return true;
                }
            }
            else{
                String partResult = convertJsonToText(sidr.getVoskManager().getRecognizer().getPartialResult());
                if(!partResult.isEmpty()){
                    if(!isSpeaking){isSpeaking=true;}
                    System.out.println(partResult);
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
    private String convertJsonToText(String text){
        int startIndex=0;
        int endIndex=0;
        startIndex = text.indexOf(":") + 3;  // После символа ": и пробела
        endIndex = text.lastIndexOf("\"");
        text = text.substring(startIndex, endIndex);
        return text;
    }

}
