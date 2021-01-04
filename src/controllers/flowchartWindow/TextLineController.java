package controllers.flowchartWindow;

import gui.TextLine;
import gui.TextWord;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TextLineController {
    List<TextLine> flowchartTextLines = new ArrayList<>();
    List<TextLine> codeWindowTextLines = new ArrayList<>();

    /**
     *
     */
    public TextLineController() {

    }

    /**
     * @param line
     * @return
     */
    public TextLine addFlowchartTextLine(TextLine line) {
        float offset = 0;
        int numberOfCharacters = 0;
        for (TextWord word : line.getWords()) {
            if (word.getSeparator().length() == 1 && word.getSeparator().charAt(0) == ' ') {
                offset += word.getSpaceSize();
            } else if (word.getSeparator().length() == 1 && word.getSeparator().charAt(0) == '\t') {
                offset += word.getSpaceSize() * ((numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH) == 0 ? GeneralSettings.DEFAULT_TAB_WIDTH : numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH);
            }
            word.setPosition(new Vector2f(line.getPosition().x + offset, line.getPosition().y));
            offset += word.getCharacterEdges()[word.getCharacterEdges().length - 1] * 2;
            numberOfCharacters += word.getCharacterEdges().length - 1;
        }
        flowchartTextLines.add(line);
        return line;
    }

    /**
     * @param line
     * @return
     */
    public TextLine addCodeWindowTextLine(TextLine line) {
        float offset = 0;
        int numberOfCharacters = 0;
        for (TextWord word : line.getWords()) {
            if (word.getSeparator().length() == 1 && word.getSeparator().charAt(0) == ' ') {
                offset += word.getSpaceSize();
            } else if (word.getSeparator().length() == 1 && word.getSeparator().charAt(0) == '\t') {
                offset += word.getSpaceSize() * ((numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH) == 0 ? GeneralSettings.DEFAULT_TAB_WIDTH : numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH);
            }
            word.setPosition(new Vector2f(line.getPosition().x + offset, line.getPosition().y));
            offset += word.getCharacterEdges()[word.getCharacterEdges().length - 1] * 2;
            numberOfCharacters += word.getCharacterEdges().length - 1;
        }
        codeWindowTextLines.add(line);
        return line;
    }


    /**
     * @return
     */
    public List<TextLine> getFlowchartTextLines() {
        return flowchartTextLines;
    }


    /**
     * @return
     */
    public List<TextLine> getCodeWindowTextLines() {
        return codeWindowTextLines;
    }

    /**
     *
     */
    public void clear() {
        flowchartTextLines.clear();
    }


}
