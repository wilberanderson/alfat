package gui.texts;

import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

/**
 *
 */
public class TextLine {

    private static float fontSize = GeneralSettings.FONT_SIZE;
    //List<TextWord> words;
    TextWord words[];
    Vector2f position = new Vector2f();
    float length = 0;
    private static float spaceSize;

    /**
     * @param words
     */
    public TextLine(List<TextWord> words) {
        words.add(0, null);
        this.words = new TextWord[words.size()];
        for(int i = 0; i < words.size(); i++){
            this.words[i] = words.get(i);
        }
    }

    /**
     * @return
     */
    public TextWord[] getWords() {
        return words;
    }

    /**
     * @return
     */
    public Vector2f getPosition() {
        return position;
    }

    /**
     * @return
     */
    public float getLength() {
        if (length < 1) {
            length = 0;
            int numberOfCharacters = 0;
            for (TextWord word : words) {
                if(word == null){
                    continue;
                }
                if (word.getSeparator().length() == 1 && word.getSeparator().charAt(0) == ' ') {
                    length += spaceSize;
                } else if (word.getSeparator().length() == 1 && word.getSeparator().charAt(0) == '\t') {
                    length += spaceSize * ((numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH) == 0 ? GeneralSettings.DEFAULT_TAB_WIDTH : numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH);
                }
                length += word.getCharacterEdges()[word.getCharacterEdges().length - 1];
                numberOfCharacters += word.getCharacterEdges().length - 1;
            }
        }
        return length;
    }

    /**
     * @param length
     */
    public void setLength(float length) {
        this.length = length;
    }
}
