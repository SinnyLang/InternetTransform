package com.main;

public class LineProcessor {
    private String line;
    public LineProcessor(String line) {
        this.line = line;
    }

    public String[] getToken() {
        return line.split(" ");
    }
}
