package gui;

import fontMeshCreator.FontType;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import utils.InputManager;

import java.util.ArrayList;
import java.util.List;


public class TextBox {
    
    private Vector2f position;
    private Vector2f size;
    private Vector3f backgroundColor;
    private Vector3f borderColor;
    private Vector3f textColor;
    private List<GUIText> texts = new ArrayList<>();
    private GUIFilledBox guiFilledBox;

    public TextBox(){
    }

    public void changeVerticalPosition(float change){
        position.y += change;
        for(GUIText text : texts){
            text.changeVerticalPosition(change);
        }
    }

    public void changeHorizontalPosition(float change){
        position.x += change;
        for(GUIText text : texts){
            text.changeHorizontalPosition(change);
        }
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
    }



    public GUIFilledBox getGuiFilledBox(){
        return guiFilledBox;
    }


    public void setGuiFilledBox(GUIFilledBox guiFilledBox){
        this.guiFilledBox = guiFilledBox;
    }


    public List<GUIText> getTexts(){
        return texts;
    }

    public void setTextColor(Vector3f textColor){
        this.textColor = textColor;
    }


    public void setBorderColor(Vector3f borderColor){
        this.borderColor = borderColor;
    }

}