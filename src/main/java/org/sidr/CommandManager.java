package org.sidr;

import java.io.IOException;

public class CommandManager {
    private final Sidr sidr;
    private final String name;
    public CommandManager(Sidr sidr){
        this.sidr = sidr;
        name = sidr.getName();
    }
    public void onText(String text) throws IOException {
        System.out.println("текст:" + text);
        if(text.equals("температура")){
            System.out.println(sidr.getHomeAssistantManager().getDeviceInfo("sensor.0xa4c138b8710c2145_temperature"));
        } else if (text.equals("переключи")) {
           sidr.getHomeAssistantManager().sendRequest("input_boolean.test","input_boolean/toggle");
        }
        else if(text.equals("ютуб")){
            System.out.println(sidr.getHomeAssistantManager().getDeviceInfo("sensor.marmok_subscribers"));
        }
        if(text.contains(name)){
            
        }
    }
}
