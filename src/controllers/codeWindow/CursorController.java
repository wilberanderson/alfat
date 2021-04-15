package controllers.codeWindow;

import gui.Cursor;
import gui.texts.EditableFormattedTextLine;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import utils.Printer;

import java.util.List;

public class CursorController {
    Cursor cursor;
    CodeWindowController codeWindow;
    List<EditableFormattedTextLine> texts;
    EditableFormattedTextLine currentText;
    private Vector2f aspectRatio;
    private int lineIndex;
    private int characterIndex;
    private boolean visible = false;
    private boolean toggle = true;
    private int savedCharacterIndex = -1;
    private Vector2f codeWindowOffset = new Vector2f(0, 0);
    private Vector2f originalAspectRatio;
    boolean originalAspectRatioSet = false;

    public CursorController(Cursor cursor, CodeWindowController codeWindow){
        this.cursor = cursor;
        this.codeWindow = codeWindow;
    }

    /**
     * When the user clicks on the screen the cursor should move to the closest boundary between two characters to that position
     * @param mousePosition
     * @param codeWindow
     */
    public void moveCursor(Vector2f mousePosition, CodeWindowController codeWindow, Vector2f codeWindowOffset){
        this.codeWindowOffset = codeWindowOffset;
        this.codeWindow = codeWindow;
        texts = codeWindow.getTextLineController().getCodeWindowTextLines();
        this.aspectRatio = new Vector2f(codeWindow.getAspectRatio());

        //Make sure the cursor is visible
        visible = true;

        //The saved character index should no longer be restored
        savedCharacterIndex = -1;

        //Modify the position by aspect ratio to support screen resizes
        mousePosition.x /= aspectRatio.x;
        mousePosition.y /= aspectRatio.y;

        //Test height will be used to find which text was just selected
        float testHeight = mousePosition.y;
        currentText = null;
        //For each text
        for (int i = 0; i < texts.size(); i++){
            //Find the texts height
            float newHeight = texts.get(i).getPosition().y+codeWindowOffset.y;
            //If this is the last text and the cursor is still lower then the cursor should jump up to this position
            if(i+1 == texts.size()){
                mousePosition.y = newHeight;
                currentText = texts.get(i);
                continue;
            }
            //If the height is closest to this texts height set the cursor to have the same height
            if (testHeight < newHeight  && testHeight > texts.get(i+1).getPosition().y+codeWindowOffset.y){
                mousePosition.y = newHeight;
                currentText = texts.get(i);
                break;
            }
        }
        //If a text was found determine the closest character boundary in that text
        if (currentText != null) {
            //Save the index of the selected text
            lineIndex = texts.indexOf(currentText);
            //character edges defines where boundaries between two characters are
            float[] characterEdges = currentText.getCharacterEdges();
            //Character edges is relative to the texts origin, create a test width in this scale
            float testWidth = mousePosition.x - (currentText.getPosition().x-codeWindowOffset.x);

            //For each edge except the last check to see if the cursor is closer to it than the next one, if so that is the closest character boundary
            int i;
            for (i = 0; i < characterEdges.length-1; i++){
                if (Math.abs(characterEdges[i+1]*2 - testWidth) > Math.abs(characterEdges[i]*2 - testWidth)){
                    mousePosition.x = (characterEdges[i]*2 + currentText.getPosition().x);
                    break;
                }
            }

            //If i is equal to the last index then the last position is the closest
            if(i == characterEdges.length-1){
                mousePosition.x = (characterEdges[i]*2 + currentText.getPosition().x);
            }
            //Save the index for editing texts
            characterIndex = i;
        }
        //The mouse position has been found, move the cursor to that position
        updatePosition();
        //cursor.setPosition(mousePosition);
    }

    /**
     * Moves the cursor one character to the left
     */
    public void moveLeft(){
        if(currentText != null) {
            //The saved character index should no longer be restored
            savedCharacterIndex = -1;

            //Try to move the cursor
            characterIndex -= 1;
            //If the cursor did not wrap onto a new line then update it's position
            if (characterIndex >= 0) {
                updateXPosition(true);
            }
            //If the cursor wrapped onto a new line
            else {
                //If this was not the very first line move to the end of the previous line
                if (lineIndex > 0) {
                    lineIndex--;
                    currentText = texts.get(lineIndex);
                    characterIndex = currentText.getCharacterEdges().length - 1;
                    if (characterIndex < 0) {
                        characterIndex = 0;
                    }
                    updatePosition();
                }
                //If this is the first line character index should not change and position does not need to be updated
                else {
                    characterIndex = 0;
                }
            }
        }
    }


    /**
     * Moves the cursor one character to the right
     */
    public void moveRight(){
        if(currentText != null) {
            //The saved character index should no longer be restored
            savedCharacterIndex = -1;

            //Try to move the cursor
            characterIndex += 1;
            //If the cursor did not wrap onto a new line then update it's position
            if (characterIndex < currentText.getCharacterEdges().length) {
                updateXPosition(true);
            }
            //If the cursor wrapped onto a new line
            else {
                //If this was not the very last line move to the beginning of the next line
                if (lineIndex < texts.size() - 1) {
                    lineIndex++;
                    currentText = texts.get(lineIndex);
                    characterIndex = 0;
                    updatePosition();
                }
                //If this is the last line character index should not change and position does not need to be updated
                else {
                    characterIndex = currentText.getCharacterEdges().length - 1;
                }
            }
        }
    }

    /**
     * Moves the cursor one line down
     */
    public void moveDown(){
        if(currentText != null) {
            //If this is not the very last text
            if (lineIndex < texts.size() - 1) {
                //Move down
                lineIndex++;
                currentText = texts.get(lineIndex);

                //Try to use the saved character index as the character index
                if (savedCharacterIndex != -1) {
                    characterIndex = savedCharacterIndex;
                }

                //Ensure that character index is within the bounds of the text
                if (characterIndex > currentText.getCharacterEdges().length - 1) {
                    //Save the character index
                    if (characterIndex > savedCharacterIndex) {
                        savedCharacterIndex = characterIndex;
                    }
                    characterIndex = currentText.getCharacterEdges().length - 1;
                }

                //Update the position
                updatePosition();
            }
            //If this is the last line then the cursor should be moved to the end of the line
            else {
                //The saved character index should no longer be restored
                savedCharacterIndex = -1;

                characterIndex = currentText.getCharacterEdges().length - 1;
                updateXPosition(true);
            }
        }
    }

    /**
     * Moves the cursor one line down
     */
    public void moveUp(){
        if(currentText != null) {
            //If this is not the very first text
            if (lineIndex > 0) {
                //Move up
                lineIndex--;
                currentText = texts.get(lineIndex);

                //Try to use the saved character index as the character index
                if (savedCharacterIndex != -1) {
                    characterIndex = savedCharacterIndex;
                }

                //Ensure that character index is within the bounds of the text
                if (characterIndex > currentText.getCharacterEdges().length - 1) {
                    //Save the character index
                    if (characterIndex > savedCharacterIndex) {
                        savedCharacterIndex = characterIndex;
                    }
                    characterIndex = currentText.getCharacterEdges().length - 1;
                }

                //Update the position
                updatePosition();
            }
            //If this is the first line then put the cursor at the start of the line instead
            else {
                //The saved character index should no longer be restored
                savedCharacterIndex = -1;

                characterIndex = 0;
                updateXPosition(true);
            }
        }
    }

    /**
     * Deletes the single character preceding the cursor
     */
    public void backSpace(){
        if(currentText != null) {
            //The saved character index should no longer be restored
            savedCharacterIndex = -1;

            //If a character is being removed from a single line
            if (characterIndex > 0) {
                currentText = codeWindow.getTextLineController().backspace(currentText, characterIndex, true);
                characterIndex--;
            }
            //If a newline character is being removed two lines need to be merged
            else if (lineIndex > 0) {
                characterIndex = codeWindow.getTextLineController().getCodeWindowTextLines().get(lineIndex - 1).getCharacterEdges().length - 1;
                currentText = codeWindow.getTextLineController().merge(codeWindow.getTextLineController().getCodeWindowTextLines().get(lineIndex - 1), currentText, codeWindow);
                lineIndex--;
            }
            //The cursors character or line index changed, update accordingly
            updatePosition();
        }
    }

    /**
     * Deletes the single character following the cursor
     */
    public void delete(){
        if(currentText != null) {
            //The saved character index should no longer be restored
            savedCharacterIndex = -1;

            //If a character is being removed from a single line
            if (characterIndex < currentText.getCharacterEdges().length - 1) {
                currentText = codeWindow.getTextLineController().backspace(currentText, characterIndex, false);
            }
            //If a newline character is being removed two lines need to be merged
            else if (lineIndex < texts.size() - 1) {
                currentText = codeWindow.getTextLineController().merge(currentText, codeWindow.getTextLineController().getCodeWindowTextLines().get(lineIndex + 1), codeWindow);
            }
            //The cursors position is unchanged
        }
    }

    /**
     * Types a single character
     * @param c
     */
    public void type(char c){
        //Only type if a text is selected
        if(currentText != null) {
            //If the character is a newline then a new line should be created and the character and line index's should be updated as appropriate
            if (c == '\n') {
                currentText = codeWindow.getTextLineController().split(currentText, characterIndex, codeWindow);
                characterIndex = 0;
                lineIndex++;
            }
            //If the character that was typed is any other character then the line will not be split, update it with the typed character and update the character index
            else {
                currentText = codeWindow.getTextLineController().update(currentText, characterIndex, c);
                characterIndex++;
            }
            //After typing the position of the cursor will be different, update the position
            updatePosition();
        }
    }



    /**
     * Moves the cursor based on the scroll
     */
    public void scroll(Vector2f codeWindowOffset){
        this.codeWindowOffset = codeWindowOffset;
        if(this.currentText != null) {
            updateXPosition(false);
            updateYPosition(false);
        }
    }

    /**
     * Updates the x position of the mouse
     */
    private void updateXPosition(boolean adjustCodeWindow){
        //Move the cursor
        cursor.getPosition().x = currentText.getCharacterEdges()[characterIndex] * 2 + currentText.getPosition().x;

//        if(adjustCodeWindow) {
//            //If the cursor is outside the bounds of the code window scroll the code window to correct it
//            if ((codeWindow.getCodeWindow().getCodeWindowPosition().x) / aspectRatio.x > cursor.getPosition().x) {
//                codeWindow.changeContentsHorizontalPosition((codeWindow.getCodeWindow().getCodeWindowPosition().x) / aspectRatio.x - (cursor.getPosition().x), codeWindow.getHorizontalScrollBar().getFactor());
//                cursor.getPosition().x = (codeWindow.getCodeWindow().getCodeWindowPosition().x) / aspectRatio.x;
//            } else if (cursor.getPosition().x / aspectRatio.x > (codeWindow.getCodeWindow().getCodeWindowPosition().x + codeWindow.getCodeWindow().getCodeWindowSize().x)) {
//                codeWindow.changeContentsHorizontalPosition(-(cursor.getPosition().x - (codeWindow.getCodeWindow().getCodeWindowPosition().x + codeWindow.getCodeWindow().getCodeWindowSize().x) / aspectRatio.x), codeWindow.getHorizontalScrollBar().getFactor());
//                cursor.getPosition().x = (codeWindow.getCodeWindow().getCodeWindowPosition().x + codeWindow.getCodeWindow().getCodeWindowSize().x) / aspectRatio.x;
//            }
//        }
        cursor.getPosition().x -= codeWindowOffset.x;
    }

    /**
     * Updates the y position of the mouse
     */
    private void updateYPosition(boolean adjustCodeWindow){
        //Move the cursor
        cursor.getPosition().y = currentText.getPosition().y;

//        if(adjustCodeWindow) {
//            //If the cursor is outside the bounds of the code window scroll the code window to correct it
//            if ((codeWindow.getCodeWindow().getCodeWindowPosition().y) / aspectRatio.y > cursor.getPosition().y - GeneralSettings.FONT_HEIGHT) {
//                codeWindow.changeContentsVerticalPosition((codeWindow.getCodeWindow().getCodeWindowPosition().y) / aspectRatio.y - (cursor.getPosition().y - GeneralSettings.FONT_HEIGHT));
//                cursor.getPosition().y = currentText.getPosition().y;
//            } else if (cursor.getPosition().y / aspectRatio.y > (codeWindow.getCodeWindow().getCodeWindowPosition().y + codeWindow.getCodeWindow().getCodeWindowSize().y)) {
//                codeWindow.changeContentsVerticalPosition(-(cursor.getPosition().y - (codeWindow.getCodeWindow().getCodeWindowPosition().y + codeWindow.getCodeWindow().getCodeWindowSize().y) / aspectRatio.y));
//                cursor.getPosition().y = currentText.getPosition().y;
//            }
//        }
        cursor.getPosition().y += codeWindowOffset.y;
    }

    /**
     * Updates the position of the mouse
     */
    private void updatePosition(){
        updateXPosition(true);
        updateYPosition(true);
    }

    public void updateAspectRatio(Vector2f codeWindowOffset){
        this.codeWindowOffset = codeWindowOffset;
        if(!originalAspectRatioSet){
            originalAspectRatio = new Vector2f(codeWindow.getAspectRatio());

        }
        this.aspectRatio = new Vector2f(codeWindow.getAspectRatio());
        if(currentText != null) {
            updatePosition();
            // Printer.print("Position updated");
        }
    }

    public CodeWindowController getCodeWindow(){
        return codeWindow;
    }

    public Cursor getCursor(){
        return cursor;
    }

    public Vector2f getAspectRatio(){
        return aspectRatio;
    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }

    public boolean isVisible(){
        return visible && toggle;
    }

}
