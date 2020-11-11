package gui.textBoxes;

import gui.GUIFilledBox;
import gui.GUIText;
import gui.buttons.HeaderMenu;
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
    private Vector2f aspectRatio = new Vector2f(1, 1);

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
            super.getTextNumberFilledBox().getSize().x = newWidth*aspectRatio.x;
            super.getGuiFilledBox().getPosition().x = 0 + newWidth*aspectRatio.x;
            super.getGuiFilledBox().setSize(new Vector2f((super.getSize().x-super.getGuiFilledBox().getPosition().x)*aspectRatio.x, super.getSize().y));
            changeContentsHorizontalPosition((newWidth-oldWidth)*aspectRatio.x);
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
            super.getTextNumberFilledBox().getSize().x = newWidth*aspectRatio.x;
            super.getGuiFilledBox().getPosition().x = 0 + newWidth*aspectRatio.x;
            super.getGuiFilledBox().setSize(new Vector2f((super.getSize().x-super.getGuiFilledBox().getPosition().x)*aspectRatio.x, super.getSize().y));
            changeContentsHorizontalPosition((newWidth-oldWidth)*aspectRatio.x);
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
        scrollChange*= aspectRatio.y;
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

    public void setAspectRatio(Vector2f aspectRatio, float headerHeight){
        float lineNumberWidth = super.getTextNumberFilledBox().getSize().x/this.aspectRatio.x*aspectRatio.x;
        float height = 2-headerHeight;
        float codeWindowWidth = super.getSize().x-lineNumberWidth;
        super.setSize(new Vector2f(super.getSize().x, height));
        super.getTextNumberFilledBox().setSize(new Vector2f(lineNumberWidth, height));
        super.getGuiFilledBox().setPosition(new Vector2f(lineNumberWidth, 0));
        super.getGuiFilledBox().setSize(new Vector2f(super.getSize().x-lineNumberWidth, height));
        //changeLineNumberVerticalPosition(-contentsVerticalPosition - ((this.getSize().y-1)-this.aspectRatio.y));
        contentsVerticalPosition = contentsVerticalPosition/this.aspectRatio.y*aspectRatio.y;
        maxVerticalPosition = maxVerticalPosition/this.aspectRatio.y*aspectRatio.y;
        //changeLineNumberVerticalPosition(contentsVerticalPosition+((this.getSize().y-1)-aspectRatio.y));
        float startingHeight = this.getSize().y - 1;
        startingHeight /= aspectRatio.y;
        for(GUIText text : super.getLineNumbers()){
            text.setPosition(new Vector2f(-1/aspectRatio.x, startingHeight));//+contentsVerticalPosition));
            startingHeight -= lineHeight;
        }
        startingHeight = this.getSize().y - 1;
        startingHeight /= aspectRatio.y;
        for(GUIText text : super.getTexts()){
            text.setPosition(new Vector2f((getCodeWindowPosition().x-1)/aspectRatio.x, startingHeight));//+contentsVerticalPosition));
            startingHeight -= lineHeight;
        }
        this.aspectRatio = aspectRatio;
    }

}
