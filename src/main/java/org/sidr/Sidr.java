package org.sidr;

import org.sidr.homeassistant.HomeAssistantManager;
import org.sidr.properties.PropertiesManager;
import org.sidr.threads.VoiceRecognition;
import org.vosk.Model;

import javax.sound.sampled.*;
import java.io.IOException;

public class Sidr {
    private CommandManager commandManager;
    private PropertiesManager propertiesManager;
    private HomeAssistantManager homeAssistantManager;
    private final String name;
    public Sidr(String modelPath,String callName) throws IOException, LineUnavailableException {
        Model model = new Model(modelPath);
        this.name = callName;
        System.out.println("Языковая модель загружена.");
        loadClasses();

        VoiceRecognition voiceRecognition = new VoiceRecognition(this,model);
        Thread thread = new Thread(voiceRecognition);
        thread.start();
        System.out.println("Сидр запущен.");


    }
    private void loadClasses(){
        System.out.println("Загрузка классов...");
        this.commandManager = new CommandManager(this);
        this.propertiesManager = new PropertiesManager();
        this.homeAssistantManager = new HomeAssistantManager(this);
        System.out.println("Загрузка классов прошла успешно!");
    }
    public CommandManager getCommandManager(){
        return this.commandManager;
    }

    public String getName() {
        return this.name;
    }

    public PropertiesManager getPropertiesManager() {
        return this.propertiesManager;
    }

    public HomeAssistantManager getHomeAssistantManager() {
        return homeAssistantManager;
    }
}
