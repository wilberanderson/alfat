package gui.textBoxes;

import gui.GUIFilledBox;
import gui.texts.CodeWindowText;
import gui.texts.Text;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;


public class TextBox {
    
    private Vector2f position;
    private Vector2f size;
    private Vector3f backgroundColor;
    private Vector3f borderColor;
    private Vector3f textColor;
    private GUIFilledBox guiFilledBox;
    private GUIFilledBox textNumberFilledBox;
    private boolean highlighted;

    public TextBox(){
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position){
        this.position = position;
    }

    public Vector2f getSize() {
        return size;
    }

    public void setSize(Vector2f size){
        this.size = size;
    }
    public Vector3f getBackgroundColor(){
        return backgroundColor;
    }

    public void setBackgroundColor(Vector3f backgroundColor){
        this.backgroundColor = backgroundColor;
        if(this.guiFilledBox != null) {

            this.guiFilledBox.setColor(backgroundColor);
        }

    }

    //TEST
    public void setTextNumberFilledBoxBackgroundColor(Vector3f newColor) {
        this.textNumberFilledBox.setColor(newColor);
    }



    public GUIFilledBox getGuiFilledBox(){
        return guiFilledBox;
    }


    public void setGuiFilledBox(GUIFilledBox guiFilledBox){
        this.guiFilledBox = guiFilledBox;
    }

    public void setBorderColor(Vector3f borderColor){
        this.borderColor = borderColor;
    }

    public void setTextNumberFilledBox(GUIFilledBox textNumberFilledBox){
        this.textNumberFilledBox = textNumberFilledBox;
    }

    public GUIFilledBox getTextNumberFilledBox(){
        return textNumberFilledBox;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted){
        this.highlighted = highlighted;
    }
}