package org.sidr;


import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.*;
import java.io.IOException;

public class Main {
    private static Sidr sidr;
    public static void main(String[] args) throws LineUnavailableException, IOException {
        System.out.println("Сидр, запуск.");

        sidr = new Sidr("D:\\Projects Java\\SidrVoiceHelper\\vosk-model-small-ru-0.22", "сидр");
    }

}
