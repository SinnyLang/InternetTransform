package com.main.cmdable;

import java.io.IOException;

public interface Sendable {
    byte getCommandType();
    void doTask() throws IOException, SendFileAlreadyExistException;
}
