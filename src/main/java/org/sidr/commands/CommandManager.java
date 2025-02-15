package org.sidr.commands;

import org.jetbrains.annotations.Nullable;
import org.sidr.Sidr;
import org.sidr.commands.types.haCommand;
import org.sidr.commands.types.sayCommand;
import org.sidr.storage.Storage;
import org.sidr.tools.SidrUtils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandManager {
    private final Sidr sidr;
    private String answer = null;
    private List<Command> commands = new ArrayList<>();
    public CommandManager(Sidr sidr){
        this.sidr = sidr;
    }
    public void onText(String text) throws IOException {
        System.out.println("текст:" + text);
        if (text.equals("температура")){
            System.out.println(sidr.getHomeAssistantManager().getDeviceInfo("sensor.0xa4c138b8710c2145_temperature"));
        } else if (text.equals("переключи")) {
           sidr.getHomeAssistantManager().sendRequest("input_boolean.test","input_boolean/toggle");
        }
        else if(text.equals("ютуб")){
            System.out.println(sidr.getHomeAssistantManager().getDeviceInfo("sensor.marmok_latest_upload"));
        }



    }

    @Nullable
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void loadCommands(){
        System.out.println("Commands loading...");
        List<String> filesList =  SidrUtils.getFileNames(sidr.getPropertiesManager().getCommandsPathName());
        if(filesList.isEmpty()){
            System.err.println("Commands are empty!!!!");
        }
        else{
            for(String fileName : filesList){
                if(!fileName.endsWith(".yml")){continue;}
                System.out.println(fileName);
                Storage storage = new Storage(sidr.getPropertiesManager().getCommandsPathName(),fileName);

                Map<String, Object> info = (Map<String, Object>) storage.getObjects().get("info");
                if(info == null || info.isEmpty()){System.err.println("ERROR with loading of command "+fileName+ "!  (info error)"); continue;}

                String type = (String) info.get("type");
                if(type == null){System.err.println("ERROR with loading of command "+fileName+ "! (type error)"); continue;}

                List<String> keyWords = (List<String>) info.get("keywords");
                if(keyWords == null || keyWords.isEmpty()){System.err.println("ERROR with loading of command "+fileName+ "! (type error)"); continue;}

                CommandHandler commandHandler=null;

                if(type.equals("say")){
                    commandHandler = new sayCommand(sidr,storage);
                }
                else if(type.equals("ha")){
                    commandHandler = new haCommand(sidr,storage);
                }

                if(commandHandler==null){System.err.println("ERROR with commandHandler type choosing! - " + fileName); continue;}
                Command command = new Command(type,keyWords,commandHandler);

                commands.add(command);

            }
        }
        System.out.println("Commands were loaded!");
    }
}
