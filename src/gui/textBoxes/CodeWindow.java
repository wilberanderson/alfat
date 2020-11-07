package gui.textBoxes;

import gui.GUIFilledBox;
import gui.GUIText;
import gui.fontMeshCreator.FontType;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import utils.InputManager;

import java.util.ArrayList;
import java.util.List;


public class CodeWindow extends TextBox{

    private float contentsVerticalPosition = 0;
    private float maxVerticalPosition;
    private Vector4f positionBounds;
    private float lineHeight;
    private float padding;

    public CodeWindow(Vector2f position, Vector2f size, Vector3f backgroundColor, Vector3f textColor, Vector3f borderColor, String content, FontType font, float fontSize, float thickness, float borderWidth, float border) {
        super();
        super.setPosition(position);
        super.setSize(size);
        super.setBackgroundColor(backgroundColor);
        super.setBorderColor(borderColor);
        super.setTextColor(textColor);
        this.padding = border;
        String[] lines = content.split("\n");
        float minHeight = border;
        lineHeight = GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR;
        positionBounds = new Vector4f(position.x, position.y, position.x + size.x, position.y + size.y);
        int lineNumber = 1;
        float longestLineNumber = 0;
        for (String line : lines){
            super.getTexts().add(new GUIText(line, fontSize, font, new Vector2f(border + position.x-1,position.y-minHeight+size.y-1), thickness, borderWidth, textColor, positionBounds, false, false, true));
            GUIText lineNumberText = new GUIText(Integer.toString(lineNumber), fontSize, font, new Vector2f(border + position.x-1, position.y-minHeight+size.y-1), thickness, borderWidth, textColor, positionBounds, false, false, false);
            super.getLineNumbers().add(lineNumberText);
            if(lineNumberText.getLength() > longestLineNumber){
                longestLineNumber = (float) lineNumberText.getLength();
            }
            minHeight += lineHeight;
            lineNumber++;
        }
        changeContentsHorizontalPosition(longestLineNumber*2+border*2);
        super.setTextNumberFilledBox(new GUIFilledBox(position, new Vector2f(longestLineNumber*2 + 2*border, size.y), GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR));
        maxVerticalPosition = minHeight-size.y;
        super.setGuiFilledBox(new GUIFilledBox(new Vector2f(longestLineNumber*2+border*2, 0), new Vector2f(size.x - longestLineNumber*2 + border*2, size.y), backgroundColor));

    }


    public void removeText(GUIText text){
        int index = super.getTexts().indexOf(text);
        super.getTexts().remove(index);
        text.remove(text);
        for(int i = index; i < super.getTexts().size(); i++){
            super.getTexts().get(i).changeVerticalPosition(lineHeight);
        }
        GUIText oldNumber = super.getLineNumbers().get(super.getLineNumbers().size()-1);
        oldNumber.remove(oldNumber);
        super.getLineNumbers().remove(oldNumber);
        maxVerticalPosition -= lineHeight;
        if(oldNumber.getTextString().length() != super.getLineNumbers().get(super.getLineNumbers().size()-1).getTextString().length()){
            float oldWidth = super.getTextNumberFilledBox().getSize().x;
            float newWidth = (float)super.getLineNumbers().get(super.getLineNumbers().size()-1).getLength()*2 + 2*padding;
            super.getTextNumberFilledBox().getSize().x = newWidth;
            super.getGuiFilledBox().getPosition().x = 0 + newWidth;
            super.getGuiFilledBox().setSize(new Vector2f(super.getSize().x-super.getGuiFilledBox().getPosition().x, super.getSize().y));
            changeContentsHorizontalPosition(newWidth-oldWidth);
        }
    }

    public void addText(GUIText text, int index){
        float heightChange = text.getFontSize() * 0.06f;
        super.getTexts().add(index, text);
        for(int i = index + 1; i < super.getTexts().size(); i++){
            super.getTexts().get(i).changeVerticalPosition(-heightChange);
        }
        GUIText lastText = super.getLineNumbers().get(super.getLineNumbers().size()-1);
        GUIText newText = new GUIText(Integer.toString(Integer.parseInt(lastText.getTextString())+1), lastText, false);
        newText.setPosition(new Vector2f(lastText.getPosition().x, lastText.getPosition().y - lineHeight));
        super.getLineNumbers().add(newText);
        maxVerticalPosition += lineHeight;

        if(lastText.getTextString().length() != newText.getLength()){
            float oldWidth = super.getTextNumberFilledBox().getSize().x;
            float newWidth = (float)super.getLineNumbers().get(super.getLineNumbers().size()-1).getLength()*2 + 2*padding;
            super.getTextNumberFilledBox().getSize().x = newWidth;
            super.getGuiFilledBox().getPosition().x = 0 + newWidth;
            super.getGuiFilledBox().setSize(new Vector2f(super.getSize().x-super.getGuiFilledBox().getPosition().x, super.getSize().y));
            changeContentsHorizontalPosition(newWidth-oldWidth);
        }
    }

    public GUIText mergeTexts(GUIText left, GUIText right){
        int index = super.getTexts().indexOf(left);
        String rightText = right.getTextString();
        GUIText newText = new GUIText(left.getTextString() + rightText, left, true);
        super.getTexts().set(index, newText);
        removeText(right);
        return newText;
    }

    public void changeContentsVerticalPosition(float change){
        for(GUIText text : super.getTexts()){
            text.changeVerticalPosition(change);
        }
        contentsVerticalPosition += change;
    }

    public void changeLineNumberVerticalPosition(float change){
        for(GUIText text : super.getLineNumbers()){
            text.changeVerticalPosition(change);
        }
    }

    public void changeContentsHorizontalPosition(float change){
        for(GUIText text : super.getTexts()){
            text.changeHorizontalPosition(change);
        }
    }



    public void scroll(float scrollChange){
        if(maxVerticalPosition > super.getPosition().x) {
            float newPosition = contentsVerticalPosition + scrollChange;
            if (newPosition < 0) {
                changeLineNumberVerticalPosition(-contentsVerticalPosition);
                changeContentsVerticalPosition(-contentsVerticalPosition);
            } else if (newPosition > maxVerticalPosition) {
                changeLineNumberVerticalPosition(maxVerticalPosition - contentsVerticalPosition);
                changeContentsVerticalPosition(maxVerticalPosition - contentsVerticalPosition);
            } else {
                changeLineNumberVerticalPosition(scrollChange);
                changeContentsVerticalPosition(scrollChange);
            }
            InputManager.SCROLL_CHANGE = 0;
        }
        else{
            System.out.println(maxVerticalPosition);
        }
    }


    public void maximize(){
        super.getSize().x = 2f;
        super.getGuiFilledBox().setSize(new Vector2f(2f, 2f));
        positionBounds.z = super.getPosition().x + super.getSize().x;
    }

    public void goSplitScreen(){
        super.getSize().x = 1f;
        super.getGuiFilledBox().setSize(new Vector2f(1f, 2f));
        positionBounds.z = super.getPosition().x + super.getSize().x;
    }

    public void minimize(){
        super.getSize().x = 0f;
        super.getGuiFilledBox().setSize(new Vector2f(0f, 2f));
        positionBounds.z = super.getPosition().x + super.getSize().x;
    }

    public void clear(){
        for(GUIText text: super.getTexts()){
            text.remove(text);
        }
        for(GUIText text: super.getLineNumbers()){
            text.remove(text);
        }
    }

    public Vector2f getCodeWindowPosition(){
        return new Vector2f(super.getPosition().x + super.getTextNumberFilledBox().getSize().x, super.getPosition().y);
    }

    public Vector2f getCodeWindowSize(){
        return new Vector2f(super.getSize().x-super.getTextNumberFilledBox().getSize().x, super.getSize().y);
    }

}
