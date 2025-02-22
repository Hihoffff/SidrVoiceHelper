package org.sidr.threads;

import org.sidr.Sidr;

public class ThreadsManager {
    private final Sidr sidr;
    //private Thread VoskThread;
    //private Thread PicovoiceThread;
    private final VoiceRecThread voiceRecThreadManager;
    private Thread voiceRecThread;
    public ThreadsManager(Sidr sidr){
        this.sidr = sidr;
        this.voiceRecThreadManager = new VoiceRecThread(sidr,sidr.getLoadMicrophone().getMyMicrophone());
    }
    public void startVoiceThread(){
        System.out.println("Starting Voice threads...");
        voiceRecThread = new Thread(voiceRecThreadManager);
        voiceRecThread.start();
    }

    public VoiceRecThread getVoiceRecThread() {
        return voiceRecThreadManager;
    }
}
