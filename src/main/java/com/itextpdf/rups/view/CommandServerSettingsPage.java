package com.itextpdf.rups.view;

import com.itextpdf.rups.RupsConfiguration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CommandServerSettingsPage extends JPanel {

    GridLayout gridLayout;
    private final JCheckBox isServerEnabled;

    public CommandServerSettingsPage() {
        super();
        gridLayout = new GridLayout(0, 1);
        setVisible(true);
        setLayout(gridLayout);
        isServerEnabled = new JCheckBox("Enable command server on startup", RupsConfiguration.INSTANCE.getCommandServerEnabled());
        isServerEnabled.addActionListener(actionEvent -> RupsConfiguration.INSTANCE.setCommandServerEnabled(isServerEnabled.isSelected()));
        add(isServerEnabled);
    }


    public void reset() {
        isServerEnabled.setSelected(RupsConfiguration.INSTANCE.getCommandServerEnabled());
    }
}

