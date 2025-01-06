package org.sidr;

public class CommandManager {
    private final Sidr sidr;
    public CommandManager(Sidr sidr){
        this.sidr = sidr;
    }
    public void onText(String text){
        System.out.println("текст:" + text);

    }
}
