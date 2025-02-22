package org.sidr.commands.types;

import org.sidr.Sidr;
import org.sidr.commands.CommandHandler;
import org.sidr.storage.Storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class haCommand implements CommandHandler {
    private static String mode;
    private static String requestID;
    private static String entityID;
    private static Sidr sidr;
    private static boolean isLoadedCorrectly;
    private List<String> json_keys = new ArrayList<>();

    public haCommand(Sidr sidr, Storage storage){
        haCommand.sidr = sidr;
        isLoadedCorrectly = load(storage);
    }

    @Override
    public void launch() throws IOException {
        if(mode.equals("get")){
            String ha_answer= sidr.getHomeAssistantManager().getDeviceInfo(entityID);
            if(json_keys.isEmpty()){
                sidr.getCommandManager().setAnswer(ha_answer);
            }
            else{
                String answer = "";
                for(String key : json_keys){
                    if(key.contains(sidr.getPropertiesManager().getJsonRootPlaceholder())){

                    }
                }
            }

        }
        else if(mode.equals("send")){
            sidr.getHomeAssistantManager().sendRequest(entityID,requestID);
        }
    }

    @Override
    public boolean isLoaded() {
        return isLoadedCorrectly;
    }

    public boolean load(Storage storage){
        Map<String, Object> command = (Map<String, Object>) storage.getObjects().get("command");
        if(command == null || command.isEmpty()){System.err.println("ERROR with loading of command "+storage.getFILENAME()+ "!  (command error)"); return false;}

        mode = (String) command.get("mode");
        if(mode == null){System.err.println("ERROR with loading of command "+storage.getFILENAME()+ "! (mode error)"); return false;}

        entityID = (String) command.get("entityID");
        if(entityID == null){System.err.println("ERROR with loading of command "+storage.getFILENAME()+ "! (entityID error)"); return false;}

        if(mode.equals("send")){
            requestID = (String) command.get("requestID");
            if(requestID == null){System.err.println("ERROR with loading of command "+storage.getFILENAME()+ "! (requestID error)"); return false;}
        }
        if(mode.equals("get")){
            List<String> get_keys = (List<String>) command.get("get_keys");
            if(get_keys!=null && !get_keys.isEmpty()){
                json_keys.addAll(get_keys);
            }
        }

        return true;
    }
}
