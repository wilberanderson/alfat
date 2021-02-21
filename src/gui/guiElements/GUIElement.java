package gui.guiElements;

import gui.GUIFilledBox;
import gui.texts.GUIText;
import rendering.text.TextMaster;

public class GUIElement {
    private GUIFilledBox filledBox;
    private GUIText guiText;


    public GUIElement(){

    }

    public GUIElement(GUIFilledBox filledBox){
        this.filledBox = filledBox;
    }

    public GUIElement(GUIText guiText){
        this.guiText = guiText;
        TextMaster.removeGuiText(guiText);
    }

    public GUIElement(GUIFilledBox filledBox, GUIText guiText){
        this.filledBox = filledBox;
        this.guiText = guiText;
    }

    public GUIFilledBox getFilledBox() {
        return filledBox;
    }

    public void setFilledBox(GUIFilledBox filledBox) {
        this.filledBox = filledBox;
    }

    public GUIText getGuiText() {
        return guiText;
    }

    public void setGuiText(GUIText guiText) {
        this.guiText = guiText;
    }
}
