package gui.Settings;

import javax.swing.*;
import java.awt.*;


/**
 * A simple object that associates a name (string) with a JPanel
 * @see gui.Settings.SettingsMenu
 * */
public class SettingsContent {
    public String name;
    public JPanel content;

    public SettingsContent() {
        content = new JPanel();
    }
    public SettingsContent(String name) {
        this.name = name;
        content = new JPanel();
    }

    public SettingsContent(String name, JPanel content) {
        this.name = name;
        this.content = content;
    }

}
