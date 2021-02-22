package gui.windows;

import controllers.gui.ButtonController;
import gui.buttons.Button;
import gui.buttons.TextButton;
import gui.guiElements.GUIElement;
import gui.guiElements.TextField;
import gui.texts.GUIText;
import main.GeneralSettings;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import rendering.text.TextMaster;

public class PopupWindow extends GUIWindow{

    public PopupWindow(String title, String contents, String cancelText, String continueText){
        super(GeneralSettings.popupWidth, GeneralSettings.popupHeight);
        this.title(title);
        GLFW.glfwMakeContextCurrent(this.window);
        this.addElement(new GUIElement(new GUIText(contents, 4, new Vector2f(-.9f, 0.9f))));
        this.elementList.get(0).getGuiText().getPosition().x = 0 - (float)(2-this.elementList.get(0).getGuiText().getLength())/2;
        Button button = new TextButton(new Vector2f(-0.1f, -0.5f), cancelText, GeneralSettings.USERPREF.getMenuBtnBGColor3f(), GeneralSettings.USERPREF.getMenuBtnHLColor3f(), null, fontType, 4, 0, 0, window) {
            @Override
            public void onPress() {
                onCancel();
            }
        };
        ButtonController.add(button);
        this.addElement(button);
        TextMaster.removeGuiText(this.elementList.get(1).getGuiText());
        button = new TextButton(new Vector2f(0.4f, -0.5f), continueText, GeneralSettings.USERPREF.getMenuBtnBGColor3f(), GeneralSettings.USERPREF.getMenuBtnHLColor3f(), null, fontType, 4, 0, 0, window) {
            @Override
            public void onPress() {
                onContinue();
            }
        };
        ButtonController.add(button);
        this.addElement(button);
        TextMaster.removeGuiText(this.elementList.get(2).getGuiText());
    }

    public PopupWindow(String title, String contents, String continueText){
        super(GeneralSettings.popupWidth, GeneralSettings.popupHeight);
        this.title(title);
        GLFW.glfwMakeContextCurrent(this.window);
        this.addElement(new GUIElement(new GUIText(contents, 4, new Vector2f(-.9f, 0.9f))));
        this.elementList.get(0).getGuiText().getPosition().x = 0 - (float)(2-this.elementList.get(0).getGuiText().getLength())/2;
        Button button = new TextButton(new Vector2f(0.4f, -0.5f), continueText, GeneralSettings.USERPREF.getMenuBtnBGColor3f(), GeneralSettings.USERPREF.getMenuBtnHLColor3f(), null, fontType, 4, 0, 0, window) {
            @Override
            public void onPress() {
                onContinue();
            }
        };
        ButtonController.add(button);
        this.addElement(button);
        TextMaster.removeGuiText(this.elementList.get(1).getGuiText());
    }

    public PopupWindow(String title, String contents){
        super(GeneralSettings.popupWidth, GeneralSettings.popupHeight);
        this.title(title);
        GLFW.glfwMakeContextCurrent(this.window);
        this.addElement(new GUIElement(new GUIText(contents, 4, new Vector2f(-.9f, 0.9f))));
        this.elementList.get(0).getGuiText().getPosition().x = 0 - (float)(2-this.elementList.get(0).getGuiText().getLength())/2;

    }

    public void onCancel(){
        System.out.println("Cancelled");
    }
    public void onContinue(){
        close();
    }
}
