package org.sidr;



import ai.picovoice.porcupine.PorcupineException;

import java.io.IOException;


public class Main {
    private static Sidr sidr;
    public static void main(String[] args) throws IOException, PorcupineException {
        System.out.println("Sidr, starting...");
        sidr = new Sidr();
    }

}
