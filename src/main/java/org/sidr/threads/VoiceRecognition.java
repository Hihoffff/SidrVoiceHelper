package org.sidr.threads;

import org.sidr.Sidr;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.*;
import java.io.IOException;

public class VoiceRecognition implements Runnable {
    private final Model model;
    private final Sidr sidr;
    private boolean workFlag = true;
    public VoiceRecognition(Sidr sidr,Model model){
        this.model = model;
        this.sidr = sidr;
    }
    public void setWorkFlag(boolean isWork){
        this.workFlag = isWork;
    }
    @Override
    public void run() {
        System.out.println("Запуск потока под распознования голоса...");
        try {
            AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);

            microphone.open(format);
            microphone.start();
            System.out.println("Микрофон настроен: "+info.toString());

            // Инициализация распознавателя
            Recognizer recognizer = new Recognizer(model, 16000);

            byte[] buffer = new byte[4096];


            int startIndex=0;
            int endIndex=0;
            String text;

            while (workFlag) {
                int bytesRead = microphone.read(buffer, 0, buffer.length);

                if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                        text = recognizer.getResult();
                        startIndex = text.indexOf(":") + 3;  // После символа ": и пробела
                        endIndex = text.lastIndexOf("\"");
                        text = text.substring(startIndex, endIndex);
                        if(!text.isEmpty()){
                            sidr.getCommandManager().onText(text);
                        }
                }
                else{
                    /*text = recognizer.getResult();
                    startIndex = text.indexOf(":") + 3;  // После символа ": и пробела
                    endIndex = text.lastIndexOf("\"");
                    text = text.substring(startIndex, endIndex);
                    if(!text.isEmpty()){
                        sidr.getCommandManager().onText(text);
                    }*/
                }
            }
        } catch (LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }


    }
}
