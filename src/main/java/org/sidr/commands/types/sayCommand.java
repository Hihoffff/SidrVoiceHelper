package org.sidr.commands.types;

import org.sidr.Sidr;
import org.sidr.storage.Storage;
import org.sidr.commands.CommandHandler;

import java.util.List;


public class sayCommand implements CommandHandler {
    private static Sidr sidr;
    private static String mode;
    private static List<String> sayWords;
    public sayCommand(Sidr sidr, Storage storage){
        sayCommand.sidr = sidr;
        load(storage);
    }
    @Override
    public void launch() {
        if(mode.equals("say")){
            int size = sayWords.size();
            if(size == 1){
                System.out.println(sayWords.get(0));
            }
            else if(size > 1){
                System.out.println(sayWords.get(sidr.getRandom().nextInt(size)));
            }
        }
        else if(mode.equals("audio")){

        }
    }
    private void load(Storage storage){

    }
}
