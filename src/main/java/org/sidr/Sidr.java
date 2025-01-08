package org.sidr;

import ai.picovoice.picovoice.Picovoice;
import org.sidr.homeassistant.HomeAssistantManager;
import org.sidr.properties.PropertiesManager;
import org.sidr.threads.VoiceRecognition;
import org.vosk.Model;
import ai.picovoice.porcupine.*;

import javax.sound.sampled.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Sidr {
    private CommandManager commandManager;
    private PropertiesManager propertiesManager;
    private HomeAssistantManager homeAssistantManager;
    public Sidr(String modelPath) throws IOException, LineUnavailableException {
        Model model = new Model(modelPath);
        System.out.println("Языковая модель загружена.");
        loadClasses();

        VoiceRecognition voiceRecognition = new VoiceRecognition(this,model);
        Thread thread = new Thread(voiceRecognition);
        //thread.start();
        wakeWord();

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


    public PropertiesManager getPropertiesManager() {
        return this.propertiesManager;
    }

    public HomeAssistantManager getHomeAssistantManager() {
        return homeAssistantManager;
    }


    public void wakeWord() throws LineUnavailableException {

        try {
            // Инициализация Porcupine
            Porcupine picovoice = new Porcupine.Builder()
                    .setKeywordPath(getPropertiesManager().getWakeNamePicoVoicePath())
                    .setModelPath(getPropertiesManager().getPorcupineNamePicoVoicePath())
                    .setAccessKey(getPropertiesManager().getTokenPicoVoice())
                    .build();


            AudioFormat format = new AudioFormat(16000f, 16, 1, true, false); // Формат PCM 16-бит моно

            // Открываем микрофон
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                System.err.println("Микрофон не поддерживает требуемый формат.");
                return;
            }
            TargetDataLine microphone;
            try {
                microphone = (TargetDataLine) AudioSystem.getLine(info);
                microphone.open(format);
            } catch (LineUnavailableException e) {
                System.err.println("Ошибка при захвате микрофона.");
                return;
            }


            microphone.start();

            System.out.println("Жду ключевое слово...");

            int count=0;
            short[] picoVoiceBuffer = new short[picovoice.getFrameLength()];
            ByteBuffer captureBuffer = ByteBuffer.allocate(picovoice.getFrameLength() * 2);
            captureBuffer.order(ByteOrder.LITTLE_ENDIAN);

            int numBytesRead;
            boolean isWakeUp= false;
            while (!isWakeUp) {
                numBytesRead = microphone.read(captureBuffer.array(), 0, captureBuffer.capacity());
                if (numBytesRead != picovoice.getFrameLength() * 2) {
                    continue;
                }
                captureBuffer.asShortBuffer().get(picoVoiceBuffer);
                int detected = picovoice.process(picoVoiceBuffer);

                if(detected==0){
                    System.out.println("Ключевое слово распознано!"+count);
                    count++;
                }
            }

            // Освобождение ресурсов
            microphone.stop();
            microphone.close();
            //picovoice.delete();

        } catch (PorcupineException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
