package org.sidr.properties;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager {
    private String ipHA;
    private String localIpHA;
    private String portHA;
    private String tokenHA;
    private String tokenPicoVoice; //токен для пиковойса https://console.picovoice.ai
    private String porcupineNamePicoVoice; //путь модели пиковойса для распознования ключевого слова
    private String wakeNamePicoVoice; //путь к модели распознования слова
    public PropertiesManager(){
        Properties properties = new Properties();
        try (InputStream input = this.getClass().getClassLoader().getResourceAsStream("configs/config.properties")) {
            properties.load(input);

            ipHA = properties.getProperty("homeassistant.ip");
            localIpHA = properties.getProperty("homeassistant.ip.local");
            portHA = properties.getProperty("homeassistant.port");
            tokenHA = properties.getProperty("homeassistant.token");
            tokenPicoVoice = properties.getProperty("picovoice.token");
            porcupineNamePicoVoice = this.getClass().getClassLoader().getResource(properties.getProperty("picovoice.porcupine.name")).getPath().replace("%20", " ").replaceFirst("/","");
            wakeNamePicoVoice = this.getClass().getClassLoader().getResource(properties.getProperty("picovoice.wakeName.name")).getPath().replace("%20", " ").replaceFirst("/","");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getIpHA() {
        return ipHA;
    }

    public String getLocalIpHA() {
        return localIpHA;
    }

    public String getPortHA() {
        return portHA;
    }

    public String getTokenHA() {
        return tokenHA;
    }

    public String getTokenPicoVoice() {
        return tokenPicoVoice;
    }

    public String getPorcupineNamePicoVoicePath() {
        return porcupineNamePicoVoice;
    }

    public String getWakeNamePicoVoicePath() {
        return wakeNamePicoVoice;
    }

}
