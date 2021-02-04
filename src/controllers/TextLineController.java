package controllers;

import gui.texts.EditableFormattedTextLine;
import gui.texts.LineNumberWord;
import gui.texts.FormattedTextLine;
import gui.texts.TextWord;
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
            if (word.getSeparator().length() == 1 && word.getSeparator().charAt(0) == ' ') {
                offset += word.getSpaceSize();
            } else if (word.getSeparator().length() == 1 && word.getSeparator().charAt(0) == '\t') {
                offset += word.getSpaceSize() * (GeneralSettings.DEFAULT_TAB_WIDTH-numberOfCharacters%4);
                numberOfCharacters = 0;
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
            if (word.getSeparator().length() == 1 && word.getSeparator().charAt(0) == ' ') {
                offset += word.getSpaceSize();
            } else if (word.getSeparator().length() == 1 && word.getSeparator().charAt(0) == '\t') {
                offset += word.getSpaceSize() * (GeneralSettings.DEFAULT_TAB_WIDTH-numberOfCharacters%4);
                numberOfCharacters = 0;
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

    public EditableFormattedTextLine split(EditableFormattedTextLine line, int characterIndex){
        int index = codeWindowFormattedTextLines.indexOf(line);
        EditableFormattedTextLine originalLine = parser.getFormattedLine(line.getTextString().substring(0, characterIndex-1));
        EditableFormattedTextLine newLine = parser.getFormattedLine(line.getTextString().substring(characterIndex-1));

        originalLine.setPosition(line.getPosition());
        newLine.setPosition(line.getPosition());

        originalLine.getWords()[0] = line.getWords()[0];
        EditableFormattedTextLine lastLine = newLine;
        for(int i = index+1; i < codeWindowFormattedTextLines.size() - 1; i++){
            lastLine.getWords()[0] = codeWindowFormattedTextLines.get(i).getWords()[0];
            lastLine = codeWindowFormattedTextLines.get(i);
        }
        System.out.println(originalLine.getWords().length);
        codeWindowFormattedTextLines.remove(line);
        addCodeWindowTextLine(originalLine, index);
        addCodeWindowTextLine(newLine, index + 1);
        float change = GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR;
        for(int i = index + 1; i < codeWindowFormattedTextLines.size(); i++){
            EditableFormattedTextLine thisLine = codeWindowFormattedTextLines.get(i);
            thisLine.changeVerticalPosition(-change);
        }
        return newLine;
    }

    public EditableFormattedTextLine update(EditableFormattedTextLine line, int index, char c){
        String string = line.getTextString().substring(0, index);
        string += c;
        string += line.getTextString().substring(index);
        System.out.println(string);
        EditableFormattedTextLine newLine = parser.getFormattedLine(string);
        newLine.setPosition(line.getPosition());
        newLine.getWords()[0] = line.getWords()[0];
        codeWindowFormattedTextLines.set(codeWindowFormattedTextLines.indexOf(line), newLine);
        return newLine;
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
