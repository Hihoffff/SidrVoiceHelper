package org.sidr.commands.types;

import org.sidr.Sidr;
import org.sidr.commands.CommandHandler;
import org.sidr.storage.Storage;

import java.io.IOException;

public class haCommand implements CommandHandler {
    private static String mode;
    private static String requestID;
    private static String entityID;
    private static Sidr sidr;

    public haCommand(Sidr sidr, Storage storage){
        haCommand.sidr = sidr;

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
    public boolean load(Storage storage){

        return true;
    }
}
