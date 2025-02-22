package org.sidr.properties;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;

public class PropertiesManager {
    private final String jarPath;
    private final String ipHA;
    private final String localIpHA;
    private final String portHA;
    private final String tokenHA;
    private final String tokenPicoVoice; //токен для пиковойса https://console.picovoice.ai
    private final String porcupineNamePicoVoice; //путь модели пиковойса для распознования ключевого слова
    private final String wakeNamePicoVoice; //путь к модели распознования слова
    private final String voskModelPath;
    private final String commandsPathName;
    private final String jsonRootPlaceholder;
    public PropertiesManager() throws UnsupportedEncodingException {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedPath = URLDecoder.decode(path, "UTF-8");
        File file = new File(decodedPath);
        this.jarPath = file.getParent() + File.separator;
        System.out.println("Jar file path: "+ jarPath);

        Properties properties = new Properties();
        try (InputStream input = this.getClass().getClassLoader().getResourceAsStream("configs/config.properties")) {
            properties.load(input);

            ipHA = properties.getProperty("homeassistant.ip");
            localIpHA = properties.getProperty("homeassistant.ip.local");
            portHA = properties.getProperty("homeassistant.port");
            tokenHA = properties.getProperty("homeassistant.token");
            tokenPicoVoice = properties.getProperty("picovoice.token");
            porcupineNamePicoVoice = jarPath+properties.getProperty("picovoice.porcupine.name").replace("\\",File.separator);
            wakeNamePicoVoice = jarPath+properties.getProperty("picovoice.wakeName.name").replace("\\",File.separator);
            voskModelPath = jarPath+properties.getProperty("vosk.model.path").replace("\\",File.separator);
            commandsPathName = jarPath+properties.getProperty("commands.path.name").replace("\\",File.separator);
            jsonRootPlaceholder = properties.getProperty("json.root.placeholder");


        } catch (IOException e) {
            throw new RuntimeException(e);
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

    public String getJarPath() {
        return jarPath;
    }

    public String getVoskModelPath() {
        return voskModelPath;
    }

    public String getCommandsPathName() {
        return commandsPathName;
    }

    public String getJsonRootPlaceholder() {
        return jsonRootPlaceholder;
    }
}
