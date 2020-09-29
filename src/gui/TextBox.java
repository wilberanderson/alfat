package gui;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class TextBox {
    
    private Vector2f position;
    private Vector2f size;
    private Vector3f backgroundColor;
    private Vector3f borderColor;
    private Vector3f textColor;
    private List<GUIText> texts = new ArrayList<>();

    private static final float LINE_HEIGHT = 0.03f;

    public TextBox(Vector2f position, Vector2f size, Vector3f backgroundColor, Vector3f textColor, Vector3f borderColor, String content, FontType font, float fontSize, float thickness, float borderWidth) {
        this.position = position;
        this.size = size;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.textColor = textColor;
        String[] lines = content.split("\n");
        float minHeight = 0;
        float lineHeight = LINE_HEIGHT*fontSize;
        for (String line : lines){
		    texts.add(new GUIText(line, fontSize, font, new Vector2f(position.x/2,(1-position.y/2)+minHeight-size.y), size.x, false, thickness, borderWidth, textColor));
		    minHeight += lineHeight;
        }
    }

    public TextBox(Vector2f position, Vector3f backgroundColor, Vector3f textColor, Vector3f borderColor, String content, FontType font, float fontSize, float thickness, float borderWidth) {
        this.position = position;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.textColor = textColor;
        String[] lines = content.split("\n");
        float lineHeight = LINE_HEIGHT * fontSize;
        float minHeight = 0;
        double greatestLength = 0;

        for (String line : lines){
		    GUIText text = new GUIText(line, fontSize, font, new Vector2f(position.x/2,(1-position.y/2)+minHeight-lineHeight*lines.length), 1, false, thickness, borderWidth, textColor);
		    texts.add(text);
            if (text.getLength() > greatestLength){
                greatestLength = text.getLength();
            }
            minHeight+=lineHeight;
        }
        this.size = new Vector2f((float)greatestLength,lineHeight*lines.length);
    }


    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getSize() {
        return size;
    }

    public Vector3f getBackgroundColor(){
        return backgroundColor;
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

    public void changeContentsVerticalPosition(float change){
        for(GUIText text : texts){
            text.changeVerticalPosition(change);
        }
    }

    public void changeContentsHorizontalPosition(float change){
        for(GUIText text : texts){
            text.changeHorizontalPosition(change);
        }
    }
}