package org.sidr;


import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.*;
import java.io.FileInputStream;
import java.io.IOException;

public class Sidr {
    public static void main(String[] args) throws IOException, LineUnavailableException {
        // Укажите путь к скачанной модели
        Model model = new Model("D:\\Projects Java\\SidrVoiceHelper\\vosk-model-small-ru-0.22"); // Замените "path_to_model" на путь к вашей модели

        // Настройка аудио формата
        AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);
        microphone.open(format);
        microphone.start();

        // Инициализация распознавателя
        Recognizer recognizer = new Recognizer(model, 16000);

        byte[] buffer = new byte[4096];
        System.out.println("Говорите...");


        while (true) {
            int bytesRead = microphone.read(buffer, 0, buffer.length);

            if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                System.out.println("Распознано: " + recognizer.getResult());


            } else {
                //System.out.println("Частичный результат: " + recognizer.getPartialResult());
            }
        }
    }

}
