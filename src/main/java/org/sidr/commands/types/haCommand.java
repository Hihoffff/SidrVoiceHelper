package org.sidr.commands.types;

import org.sidr.Sidr;
import org.sidr.commands.CommandHandler;
import org.sidr.storage.Storage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class haCommand implements CommandHandler {
    private static String mode;
    private static String requestID;
    private static String entityID;
    private static Sidr sidr;
    private static boolean isLoadedCorrectly;

    public haCommand(Sidr sidr, Storage storage){
        haCommand.sidr = sidr;
        isLoadedCorrectly = load(storage);
    }

    @Override
    public void launch() throws IOException {
        if(mode.equals("get")){
            sidr.getCommandManager().setAnswer(sidr.getHomeAssistantManager().getDeviceInfo(entityID));
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

        return true;
    }
}
