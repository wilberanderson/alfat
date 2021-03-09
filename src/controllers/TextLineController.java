package controllers;

import controllers.codeWindow.CodeWindowController;
import gui.texts.*;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import parser.GlobalParser;
import parser.Parser;
import utils.Printer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TextLineController {
    List<FormattedTextLine> flowchartFormattedTextLines = new ArrayList<>();
    List<EditableFormattedTextLine> codeWindowFormattedTextLines = new ArrayList<>();
    List<FormattedTextLine> loadedTexts = new ArrayList<>();
    //Parser parser = new Parser();

    /**
     *
     */
    public TextLineController() {

    }

    /**
     * Adds a text line which will be rendered when rendering the flowchart
     * @param line the line to be added
     * @return the line which was added
     */
    public FormattedTextLine addFlowchartTextLine(FormattedTextLine line) {
        float offset = 0;
        int numberOfCharacters = 0;
        //For each text word
        for (TextWord word : line.getWords()) {
            //If the text word is a separator
            if(word instanceof SeparatorWord){
                float spaceSize = word.getFont().getSpaceSize()/64;
                if(((SeparatorWord) word).getText().length() > 0) {
                    //If the separator is a space add one space size
                    if (((SeparatorWord) word).getText().charAt(0) == ' ') {
                        offset += spaceSize;
                    }
                    //Tabs are used for alignment, add enough tabs to meet the currently used tab count
                    else if (((SeparatorWord) word).getText().charAt(0) == '\t') {
                        offset += spaceSize * (GeneralSettings.DEFAULT_TAB_WIDTH - numberOfCharacters % 4);//((numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH) == 0 ? GeneralSettings.DEFAULT_TAB_WIDTH : numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH);
                        numberOfCharacters = 0;
                    }
                }
            }
            //Set the words position
            word.setPosition(new Vector2f(line.getPosition().x + offset, line.getPosition().y));
            //Update the offset for the next word to be placed at
            offset += word.getCharacterEdges()[word.getCharacterEdges().length - 1] * 2;
            //If the word is not the line number save the count of characters read for use in tab alignment
            if(!(word instanceof LineNumberWord)) {
                numberOfCharacters += word.getCharacterEdges().length - 1;
            }
        }

        //Save this word in text lines
        flowchartFormattedTextLines.add(line);
        return line;
    }
    /**
     * Adds a text line which will be rendered when rendering the text editor and
     * be able to be edited
     * @param line the line to be added
     * @param index the index where the line is to be added, -1 indicates the end
     * @return the line which was added
     */
    public void addCodeWindowTextLine(EditableFormattedTextLine line, int index){
        //If there is more than one word save the offset for positioning
        float offset;
        if(line.getWords().length > 1){
            offset = line.getPosition().x + EditableFormattedTextLine.getLineNumberOffset()*2 - line.getWords()[1].getPosition().x;
        }else{
            offset = 0;
        }

        int numberOfCharacters = 0;
        //For each text word
        int i =  0;
        for (TextWord word : line.getWords()) {
            i++;
            //Skip line numbers
            if(word instanceof LineNumberWord || word == null){
                continue;
            }

            //If the text word is a separator
            if(word instanceof SeparatorWord){
                float spaceSize = word.getFont().getSpaceSize()/64;
                if(((SeparatorWord) word).getText().length() > 0) {
                    //If the separator is a space add one space size
                    if (((SeparatorWord) word).getText().charAt(0) == ' ') {
                        offset += spaceSize;
                    }
                    //Tabs are used for alignment, add enough tabs to meet the currently used tab count
                    else if (((SeparatorWord) word).getText().charAt(0) == '\t') {
                        offset += spaceSize * 128 * (GeneralSettings.DEFAULT_TAB_WIDTH - numberOfCharacters % 4);//((numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH) == 0 ? GeneralSettings.DEFAULT_TAB_WIDTH : numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH);
                        numberOfCharacters = 0;
                    }
                }
            }

            //Set the words position
            word.setPosition(new Vector2f(line.getPosition().x + offset, line.getPosition().y));
            //Update the offset for the next word to be placed at
            offset += word.getCharacterEdges()[word.getCharacterEdges().length - 1] * 2;
            //If the word is not the line number save the count of characters read for use in tab alignment
            if(!(word instanceof LineNumberWord)) {
                numberOfCharacters += word.getCharacterEdges().length - 1;
            }
        }
        //The edges of characters are needed to be known for editing, generate them
        line.generateCharacterEdges();
        //An index of -1 indicates that it should be added to the end of the line rather than a specific index
        if(index == -1){
            codeWindowFormattedTextLines.add(line);
        }else {
            codeWindowFormattedTextLines.add(index, line);
        }
        loadedTexts.add(line);
    }

    /**
     * Splits a single text line into two
     * @param line the line to be split
     * @param characterIndex the index where the split is to be made
     * @param controller the code window controller which needs to be updated
     * @return the line which is to be selected by the cursor controller
     */
    public EditableFormattedTextLine split(EditableFormattedTextLine line, int characterIndex, CodeWindowController controller){
        //The index of of the line being split is saved
        int index = codeWindowFormattedTextLines.indexOf(line);
        //Create two new lines
        EditableFormattedTextLine originalLine;
        EditableFormattedTextLine newLine;

        //If the line is being split at an index greater than 0
        if(characterIndex > 0) {
            //Create new lines using the appropriate portions of the text string
            //originalLine = parser.getFormattedLine(line.getTextString().substring(0, characterIndex));
            originalLine = GlobalParser.PARSER_MANAGER.getFormattedLine(line.getTextString().substring(0, characterIndex));

            //newLine = parser.getFormattedLine(line.getTextString().substring(characterIndex));
            newLine = GlobalParser.PARSER_MANAGER.getFormattedLine(line.getTextString().substring(characterIndex));


        }
        //If the index is 0 the first line should be generated with an empty string
        else{
            //originalLine = parser.getFormattedLine("");
            originalLine = GlobalParser.PARSER_MANAGER.getFormattedLine("");
            //newLine = parser.getFormattedLine(line.getTextString());
            newLine = GlobalParser.PARSER_MANAGER.getFormattedLine(line.getTextString());
        }

        //Set both lines to be at the original lines position
        originalLine.setPosition(new Vector2f(line.getPosition()));
        newLine.setPosition(new Vector2f(line.getPosition()));

        //Set originalLines line number to be the one from original lines
        originalLine.getWords()[0] = line.getWords()[0];

        //For each line remaining in the file the line numbers should be associated with one word earlier
        EditableFormattedTextLine lastLine = newLine;
        for(int i = index+1; i < codeWindowFormattedTextLines.size(); i++){
            lastLine.getWords()[0] = codeWindowFormattedTextLines.get(i).getWords()[0];
            lastLine = codeWindowFormattedTextLines.get(i);
        }

        //Tell the controller how the number of lines changed
        controller.changeNumberOfLines(1);

        //Remove the old line and add the two new lines
        codeWindowFormattedTextLines.remove(line);
        addCodeWindowTextLine(originalLine, index);
        if(index < codeWindowFormattedTextLines.size() - 1) {
            addCodeWindowTextLine(newLine, index + 1);

            //Update the positions of all remaining lines
            float change = GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR;
            for(int i = index + 1; i < codeWindowFormattedTextLines.size(); i++){
                EditableFormattedTextLine thisLine = codeWindowFormattedTextLines.get(i);
                thisLine.changeContentsVerticalPosition(-change);
            }

            //The last line needs a new line number
            LineNumberWord newLineNumber = new LineNumberWord(Integer.toString(controller.getNumberOfLines()), new Vector2f(lastLine.getWords()[0].getPosition().x, lastLine.getPosition().y));
            lastLine.getWords()[0] = newLineNumber;
        }else{
            //The last line needs a new line number
            LineNumberWord newLineNumber = new LineNumberWord(Integer.toString(controller.getNumberOfLines()), new Vector2f(originalLine.getWords()[0].getPosition().x, originalLine.getPosition().y));
            newLine.getWords()[0] = newLineNumber;

            addCodeWindowTextLine(newLine, index + 1);
            newLine.changeVerticalPosition(-GeneralSettings.FONT_HEIGHT);
        }

        //After splitting a line the user expects the cursor to be on the newly created line
        return newLine;
    }

    /**
     * Merges two lines together
     * @param left the first line to be merged
     * @param right the second line to be merged
     * @param controller the code window controller which needs to be updated
     * @return the line which is to be selected by the cursor controller
     */
    public EditableFormattedTextLine merge(EditableFormattedTextLine left, EditableFormattedTextLine right, CodeWindowController controller){
        //Merge the two text strings together
        String string = left.getTextString()+right.getTextString();

        //Create the new line
        //EditableFormattedTextLine newLine = parser.getFormattedLine(string);

        EditableFormattedTextLine newLine = GlobalParser.PARSER_MANAGER.getFormattedLine(string);
        newLine.setPosition(left.getPosition());

        //Adjust the line numbers
        newLine.getWords()[0] = left.getWords()[0];
        for(int i = codeWindowFormattedTextLines.size()-1; i > codeWindowFormattedTextLines.indexOf(right); i--){
            codeWindowFormattedTextLines.get(i).getWords()[0] = codeWindowFormattedTextLines.get(i-1).getWords()[0];
        }

        //Adjust the line contents
        for(int i = codeWindowFormattedTextLines.indexOf(right)+1; i < codeWindowFormattedTextLines.size(); i++){
            codeWindowFormattedTextLines.get(i).changeContentsVerticalPosition(GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR);
        }

        //Generate the character edges used to reposition the cursor
        newLine.generateCharacterEdges();

        //Delete right and replace left
        codeWindowFormattedTextLines.remove(right);
        replaceCodeWindowTextLine(newLine, codeWindowFormattedTextLines.indexOf(left));

        //Update the number of lines which the controller has to ensure proper behavior of scrolling
        controller.changeNumberOfLines(-1);

        //Return the line so that the cursor controller knows what line the cursor is currently in
        return newLine;
    }

    public void replaceCodeWindowTextLine(EditableFormattedTextLine line, int index){
        //If there is more than one word save the offset for positioning
        float offset;
        if(line.getWords().length > 1){
            offset = line.getPosition().x + EditableFormattedTextLine.getLineNumberOffset()*2 - line.getWords()[1].getPosition().x;
        }else{
            offset = 0;
        }

        int numberOfCharacters = 0;
        //For each text word
        for (TextWord word : line.getWords()) {
            //Skip line numbers
            if(word instanceof LineNumberWord){
                continue;
            }

            //If the text word is a separator
            if(word instanceof SeparatorWord){
                float spaceSize = word.getFont().getSpaceSize()/64;
                if(((SeparatorWord) word).getText().length() > 0) {
                    //If the separator is a space add one space size
                    if (((SeparatorWord) word).getText().charAt(0) == ' ') {
                        offset += spaceSize;
                    }
                    //Tabs are used for alignment, add enough tabs to meet the currently used tab count
                    else if (((SeparatorWord) word).getText().charAt(0) == '\t') {
                        offset += spaceSize * 128*(GeneralSettings.DEFAULT_TAB_WIDTH - numberOfCharacters % 4);//((numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH) == 0 ? GeneralSettings.DEFAULT_TAB_WIDTH : numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH);

                        Printer.print(spaceSize*128*(GeneralSettings.DEFAULT_TAB_WIDTH - numberOfCharacters%4));
                        numberOfCharacters = 0;
                    }
                }
            }

            //Set the words position
            word.setPosition(new Vector2f(line.getPosition().x + offset, line.getPosition().y));
            //Update the offset for the next word to be placed at
            offset += word.getCharacterEdges()[word.getCharacterEdges().length - 1] * 2;
            //If the word is not the line number save the count of characters read for use in tab alignment
            if(!(word instanceof LineNumberWord)) {
                numberOfCharacters += word.getCharacterEdges().length - 1;
            }
        }
        //The edges of characters are needed to be known for editing, generate them
        line.generateCharacterEdges();

        //The old text should no longer be rendered and the new text should now be rendered
        loadedTexts.remove(codeWindowFormattedTextLines.get(index));
        loadedTexts.add(line);

        //Replace the line at this index with the new line
        codeWindowFormattedTextLines.set(index, line);
    }

    /**
     * Updates a text line to have one additional character
     * @param line
     * @param index
     * @param c
     * @return
     */
    public EditableFormattedTextLine update(EditableFormattedTextLine line, int index, char c){
        //Get the first portion of the string
        String string = line.getTextString().substring(0, index);
        //Add the new character if one is being added
        if(c != '\0') {
            string += c;
        }
        //Add the remainder of the original string
        string += line.getTextString().substring(index);

        //Create the new line
        //EditableFormattedTextLine newLine = parser.getFormattedLine(string);

        EditableFormattedTextLine newLine = GlobalParser.PARSER_MANAGER.getFormattedLine(string);
        //Set it's position
        newLine.setPosition(line.getPosition());
        //Set it to use the old lines line number
        newLine.getWords()[0] = line.getWords()[0];
        //Replace the old text line
        replaceCodeWindowTextLine(newLine, codeWindowFormattedTextLines.indexOf(line));
        
        return newLine;
    }

    public EditableFormattedTextLine backspace(EditableFormattedTextLine line, int characterIndex, boolean backspace){
        //If action is backspace
        if(backspace){
            //Create a string that contains all of the lines text except for the character before the cursor
            String string = line.getTextString().substring(0, characterIndex-1);
            string += line.getTextString().substring(characterIndex);

            //Create a new line with this string and the
            //EditableFormattedTextLine newLine = parser.getFormattedLine(string);
            EditableFormattedTextLine newLine = GlobalParser.PARSER_MANAGER.getFormattedLine(string);

            newLine.setPosition(line.getPosition());
            newLine.getWords()[0] = line.getWords()[0];

            //Replace the old line in codeWindowFormattedTextLines
            replaceCodeWindowTextLine(newLine, codeWindowFormattedTextLines.indexOf(line));

            //Return the line so that the cursor controller knows what line the cursor is currently in
            return newLine;
        }
        //If action is delete
        else{
            //Create a string that contains all of the lines text except for the character after the cursor
            String string = line.getTextString().substring(0, characterIndex);
            string += line.getTextString().substring(characterIndex+1);

            //Create a new line with this string and the
            //EditableFormattedTextLine newLine = parser.getFormattedLine(string);
            EditableFormattedTextLine newLine = GlobalParser.PARSER_MANAGER.getFormattedLine(string);

            newLine.setPosition(line.getPosition());
            newLine.getWords()[0] = line.getWords()[0];

            //Replace the old line in codeWindowFormattedTextLines
            replaceCodeWindowTextLine(newLine, codeWindowFormattedTextLines.indexOf(line));

            //Return the line so that the cursor controller knows what line the cursor is currently in
            return newLine;
        }
    }


    /**
     * @return
     */
    public List<FormattedTextLine> getFlowchartTextLines() {
        return flowchartFormattedTextLines;
    }


    /**
     * @return
     */
    public List<EditableFormattedTextLine> getCodeWindowTextLines() {
        return codeWindowFormattedTextLines;
    }

    public List<FormattedTextLine> getLoadedTexts(){
        return loadedTexts;
    }

    /**
     *
     */
    public void clear() {
        flowchartFormattedTextLines.clear();
    }

    public void unloadText(FormattedTextLine textLine){
        loadedTexts.remove(textLine);
    }

    public void loadText(FormattedTextLine textLine){
        //Duplicates may be added, ensure that the text is only added if it is not in the list currently
        if(loadedTexts.indexOf(textLine) == -1){
            loadedTexts.add(textLine);
        }
    }


}
