package controllers.codeWindow;

import controllers.ControllerSettings;
import gui.Cursor;
import gui.GUIFilledBox;
import gui.GUIText;
import gui.fontMeshCreator.FontType;
import gui.textBoxes.CodeWindow;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;

public class CodeWindowController {
    CursorController cursorController;
    CodeWindowTextLineController lineController;

    CodeWindow codeWindow;
    Vector2f mousePosition;
    Vector2f aspectRatio = new Vector2f(1, 1);

    private float contentsVerticalPosition = 0;
    private float maxVerticalPosition;

    private float lineHeight;
    private float padding;
    private boolean scrollable = false;

    private boolean inBounds = false;

    public CodeWindowController(Vector2f position, Vector2f size, Vector3f backgroundColor, Vector3f textColor, Vector3f borderColor, String content, FontType font, float fontSize, float thickness, float borderWidth, float border, float headerHeight){
        this.codeWindow = new CodeWindow();
        this.codeWindow.setPosition(position);
        this.codeWindow.setSize(size);
        this.codeWindow.setBackgroundColor(backgroundColor);
        this.codeWindow.setBorderColor(borderColor);
        this.codeWindow.setTextColor(textColor);
        this.padding = border;
        String[] lines = content.split("\n");
        float minHeight = border;
        lineHeight = GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR;
        codeWindow.setPositionBounds(new Vector4f(position.x, position.y, position.x + size.x, position.y + size.y));
        int lineNumber = 1;
        float longestLineNumber = 0;
        for (String line : lines){
            this.codeWindow.getTexts().add(new GUIText(line, fontSize, font, new Vector2f(border + position.x-1,position.y-minHeight+size.y-1), thickness, borderWidth, textColor, codeWindow.getPositionBounds(), false, false, true));
            GUIText lineNumberText = new GUIText(Integer.toString(lineNumber), fontSize, font, new Vector2f(border + position.x-1, position.y-minHeight+size.y-1), thickness, borderWidth, textColor, codeWindow.getPositionBounds(), false, false, false);
            this.codeWindow.getLineNumbers().add(lineNumberText);
            if(lineNumberText.getLength() > longestLineNumber){
                longestLineNumber = (float) lineNumberText.getLength();
            }
            minHeight += lineHeight;
            lineNumber++;
        }
        changeContentsHorizontalPosition(longestLineNumber*2+border*2);
        this.codeWindow.setTextNumberFilledBox(new GUIFilledBox(position, new Vector2f(longestLineNumber*2 + 2*border, size.y), GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR));
        maxVerticalPosition = minHeight-size.y;
        this.codeWindow.setGuiFilledBox(new GUIFilledBox(new Vector2f(longestLineNumber*2+border*2, 0), new Vector2f(size.x - longestLineNumber*2 + border*2, size.y), backgroundColor));

        cursorController = new CursorController(new Cursor(), this);
        updateAspectRatio(new Vector2f(GeneralSettings.ASPECT_RATIO.m00, GeneralSettings.ASPECT_RATIO.m11), headerHeight);

    }

    public boolean mouseLeftClick(){
        if(inBounds){
            cursorController.moveCursor(mousePosition, this);
            return true;
        }else{
            cursorController.setVisible(false);
            return false;
        }
    }

    public void mouseLeftRelease(){
        if(inBounds){
        }
    }

    public void keyPress(int key){
        if(key == ControllerSettings.CURSOR_LEFT){
            cursorController.moveLeft();
        }else if(key == ControllerSettings.CURSOR_RIGHT){
            cursorController.moveRight();
        }else if(key == ControllerSettings.CURSOR_UP){
            cursorController.moveUp();
        }else if(key == ControllerSettings.CURSOR_DOWN){
            cursorController.moveDown();
        }else if(key == ControllerSettings.CURSOR_BACKSPACE){
            cursorController.backSpace();
        }else if(key == ControllerSettings.CURSOR_DELETE){
            cursorController.delete();
        }
    }

    public void scroll(float scrollChange){
        scrollChange = -scrollChange;
        scrollChange *= aspectRatio.y;
        if (maxVerticalPosition > codeWindow.getPosition().x) {
            float newPosition = contentsVerticalPosition + scrollChange;
            if (newPosition < 0) {
                changeLineNumberVerticalPosition(-contentsVerticalPosition);
                changeContentsVerticalPosition(-contentsVerticalPosition);
                cursorController.scroll(-contentsVerticalPosition);
            } else if (newPosition > maxVerticalPosition) {
                changeLineNumberVerticalPosition(maxVerticalPosition - contentsVerticalPosition);
                changeContentsVerticalPosition(maxVerticalPosition - contentsVerticalPosition);
                cursorController.scroll(maxVerticalPosition - contentsVerticalPosition);
            } else {
                changeLineNumberVerticalPosition(scrollChange);
                changeContentsVerticalPosition(scrollChange);
                cursorController.scroll(scrollChange);
            }
        }
    }

    public void type(char c){
        cursorController.type(c);
    }

    public void moveMouse(Vector2f mousePosition){
        //TODO: Update for resized code windows
        //if(mousePosition.x > codeWindow.getPosition().x && mousePosition.y > codeWindow.getPosition().y && mousePosition.x < codeWindow.getPosition().x + codeWindow.getSize().x && mousePosition.y < codeWindow.getPosition().y + codeWindow.getSize().y){
        if(mousePosition.x > codeWindow.getPosition().x-1 && mousePosition.y > codeWindow.getPosition().y -1 && mousePosition.x < codeWindow.getPosition().x + codeWindow.getSize().x -1 && mousePosition.y < codeWindow.getPosition().y + codeWindow.getSize().y - 1){
            inBounds = true;
            this.mousePosition = mousePosition;
        }else{
            System.out.println("Out of bounds");
            inBounds = false;
        }
    }

    public void updateAspectRatio(Vector2f aspectRatio, float headerHeight){
        float lineNumberWidth = codeWindow.getTextNumberFilledBox().getSize().x/this.aspectRatio.x*aspectRatio.x;
        float height = 2-headerHeight;
        float codeWindowWidth = codeWindow.getSize().x-lineNumberWidth;
        codeWindow.setSize(new Vector2f(codeWindow.getSize().x, height));
        codeWindow.getTextNumberFilledBox().setSize(new Vector2f(lineNumberWidth, height));
        codeWindow.getGuiFilledBox().setPosition(new Vector2f(lineNumberWidth, 0));
        codeWindow.getGuiFilledBox().setSize(new Vector2f(codeWindow.getSize().x-lineNumberWidth, height));
        //changeLineNumberVerticalPosition(-contentsVerticalPosition - ((codeWindow.getSize().y-1)-codeWindow.aspectRatio.y));
        contentsVerticalPosition = contentsVerticalPosition/this.aspectRatio.y*aspectRatio.y;
        maxVerticalPosition = maxVerticalPosition/this.aspectRatio.y*aspectRatio.y;
        //changeLineNumberVerticalPosition(contentsVerticalPosition+((codeWindow.getSize().y-1)-aspectRatio.y));
        float startingHeight = codeWindow.getSize().y - 1;
        startingHeight /= aspectRatio.y;
        for(GUIText text : codeWindow.getLineNumbers()){
            text.setPosition(new Vector2f(-1/aspectRatio.x, startingHeight));//+contentsVerticalPosition));
            startingHeight -= lineHeight;
        }
        startingHeight = codeWindow.getSize().y - 1;
        startingHeight /= aspectRatio.y;
        for(GUIText text : codeWindow.getTexts()){
            text.setPosition(new Vector2f((codeWindow.getCodeWindowPosition().x-1)/aspectRatio.x, startingHeight));//+contentsVerticalPosition));
            startingHeight -= lineHeight;
        }
        this.aspectRatio = aspectRatio;


        cursorController.updateAspectRatio();
    }



    public void removeText(GUIText text){
        int index = codeWindow.getTexts().indexOf(text);
        codeWindow.getTexts().remove(index);
        text.remove(text);
        for(int i = index; i < codeWindow.getTexts().size(); i++){
            codeWindow.getTexts().get(i).changeVerticalPosition(lineHeight);
        }
        GUIText oldNumber = codeWindow.getLineNumbers().get(codeWindow.getLineNumbers().size()-1);
        oldNumber.remove(oldNumber);
        codeWindow.getLineNumbers().remove(oldNumber);
        maxVerticalPosition -= lineHeight;
        if(oldNumber.getTextString().length() != codeWindow.getLineNumbers().get(codeWindow.getLineNumbers().size()-1).getTextString().length()){
            float oldWidth = codeWindow.getTextNumberFilledBox().getSize().x;
            float newWidth = (float)codeWindow.getLineNumbers().get(codeWindow.getLineNumbers().size()-1).getLength()*2 + 2*padding;
            codeWindow.getTextNumberFilledBox().getSize().x = newWidth*aspectRatio.x;
            codeWindow.getGuiFilledBox().getPosition().x = 0 + newWidth*aspectRatio.x;
            codeWindow.getGuiFilledBox().setSize(new Vector2f((codeWindow.getSize().x-codeWindow.getGuiFilledBox().getPosition().x)*aspectRatio.x, codeWindow.getSize().y));
            changeContentsHorizontalPosition((newWidth-oldWidth)*aspectRatio.x);
        }
    }

    public void addText(GUIText text, int index){
        float heightChange = text.getFontSize() * 0.06f;
        codeWindow.getTexts().add(index, text);
        for(int i = index + 1; i < codeWindow.getTexts().size(); i++){
            codeWindow.getTexts().get(i).changeVerticalPosition(-heightChange);
        }
        GUIText lastText = codeWindow.getLineNumbers().get(codeWindow.getLineNumbers().size()-1);
        GUIText newText = new GUIText(Integer.toString(Integer.parseInt(lastText.getTextString())+1), lastText, false);
        newText.setPosition(new Vector2f(lastText.getPosition().x, lastText.getPosition().y - lineHeight));
        codeWindow.getLineNumbers().add(newText);
        maxVerticalPosition += lineHeight;
        if(lastText.getTextString().length() == newText.getTextString().length()){
            float oldWidth = codeWindow.getTextNumberFilledBox().getSize().x;
            float newWidth = (float)codeWindow.getLineNumbers().get(codeWindow.getLineNumbers().size()-1).getLength()*2 + 2*padding;
            codeWindow.getTextNumberFilledBox().getSize().x = newWidth*aspectRatio.x;
            codeWindow.getGuiFilledBox().getPosition().x = 0 + newWidth*aspectRatio.x;
            codeWindow.getGuiFilledBox().setSize(new Vector2f((codeWindow.getSize().x-codeWindow.getGuiFilledBox().getPosition().x), codeWindow.getSize().y));
            //changeContentsHorizontalPosition((newWidth-oldWidth)*aspectRatio.x);
        }
    }

    public GUIText mergeTexts(GUIText left, GUIText right){
        int index = codeWindow.getTexts().indexOf(left);
        String rightText = right.getTextString();
        GUIText newText = new GUIText(left.getTextString() + rightText, left, true);
        codeWindow.getTexts().set(index, newText);
        removeText(right);
        return newText;
    }

    public void changeContentsVerticalPosition(float change){
        for(GUIText text : codeWindow.getTexts()){
            text.changeVerticalPosition(change);
        }
        contentsVerticalPosition += change;
    }

    public void changeLineNumberVerticalPosition(float change){
        for(GUIText text : codeWindow.getLineNumbers()){
            text.changeVerticalPosition(change);
        }
    }

    public void changeContentsHorizontalPosition(float change){
        for(GUIText text : codeWindow.getTexts()){
            text.changeHorizontalPosition(change);
        }
    }
    
    public void maximize(){
        codeWindow.getSize().x = 2f;
        codeWindow.getGuiFilledBox().setSize(new Vector2f(2f, 2f));
        codeWindow.getPositionBounds().z = codeWindow.getPosition().x + codeWindow.getSize().x;
        setScrollable(true);
    }

    public void goSplitScreen(){
        codeWindow.getSize().x = 1f;
        codeWindow.getGuiFilledBox().setSize(new Vector2f(1f, 2f));
        codeWindow.getPositionBounds().z = codeWindow.getPosition().x + codeWindow.getSize().x;
    }

    public void minimize(){
        codeWindow.getSize().x = 0f;
        codeWindow.getGuiFilledBox().setSize(new Vector2f(0f, 2f));
        codeWindow.getPositionBounds().z = codeWindow.getPosition().x + codeWindow.getSize().x;
        setScrollable(false);
    }

    public void clear(){
        codeWindow.clear();
    }

    public CursorController getCursorController(){
        return cursorController;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    public List<GUIText> getTexts(){
        return codeWindow.getTexts();
    }

    public Vector2f getAspectRatio(){
        return aspectRatio;
    }

    public CodeWindow getCodeWindow(){
        return codeWindow;
    }

}
