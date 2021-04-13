package controllers.gui;

public class GUIController {

    GUIWindowController guiWindowController;

    GUIController(){
        guiWindowController = new GUIWindowController();
    }


    public GUIWindowController getGuiWindowController() {
        return guiWindowController;
    }
}
