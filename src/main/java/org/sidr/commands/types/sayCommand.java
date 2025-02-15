package org.sidr.commands.types;

import org.sidr.Sidr;
import org.sidr.storage.Storage;
import org.sidr.commands.CommandHandler;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class sayCommand implements CommandHandler {
    private static Sidr sidr;
    private String mode;
    private List<String> sayWords;
    private boolean isLoadedCorrectly;
    public sayCommand(Sidr sidr, Storage storage){
        sayCommand.sidr = sidr;
        isLoadedCorrectly = load(storage);
    }
    @Override
    public void launch() {
        if(mode.equals("say")){
            int size = sayWords.size();
            if(size == 1){
                System.out.println(replacePlaceHolders(sayWords.get(0)));
            }
            else if(size > 1){
                System.out.println(replacePlaceHolders(sayWords.get(sidr.getRandom().nextInt(size))));
            }
        }
        else if(mode.equals("audio")){

        }
    }

    @Override
    public boolean isLoaded() {
        return isLoadedCorrectly;
    }

    private boolean load(Storage storage){
        Map<String, Object> command = (Map<String, Object>) storage.getObjects().get("command");
        if(command == null || command.isEmpty()){System.err.println("ERROR with loading of command "+storage.getFILENAME()+ "!  (command error)"); return false;}

        mode = (String) command.get("mode");
        if(mode == null){System.err.println("ERROR with loading of command "+storage.getFILENAME()+ "! (mode error)"); return false;}

        sayWords = (List<String>) command.get("say");
        if(sayWords == null || sayWords.isEmpty()){System.err.println("ERROR with loading of command "+storage.getFILENAME()+ "! (say error)"); return false;}

        return true;
    }

    private String replacePlaceHolders(String txt){
        if(txt.contains("{") && txt.contains("}")){
            txt = txt.replace("{time}", LocalDateTime.now().getHour()+":"+LocalDateTime.now().getMinute());
        }
        return txt;
    }
}
