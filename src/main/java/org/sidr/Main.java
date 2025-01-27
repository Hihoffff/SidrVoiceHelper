package org.sidr;



import ai.picovoice.porcupine.PorcupineException;

import javax.sound.sampled.*;
import java.io.IOException;


public class Main {
    private static Sidr sidr;
    public static void main(String[] args) throws LineUnavailableException, IOException, PorcupineException {
        System.out.println("Сидр, запуск.");
        sidr = new Sidr();
    }

}
