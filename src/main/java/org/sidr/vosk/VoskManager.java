package org.sidr.vosk;

import org.sidr.Sidr;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.*;
import java.io.IOException;

public class VoskManager implements Runnable {
    private final Sidr sidr;
    private Recognizer recognizer;
    private final byte[] buffer = new byte[4096];
    private final TargetDataLine microphone;


    public VoskManager(Sidr sidr, TargetDataLine microphone){
        this.sidr = sidr;
        this.microphone = microphone;
    }

    public void load() throws IOException {
        System.out.println("Loading Vosk model...");
        Model model = new Model(sidr.getPropertiesManager().getVoskModelPath());
        System.out.println("Vosk model was loaded!");
        System.out.println("Loading recognizer Vosk...");
        recognizer = new Recognizer(model, 16000);
        System.out.println("Recognizer Vosk was loaded!");
    }
    @Override
    public void run() {
        System.out.println("Starting thread for vosk...");
        if(microphone == null){
            System.err.println("Error with microphone loading!");
            return;
        }
        int startIndex=0;
        int endIndex=0;
        String text;
        while(true){
            while (sidr.isWakeUp()) {
                try {
                    int bytesRead = microphone.read(buffer, 0, buffer.length);

                    if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                        text = recognizer.getResult();
                        startIndex = text.indexOf(":") + 3;  // После символа ": и пробела
                        endIndex = text.lastIndexOf("\"");
                        text = text.substring(startIndex, endIndex);
                        if(!text.isEmpty()){
                            sidr.getCommandManager().onText(text);
                            sidr.setWakeUp(false);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
