package gui;

import gui.textBoxes.TextBox;
import org.lwjgl.util.vector.Vector2f;

public class Selection {

    private Vector2f startPosition;
    private float endPosition;
    private float lineHeight;
    private int numberOfLines;
    private String textString;
    private TextBox textBox;


    public Selection(Vector2f startPosition, float endPosition, float lineHeight, int numberOfLines, String textString, TextBox textBox){
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.lineHeight = lineHeight;
        this.numberOfLines = numberOfLines;
        this.textString = textString;
        this.textBox = textBox;
    }

    public void setStartPosition(Vector2f startPosition){
        this.startPosition = startPosition;
    }

    public void setEndPosition(float endPosition){
        this.endPosition = endPosition;
    }

    public void setNumberOfLines(int numberOfLines){
        this.numberOfLines = numberOfLines;
    }

    public void setLineHeight(float lineHeight){
        this.lineHeight = lineHeight;
    }

    public void setTextString(String textString){
        this.textString = textString;
    }

    public Vector2f getStartPosition() {
        return startPosition;
    }

    public float getEndPosition() {
        return endPosition;
    }

    public float getLineHeight() {
        return lineHeight;
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public String getTextString() {
        return textString;
    }

    public TextBox getTextBox() {
        return textBox;
    }
}
