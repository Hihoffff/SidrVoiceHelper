package org.sidr.commands;

import java.io.IOException;

public interface CommandHandler {
    void launch() throws IOException;
    boolean isLoaded();
}
