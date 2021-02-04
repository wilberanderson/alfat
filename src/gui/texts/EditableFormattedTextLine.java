package gui.texts;

import main.GeneralSettings;

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
                    if (word.getSeparator().equals(" ")) {
                        last += word.getFont().getSpaceSize();
                    } else if (word.getSeparator().equals("\t")) {
                        numberOfCharacters %= 4;
                        last += word.getFont().getSpaceSize() * (GeneralSettings.DEFAULT_TAB_WIDTH-numberOfCharacters);
                        numberOfCharacters = 0;
                    }
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
}
