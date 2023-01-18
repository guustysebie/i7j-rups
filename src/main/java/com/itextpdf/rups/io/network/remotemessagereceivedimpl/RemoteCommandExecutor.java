package com.itextpdf.rups.io.network.remotemessagereceivedimpl;


import com.itextpdf.rups.controller.IRupsController;
import com.itextpdf.rups.io.cli.RupsCliArguments;
import com.itextpdf.rups.io.network.IRemoteMessageReceived;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RemoteCommandExecutor implements IRemoteMessageReceived {

    private final IRupsController rupsController;

    public RemoteCommandExecutor(IRupsController rupsController) {
        this.rupsController = rupsController;
    }

    @Override
    public void messageReceived(String message) {

        //split this message into an array of strings
        // each parameter in between double quotes and a space
        // will be a separate string
        List<String> args = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (c == '"') {
                if (inQuotes){
                    args.add(sb.toString());
                    sb = new StringBuilder();
                }
                inQuotes = !inQuotes;
            } else if ((c == ' ' && !inQuotes) ) {
                args.add(sb.toString());
                sb = new StringBuilder();
            } else {
                sb.append(c);
            }
        }
        System.out.println(args);

        RupsCliArguments arguments = new RupsCliArguments(args.toArray(new String[0]));
        for (String pdfFile : arguments.getPdfFiles()) {
            rupsController.openNewFile(new File(pdfFile));
        }
    }
}









