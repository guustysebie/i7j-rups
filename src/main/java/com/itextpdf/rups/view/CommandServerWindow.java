package com.itextpdf.rups.view;

import com.itextpdf.rups.RupsConfiguration;
import com.itextpdf.rups.controller.RupsController;
import com.itextpdf.rups.io.network.CommandServer;
import com.itextpdf.rups.io.network.ServerStatus;
import com.itextpdf.rups.io.network.remotemessagereceivedimpl.LogRemoteMessage;
import com.itextpdf.rups.io.network.remotemessagereceivedimpl.RemoteCommandExecutor;
import com.itextpdf.rups.model.LoggerHelper;
import com.itextpdf.rups.view.icons.FrameIconUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CommandServerWindow {


    private JDialog jDialog;
    private JLabel serverStatusLabel;
    private JButton startStopButton;

    private ActionListener currentBtnClickedAction;
    private final CommandServer commandServer;


    public CommandServerWindow(RupsController rupsController) {
        commandServer = new CommandServer();
        commandServer.addNewCommandReceivedListener(new LogRemoteMessage());
        commandServer.addNewCommandReceivedListener(new RemoteCommandExecutor(rupsController));
        commandServer.addOnServerStatusChangedListener(this::refreshData);

        initializeJDialog();
        setupWindow();
        completeJDialogCreation();
        if (RupsConfiguration.INSTANCE.getCommandServerEnabled()) {
            commandServer.start();
        } else {
            LoggerHelper.info("Command server disabled", CommandServerWindow.class);
        }
    }

    private void setupWindow() {

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(500, 200));
        GridLayout gridLayout = new GridLayout(0, 1);
        panel.setLayout(gridLayout);

        this.serverStatusLabel = new JLabel();
        this.serverStatusLabel.setOpaque(true);
        panel.add(this.serverStatusLabel);

        this.startStopButton = new JButton();

        panel.add(startStopButton);


        this.jDialog.add(panel);
    }


    private void refreshData(ServerStatus status) {
        System.out.println("refreshData");
        if (status == ServerStatus.STARTED) {
            System.out.println("refreshData isRunning");
            this.serverStatusLabel.setText("Server status: running");
            this.serverStatusLabel.setBackground(Color.green);

            this.startStopButton.setText("Stop server");
            this.startStopButton.setBackground(Color.red);

        } else {
            System.out.println("refreshData else");
            this.serverStatusLabel.setText("Server status: stopped");
            this.serverStatusLabel.setBackground(Color.red);

            this.startStopButton.setText("Start server");
            this.startStopButton.setBackground(Color.green);
        }
        if (currentBtnClickedAction != null) {
            this.startStopButton.removeActionListener(currentBtnClickedAction);
        }
        this.currentBtnClickedAction = e -> {
            if (status == ServerStatus.STARTED) {
                commandServer.stop();
            } else {
                commandServer.start();
            }
        };
        this.startStopButton.addActionListener(currentBtnClickedAction);
        this.jDialog.repaint();

    }

    private void initializeJDialog() {
        this.jDialog = new JDialog();
        this.jDialog.setTitle("Command  server");
        this.jDialog.setIconImages(FrameIconUtil.loadFrameIcons());
        this.jDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.jDialog.setModal(true);
        this.jDialog.setLayout(new BorderLayout());
    }

    private void completeJDialogCreation() {
        this.jDialog.pack();
        this.jDialog.setResizable(false);
    }

    public void show(Component component) {
        jDialog.setLocationRelativeTo(component);
        jDialog.setVisible(true);
    }
}
