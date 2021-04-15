package gui.texts;

import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

/**
 *
 */
public class FormattedTextLine {

    private static float fontSize = GeneralSettings.FONT_SIZE;
    //List<TextWord> words;
    TextWord words[];
    Vector2f position = new Vector2f();
    float length = 0;
    private static float spaceSize;

    /**
     * Creates a formatted text line with the content of all words passed into this
     * @param words The list of words. In general words may either contain text, or be separators which control spacing of words containing text
     */
    public FormattedTextLine(List<TextWord> words) {
        //A line number word will be added elsewhere
        words.add(0, null);

        //Populate words with the words passed in
        this.words = new TextWord[words.size()];
        for(int i = 0; i < words.size(); i++){
            this.words[i] = words.get(i);
        }

        //Position will be determined using this vector elsewhere
        this.position = new Vector2f(0, 0);
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
        //If length has already been calculated do not recalculate it
        if (length == 0) {
            //The number of characters, including ones in separators, in the word
            int numberOfCharacters = 0;
            for (TextWord word : words) {
                //Skip the line number word at the beginning
                if(word == null){
                    continue;
                }

                //If the word is a separator word the length may have multiple possible values
                if(word instanceof WhiteSpaceWord){
                    if(((WhiteSpaceWord) word).getText().length() > 0) {
                        //If the word is a space length should be advanced by space size
                        if (((WhiteSpaceWord) word).getText().charAt(0) == ' ') {
                            length += spaceSize;
                        }
                        //If the word is a tab then account for variable tab spacing
                        else if (((WhiteSpaceWord) word).getText().charAt(0) == '\t') {
                            length += spaceSize * (GeneralSettings.DEFAULT_TAB_WIDTH - numberOfCharacters % 4);//((numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH) == 0 ? GeneralSettings.DEFAULT_TAB_WIDTH : numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH);
                            numberOfCharacters = 0;
                        }
                    }
                }
                //Add the length of the word to the length, and save the number of characters
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

    public void setPosition(Vector2f position){
        //Update the position of each word that should be moved
        for(TextWord word : words) {
            if (!(word instanceof LineNumberWord) && word != null) {
                word.setPosition(new Vector2f(position.x + (word.getPosition().x - this.position.x), word.getPosition().y + position.y - this.position.y));
            }
        }
        //Update the position
        this.position = position;
    }

    public void changeVerticalPosition(float change){
        //Update each word
        for(TextWord word : words){
            word.setPosition(new Vector2f(word.getPosition().x, word.getPosition().y+change));
        }
        //Update the position
        position.y += change;
    }

    public void changeContentsVerticalPosition(float offset){
        //Update each word
        for(TextWord word : words){
            if(!(word instanceof LineNumberWord)){
                word.setPosition(new Vector2f(word.getPosition().x, word.getPosition().y+offset));
            }
        }
        //Update the position
        position.y += offset;
    }

    public void changeHorizontalPosition(float change){
        //Update each word
        for(TextWord word : words){
            word.setPosition(new Vector2f(word.getPosition().x+change, word.getPosition().y));
        }
        //Update the position
        position.x += change;
    }


    public void changeContentsHorizontalPosition(float offset, boolean adjustPosition){
        //Update each word
        for(TextWord word : words){
            if(!(word instanceof LineNumberWord)){
                word.setPosition(new Vector2f(word.getPosition().x+offset, word.getPosition().y));
            }
        }
        //If the position should be changed update it
        if(adjustPosition) {
            position.x += offset;
        }
    }
}
