package org.sidr;

import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.*;
import java.io.IOException;

public class Sidr {
    private CommandManager commandManager;
    private final String name;
    public Sidr(String modelPath,String callName) throws IOException, LineUnavailableException {
        Model model = new Model(modelPath);
        this.name = callName;
        System.out.println("Языковая модель загружена.");
        loadClasses();
        // Настройка аудио формата
        AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);
        microphone.open(format);
        microphone.start();
        System.out.println("Микрофон настроен: "+info.toString());

        // Инициализация распознавателя
        Recognizer recognizer = new Recognizer(model, 16000);

        byte[] buffer = new byte[4096];
        System.out.println("Сидр запущен!");

        int startIndex=0;
        int endIndex=0;
        String text;

        while (true) {
            int bytesRead = microphone.read(buffer, 0, buffer.length);

            if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                text = recognizer.getResult();
                startIndex = text.indexOf(":") + 3;  // После символа ": и пробела
                endIndex = text.lastIndexOf("\"");
                text = text.substring(startIndex, endIndex);
                if(!text.isEmpty()){
                    getCommandManager().onText(text);
                }
            }
        }

    }
    private void loadClasses(){
        System.out.println("Загрузка классов...");
        this.commandManager = new CommandManager(this);
        System.out.println("Загрузка классов прошла успешно!");
    }
    public CommandManager getCommandManager(){
        return this.commandManager;
    }

    public String getName() {
        return this.name;
    }
}
