package com.itextpdf.rups.io.cli;

public class ArgumentTokenizer {
    private final String[] args;
    private int index = 0;

    public ArgumentTokenizer(String[] args) {
        this.args = args;
    }

    public boolean hasNext() {
        return index < args.length;
    }

    public String next() {
        return args[index++];
    }

    public String peek() {
        return args[index];
    }

}
