package org.sidr;


import org.sidr.homeassistant.HomeAssistantManager;
import org.sidr.microphone.LoadMicrophone;
import org.sidr.picovoice.PicovoiceManager;
import org.sidr.properties.PropertiesManager;
import org.sidr.threads.VoiceRecognition;

import ai.picovoice.porcupine.*;


import java.io.IOException;



public class Sidr {
    private CommandManager commandManager;
    private PropertiesManager propertiesManager;
    private HomeAssistantManager homeAssistantManager;
    LoadMicrophone loadMicrophone;
    VoiceRecognition voiceRecognition;
    private PicovoiceManager picovoiceManager;
    public Sidr() throws IOException, PorcupineException {
        loadClasses();

        System.out.println("Языковая модель загружена.");



        //загрузка микрофона

        System.out.println("Жду ключевое слово...");
        getPicovoiceManager().start();
        Thread thread = new Thread(voiceRecognition);
        thread.start();






    }
    private void loadClasses() throws IOException, PorcupineException {
        System.out.println("Загрузка классов...");
        this.loadMicrophone = new LoadMicrophone();
        this.commandManager = new CommandManager(this);
        this.propertiesManager = new PropertiesManager();
        this.homeAssistantManager = new HomeAssistantManager(this);
        this.picovoiceManager = new PicovoiceManager(this, getLoadMicrophone().getMyMicrophone());
        getPicovoiceManager().load();
        this.voiceRecognition = new VoiceRecognition(this, getLoadMicrophone().getMyMicrophone());
        getVoiceRecognition().load();
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

    public VoiceRecognition getVoiceRecognition() {
        return voiceRecognition;
    }
}
