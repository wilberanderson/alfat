package controllers.codeWindow;

import controllers.ControllerSettings;
import controllers.TextLineController;
import dataStructures.RawModel;
import gui.Cursor;
import gui.FlowchartLine;
import gui.GUIFilledBox;
import gui.Mouse;
import gui.buttons.HorizontalScrollBar;
import gui.buttons.VerticalScrollBar;
import gui.texts.*;
import gui.fontMeshCreator.FontType;
import gui.textBoxes.CodeWindow;
import loaders.Loader;
import main.EngineTester;
import main.GeneralSettings;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.CallbackI;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import parser.GlobalParser;
import parser.LogicScripter.Ruler;
import parser.Parser;
import parser.ParserManager;
import utils.Printer;

import java.nio.FloatBuffer;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class CodeWindowController {
    CursorController cursorController;

    TextLineController textLineController;

    CodeWindow codeWindow;
    Vector2f mousePosition;
    Vector2f aspectRatio = new Vector2f(1, 1);

    private float contentsVerticalPosition = 0;
    private float maxVerticalPosition;
    private float minVerticalPosition;

    private float contentsHorizontalPosition = 0;
    private float maxHorizontalPosition;
    private float minHorizontalPosition;

    private float padding;
    private boolean scrollable = false;

    private boolean inBounds = false;

    private int numberOfLines = 0;

    private boolean scrollingVertical = false;
    private boolean scrollingHorizontal = false;
    private boolean verticalHovered = false;
    private boolean horizontalHovered = false;
    private VerticalScrollBar verticalScrollBar;
    private HorizontalScrollBar horizontalScrollBar;

    float longestLine = 0;

    int startIndex = 0;
    int endIndex;

    float defaultHeight;
    Vector2f scaleFactor = new Vector2f(1, 1);

    private static final float[] VERTICES = {
            0, 0,
            0, -1
    };
    public RawModel ruler;

    public CodeWindowController(Vector2f position, Vector2f size, Vector3f backgroundColor, Vector3f textColor, Vector3f borderColor, String content, FontType font, float fontSize, float thickness, float borderWidth, float border, float headerHeight, TextLineController textLineController){
        this.textLineController = textLineController;
        this.codeWindow = new CodeWindow();
        this.codeWindow.setPosition(position);
        this.codeWindow.setSize(size);
        defaultHeight = 2-headerHeight;
        this.codeWindow.setBackgroundColor(backgroundColor);
        this.codeWindow.setBorderColor(borderColor);
        this.padding = border;

        //Split the input text on each line
        String[] lines = content.split("\n");
        //Texts will start being added with a slight offset from the top
        float minHeight = border;

        //Calculate the bounds of the window to be used for clipping text
        codeWindow.setPositionBounds(new Vector4f(position.x, position.y, position.x + size.x, position.y + size.y));

        //The length of the longest line number will be needed later
        float longestLineNumber = 0;

        //Create a parser that is used for parsing each line
        //TODO: Make parser static and remove this. OK I DO IT :( U LIKE? :)
        //Parser parser = new Parser();


        //List that holds the lines that were added, used to prevent concurrent modification
        List<EditableFormattedTextLine> newLines = new ArrayList<>();

        //For each line in the file
        for (String line : lines){
            numberOfLines++;

            //Create the line
            //Use parsers single line functionality to generate one line
            //EditableFormattedTextLine formattedTextLine = parser.getFormattedLine(line);
            EditableFormattedTextLine formattedTextLine = GlobalParser.PARSER_MANAGER.getFormattedLine(line);

            //And add the line number to it
            LineNumberWord lineNumberWord = new LineNumberWord(Integer.toString(numberOfLines), new Vector2f(-1, position.y-minHeight+size.y));
            formattedTextLine.getWords()[0] = lineNumberWord;
            //Set it's position
            formattedTextLine.setPosition(new Vector2f(border + position.x,position.y-minHeight+size.y));
            //Add it to the list of new lines
            newLines.add(formattedTextLine);

            //Update the line
            if(lineNumberWord.getLength() > longestLineNumber){
                longestLineNumber = (float) lineNumberWord.getLength();
            }

            //The next line will be offset by the height of a line
            minHeight += GeneralSettings.FONT_HEIGHT;
        }
        //If a line was added
        if(newLines.size() > 0){
            //Set them each to use a line number offset based on the longest line number found
            newLines.get(0).setLineNumberOffset(longestLineNumber + padding);
            //Add each line
            for(EditableFormattedTextLine textLine : newLines){
                textLineController.addCodeWindowTextLine(textLine, -1);
            }
        }
        //Generate the character edges used for editing texts and find the longest line
        for(EditableFormattedTextLine line : textLineController.getCodeWindowTextLines()){
            line.generateCharacterEdges();
            if(line.getLength() > longestLine){
                longestLine = line.getLength();
            }
        }

        //Create the filled box
        this.codeWindow.setTextNumberFilledBox(new GUIFilledBox(new Vector2f(position.x, position.y), new Vector2f(longestLineNumber*2 + 2*border, size.y), GeneralSettings.USERPREF.getTexteditorLinenumberBGColor3f()));
        this.codeWindow.setGuiFilledBox(new GUIFilledBox(new Vector2f(longestLineNumber*2+border*2, 0), new Vector2f(size.x - longestLineNumber*2 + border*2, size.y), backgroundColor));

        //Calculate the max positions used for scrolling
        maxVerticalPosition = minHeight-size.y;
        maxHorizontalPosition = longestLine + codeWindow.getTextNumberFilledBox().getSize().x + codeWindow.getSize().x/2;// - size.x;

        //Create component elements
        cursorController = new CursorController(new Cursor(GeneralSettings.TEXT_COLOR), this);
        verticalScrollBar = new VerticalScrollBar(new Vector2f(-0.02f, -.92f), 0.02f, size.y-0.08f, size.y, maxVerticalPosition + size.y, 0);
        horizontalScrollBar = new HorizontalScrollBar(new Vector2f(position.x + codeWindow.getTextNumberFilledBox().getSize().x, -1f),0.03f, size.x-codeWindow.getTextNumberFilledBox().getSize().x-0.03f, size.x-codeWindow.getTextNumberFilledBox().getSize().x, longestLine*2, 0);

        //Update the aspect ratio in case aspect ratio was changed before opening this file
        aspectRatio = new Vector2f(GeneralSettings.ASPECT_RATIO.m00, GeneralSettings.ASPECT_RATIO.m11);
        endIndex = numberOfLines - 1;

        //Generate the vao for the ruler
        ruler = Loader.loadToVAO(VERTICES, 2);
        populateVBO(GlobalParser.PARSER_MANAGER.getRules(), ruler.getVaoID());

        updateAspectRatio(aspectRatio, headerHeight);


//        scroll(maxVerticalPosition);
//        scrollHorizontal(maxHorizontalPosition, horizontalScrollBar.getFactor());

    }

    public int populateVBO(Ruler ruler, int vao){
        int instanceCount = ruler.size() - 1;

        int vbo = Loader.createEmptyVbo(instanceCount*GeneralSettings.RULER_INSTANCED_DATA_LENGTH, GL15.GL_STATIC_DRAW);
        Loader.addInstanceAttribute(vao, vbo, 1, 4, GeneralSettings.RULER_INSTANCED_DATA_LENGTH, 0);
        Loader.addInstanceAttribute(vao, vbo, 2, 3, GeneralSettings.RULER_INSTANCED_DATA_LENGTH, 4);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(instanceCount*GeneralSettings.RULER_INSTANCED_DATA_LENGTH);
        float data[] = new float[instanceCount*GeneralSettings.RULER_INSTANCED_DATA_LENGTH];
        int i = 0;
        for(int j = 1; j < ruler.size(); j++){
                data[i] = codeWindow.getCodeWindowPosition().x + ruler.get(j-1)+padding;//.getPositions().get(j).x;
                i++;
                data[i] = codeWindow.getCodeWindowPosition().y;
                i++;
                data[i] = codeWindow.getCodeWindowPosition().x + ruler.get(j-1)+padding;
                i++;
                data[i] = codeWindow.getCodeWindowPosition().y + codeWindow.getCodeWindowSize().y;
                i++;
                data[i] = 1;//line.getColor().x;
                i++;
                data[i] = 1;//line.getColor().y;
                i++;
                data[i] = 1;//line.getColor().z;
                i++;
        }
        Loader.updateVbo(vbo, data, buffer);
        return instanceCount;
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
        maxVerticalPosition -= minVerticalPosition;
        maxVerticalPosition += codeWindow.getSize().y/this.aspectRatio.y*scaleFactor.y;
        codeWindow.setSize(new Vector2f(codeWindow.getSize().x, height));

        //Calculate new max horizontal position
//        maxHorizontalPosition = maxHorizontalPosition - codeWindow.getTextNumberFilledBox().getSize().x;
//        maxHorizontalPosition += minHorizontalPosition;
//        maxHorizontalPosition = maxHorizontalPosition*this.aspectRatio.x / scaleFactor.x;

        //Set various sizes to the appropriate values
        codeWindow.getTextNumberFilledBox().setSize(new Vector2f(lineNumberWidth, height));
        codeWindow.getGuiFilledBox().setPosition(new Vector2f(lineNumberWidth-1, -1));
        codeWindow.getGuiFilledBox().setSize(new Vector2f(codeWindow.getSize().x-lineNumberWidth, height));

        //Calculate the new scale factors
        scaleFactor = new Vector2f(1*aspectRatio.x, height*aspectRatio.y/defaultHeight);


        maxVerticalPosition -= codeWindow.getSize().y/aspectRatio.y*scaleFactor.y;
//        maxHorizontalPosition = maxHorizontalPosition / aspectRatio.x * scaleFactor.x;
//        maxHorizontalPosition += lineNumberWidth;

        minVerticalPosition = (defaultHeight/scaleFactor.y-defaultHeight)/2;
        minHorizontalPosition = (1/scaleFactor.x-1);

        maxHorizontalPosition = (longestLine)/aspectRatio.x*scaleFactor.x - codeWindow.getCodeWindowSize().x/2/scaleFactor.x;

        maxVerticalPosition += minVerticalPosition;
        maxHorizontalPosition += minHorizontalPosition;

        //TODO: Fix this to actually change the position to appear the same
        contentsVerticalPosition = 0 + minVerticalPosition;// - (height-defaultHeight);
        contentsHorizontalPosition = 0 + minHorizontalPosition;

        //The texts start at the top of the window
//        float startingHeight = codeWindow.getSize().y - 1;
//        startingHeight /= aspectRatio.y;

        //For each text
//        for(EditableFormattedTextLine line:textLineController.getCodeWindowTextLines()){
//            //Change the horizontal position based on the change to the line number size
//            line.changeContentsHorizontalPosition(-EditableFormattedTextLine.getLineNumberOffset(), true);
//
//            //Set the position
//            line.setPosition(new Vector2f((codeWindow.getPosition().x+padding*4f)/aspectRatio.x, startingHeight), true);//+contentsVerticalPosition));
//            line.getWords()[0].getPosition().x =(-1 + padding)/aspectRatio.x;
//
//            //Reset contents horizontal position
//            line.changeContentsHorizontalPosition(EditableFormattedTextLine.getLineNumberOffset(), true);
//            startingHeight -= GeneralSettings.FONT_HEIGHT;
//        }
        unloadTextsResize(aspectRatio.y >= this.aspectRatio.y);

        //Save the aspect ratio for future changes
        this.aspectRatio = new Vector2f(aspectRatio);

        //Update related entities
        verticalScrollBar.updateAspectRatio(0.02f*aspectRatio.x, codeWindow.getSize().y, codeWindow.getSize().y-0.03f*aspectRatio.y, maxVerticalPosition + codeWindow.getSize().y);
        float range = codeWindow.getSize().x - codeWindow.getTextNumberFilledBox().getSize().x;
        horizontalScrollBar.updateAspectRatio(new Vector2f(codeWindow.getGuiFilledBox().getPosition()), 0.03f*aspectRatio.y, range, range -0.02f*aspectRatio.x, (maxHorizontalPosition)*aspectRatio.x/scaleFactor.x);//*aspectRatio.x*scaleFactor.x);
        cursorController.updateAspectRatio();
        textLineController.update(textLineController.getCodeWindowTextLines().get(0), 0, '\0');

    }

    /**
     * Scrolls vertically
     * @param scrollChange the amount to change
     */
    public void scroll(float scrollChange){
        if(scrollChange != 0) {
            //If the contents are larger than the window
            if (maxVerticalPosition > codeWindow.getSize().y) {
                float newPosition = contentsVerticalPosition + scrollChange;
                //Update the position of the contents, cursor, and scroll bar
                //If the position would be negative change position so it would be 0 instead
                if (newPosition < minVerticalPosition) {
                    changeContentsVerticalPosition(minVerticalPosition-contentsVerticalPosition);
                    cursorController.scroll();
                }
                //If the position would be greater than max make it so it will be max instead
                else if (newPosition > maxVerticalPosition) {
                    changeContentsVerticalPosition(maxVerticalPosition - contentsVerticalPosition);
                    cursorController.scroll();

                }
                //Otherwise scroll
                else {
                    changeContentsVerticalPosition(scrollChange);
                    cursorController.scroll();
                }
            }
            unloadTexts();
        }
    }

    /**
     * Scrolls horizontally
     * @param scrollChange the amount to change
     * @param factor the scaling factor used by the horizontal scroll bar
     */
    public void scrollHorizontal(float scrollChange, float factor){
        if(scrollChange != 0) {
            //If the contents are larger than the window
            if (maxHorizontalPosition > codeWindow.getSize().x) {
                float newPosition = contentsHorizontalPosition + scrollChange;
                //If the position would be negative change position so it would be 0 instead
                if (newPosition < minHorizontalPosition) {
                    changeContentsHorizontalPosition(contentsHorizontalPosition-minHorizontalPosition, factor);
                    cursorController.scroll();
                }
                //If the position would be greater than max make it so it will be max instead
                else if (newPosition > maxHorizontalPosition) {
                    changeContentsHorizontalPosition(-(maxHorizontalPosition - contentsHorizontalPosition), factor);
                    cursorController.scroll();
                }
                //Otherwise scroll
                else {
                    changeContentsHorizontalPosition(-scrollChange, factor);
                    cursorController.scroll();
                }
            }
        }
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
        if(endIndex == numberOfLines - 1){
            endIndex = newNumberOfLines - 1;
        }
        numberOfLines = newNumberOfLines;
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
     * processes type events
     * @param c the char that was typed
     */
    public void type(char c){
        //Pass the type event to the cursor controller to process
        cursorController.type(c);
    }

    /**
     * Processes clicks of the left mouse button
     * @return true if the event should be processed no further, false if the event needs more processing
     */
    public boolean mouseLeftClick(){
        if(inBounds){
            //If vertical  scroll bar was clicked start scrolling vertically
            if(verticalHovered){
                scrollingVertical = true;
            }
            //If horizontal scroll bar was clicked start scrolling horizontally
            else if(horizontalHovered){
                scrollingHorizontal = true;
            }
            //Move the physical cursor to the mouse's position
            //If a scroll bar was just grabbed do not move the cursor
            else{
                cursorController.moveCursor(new Vector2f(mousePosition.x+contentsHorizontalPosition, mousePosition.y+contentsVerticalPosition), this);
            }

            //The event has been processesd, return true
            return true;
        }else{
            //The cursor should not be rendered and this is no longer selected
            cursorController.setVisible(false);
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
     * Processes events related to mouse movements
     * @param mousePosition
     */
    public void moveMouse(Vector2f mousePosition){
        //If vertical  scroll bar was clicked start scrolling vertically
        if(mousePosition.x > verticalScrollBar.getFilledBox().getPosition().x && mousePosition.y > verticalScrollBar.getFilledBox().getPosition().y
                && mousePosition.x < verticalScrollBar.getFilledBox().getPosition().x + verticalScrollBar.getFilledBox().getSize().x
                && mousePosition.y < verticalScrollBar.getFilledBox().getPosition().y + verticalScrollBar.getFilledBox().getSize().y){
            verticalHovered = true;
            Mouse.setHand();
        }else{
            verticalHovered = false;
        }
        //If horizontal scroll bar was clicked start scrolling horizontally
        if(mousePosition.x > horizontalScrollBar.getFilledBox().getPosition().x && mousePosition.y > horizontalScrollBar.getFilledBox().getPosition().y
                && mousePosition.x < horizontalScrollBar.getFilledBox().getPosition().x + horizontalScrollBar.getFilledBox().getSize().x
                && mousePosition.y < horizontalScrollBar.getFilledBox().getPosition().y + horizontalScrollBar.getFilledBox().getSize().y){
            horizontalHovered = true;
            Mouse.setHand();
        }else{
            horizontalHovered = false;
        }
        //If the position did not exist this will cause a crash or weird behavior
        if(this.mousePosition != null) {
            //If the vertical scroll bar is selected scroll appropriately
            if(scrollingVertical) {
                scroll(-((mousePosition.y - this.mousePosition.y)) / verticalScrollBar.getFactor() / aspectRatio.y);
            }
            //If the horizontal scroll bar is selected scroll appropriately
            else if(scrollingHorizontal){
                scrollHorizontal(((mousePosition.x - this.mousePosition.x)) / horizontalScrollBar.getFactor() / aspectRatio.x, horizontalScrollBar.getFactor());
            }
        }
        //Save the position
        this.mousePosition = new Vector2f(mousePosition);
        //Determine if the mouse is in the window
        if(!inBounds && mousePosition.x > codeWindow.getPosition().x && mousePosition.y > codeWindow.getPosition().y && mousePosition.x < codeWindow.getPosition().x + codeWindow.getSize().x && mousePosition.y < codeWindow.getPosition().y + codeWindow.getSize().y){
            inBounds = true;

        }else if(inBounds && !(mousePosition.x > codeWindow.getPosition().x && mousePosition.y > codeWindow.getPosition().y && mousePosition.x < codeWindow.getPosition().x + codeWindow.getSize().x && mousePosition.y < codeWindow.getPosition().y + codeWindow.getSize().y)){
            inBounds = false;
        }
        //Change the cursor
        if(inBounds && !horizontalHovered && !verticalHovered) {
            Mouse.setIBeam();
        }
    }

    /**
     * Changes the position of each text line vertically
     * @param change the amount to change
     */
//    public void changeContentsVerticalPosition(float change){
//        //Update each line
//        for(FormattedTextLine text : textLineController.getLoadedTexts()){
//            if(text instanceof EditableFormattedTextLine) {
//                text.changeVerticalPosition(change);
//            }
//        }
//        //Update the saved position
//        contentsVerticalPosition += change;
//        verticalScrollBar.changePosition(change);
//    }
    public void changeContentsVerticalPosition(float change){
        //Update each line
//        for(FormattedTextLine text : textLineController.getCodeWindowTextLines()){
//            text.changeVerticalPosition(change);
//        }
        //Update the saved position
        contentsVerticalPosition += change;
        verticalScrollBar.changePosition(change);
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
     * Moves the boundary between the code window and flowchart window
     * @param change
     */
    public void moveBoundary(float change){
        //Change the size of this window
        codeWindow.getSize().x += change;
        codeWindow.getGuiFilledBox().getSize().x += change;

        //Adjust scroll bars
        //The vertical scroll bar needs to be moved
        verticalScrollBar.changeXPosition(change);
        //The horizontal scroll bar needs it's range adjusted
        horizontalScrollBar.changeWindowWidth(change);
    }

    /**
     * Clears the text which was included in this code window
     */
    public void clear(){
        for(EditableFormattedTextLine line : textLineController.getCodeWindowTextLines()){
            textLineController.unloadText(line, 0);
        }
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
     * Change the background color the code window
     * @param newColor Vector3f
     * */
    public void changeCodewindowBGcolor3f(Vector3f newColor) {
        codeWindow.setBackgroundColor(newColor);
    }

    /**
     * Change the background color of the linenumber bar
     * @param newColor Vector3f
     * */
    public void changeCodewindowLinenumberBGColor3f(Vector3f newColor) {
        codeWindow.setTextNumberFilledBoxBackgroundColor(newColor);
    }

    /**
     * Change the background color of the scroll bars
     * @param newColor Vector3f
     * */
    public void changeScrollBarsColor3f(Vector3f newColor) {
        this.horizontalScrollBar.setBackgroundColor(newColor);
        this.verticalScrollBar.setBackgroundColor(newColor);
    }

    public TextLineController getTextLineController() {
        return textLineController;
    }



    public VerticalScrollBar getVerticalScrollBar() {
        return verticalScrollBar;
    }

    public HorizontalScrollBar getHorizontalScrollBar() {
        return horizontalScrollBar;
    }

    public boolean isHoveringScroll(){
        return verticalHovered || horizontalHovered;
    }

    public void unloadTexts(){
        //If the first line is above the screen then lines need to be unloaded at the top and loaded at the bottom
        if(textLineController.getCodeWindowTextLines().get(startIndex).getPosition().y/aspectRatio.y*scaleFactor.y + contentsVerticalPosition > 1f/aspectRatio.y){
//            //Unload the lines at the top
            EditableFormattedTextLine line = textLineController.getCodeWindowTextLines().get(startIndex);
            while(line.getPosition().y/aspectRatio.y*scaleFactor.y + contentsVerticalPosition > 1f/aspectRatio.y){
                textLineController.unloadText(line, contentsVerticalPosition);
                startIndex++;
                line = textLineController.getCodeWindowTextLines().get(startIndex);
            }
            //Load lines at the bottom
            line = textLineController.getCodeWindowTextLines().get(endIndex);
            while(line.getPosition().y/aspectRatio.y*scaleFactor.y + contentsVerticalPosition > -1f/aspectRatio.y && endIndex < textLineController.getCodeWindowTextLines().size()){
                textLineController.loadText(line, contentsVerticalPosition);
                endIndex++;
                //Will cause crash for last line if not included
                if(endIndex < textLineController.getCodeWindowTextLines().size()) {
                    line = textLineController.getCodeWindowTextLines().get(endIndex);
                }
            }
            //Ensure that array overflow does not occur if the last text is loaded
            if(endIndex == textLineController.getCodeWindowTextLines().size()){
                endIndex--;
            }
        }
        //Otherwise load lines at the top and unload them at the bottom
        else{
            //Load the lines at the top
            EditableFormattedTextLine line = textLineController.getCodeWindowTextLines().get(startIndex);
            while(line.getPosition().y/aspectRatio.y*scaleFactor.y + contentsVerticalPosition < 1f/aspectRatio.y && startIndex >= 0){
                line = textLineController.getCodeWindowTextLines().get(startIndex);
                textLineController.loadText(line, contentsVerticalPosition);
                startIndex--;
            }
            //Ensure that array underflow does not occur if text one is loaded
            if(startIndex < 0){
                startIndex = 0;
            }
            //Unload lines at the bottom
            line = textLineController.getCodeWindowTextLines().get(endIndex);
            while(line.getPosition().y/aspectRatio.y*scaleFactor.y + contentsVerticalPosition < -1f/aspectRatio.y){
                textLineController.unloadText(line, contentsVerticalPosition);
                endIndex--;
                line = textLineController.getCodeWindowTextLines().get(endIndex);
            }
        }
    }

    public void unloadTextsResize(boolean decrease){
        //If the first line is above the screen then lines need to be unloaded at the top and loaded at the bottom
        if(decrease){
            //Unload the lines at the top
            EditableFormattedTextLine line = textLineController.getCodeWindowTextLines().get(startIndex);
            while(line.getPosition().y/aspectRatio.y*scaleFactor.y + contentsVerticalPosition > 1f/aspectRatio.y){
                textLineController.unloadText(line, contentsVerticalPosition);
                startIndex++;
                line = textLineController.getCodeWindowTextLines().get(startIndex);
            }
            //Unload lines at the bottom
            line = textLineController.getCodeWindowTextLines().get(endIndex);
            while(line.getPosition().y/aspectRatio.y*scaleFactor.y + contentsVerticalPosition < -1f/aspectRatio.y){
                textLineController.unloadText(line, contentsVerticalPosition);
                endIndex--;
                line = textLineController.getCodeWindowTextLines().get(endIndex);
            }
        }
        //Otherwise load lines at the top and unload them at the bottom
        else{
            //Load the lines at the top
            EditableFormattedTextLine line = textLineController.getCodeWindowTextLines().get(startIndex);
            while(line.getPosition().y/aspectRatio.y*scaleFactor.y + contentsVerticalPosition < 1f/aspectRatio.y && startIndex >= 0){
                line = textLineController.getCodeWindowTextLines().get(startIndex);
                textLineController.loadText(line, contentsVerticalPosition);
                startIndex--;
            }
            //Ensure that array underflow does not occur if text one is loaded
            if(startIndex < 0){
                startIndex = 0;
            }
            //Load lines at the bottom
            line = textLineController.getCodeWindowTextLines().get(endIndex);
            while(line.getPosition().y/aspectRatio.y*scaleFactor.y + contentsVerticalPosition > -1f/aspectRatio.y && endIndex < textLineController.getCodeWindowTextLines().size()){
                textLineController.loadText(line, contentsVerticalPosition);
                endIndex++;
                //Will cause crash for last line if not included
                if(endIndex < textLineController.getCodeWindowTextLines().size()) {
                    line = textLineController.getCodeWindowTextLines().get(endIndex);
                }
            }
            //Ensure that array overflow does not occur if the last text is loaded
            if(endIndex == textLineController.getCodeWindowTextLines().size()){
                endIndex--;
            }
        }
    }

    public float getContentsVerticalPosition() {
        return contentsVerticalPosition;
    }

    public float getContentsHorizontalPosition() {
        return contentsHorizontalPosition;
    }

    public Vector2f getScaleFactor(){
        return scaleFactor;
    }

    public float getMinHorizontalPosition() {
        return minHorizontalPosition;
    }
}
