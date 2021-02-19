package controllers.codeWindow;

import controllers.ControllerSettings;
import controllers.TextLineController;
import gui.Cursor;
import gui.GUIFilledBox;
import gui.buttons.HorizontalScrollBar;
import gui.buttons.VerticalScrollBar;
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

    private float contentsHorizontalPosition = 0;
    private float maxHorizontalPosition;

    private float lineHeight;
    private float padding;
    private boolean scrollable = false;

    private boolean inBounds = false;

    private int numberOfLines = 0;

    private boolean scrollingVertical = false;
    private boolean scrollingHorizontal = false;
    private VerticalScrollBar verticalScrollBar;
    private HorizontalScrollBar horizontalScrollBar;
    private boolean selected = false;

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
        this.padding = border;

        //Split the input text on each line
        String[] lines = content.split("\n");
        //Texts will start being added with a slight offset from the top
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
        float longestLine = 0;
        for(EditableFormattedTextLine line : textLineController.getCodeWindowTextLines()){
//            line.getWords()[0].getPosition().x = -1f;
            //line.setPosition(new Vector2f(-1, line.getPosition().y));
            line.generateCharacterEdges();
            if(line.getLength() > longestLine){
                longestLine = line.getLength();
            }
        }

        //changeContentsHorizontalPosition(longestLineNumber*2+border*2);
        this.codeWindow.setTextNumberFilledBox(new GUIFilledBox(new Vector2f(position.x, position.y), new Vector2f(longestLineNumber*2 + 2*border, size.y), GeneralSettings.USERPREF.getTexteditorLinenumberBGColor3f()));
        maxVerticalPosition = minHeight-size.y;
        maxHorizontalPosition = longestLine*2 + codeWindow.getTextNumberFilledBox().getSize().x;// - size.x;
        this.codeWindow.setGuiFilledBox(new GUIFilledBox(new Vector2f(longestLineNumber*2+border*2, 0), new Vector2f(size.x - longestLineNumber*2 + border*2, size.y), backgroundColor));

        cursorController = new CursorController(new Cursor(), this);
        verticalScrollBar = new VerticalScrollBar(new Vector2f(-0.02f, -.92f), 0.02f, size.y-0.08f, size.y, maxVerticalPosition + size.y, 0);
        horizontalScrollBar = new HorizontalScrollBar(new Vector2f(position.x + codeWindow.getTextNumberFilledBox().getSize().x, -1f),0.03f, size.x-codeWindow.getTextNumberFilledBox().getSize().x-0.03f, size.x-codeWindow.getTextNumberFilledBox().getSize().x, longestLine*2, 0);
        updateAspectRatio(new Vector2f(GeneralSettings.ASPECT_RATIO.m00, GeneralSettings.ASPECT_RATIO.m11), headerHeight);


    }

    /**
     * Processes clicks of the left mouse button
     * @return true if the event should be processed no further, false if the event needs more processing
     */
    public boolean mouseLeftClick(){
        if(inBounds){
            //If vertical  scroll bar was clicked start scrolling vertically
            if(mousePosition.x > verticalScrollBar.getFilledBox().getPosition().x && mousePosition.y > verticalScrollBar.getFilledBox().getPosition().y
                    && mousePosition.x < verticalScrollBar.getFilledBox().getPosition().x + verticalScrollBar.getFilledBox().getSize().x
                    && mousePosition.y < verticalScrollBar.getFilledBox().getPosition().y + verticalScrollBar.getFilledBox().getSize().y){
                scrollingVertical = true;
            }
            //If horizontal scroll bar was clicked start scrolling horizontally
            else if(mousePosition.x > horizontalScrollBar.getFilledBox().getPosition().x && mousePosition.y > horizontalScrollBar.getFilledBox().getPosition().y
                    && mousePosition.x < horizontalScrollBar.getFilledBox().getPosition().x + horizontalScrollBar.getFilledBox().getSize().x
                    && mousePosition.y < horizontalScrollBar.getFilledBox().getPosition().y + horizontalScrollBar.getFilledBox().getSize().y){
                scrollingHorizontal = true;
            }
            //Move the physical cursor to the mouse's position
            //If a scroll bar was just grabbed do not move the cursor
            else{
                cursorController.moveCursor(new Vector2f(mousePosition), this);
            }

            //Mark that this is selected
            selected = true;

            //The event has been processesd, return true
            return true;
        }else{
            //The cursor should not be rendered and this is no longer selected
            cursorController.setVisible(false);
            selected = false;
            return false;
        }
    }

    /**
     * Process releases of the left mouse button
     */
    public void mouseLeftRelease(){
        //We are no longer scrolling
        scrollingVertical = false;
        scrollingHorizontal = false;
    }

    /**
     * Processes a key press
     * @param key the GLFW key code indicating which key was pressed
     */
    public void keyPress(int key){
        //Cursor movement keys
        if(key == ControllerSettings.CURSOR_LEFT){
            cursorController.moveLeft();
        }else if(key == ControllerSettings.CURSOR_RIGHT){
            cursorController.moveRight();
        }else if(key == ControllerSettings.CURSOR_UP){
            cursorController.moveUp();
        }else if(key == ControllerSettings.CURSOR_DOWN){
            cursorController.moveDown();
        }
        //Deletion keys
        else if(key == ControllerSettings.CURSOR_BACKSPACE){
            cursorController.backSpace();
        }else if(key == ControllerSettings.CURSOR_DELETE){
            cursorController.delete();
        }
        //No other raw key presses are registered
    }
    /**
     * Scrolls vertically
     * @param scrollChange the amount to change
     */
    public void scroll(float scrollChange){
        //If the contents are larger than the window
        if(maxVerticalPosition > codeWindow.getSize().y) {
            float newPosition = contentsVerticalPosition + scrollChange;
            //Update the position of the contents, cursor, and scroll bar
            //If the position would be negative change position so it would be 0 instead
            if (newPosition < 0) {
                changeContentsVerticalPosition(-contentsVerticalPosition);
                cursorController.scroll(-contentsVerticalPosition);
                verticalScrollBar.changePosition(-contentsVerticalPosition);
            }
            //If the position would be greater than max make it so it will be max instead
            else if (newPosition > maxVerticalPosition) {
                changeContentsVerticalPosition(maxVerticalPosition - contentsVerticalPosition);
                cursorController.scroll(maxVerticalPosition - contentsVerticalPosition);
                verticalScrollBar.changePosition(maxVerticalPosition-contentsVerticalPosition);
            }
            //Otherwise scroll
            else {
                changeContentsVerticalPosition(scrollChange);
                cursorController.scroll(scrollChange);
                verticalScrollBar.changePosition(scrollChange);
            }
        }
    }

    /**
     * Scrolls horizontally
     * @param scrollChange the amount to change
     * @param factor the scaling factor used by the horizontal scroll bar
     */
    public void scrollHorizontal(float scrollChange, float factor){
        //If the contents are larger than the window
        if(maxHorizontalPosition > codeWindow.getSize().x) {
            float newPosition = contentsHorizontalPosition + scrollChange;
            //If the position would be negative change position so it would be 0 instead
            if (newPosition < 0) {
                changeContentsHorizontalPosition(contentsHorizontalPosition, factor);
            }
            //If the position would be greater than max make it so it will be max instead
            else if (newPosition > maxHorizontalPosition-codeWindow.getSize().x) {
                changeContentsHorizontalPosition(-((maxHorizontalPosition - contentsHorizontalPosition)-codeWindow.getSize().x), factor);
            }
            //Otherwise scroll
            else {
                changeContentsHorizontalPosition(-scrollChange, factor);
            }
        }
    }

    /**
     * processes type events
     * @param c the char that was typed
     */
    public void type(char c){
        //Pass the type event to the cursor controller to process
        cursorController.type(c);
    }

    /**
     * Processes events related to mouse movements
     * @param mousePosition
     */
    public void moveMouse(Vector2f mousePosition){
        //If the position did not exist this will cause a crash or weird behavior
        if(this.mousePosition != null) {
            //If the vertical scroll bar is selected scroll appropriately
            if(scrollingVertical) {
                scroll(-((mousePosition.y - this.mousePosition.y)) / verticalScrollBar.getFactor());
            }
            //If the horizontal scroll bar is selected scroll appropriately
            else if(scrollingHorizontal){
                scrollHorizontal(((mousePosition.x - this.mousePosition.x)) / horizontalScrollBar.getFactor(), horizontalScrollBar.getFactor());
            }
        }
        //Save the position
        this.mousePosition = new Vector2f(mousePosition);
        //Determine if the mouse is in the window
        if(mousePosition.x > codeWindow.getPosition().x && mousePosition.y > codeWindow.getPosition().y && mousePosition.x < codeWindow.getPosition().x + codeWindow.getSize().x && mousePosition.y < codeWindow.getPosition().y + codeWindow.getSize().y){
            inBounds = true;
        }else{
            inBounds = false;
        }
        //Maintain selected while scrolling vertically
        if(!inBounds && scrollingVertical){
            selected = true;
        }else{
            selected = false;
        }
    }

    /**
     * Updates the aspect ratio to prevent artifacts when rescaling the window
     * @param aspectRatio the new aspect ratio of the window compared to the original window size
     * @param headerHeight the height of the header(determines height of code window)
     */
    public void updateAspectRatio(Vector2f aspectRatio, float headerHeight){
        //Adjust the width of the line number
        float lineNumberWidth = codeWindow.getTextNumberFilledBox().getSize().x/this.aspectRatio.x*aspectRatio.x;
        //The height is the full possible height minus the height used by the header
        float height = 2-headerHeight;

        //More height may be visible after rescaling resulting in a different max position, adjust for it while setting the code windows new size
        maxVerticalPosition += codeWindow.getSize().y/this.aspectRatio.y;
        codeWindow.setSize(new Vector2f(codeWindow.getSize().x, height));
        maxVerticalPosition -= codeWindow.getSize().y/aspectRatio.y;

        //Set various sizes to the appropriate values
        codeWindow.getTextNumberFilledBox().setSize(new Vector2f(lineNumberWidth, height));
        codeWindow.getGuiFilledBox().setPosition(new Vector2f(lineNumberWidth-1, -1));
        codeWindow.getGuiFilledBox().setSize(new Vector2f(codeWindow.getSize().x-lineNumberWidth, height));

        //TODO: Fix this to actually change the position to appear the same
        contentsVerticalPosition = 0;

        //The texts start at the top of the window
        float startingHeight = codeWindow.getSize().y - 1;
        startingHeight /= aspectRatio.y;

        //For each text
        for(EditableFormattedTextLine line:textLineController.getCodeWindowTextLines()){
            //Change the horizontal position based on the change to the line number size
            line.changeContentsHorizontalPosition(-EditableFormattedTextLine.getLineNumberOffset(), true);

            //Set the position
            line.setPosition(new Vector2f((codeWindow.getPosition().x+padding*4f)/aspectRatio.x, startingHeight), true);//+contentsVerticalPosition));
            line.getWords()[0].getPosition().x =(-1 + padding)/aspectRatio.x;

            //Reset contents horizontal position
            line.changeContentsHorizontalPosition(EditableFormattedTextLine.getLineNumberOffset(), true);
            startingHeight -= lineHeight;
        }

        //Save the aspect ratio for future changes
        this.aspectRatio = new Vector2f(aspectRatio);

        //Update related entities
        verticalScrollBar.updateAspectRatio(0.02f*aspectRatio.x, codeWindow.getSize().y, codeWindow.getSize().y - 0.08f*aspectRatio.y, maxVerticalPosition + codeWindow.getSize().y);
        cursorController.updateAspectRatio();
        textLineController.update(textLineController.getCodeWindowTextLines().get(0), 0, '\0');
    }

    /**
     * Changes the position of each text line vertically
     * @param change the amount to change
     */
    public void changeContentsVerticalPosition(float change){
        //Update each line
        for(FormattedTextLine text : textLineController.getCodeWindowTextLines()){
            text.changeVerticalPosition(change);
        }
        //Update the saved position
        contentsVerticalPosition += change;
    }

    /**
     * Changes the position of the words in each text line horizontally
     * @param change the amount to change
     * @param factor the scaling factor used by the horizontal scroll bar
     */
    public void changeContentsHorizontalPosition(float change, float factor){
        //Update each line
        for(EditableFormattedTextLine text : textLineController.getCodeWindowTextLines()){
            text.changeContentsHorizontalPosition(change, true);
        }
        //Update the horizontal scroll bar
        horizontalScrollBar.changePosition(change*factor);
        //Update the saved position
        contentsHorizontalPosition -= change;
    }
    
    public void maximize(){
        //Ensure that scrolling will effect this window
        setScrollable(true);

        //Perform operations to scroll bars to minimize them
        float factor = horizontalScrollBar.getFactor();
        verticalScrollBar.maximize();
        //If the text now fits on the full screen scroll it all the way to the left
        if(horizontalScrollBar.maximize()){
            scrollHorizontal(-contentsHorizontalPosition, factor);
        }

        //The size of this and it's filled box should now be 2
        codeWindow.getSize().x = 2f;
        codeWindow.getGuiFilledBox().setSize(new Vector2f(2f, 2f));
        //Update the position bounds used for clipping
        codeWindow.getPositionBounds().z = codeWindow.getPosition().x + codeWindow.getSize().x;
    }

    /**
     * Moves this window into splitscreen
     */
    public void goSplitScreen(){
        //If split screen the size of this and the filled box should be 1
        codeWindow.getSize().x = 1f;
        codeWindow.getGuiFilledBox().setSize(new Vector2f(1f, 2f));
        //Update the position bounds used for clipping
        codeWindow.getPositionBounds().z = codeWindow.getPosition().x + codeWindow.getSize().x;
        //Perform operations to scroll bars to split screen them
        verticalScrollBar.goSplitScreen();
        horizontalScrollBar.goSplitScreen();
    }

    /**
     * Minimizes this window
     */
    public void minimize(){
        //Set size in the x direction for this and the filled box to 0
        codeWindow.getSize().x = 0f;
        codeWindow.getGuiFilledBox().setSize(new Vector2f(0f, 2f));
        //Update the position bounds used for clipping
        codeWindow.getPositionBounds().z = codeWindow.getPosition().x + codeWindow.getSize().x;
        //Set that scrolling should not scroll
        setScrollable(false);
        //Perform operations to scroll bars to minimize them
        verticalScrollBar.minimize();
        horizontalScrollBar.minimize();
    }

    /**
     * Clears the text which was included in this code window
     */
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

    /**
     * Changes the number of lines which the code window is accounting for
     * @param change
     */
    public void changeNumberOfLines(int change) {
        //Determine the new last line number
        int newNumberOfLines = change + numberOfLines;

        //Find the current number of characters in the number of lines
        int number = 0;
        int temp = numberOfLines;
        while(temp > 0){
            number++;
            temp /= 10;
        }

        //Find the new number of characters in the number of lines
        int newNumber = 0;
        temp = newNumberOfLines;
        while(temp > 0){
            newNumber++;
            temp /= 10;
        }

        //If the number of characters changed
        if(number != newNumber){
            EditableFormattedTextLine line = textLineController.getCodeWindowTextLines().get(textLineController.getCodeWindowTextLines().size()-1);
            line.setLineNumberOffset((float)line.getWords()[0].getLength()+padding);
            codeWindow.getTextNumberFilledBox().getSize().x = EditableFormattedTextLine.getLineNumberOffset()*4;
            codeWindow.getGuiFilledBox().getPosition().x = EditableFormattedTextLine.getLineNumberOffset()*4;
//            changeContentsHorizontalPosition(-EditableFormattedTextLine.getLineNumberOffset()*4);
            changeContentsHorizontalPosition(EditableFormattedTextLine.getLineNumberOffset()*2, horizontalScrollBar.getFactor());
        }
        maxVerticalPosition += change * GeneralSettings.FONT_HEIGHT;
        verticalScrollBar.changeContentsHeight(change*GeneralSettings.FONT_HEIGHT);
        numberOfLines = newNumberOfLines;
    }

    public VerticalScrollBar getVerticalScrollBar() {
        return verticalScrollBar;
    }

    public HorizontalScrollBar getHorizontalScrollBar() {
        return horizontalScrollBar;
    }

    public boolean isSelected() {
        return selected;
    }
}
