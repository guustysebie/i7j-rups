package com.itextpdf.rups.io.network;

import com.itextpdf.rups.io.cli.RupsCliArguments;
import com.itextpdf.rups.model.LoggerHelper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CommandServer {
    private static final int SERVER_PORT = 35645;
    private static final String SERVER_WELCOME_MESSAGE = "RUPS_REMOTE_ACTION_PROTOCOL_V_0_0_1";

    private final List<IRemoteMessageReceived> newCommandListeners = new ArrayList<>();
    private final List<Consumer<ServerStatus>> onServerStatusChangedListeners = new ArrayList<>();
    private Thread threadHandler;
    private Socket server;
    private ServerSocket serverSocket;

    private ServerStatus status = ServerStatus.STOPPED;

    public void addNewCommandReceivedListener(IRemoteMessageReceived listener) {
        newCommandListeners.add(listener);
    }


    public void addOnServerStatusChangedListener(Consumer<ServerStatus> listener) {
        onServerStatusChangedListeners.add(listener);
    }

    private void notifyStatusChange(ServerStatus status) {
        this.status = status;
        for (Consumer<ServerStatus> listener : onServerStatusChangedListeners) {
            listener.accept(status);
        }
    }



    public boolean isRunning() {
        return status == ServerStatus.STARTED;
    }

    public void start() {
        this.stop();
        threadHandler = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(SERVER_PORT);
            } catch (IOException e) {
                LoggerHelper.info("Port " + SERVER_PORT + " was not available because an instance is already running the command server", CommandServer.class);
                notifyStatusChange(ServerStatus.STOPPED);
                return;
            }
            LoggerHelper.info("Started command server on port: " + SERVER_PORT, CommandServer.class);
            while (true) {
                notifyStatusChange(ServerStatus.STARTED);
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
                try {
                    server = serverSocket.accept();
                    OutputStream output = server.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);
                    writer.println(SERVER_WELCOME_MESSAGE);
                    BufferedReader fromClient = new BufferedReader(new InputStreamReader(server.getInputStream()));
                    String line = fromClient.readLine();
                    System.out.println(line);
                    for (IRemoteMessageReceived newCommandListener : newCommandListeners) {
                        try {
                            newCommandListener.messageReceived(line);
                        } catch (Exception e) {
                            LoggerHelper.error("Error while processing command", CommandServer.class);
                        }
                    }
                    writer.println("OK");
                    server.close();
                } catch (IOException ignored) {
                    //exception here is expected when stopping the server
                }
            }
        });
        threadHandler.start();
    }

    public void stop() {
        if (this.threadHandler != null) {
            this.threadHandler.interrupt();
        }
        try {
            if (this.server != null) {
                this.server.close();
            }
            if (this.serverSocket != null) {
                this.serverSocket.close();
            }
        } catch (IOException e) {
            LoggerHelper.warn("Error: " + e, CommandServer.class);
        }
        LoggerHelper.info("Stopped command server", CommandServer.class);
        notifyStatusChange(ServerStatus.STOPPED);
    }

    public static boolean isThereAlreadyAnInstanceRunning() {
        String hostname = "localhost";
        try (Socket socket = new Socket(hostname, SERVER_PORT)) {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String character;
            socket.setSoTimeout(100);
            character = reader.readLine();
            return SERVER_WELCOME_MESSAGE.equals(character);
        } catch (UnknownHostException ex) {
            LoggerHelper.warn("Server not found: " + ex.getMessage(), CommandServer.class);

        } catch (IOException ex) {
            LoggerHelper.warn("I/O error: " + ex.getMessage(), CommandServer.class);
        }
        return false;
    }

    public static void sendCommandToRunningInstance(RupsCliArguments rupsWithArgumentsLauncher) {
        String hostname = "localhost";
        try (Socket socket = new Socket(hostname, SERVER_PORT)) {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String character;
            socket.setSoTimeout(100);
            character = reader.readLine();
            if (SERVER_WELCOME_MESSAGE.equals(character)) {
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
                writer.println(new RupsCliArgumentSerializer(rupsWithArgumentsLauncher).serialize());
            }
        } catch (UnknownHostException ex) {
            LoggerHelper.warn("Server not found: " + ex.getMessage(), CommandServer.class);

        } catch (IOException ex) {
            LoggerHelper.warn("I/O error: " + ex.getMessage(), CommandServer.class);
        }
    }

    static class RupsCliArgumentSerializer {
        private final RupsCliArguments rupsCliArguments;

        RupsCliArgumentSerializer(RupsCliArguments rupsCliArguments) {
            this.rupsCliArguments = rupsCliArguments;
        }

        public String serialize() {
            StringBuilder stringBuilder = new StringBuilder();
            for (String pdfFile : rupsCliArguments.getPdfFiles()) {
                File file = new File(pdfFile);
                if (file.exists() && file.isFile()) {
                    stringBuilder.append('"')
                            .append(file.getAbsolutePath())
                            .append('"')
                            .append(" ");

                }
            }
            return stringBuilder.toString();
        }
    }
}

