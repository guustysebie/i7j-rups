package com.itextpdf.rups.io.network;

import com.itextpdf.rups.io.cli.RupsCliArguments;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.*;

public class CommandServerTest {


    @Test
    public void startCommandServerSingleInstance() throws InterruptedException {
        CommandServer commandServer = new CommandServer();
        commandServer.start();
        // wait for server to start
        Thread.sleep(100);
        Assert.assertTrue(commandServer.isRunning());
        Assert.assertTrue(CommandServer.isThereAlreadyAnInstanceRunning());
        commandServer.stop();
        Assert.assertFalse(commandServer.isRunning());
        Assert.assertFalse(CommandServer.isThereAlreadyAnInstanceRunning());
    }



    @Test
    public void startCommandServerDoubleInstancesOnlyOneRunning() throws InterruptedException {
        CommandServer commandServer = new CommandServer();
        commandServer.start();
        Thread.sleep(1000);
        Assert.assertTrue(commandServer.isRunning());
        Assert.assertTrue(CommandServer.isThereAlreadyAnInstanceRunning());

        CommandServer commandServer2 = new CommandServer();
        commandServer2.start();
        Thread.sleep(100);
        Assert.assertFalse(commandServer2.isRunning());
        Assert.assertTrue(CommandServer.isThereAlreadyAnInstanceRunning());

        commandServer.stop();
        Assert.assertFalse(CommandServer.isThereAlreadyAnInstanceRunning());
    }

    @Test
    public void onStoppingServerOneServer2TakesOver() throws InterruptedException {
        CommandServer commandServer = new CommandServer();
        commandServer.start();
        Thread.sleep(100);
        Assert.assertTrue(commandServer.isRunning());
        Assert.assertTrue(CommandServer.isThereAlreadyAnInstanceRunning());
        CommandServer commandServer2 = new CommandServer();
        commandServer2.start();
        Thread.sleep(100);

        Assert.assertFalse(commandServer2.isRunning());
        Assert.assertTrue(CommandServer.isThereAlreadyAnInstanceRunning());

        commandServer.stop();
        Assert.assertFalse(CommandServer.isThereAlreadyAnInstanceRunning());
        Assert.assertFalse(commandServer.isRunning());

        commandServer2.start();
        Thread.sleep(100);
        Assert.assertTrue(commandServer2.isRunning());
        Assert.assertTrue(CommandServer.isThereAlreadyAnInstanceRunning());
        commandServer2.stop();
        Thread.sleep(100);
    }

    @Test
    public void sendMessageToServer() throws InterruptedException {
        String testFile = "src/test/resources/com/itextpdf/rups/io/network/hello_network.pdf";
        CommandServer server = new CommandServer();
        TestReceived dummyClient = new TestReceived();
        server.addNewCommandReceivedListener(dummyClient);
        server.start();
        Thread.sleep(100);
        CommandServer.sendCommandToRunningInstance(new RupsCliArguments(new String[]{testFile}));
        Thread.sleep(150);
        Assert.assertEquals(1, dummyClient.getMessages().size());
        //absolute path so the path will be longer
        Assert.assertTrue(testFile.length() < dummyClient.getMessages().get(0).length());
    }

    static class TestReceived implements IRemoteMessageReceived {
        List<String> messages = new ArrayList<>();

        public List<String> getMessages() {
            return messages;
        }

        @Override
        public void messageReceived(String message) {
            messages.add(message);
        }
    }
}