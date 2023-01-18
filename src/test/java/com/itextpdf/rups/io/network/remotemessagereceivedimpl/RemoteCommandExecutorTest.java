package com.itextpdf.rups.io.network.remotemessagereceivedimpl;

import com.itextpdf.rups.controller.IRupsController;
import com.itextpdf.rups.mock.MockedRupsController;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class RemoteCommandExecutorTest {

    @Test
    public void messageReceived() {
        MockedRupsController rupsController = new MockedRupsController();
        RemoteCommandExecutor remoteCommandExecutor = new RemoteCommandExecutor(rupsController);
        remoteCommandExecutor.messageReceived("\"test.pdf\"");
        Assert.assertEquals(1, rupsController.getOpenedCount());
    }

    @Test
    public void twoMessageReceived() {
        MockedRupsController rupsController = new MockedRupsController();
        RemoteCommandExecutor remoteCommandExecutor = new RemoteCommandExecutor(rupsController);
        remoteCommandExecutor.messageReceived("\"test.pdf\" \"test2.pdf\"");
        Assert.assertEquals(2, rupsController.getOpenedCount());
    }
    @Test
    public void messageWithSpacesInFilePath() {
        MockedRupsController rupsController = new MockedRupsController();
        RemoteCommandExecutor remoteCommandExecutor = new RemoteCommandExecutor(rupsController);
        remoteCommandExecutor.messageReceived("\"frank test.pdf\" \"test2.pdf\"");
        Assert.assertEquals(2, rupsController.getOpenedCount());
    }

    @Test
    public void messageWithFlagReceived() {
        MockedRupsController rupsController = new MockedRupsController();
        RemoteCommandExecutor remoteCommandExecutor = new RemoteCommandExecutor(rupsController);
        remoteCommandExecutor.messageReceived("\"test.pdf\" -p ");
        Assert.assertEquals(2, rupsController.getOpenedCount());

    }

}