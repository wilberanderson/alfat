package gui.windows;

import controllers.ApplicationController;
import controllers.gui.ButtonController;
import gui.buttons.Button;
import gui.buttons.TextButton;
import gui.fontMeshCreator.FontType;
import gui.guiElements.GUIElement;
import gui.guiElements.TextField;
import gui.texts.GUIText;
import loaders.Loader;
import main.EngineTester;
import main.GeneralSettings;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import rendering.text.TextMaster;
import utils.MyFile;

public class AnalysisWindow extends GUIWindow{
    ApplicationController controller;
    private FontType analysisFont;

    public AnalysisWindow(ApplicationController controller){
        super(400, 100);
        int fontSize = 8;
        analysisFont = new FontType(Loader.loadTexture(new MyFile(GeneralSettings.DEFAULT_FONT_LOCATION + ".png")), new MyFile(GeneralSettings.DEFAULT_FONT_LOCATION + ".fnt"), 400, 100);

        this.title("Register search");
        GLFW.glfwMakeContextCurrent(this.window);
        this.addElement(new GUIElement(new GUIText("Search: ", fontSize, new Vector2f(-.95f, 0.8f), analysisFont)));
//        this.elementList.get(0).g
        this.elementList.get(0).getGuiText().getPosition().x = 0 - (float)(2-this.elementList.get(0).getGuiText().getLength())/2;
        Button button = new TextButton(new Vector2f(-0.1f, -0.5f), "cancel", new Vector3f(0.2f, 0.2f, 0.2f), new Vector3f(1, 1, 1), null, analysisFont, fontSize, 0, 0, window) {
            @Override
            public void onPress() {
                onCancel();
            }
        };
        ButtonController.add(button);
        this.addElement(button);
        TextMaster.removeGuiText(this.elementList.get(1).getGuiText());
        button = new TextButton(new Vector2f(0.4f, -0.5f), "run", new Vector3f(0.2f, 0.2f, 0.2f), new Vector3f(1, 1, 1), null, analysisFont, fontSize, 0, 0, window) {
            @Override
            public void onPress() {
                onContinue();
            }
        };
        ButtonController.add(button);
        this.addElement(button);
        TextMaster.removeGuiText(this.elementList.get(2).getGuiText());

        this.addElement(new TextField(new Vector2f(-1 + (float)elementList.get(0).getGuiText().getLength()*2 + 0.1f, 0.8f-fontSize*0.05f), new Vector2f(1.8f - ((float)elementList.get(0).getGuiText().getLength()*2 + 0.1f), fontSize * 0.05f), fontSize, analysisFont, 0.05f));
        this.controller = controller;
    }

    public void onCancel(){
        controller.getFlowchartWindowController().locateRegisters("");
        close();
    }

    @Override
    public  void onContinue(){
//        GLFW.glfwMakeContextCurrent(window);
        String text = elementList.get(elementList.size()-1).getGuiText().getTextString();
        if (text.isEmpty()){
            GLFW.glfwSetWindowTitle(EngineTester.getWindow(), GeneralSettings.WINDOW_TITLE);
        } else {
            GLFW.glfwSetWindowTitle(EngineTester.getWindow(), GeneralSettings.WINDOW_TITLE + " [" + text + "]");
        }
//        GLFW.glfwMakeContextCurrent(EngineTester.getWindow());
        controller.getFlowchartWindowController().locateRegisters(text);
        GLFW.glfwMakeContextCurrent(window);
        close();
    }
}
