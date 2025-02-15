package org.sidr.commands;


import java.util.List;

public class Command {
    private final CommandHandler commandHandler;
    private final List<String> keyWords;
    private final String type;

    public Command(String type, List<String> keyWords ,CommandHandler commandHandler){
        this.type = type;
        this.commandHandler = commandHandler;
        this.keyWords = List.copyOf(keyWords);
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public List<String> getKeyWords() {
        return keyWords;
    }

    public String getType() {
        return type;
    }

}
