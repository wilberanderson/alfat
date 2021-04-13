package gui.windows;

import controllers.gui.ButtonController;
import gui.buttons.Button;
import gui.buttons.TextButton;
import gui.fontMeshCreator.FontType;
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
        this.addElement(new GUIElement(new GUIText(contents, 4, new Vector2f(-.9f, 0.9f), 1.8f)));
//        this.elementList.get(0).getGuiText().getPosition().x = 0 - (float)(2-this.elementList.get(0).getGuiText().getLength())/2;
//        this.elementList.get(0).getGuiText().getPosition().x = -2;
        Button button = new TextButton(new Vector2f(-0.1f, -0.5f), continueText, GeneralSettings.USERPREF.getMenuBtnBGColor3f(), GeneralSettings.USERPREF.getMenuBtnHLColor3f(), null, fontType, 4, 0, 0, window) {
            @Override
            public void onPress() {
                onContinue();
            }
        };
        button.setPosition(new Vector2f(1-(button.getSize().x + 0.1f), -0.9f));
        button.getGuiText().setPosition(new Vector2f(button.getPosition().x, button.getPosition().y + 0.045f*4));
        button.getFilledBox().setPosition(button.getPosition());
        float position = button.getPosition().x;
        ButtonController.add(button);
        this.addElement(button);
        TextMaster.removeGuiText(this.elementList.get(1).getGuiText());
        button = new TextButton(new Vector2f(0.4f, -0.5f), cancelText, GeneralSettings.USERPREF.getMenuBtnBGColor3f(), GeneralSettings.USERPREF.getMenuBtnHLColor3f(), null, fontType, 4, 0, 0, window) {
            @Override
            public void onPress() {
                onCancel();
            }
        };
        button.setPosition(new Vector2f(position-(button.getSize().x + 0.1f), -0.9f));
        button.getGuiText().setPosition(new Vector2f(button.getPosition().x, button.getPosition().y + 0.045f*4));
        button.getFilledBox().setPosition(button.getPosition());
        ButtonController.add(button);
        this.addElement(button);
        TextMaster.removeGuiText(this.elementList.get(2).getGuiText());
    }

    public PopupWindow(String title, String contents, String continueText){
        super(GeneralSettings.popupWidth, GeneralSettings.popupHeight);
        this.title(title);
        GLFW.glfwMakeContextCurrent(this.window);
        this.addElement(new GUIElement(new GUIText(contents, 4, new Vector2f(-.9f, 0.9f), 1.8f)));
//        this.elementList.get(0).getGuiText().getPosition().x = 0 - (float)(2-this.elementList.get(0).getGuiText().getLength())/2;
        Button button = new TextButton(new Vector2f(0.4f, -0.5f), continueText, GeneralSettings.USERPREF.getMenuBtnBGColor3f(), GeneralSettings.USERPREF.getMenuBtnHLColor3f(), null, fontType, 4, 0, 0, window) {
            @Override
            public void onPress() {
                onContinue();
            }
        };
        button.setPosition(new Vector2f(1-(button.getSize().x - 0.1f), -0.9f));
        button.getGuiText().setPosition(new Vector2f(button.getPosition().x, button.getPosition().y + 0.045f*4));
        button.getFilledBox().setPosition(button.getPosition());
        ButtonController.add(button);
        this.addElement(button);
        TextMaster.removeGuiText(this.elementList.get(1).getGuiText());
    }

    public PopupWindow(String title, String contents){
        super(GeneralSettings.popupWidth, GeneralSettings.popupHeight);
        this.title(title);
        GLFW.glfwMakeContextCurrent(this.window);
        this.addElement(new GUIElement(new GUIText(contents, 4, new Vector2f(-.9f, 0.9f), 1.8f)));
//        this.elementList.get(0).getGuiText().getPosition().x = 0 - (float)(2-this.elementList.get(0).getGuiText().getLength())/2;

    }

    public void onCancel(){
        System.out.println("Cancelled");
    }
    public void onContinue(){
        close();
    }



    public void makeButton(float x, float y, String text) {
        Button button = new TextButton(new Vector2f(x, y), text, GeneralSettings.USERPREF.getMenuBtnBGColor3f(), GeneralSettings.USERPREF.getMenuBtnHLColor3f(), null, fontType, 4, 0, 0, window) {
            @Override
            public void onPress() {
                onContinue();
            }
        };
        button.setPosition(new Vector2f((button.getSize().x - 0.5f), -0.9f));
        button.getGuiText().setPosition(new Vector2f(button.getPosition().x, button.getPosition().y + 0.045f*4));
        button.getFilledBox().setPosition(button.getPosition());
        ButtonController.add(button);
        this.addElement(button);
        TextMaster.removeGuiText(this.elementList.get(1).getGuiText());
    }


    public FontType getFontType() {
        FontType temp = fontType;
        return temp;
    }
}
