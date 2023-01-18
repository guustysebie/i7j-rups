package com.itextpdf.rups.io.cli;


import com.itextpdf.kernel.actions.data.ITextCoreProductData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RupsCliArguments {

    private final String[] args;

    private final Set<String> flags = new HashSet<>();
    private final List<String> files = new ArrayList<>();

    public RupsCliArguments(String[] args) {
        this.args = args;
        this.parse();
    }

    private void parse() {
        if (this.args == null){
            return;
        }
        ArgumentTokenizer tokenizer = new ArgumentTokenizer(args);
        while (tokenizer.hasNext()) {
            String token = tokenizer.next();
            if (token == null || token.isEmpty()) {
                continue;
            }
            switch (token) {
                case "-a":
                case "--attach":
                    flags.add("attach");
                    break;
                case "-h":
                case "--help":
                    flags.add("help");
                    break;
                case "-v":
                case "--version":
                    flags.add("version");
                    break;
                default:
                    addPdfFile(token);
                    break;
            }
        }
    }

    public void printHelp() {
        System.out.println("Usage: rups [options] [files]");
        System.out.println("Options:");
        System.out.println("  -a, --attach\t\tSends the command to the running instance of RUPS.\n\t\t\tIf no instance is running, a new instance will be started.");
        System.out.println("  -h, --help\t\tPrints this help message.");
        System.out.println("  -v, --version\t\tPrints the version of RUPS.");
        System.out.println();
        printCopyRightInformation();
    }

    public void printVersion() {
        printCopyRightInformation();
    }

    private void printCopyRightInformation() {
        System.out.println(ITextCoreProductData.getInstance().getPublicProductName() +  " version: " + ITextCoreProductData.getInstance().getVersion());
        System.out.println("Copyright (c) " + ITextCoreProductData.getInstance().getSinceCopyrightYear() + " - " + ITextCoreProductData.getInstance().getToCopyrightYear() + " iText Group NV");

    }


    public boolean attachToRunningInstance() {
        return flags.contains("attach");
    }

    public boolean shouldPrintHelp() {
        return flags.contains("help");
    }

    public boolean shouldPrintVersion() {
        return flags.contains("version");
    }

    public List<String> getPdfFiles() {
        return files;
    }

    private void addPdfFile(String file) {
        if (file == null) {
            return;
        }
        files.add(file);
    }

}


