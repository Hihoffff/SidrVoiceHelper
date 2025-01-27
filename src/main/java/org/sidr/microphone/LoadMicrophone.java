package org.sidr.microphone;

import javax.sound.sampled.*;

public class LoadMicrophone {
    private TargetDataLine myMicrophone = null;
    public LoadMicrophone(){
        myMicrophone = startMicrophone();
    }
    public TargetDataLine startMicrophone(){
        System.out.println("Запуск микрофона...");
        AudioFormat format = new AudioFormat(16000f, 16, 1, true, false); // Формат PCM 16-бит моно
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            System.err.println("Микрофон не поддерживает требуемый формат.");
            return null;
        }
        TargetDataLine microphone;
        try {
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
        } catch (LineUnavailableException e) {
            System.err.println("Ошибка при захвате микрофона.");
            return null;
        }
        microphone.start();
        System.out.println("Микрофон запущен!");
        return microphone;
    }
    public TargetDataLine getMyMicrophone(){
        if(myMicrophone == null){
            this.myMicrophone = startMicrophone();
        }
        return myMicrophone;
    }
    public void stopMicrophone(){
        myMicrophone.stop();
        myMicrophone.close();
        myMicrophone = null;
    }
}
