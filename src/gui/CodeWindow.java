package gui;

import fontMeshCreator.FontType;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import utils.InputManager;

import java.util.List;


public class CodeWindow extends TextBox{

    private float contentsVerticalPosition = 0;
    private float maxVerticalPosition;
    private Vector4f positionBounds;
    private float lineHeight;

    public CodeWindow(Vector2f position, Vector2f size, Vector3f backgroundColor, Vector3f textColor, Vector3f borderColor, String content, FontType font, float fontSize, float thickness, float borderWidth, float border) {
        super();
        super.setPosition(position);
        super.setSize(size);
        super.setBackgroundColor(backgroundColor);
        super.setBorderColor(borderColor);
        super.setTextColor(textColor);
        String[] lines = content.split("\n");
        float minHeight = border;
        lineHeight = GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR;
        positionBounds = new Vector4f(position.x, position.y, position.x + size.x, position.y + size.y);
        for (String line : lines){
            super.getTexts().add(new GUIText(line, fontSize, font, new Vector2f(border + position.x-1,position.y-minHeight+size.y-1), thickness, borderWidth, textColor, positionBounds, false, false));
            minHeight += lineHeight;
        }
        maxVerticalPosition = minHeight-size.y;
        super.setGuiFilledBox(new GUIFilledBox(position, size, backgroundColor));
    }


    public void removeText(GUIText text){
        int index = super.getTexts().indexOf(text);
        float heightChange = text.getFontSize() * 0.06f;
        super.getTexts().remove(index);
        text.remove(text);
        for(int i = index; i < super.getTexts().size(); i++){
            super.getTexts().get(i).changeVerticalPosition(heightChange);
        }
        maxVerticalPosition -= lineHeight;
    }

    public void addText(GUIText text, int index){
        float heightChange = text.getFontSize() * 0.06f;
        super.getTexts().add(index, text);
        for(int i = index + 1; i < super.getTexts().size(); i++){
            super.getTexts().get(i).changeVerticalPosition(-heightChange);
        }
        maxVerticalPosition += lineHeight;
    }

    public GUIText mergeTexts(GUIText left, GUIText right){
        int index = super.getTexts().indexOf(left);
        String rightText = right.getTextString();
        removeText(right);
        GUIText newText = new GUIText(left.getTextString() + rightText, left, true);
        super.getTexts().set(index, newText);
        maxVerticalPosition -= lineHeight;
        return newText;
    }

    public void changeContentsVerticalPosition(float change){
        for(GUIText text : super.getTexts()){
            text.changeVerticalPosition(change);
        }
        contentsVerticalPosition += change;
    }

    public void changeContentsHorizontalPosition(float change){
        for(GUIText text : super.getTexts()){
            text.changeHorizontalPosition(change);
        }
    }



    public void scroll(float scrollChange){
        float newPosition = contentsVerticalPosition + scrollChange;
        if(newPosition < 0){
            changeContentsVerticalPosition(-contentsVerticalPosition);
            contentsVerticalPosition = 0;
        }
        else if(newPosition > maxVerticalPosition){
            changeContentsVerticalPosition(maxVerticalPosition-contentsVerticalPosition);
            contentsVerticalPosition = maxVerticalPosition;
        }else{
            changeContentsVerticalPosition(scrollChange);
            contentsVerticalPosition = newPosition;
        }
        InputManager.SCROLL_CHANGE = 0;
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

}
