package gui.windows;

import controllers.ApplicationController;
import gui.guiElements.TextField;
import main.EngineTester;
import main.GeneralSettings;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;

public class AnalysisWindow extends PopupWindow{
    ApplicationController controller;

    public AnalysisWindow(ApplicationController controller){
        super("Register search", "Enter a search string:",  "Cancel", "Continue");
        this.addElement(new TextField(new Vector2f(-0.9f, -0.2f), new Vector2f(1.8f, 4* GeneralSettings.FONT_SCALING_FACTOR)));
        this.controller = controller;
    }

    @Override
    public void onCancel(){
        close();
    }

    @Override
    public  void onContinue(){
        String text = elementList.get(elementList.size()-1).getGuiText().getTextString();
        GLFW.glfwSetWindowTitle(EngineTester.getWindow(), GeneralSettings.WINDOW_TITLE + " [" + text + "]");
        controller.getFlowchartWindowController().locateRegisters(text);
    }
}
