package org.sidr;


import org.sidr.commands.CommandManager;
import org.sidr.homeassistant.HomeAssistantManager;
import org.sidr.microphone.LoadMicrophone;
import org.sidr.picovoice.PicovoiceManager;
import org.sidr.properties.PropertiesManager;
import org.sidr.threads.ThreadsManager;
import org.sidr.tools.SidrUtils;
import org.sidr.vosk.VoskManager;

import ai.picovoice.porcupine.*;


import java.io.IOException;
import java.util.Random;


public class Sidr {
    private CommandManager commandManager;
    private PropertiesManager propertiesManager;
    private HomeAssistantManager homeAssistantManager;
    LoadMicrophone loadMicrophone;
    VoskManager voskManager;
    private PicovoiceManager picovoiceManager;
    private ThreadsManager threadsManager;
    private final Random random = new Random();
    private boolean WakeUp;

    public Sidr() throws IOException, PorcupineException {
        loadClasses();
        getThreadsManager().startVoiceThread();
        while (true) {
            try {
                Thread.sleep(10000); // Спим 10 секунду, чтобы не нагружать CPU
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void loadClasses() throws IOException, PorcupineException {
        System.out.println("Loading classes...");
        this.loadMicrophone = new LoadMicrophone();
        this.commandManager = new CommandManager(this);
        this.propertiesManager = new PropertiesManager();
        this.homeAssistantManager = new HomeAssistantManager(this);
        this.picovoiceManager = new PicovoiceManager(this, getLoadMicrophone().getMyMicrophone());
        getPicovoiceManager().load();
        this.voskManager = new VoskManager(this, getLoadMicrophone().getMyMicrophone());
        getVoskManager().load();
        this.threadsManager = new ThreadsManager(this);
        commandManager.loadCommands();
        System.out.println("Classes were loaded successfully!");
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

    public Random getRandom() {
        return random;
    }
}
