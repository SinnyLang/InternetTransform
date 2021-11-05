package com.main.cmdable;

import java.io.IOException;

public class SendFileAlreadyExistException extends IOException {
    public SendFileAlreadyExistException(String s) {
        super(s);
    }
}
