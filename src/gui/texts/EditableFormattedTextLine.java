package gui.texts;

import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

public class EditableFormattedTextLine extends FormattedTextLine{
    String textString;
    float[] characterEdges;
    static float fontSize = GeneralSettings.FONT_SIZE;
    private static float lineNumberOffset = 0;

    public EditableFormattedTextLine(List<TextWord> words, String textString){
        super(words);
        this.textString = textString;
    }

    public void generateCharacterEdges(){
        int numberOfEdges = 0;
        for(TextWord word: words){
            if(!(word instanceof LineNumberWord)) {
                numberOfEdges += word.getCharacterEdges().length;
            }
        }
        characterEdges = new float[numberOfEdges];
        int numberOfCharacters = 0;
        int index = 0;
        if(characterEdges.length > 0) {
            characterEdges[0] = words[0].getCharacterEdges()[0] + lineNumberOffset*2;
            float last = lineNumberOffset;
            for (TextWord word : words) {
                if (!(word instanceof LineNumberWord)) {
                    float spaceSize = word.getFont().getSpaceSize();
                    if(word instanceof SeparatorWord){
                        if(((SeparatorWord) word).getText().length() > 0) {
                            if (((SeparatorWord) word).getText().charAt(0) == ' ') {
                                length += spaceSize;
                            } else if (((SeparatorWord) word).getText().charAt(0) == '\t') {
                                length += spaceSize * (GeneralSettings.DEFAULT_TAB_WIDTH - numberOfCharacters % 4);//((numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH) == 0 ? GeneralSettings.DEFAULT_TAB_WIDTH : numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH);
                                numberOfCharacters = 0;
                            }
                        }
                    }
//                    if (word.getSeparator().equals(" ")) {
//                        last += word.getFont().getSpaceSize();
//                    } else if (word.getSeparator().equals("\t")) {
//                        numberOfCharacters %= 4;
//                        last += word.getFont().getSpaceSize() * (GeneralSettings.DEFAULT_TAB_WIDTH-numberOfCharacters);
//                        numberOfCharacters = 0;
//                    }
                    for (int i = 0; i < word.getCharacterEdges().length; i++) {
                        characterEdges[index] = word.getCharacterEdges()[i] + last;
                        index++;
                    }
                    last = characterEdges[index - 1];
                    numberOfCharacters += word.getCharacterEdges().length-1;
                }
            }
        }
        removeDuplicateCharacterEdges();
    }

    private void removeDuplicateCharacterEdges(){
        int duplicateCount = 0;
        for(int i = 1; i < characterEdges.length; i++){
            if(characterEdges[i-1] == characterEdges[i]){
                duplicateCount++;
            }
        }
        float[] newEdges = new float[characterEdges.length-duplicateCount];
        int index = 0;
        int i;
        for(i = 1; i < characterEdges.length; i++){
            if(characterEdges[i-1] != characterEdges[i]){
                newEdges[index] = characterEdges[i-1];
                index++;
            }
        }
        if(i > 1) {
            newEdges[index] = characterEdges[i - 1];
        }
        characterEdges = newEdges;
    }

    public String getTextString() {
        return textString;
    }

    public float[] getCharacterEdges() {
        return characterEdges;
    }

    public static float getFontSize(){
        return fontSize;
    }

    public static float getLineNumberOffset() {
        return lineNumberOffset;
    }

    public void setLineNumberOffset(float lineNumberOffset) {
        for(TextWord word : words){
            if(!(word instanceof LineNumberWord)){
                word.getPosition().x = word.getPosition().x - EditableFormattedTextLine.lineNumberOffset + lineNumberOffset;
            }
        }
        EditableFormattedTextLine.lineNumberOffset = lineNumberOffset;
    }

    public void changeContentsVerticalPosition(float offset){
        for(TextWord word : words){
            if(!(word instanceof LineNumberWord)){
                word.setPosition(new Vector2f(word.getPosition().x, word.getPosition().y+offset));
            }
        }
        position.y += offset;
    }

    public void changeContentsHorizontalPosition(float offset){
        for(TextWord word : words){
            if(!(word instanceof LineNumberWord)){
                word.setPosition(new Vector2f(word.getPosition().x+offset, word.getPosition().y));
            }
        }
        position.y += offset;
    }

    public void setPosition(Vector2f position, boolean changeLineNumbers){
        for(TextWord word : words) {
            if (((word instanceof LineNumberWord) && changeLineNumbers || !(word instanceof LineNumberWord)) && word != null) {
                word.setPosition(new Vector2f(position.x + (word.getPosition().x - this.position.x), word.getPosition().y + position.y - this.position.y));
            }
        }
        this.position = position;
    }
}
