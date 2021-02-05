package controllers;

import controllers.codeWindow.CodeWindowController;
import gui.texts.*;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import parser.Parser;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TextLineController {
    List<FormattedTextLine> flowchartFormattedTextLines = new ArrayList<>();
    List<EditableFormattedTextLine> codeWindowFormattedTextLines = new ArrayList<>();
    Parser parser = new Parser();

    /**
     *
     */
    public TextLineController() {

    }

    /**
     * @param line
     * @return
     */
    public FormattedTextLine addFlowchartTextLine(FormattedTextLine line) {
        float offset = 0;
        int numberOfCharacters = 0;
        for (TextWord word : line.getWords()) {
            if(word instanceof SeparatorWord){
                float spaceSize = word.getFont().getSpaceSize()/64;
                if(((SeparatorWord) word).getText().length() > 0) {
                    if (((SeparatorWord) word).getText().charAt(0) == ' ') {
                        offset += spaceSize;
                    } else if (((SeparatorWord) word).getText().charAt(0) == '\t') {
                        offset += spaceSize * (GeneralSettings.DEFAULT_TAB_WIDTH - numberOfCharacters % 4);//((numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH) == 0 ? GeneralSettings.DEFAULT_TAB_WIDTH : numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH);
                        numberOfCharacters = 0;
                    }
                }
            }

            word.setPosition(new Vector2f(line.getPosition().x + offset, line.getPosition().y));
            offset += word.getCharacterEdges()[word.getCharacterEdges().length - 1] * 2;
            if(!(word instanceof LineNumberWord)) {
                numberOfCharacters += word.getCharacterEdges().length - 1;
            }
        }
        flowchartFormattedTextLines.add(line);
        return line;
    }

    public void addCodeWindowTextLine(EditableFormattedTextLine line, int index){
        float offset;
        if(line.getWords().length > 1){
            offset = line.getPosition().x + EditableFormattedTextLine.getLineNumberOffset()*2 - line.getWords()[1].getPosition().x;
        }else{
            offset = 0;
        }
        int numberOfCharacters = 0;
        for (TextWord word : line.getWords()) {
            if(word instanceof LineNumberWord){
                continue;
            }

            if(word instanceof SeparatorWord){
                float spaceSize = word.getFont().getSpaceSize()/64;
                if(((SeparatorWord) word).getText().length() > 0) {
                    if (((SeparatorWord) word).getText().charAt(0) == ' ') {
                        offset += spaceSize;
                    } else if (((SeparatorWord) word).getText().charAt(0) == '\t') {
                        offset += spaceSize * (GeneralSettings.DEFAULT_TAB_WIDTH - numberOfCharacters % 4);//((numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH) == 0 ? GeneralSettings.DEFAULT_TAB_WIDTH : numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH);
                        numberOfCharacters = 0;

                    }
                }
            }
            word.setPosition(new Vector2f(line.getPosition().x + offset, line.getPosition().y));
            offset += word.getCharacterEdges()[word.getCharacterEdges().length - 1] * 2;
            if(!(word instanceof LineNumberWord)) {
                numberOfCharacters += word.getCharacterEdges().length - 1;
            }
        }
        line.generateCharacterEdges();
        if(index == -1){
            codeWindowFormattedTextLines.add(line);
        }else {
            codeWindowFormattedTextLines.add(index, line);
        }
    }

    public EditableFormattedTextLine split(EditableFormattedTextLine line, int characterIndex, CodeWindowController controller){
        int index = codeWindowFormattedTextLines.indexOf(line);
        EditableFormattedTextLine originalLine;
        EditableFormattedTextLine newLine;
        if(characterIndex > 0) {
             originalLine = parser.getFormattedLine(line.getTextString().substring(0, characterIndex - 1));
             newLine = parser.getFormattedLine(line.getTextString().substring(characterIndex - 1));
        }else{
            originalLine = parser.getFormattedLine("");
            newLine = parser.getFormattedLine(line.getTextString());
        }
        originalLine.setPosition(new Vector2f(line.getPosition()));
        newLine.setPosition(line.getPosition());

        originalLine.getWords()[0] = line.getWords()[0];
        EditableFormattedTextLine lastLine = newLine;
        for(int i = index+1; i < codeWindowFormattedTextLines.size(); i++){
            lastLine.getWords()[0] = codeWindowFormattedTextLines.get(i).getWords()[0];
            lastLine = codeWindowFormattedTextLines.get(i);
        }
        controller.changeNumberOfLines(1);
        codeWindowFormattedTextLines.remove(line);
        addCodeWindowTextLine(originalLine, index);
        addCodeWindowTextLine(newLine, index + 1);
        float change = GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR;
        for(int i = index + 1; i < codeWindowFormattedTextLines.size(); i++){
            EditableFormattedTextLine thisLine = codeWindowFormattedTextLines.get(i);
            thisLine.changeContentsVerticalPosition(-change);
        }
        LineNumberWord newLineNumber = new LineNumberWord(Integer.toString(controller.getNumberOfLines()), new Vector2f(lastLine.getWords()[0].getPosition().x, lastLine.getPosition().y));
        lastLine.getWords()[0] = newLineNumber;
        //TODO: Determine why this is needed
        // lastLine.changeVerticalPosition(0.009f);
        return newLine;
    }

    public EditableFormattedTextLine merge(EditableFormattedTextLine left, EditableFormattedTextLine right, CodeWindowController controller){
        //Merge the two text strings together
        String string = left.getTextString()+right.getTextString();

        //Create the new line
        EditableFormattedTextLine newLine = parser.getFormattedLine(string);
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
        float offset;
        if(line.getWords().length > 1){
            offset = line.getPosition().x + EditableFormattedTextLine.getLineNumberOffset()*2f - line.getWords()[1].getPosition().x;
        }else{
            offset = 0;
        }
        int numberOfCharacters = 0;
        for (TextWord word : line.getWords()) {
            if(word instanceof LineNumberWord){
                continue;
            }
            if(word instanceof SeparatorWord){
                float spaceSize = word.getFont().getSpaceSize()/64;
                if(((SeparatorWord) word).getText().length() > 0) {
                    if (((SeparatorWord) word).getText().charAt(0) == ' ') {
                        offset += spaceSize;
                    } else if (((SeparatorWord) word).getText().charAt(0) == '\t') {
                        offset += spaceSize * (GeneralSettings.DEFAULT_TAB_WIDTH - numberOfCharacters % 4);//((numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH) == 0 ? GeneralSettings.DEFAULT_TAB_WIDTH : numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH);
                        numberOfCharacters = 0;
                    }
                }
            }

//            if (word.getSeparator().length() == 1 && word.getSeparator().charAt(0) == ' ') {
//                offset += word.getSpaceSize();
//            } else if (word.getSeparator().length() == 1 && word.getSeparator().charAt(0) == '\t') {
//                offset += word.getSpaceSize() * (GeneralSettings.DEFAULT_TAB_WIDTH-numberOfCharacters%4);
//                numberOfCharacters = 0;
//            }
            word.setPosition(new Vector2f(line.getPosition().x + offset, line.getPosition().y));
            offset += word.getCharacterEdges()[word.getCharacterEdges().length - 1] * 2;
            if(!(word instanceof LineNumberWord)) {
                numberOfCharacters += word.getCharacterEdges().length - 1;
            }
        }
        line.generateCharacterEdges();
        codeWindowFormattedTextLines.set(index, line);
    }

    public EditableFormattedTextLine update(EditableFormattedTextLine line, int index, char c){
        String string = line.getTextString().substring(0, index);
        if(c != '\0') {
            string += c;
        }
        string += line.getTextString().substring(index);
        EditableFormattedTextLine newLine = parser.getFormattedLine(string);
        newLine.setPosition(line.getPosition());
        newLine.getWords()[0] = line.getWords()[0];
        replaceCodeWindowTextLine(newLine, codeWindowFormattedTextLines.indexOf(line));
        //codeWindowFormattedTextLines.set(codeWindowFormattedTextLines.indexOf(line), newLine);
        return newLine;
    }

    public EditableFormattedTextLine backspace(EditableFormattedTextLine line, int characterIndex, boolean backspace){
        //If action is backspace
        if(backspace){
            //Create a string that contains all of the lines text except for the character before the cursor
            String string = line.getTextString().substring(0, characterIndex-1);
            string += line.getTextString().substring(characterIndex);

            //Create a new line with this string and the
            EditableFormattedTextLine newLine = parser.getFormattedLine(string);
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
            EditableFormattedTextLine newLine = parser.getFormattedLine(string);
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

    /**
     *
     */
    public void clear() {
        flowchartFormattedTextLines.clear();
    }


}
