package controllers.codeWindow;

import controllers.ControllerSettings;
import controllers.TextLineController;
import gui.Cursor;
import gui.GUIFilledBox;
import gui.texts.*;
import gui.fontMeshCreator.FontType;
import gui.textBoxes.CodeWindow;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import parser.Parser;

import java.util.ArrayList;
import java.util.List;

public class CodeWindowController {
    CursorController cursorController;

    public TextLineController getTextLineController() {
        return textLineController;
    }

    TextLineController textLineController;

    CodeWindow codeWindow;
    Vector2f mousePosition;
    Vector2f aspectRatio = new Vector2f(1, 1);

    private float contentsVerticalPosition = 0;
    private float maxVerticalPosition;

    private float lineHeight;
    private float padding;
    private boolean scrollable = false;

    private boolean inBounds = false;

    private int numberOfLines = 0;


    public void changeCodewindowBGcolor3f(Vector3f newColor) {
        codeWindow.setBackgroundColor(newColor);
    }

    public void changeCodewindowLinenumberBGColor3f(Vector3f newColor) {
        codeWindow.setTextNumberFilledBoxBackgroundColor(newColor);
    }


    public CodeWindowController(Vector2f position, Vector2f size, Vector3f backgroundColor, Vector3f textColor, Vector3f borderColor, String content, FontType font, float fontSize, float thickness, float borderWidth, float border, float headerHeight, TextLineController textLineController){
        this.textLineController = textLineController;
        this.codeWindow = new CodeWindow();
        this.codeWindow.setPosition(position);
        this.codeWindow.setSize(size);
        this.codeWindow.setBackgroundColor(backgroundColor);
        this.codeWindow.setBorderColor(borderColor);
        //this.codeWindow.setTextColor(textColor);
        this.padding = border;
        String[] lines = content.split("\n");
        float minHeight = border;
        lineHeight = GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR;
        codeWindow.setPositionBounds(new Vector4f(position.x, position.y, position.x + size.x, position.y + size.y));
        int lineNumber = 1;
        float longestLineNumber = 0;

        Parser parser = new Parser();

        List<EditableFormattedTextLine> newLines = new ArrayList<>();

        for (String line : lines){
            numberOfLines++;
            EditableFormattedTextLine formattedTextLine = parser.getFormattedLine(line);

            //this.codeWindow.getTexts().add(new CodeWindowText(line, fontSize, new Vector2f(border + position.x,position.y-minHeight+size.y)));
            LineNumberWord lineNumberWord = new LineNumberWord(Integer.toString(lineNumber), new Vector2f(-1, position.y-minHeight+size.y));
            //this.codeWindow.getLineNumbers().add(lineNumberText);

            formattedTextLine.getWords()[0] = lineNumberWord;
            formattedTextLine.setPosition(new Vector2f(border + position.x,position.y-minHeight+size.y));
//            formattedTextLine.changeContentsHorizontalPosition(-border*10);

            newLines.add(formattedTextLine);
            //textLineController.addCodeWindowTextLine(formattedTextLine, -1);

            if(lineNumberWord.getLength() > longestLineNumber){
                longestLineNumber = (float) lineNumberWord.getLength();
            }
            minHeight += lineHeight;
            lineNumber++;
        }
        if(newLines.size() > 0){
            newLines.get(0).setLineNumberOffset(longestLineNumber/2 + padding);
            for(EditableFormattedTextLine textLine : newLines){
                textLineController.addCodeWindowTextLine(textLine, -1);
            }
        }
        for(EditableFormattedTextLine line : textLineController.getCodeWindowTextLines()){
//            line.getWords()[0].getPosition().x = -1f;
            //line.setPosition(new Vector2f(-1, line.getPosition().y));
            line.generateCharacterEdges();
        }

        //changeContentsHorizontalPosition(longestLineNumber*2+border*2);
        this.codeWindow.setTextNumberFilledBox(new GUIFilledBox(new Vector2f(position.x, position.y), new Vector2f(longestLineNumber*2 + 2*border, size.y), GeneralSettings.USERPREF.getTexteditorLinenumberBGColor3f()));
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
        if(maxVerticalPosition > codeWindow.getSize().y) {
            scrollChange = -scrollChange;
            scrollChange *= aspectRatio.y;
            if (maxVerticalPosition > codeWindow.getPosition().x) {
                float newPosition = contentsVerticalPosition + scrollChange;
                if (newPosition < 0) {
                    changeContentsVerticalPosition(-contentsVerticalPosition);
                    cursorController.scroll(-contentsVerticalPosition);
                } else if (newPosition > maxVerticalPosition) {
                    changeContentsVerticalPosition(maxVerticalPosition - contentsVerticalPosition);
                    cursorController.scroll(maxVerticalPosition - contentsVerticalPosition);
                } else {
                    changeContentsVerticalPosition(scrollChange);
                    cursorController.scroll(scrollChange);
                }
            }
        }
    }

    public void type(char c){
        cursorController.type(c);
    }

    public void moveMouse(Vector2f mousePosition){
        if(mousePosition.x > codeWindow.getPosition().x && mousePosition.y > codeWindow.getPosition().y && mousePosition.x < codeWindow.getPosition().x + codeWindow.getSize().x && mousePosition.y < codeWindow.getPosition().y + codeWindow.getSize().y){
            inBounds = true;
            this.mousePosition = mousePosition;
        }else{
            inBounds = false;
        }
    }

    public void updateAspectRatio(Vector2f aspectRatio, float headerHeight){
        float lineNumberWidth = codeWindow.getTextNumberFilledBox().getSize().x/this.aspectRatio.x*aspectRatio.x;
        float height = 2-headerHeight;
        float codeWindowWidth = codeWindow.getSize().x-lineNumberWidth;
        codeWindow.setSize(new Vector2f(codeWindow.getSize().x, height));
        codeWindow.getTextNumberFilledBox().setSize(new Vector2f(lineNumberWidth, height));
        codeWindow.getGuiFilledBox().setPosition(new Vector2f(lineNumberWidth-1, -1));
        codeWindow.getGuiFilledBox().setSize(new Vector2f(codeWindow.getSize().x-lineNumberWidth, height));
        //changeLineNumberVerticalPosition(-contentsVerticalPosition - ((codeWindow.getSize().y-1)-codeWindow.aspectRatio.y));
        contentsVerticalPosition = contentsVerticalPosition/this.aspectRatio.y*aspectRatio.y;
        maxVerticalPosition = maxVerticalPosition/this.aspectRatio.y*aspectRatio.y;
        //changeLineNumberVerticalPosition(contentsVerticalPosition+((codeWindow.getSize().y-1)-aspectRatio.y));
        float startingHeight = codeWindow.getSize().y - 1;
        startingHeight /= aspectRatio.y;
//        for(TextLine text : codeWindow.getLineNumbers()){
//            text.setPosition(new Vector2f(-1/aspectRatio.x, startingHeight));//+contentsVerticalPosition));
//            startingHeight -= lineHeight;
//        }
        startingHeight = codeWindow.getSize().y - 1;
        startingHeight /= aspectRatio.y;
        for(EditableFormattedTextLine line:textLineController.getCodeWindowTextLines()){
            line.setPosition(new Vector2f((codeWindow.getPosition().x+padding*8)/aspectRatio.x, startingHeight), true);//+contentsVerticalPosition));
            startingHeight -= lineHeight;
        }
        this.aspectRatio = aspectRatio;


        cursorController.updateAspectRatio();

        textLineController.update(textLineController.getCodeWindowTextLines().get(0), 0, '\0');
    }

    public void changeContentsVerticalPosition(float change){
        for(FormattedTextLine text : textLineController.getCodeWindowTextLines()){
            text.changeVerticalPosition(change);
        }
        contentsVerticalPosition += change;
    }

    public void changeContentsHorizontalPosition(float change){
        for(EditableFormattedTextLine text : textLineController.getCodeWindowTextLines()){
            text.changeContentsHorizontalPosition(change);
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
        textLineController.getCodeWindowTextLines().clear();
    }

    public CursorController getCursorController(){
        return cursorController;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    public List<EditableFormattedTextLine> getTexts(){
        return textLineController.getCodeWindowTextLines();
    }

    public Vector2f getAspectRatio(){
        return aspectRatio;
    }

    public CodeWindow getCodeWindow(){
        return codeWindow;
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public void changeNumberOfLines(int change) {
        int newNumberOfLines = change + numberOfLines;
        int number = 0;
        int temp = numberOfLines;
        while(temp > 0){
            number++;
            temp /= 10;
        }
        int newNumber = 0;
        temp = newNumberOfLines;
        while(temp > 0){
            newNumber++;
            temp /= 10;
        }
        if(number != newNumber){
            EditableFormattedTextLine line = textLineController.getCodeWindowTextLines().get(textLineController.getCodeWindowTextLines().size()-1);
            line.setLineNumberOffset(line.getLength()/2+padding);
        }
        maxVerticalPosition += change * GeneralSettings.FONT_SIZE * GeneralSettings.FONT_SCALING_FACTOR;
        numberOfLines = newNumberOfLines;
    }
}
