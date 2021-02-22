package gui.guiElements;

import gui.Cursor;
import gui.GUIFilledBox;
import gui.fontMeshCreator.FontType;
import gui.texts.DarkGUIText;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import rendering.text.TextMaster;

import java.awt.*;

public class TextField extends GUIElement{
    Cursor cursor;
    private Vector2f size;
    private Vector2f position;
    private static Vector3f filledBoxColor = new Vector3f(1, 1, 1);
    private int characterIndex = 0;
    private int fontSize;
    private FontType fontType;
    private float scalingFactor;

    public TextField(Vector2f position, Vector2f size, int fontSize, FontType fontType, float scalingFactor){
        this.position = position;
        this.size = size;
        this.setFilledBox(new GUIFilledBox(position, size, filledBoxColor));
        this.setGuiText(new DarkGUIText("", fontSize, position, fontType));
        TextMaster.removeGuiText(this.getGuiText());
        this.cursor = new Cursor(new Vector3f(0.5f, 0.5f, 0.5f));
        this.cursor.setPosition(new Vector2f(position));
        this.fontSize = fontSize;
        this.fontType = fontType;
        this.scalingFactor = scalingFactor;
    }

    public void type(char c){
        String text = "";
        text += getGuiText().getTextString().substring(0, characterIndex);
        text += c;
        text += getGuiText().getTextString().substring(characterIndex);

        this.setGuiText(new DarkGUIText(text, fontSize, new Vector2f(getGuiText().getPosition().x, position.y + fontSize * scalingFactor), fontType));
        TextMaster.removeGuiText(this.getGuiText());
//        this.cursor.setPosition(new Vector2f(position.x + (float)this.getGuiText().getLength()*2, position.y));
        characterIndex++;
        updateXPosition();
    }


    public void delete(boolean backspace){

        String text = "";
        if(backspace){
            if(characterIndex > 0){
                text += getGuiText().getTextString().substring(0, characterIndex-1);
                text += getGuiText().getTextString().substring(characterIndex);
                this.setGuiText(new DarkGUIText(text, fontSize, new Vector2f(getGuiText().getPosition().x, position.y + fontSize * scalingFactor), fontType));
                TextMaster.removeGuiText(this.getGuiText());
                characterIndex--;
                updateXPosition();
            }
        }else{
            if(characterIndex < getGuiText().getCharacterEdges().length - 1) {
                text += getGuiText().getTextString().substring(0, characterIndex);
                text += getGuiText().getTextString().substring(characterIndex + 1);
                this.setGuiText(new DarkGUIText(text, fontSize, new Vector2f(getGuiText().getPosition().x, position.y + fontSize * scalingFactor), fontType));
                TextMaster.removeGuiText(this.getGuiText());
            }
        }


    }

    /**
     * Moves the cursor one character to the left
     */
    public void moveLeft(){
        //The saved character index should no longer be restored
//        savedCharacterIndex = -1;

        //Try to move the cursor
        characterIndex --;
        //If the cursor did not wrap onto a new line then update it's position
        if(characterIndex < 0) {
            characterIndex = 0;
        }
        updateXPosition();
    }


    /**
     * Moves the cursor one character to the right
     */
    public void moveRight(){
        //Try to move the cursor
        characterIndex ++;
        //If the cursor did not wrap onto a new line then update it's position
        if(characterIndex == getGuiText().getCharacterEdges().length) {
            characterIndex --;
        }
        updateXPosition();
    }


    /**
     * Updates the x position of the mouse
     */
    private void updateXPosition(){
        //Move the cursor
        cursor.getPosition().x = getGuiText().getCharacterEdges()[characterIndex] * 2 + getGuiText().getPosition().x;

        //If the cursor is outside the bounds of the text field reposition the text to correct it
        if (position.x > cursor.getPosition().x) {
            getGuiText().getPosition().x += position.x - cursor.getPosition().x;
            cursor.getPosition().x = position.x;
        } else if (cursor.getPosition().x > position.x + size.x) {
            getGuiText().getPosition().x -= cursor.getPosition().x - (position.x + size.x);
            cursor.getPosition().x = position.x + size.x;
        }
    }

    public Vector2f getSize() {
        return size;
    }

    public void setSize(Vector2f size) {
        this.size = size;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public Cursor getCursor() {
        return cursor;
    }
}
