package org.sidr.commands;

import java.util.List;

public class Command {
    private static CommandHandler commandHandler;
    private static List<String> keyWords;
    private static String type;

    public Command(String type, List<String> keyWords ,CommandHandler commandHandler){
        Command.type = type;
        Command.commandHandler = commandHandler;
        Command.keyWords = keyWords;
    }

    public static CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public static List<String> getKeyWords() {
        return keyWords;
    }

    public static String getType() {
        return type;
    }

}
