package org.sidr.threads;

import org.sidr.Sidr;

public class ThreadsManager {
    private final Sidr sidr;
    private Thread VoskThread;
    private Thread PicovoiceThread;
    public ThreadsManager(Sidr sidr){
        this.sidr = sidr;
    }
    public void startVoiceThread(){
        PicovoiceThread = new Thread(sidr.getPicovoiceManager());
        VoskThread = new Thread(sidr.getVoskManager());
        PicovoiceThread.start();
        VoskThread.start();
    }
}
