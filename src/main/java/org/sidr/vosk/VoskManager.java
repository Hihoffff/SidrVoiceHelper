package org.sidr.vosk;

import org.sidr.Sidr;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.*;
import java.io.IOException;

public class VoskManager {
    private final Sidr sidr;
    private Recognizer recognizer;
    private final byte[] buffer = new byte[4096];

    private final TargetDataLine microphone;


    public VoskManager(Sidr sidr, TargetDataLine microphone){
        this.sidr = sidr;
        this.microphone = microphone;
    }

    public void load() throws IOException {

        Model model;
        try {
            System.out.println("Loading Vosk model...");
            model = new Model(sidr.getPropertiesManager().getVoskModelPath());
            System.out.println("Vosk model was loaded!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Loading recognizer Vosk...");
        recognizer = new Recognizer(model, 16000);
        System.out.println("Recognizer Vosk was loaded!");
    }

    public Recognizer getRecognizer() {
        return recognizer;
    }

}
