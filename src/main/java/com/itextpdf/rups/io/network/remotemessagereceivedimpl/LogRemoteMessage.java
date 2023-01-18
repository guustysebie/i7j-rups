package com.itextpdf.rups.io.network.remotemessagereceivedimpl;

import com.itextpdf.rups.io.network.IRemoteMessageReceived;
import com.itextpdf.rups.model.LoggerHelper;

public class LogRemoteMessage implements IRemoteMessageReceived {
    @Override
    public void messageReceived(String message) {
        LoggerHelper.info("Message received from remote: <" + message + ">", LogRemoteMessage.class);
    }
}
