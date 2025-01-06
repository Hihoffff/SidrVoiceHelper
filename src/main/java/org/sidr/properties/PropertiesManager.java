package org.sidr.properties;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager {
    private String ipHA;
    private String localIpHA;
    private String portHA;
    private String tokenHA;
    public PropertiesManager(){
        Properties properties = new Properties();
        try (InputStream input = this.getClass().getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);

            ipHA = properties.getProperty("homeassistant.ip");
            localIpHA = properties.getProperty("homeassistant.ip.local");
            portHA = properties.getProperty("homeassistant.port");
            tokenHA = properties.getProperty("homeassistant.token");

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
}
