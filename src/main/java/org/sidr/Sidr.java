package org.sidr;


import org.sidr.homeassistant.HomeAssistantManager;
import org.sidr.microphone.LoadMicrophone;
import org.sidr.picovoice.PicovoiceManager;
import org.sidr.properties.PropertiesManager;
import org.sidr.threads.ThreadsManager;
import org.sidr.vosk.VoskManager;

import ai.picovoice.porcupine.*;


import java.io.IOException;



public class Sidr {
    private CommandManager commandManager;
    private PropertiesManager propertiesManager;
    private HomeAssistantManager homeAssistantManager;
    LoadMicrophone loadMicrophone;
    VoskManager voskManager;
    private PicovoiceManager picovoiceManager;
    private ThreadsManager threadsManager;

    private boolean WakeUp;

    public Sidr() throws IOException, PorcupineException {
        loadClasses();
        System.out.println("Запуск потоков...");
        getThreadsManager().startVoiceThread();
    }
    private void loadClasses() throws IOException, PorcupineException {
        System.out.println("Загрузка классов...");
        this.loadMicrophone = new LoadMicrophone();
        this.commandManager = new CommandManager(this);
        this.propertiesManager = new PropertiesManager();
        this.homeAssistantManager = new HomeAssistantManager(this);
        this.picovoiceManager = new PicovoiceManager(this, getLoadMicrophone().getMyMicrophone());
        getPicovoiceManager().load();
        this.voskManager = new VoskManager(this, getLoadMicrophone().getMyMicrophone());
        getVoskManager().load();
        this.threadsManager = new ThreadsManager(this);

        System.out.println("Загрузка классов прошла успешно!");
    }
    public CommandManager getCommandManager(){
        return this.commandManager;
    }


    public PropertiesManager getPropertiesManager() {
        return this.propertiesManager;
    }

    public HomeAssistantManager getHomeAssistantManager() {
        return homeAssistantManager;
    }

    public LoadMicrophone getLoadMicrophone() {
        return loadMicrophone;
    }

    public PicovoiceManager getPicovoiceManager() {
        return picovoiceManager;
    }

    public VoskManager getVoskManager() {
        return voskManager;
    }

    public ThreadsManager getThreadsManager() {
        return threadsManager;
    }

    public boolean isWakeUp() {
        return WakeUp;
    }
    public void setWakeUp(boolean isWakeUp){
        this.WakeUp = isWakeUp;
    }
}
