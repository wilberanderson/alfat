package gui.texts;

import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import utils.Printer;

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

    /**
     * On creation character edges will not know where the edges of characters in words are.
     * This method generates the edge locations and compiles them into one list used to edit
     * texts.
     */
    public void generateCharacterEdges(){
        //Find the number of characters in the content words
        int numberOfEdges = 0;
        for(TextWord word: words){
            if(!(word instanceof LineNumberWord) && word != null ) {
                numberOfEdges += word.getCharacterEdges().length;
            }
        }

        //Create an array to hold them
        characterEdges = new float[numberOfEdges + 1];

        //Populate the array
        int numberOfCharacters = 0;
        int index = 0;
        if(characterEdges.length > 0) {
            //Load the first edge
            characterEdges[0] = lineNumberOffset;
//            characterEdges[1] = words[0].getCharacterEdges()[0] + lineNumberOffset;
            index = 1;
            float last = lineNumberOffset;
            //Load the edges for each word
            for (TextWord word : words) {
                //Skip line number words
                if (!(word instanceof LineNumberWord) && word != null) {
                    //Determine space size to be used
                    float spaceSize = word.getFont().getSpaceSize();
                    if(word instanceof SeparatorWord){
                        if(((SeparatorWord) word).getText().length() > 0) {
                            //If the seperator is a space add one space size
                            if (((SeparatorWord) word).getText().charAt(0) == ' ') {
                                last += spaceSize;
                                numberOfCharacters++;
                            }
                            //Tabs align text, add space size appropriate to the number of tabs needed for alignment
                            else if (((SeparatorWord) word).getText().charAt(0) == '\t') {
                                last += spaceSize * (GeneralSettings.DEFAULT_TAB_WIDTH - numberOfCharacters % 4);//((numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH) == 0 ? GeneralSettings.DEFAULT_TAB_WIDTH : numberOfCharacters % GeneralSettings.DEFAULT_TAB_WIDTH);
                                numberOfCharacters = 0;
                            }
                        }
                    }
                    //For each character in character edges save it to character edges
                    for (int i = 0; i < word.getCharacterEdges().length; i++) {
                        characterEdges[index] = word.getCharacterEdges()[i] + last;
                        index++;
                    }
                    //Update which character edge was last used
                    last = characterEdges[index - 1];

                    //Update the number of characters
                    numberOfCharacters += word.getCharacterEdges().length-1;
                }
            }
        }
        Printer.print("Edge" + characterEdges[0]);
        //This may produce duplicate entries, remove any duplicate entries
        removeDuplicateCharacterEdges();
        Printer.print(characterEdges[0]);
    }

    /**
     * generateCharacterEdges may produce multiple character edges with the same offset.
     * This method removes these duplicates to prevent unexpected editing behavior
     */
    private void removeDuplicateCharacterEdges(){
        //Find the number of duplicates
        int duplicateCount = 0;
        for(int i = 1; i < characterEdges.length; i++){
            if(characterEdges[i-1] == characterEdges[i]){
                duplicateCount++;
            }
        }

        //Create a new array that will not include these duplicates
        float[] newEdges = new float[characterEdges.length-duplicateCount];

        //Populate the new array
        int index = 0;
        int i;
        for(i = 1; i < characterEdges.length; i++){
            if(characterEdges[i-1] != characterEdges[i]){
                newEdges[index] = characterEdges[i-1];
                index++;
            }
        }
        //If the last word had more than one character then the last character edge will be lost, save it
        if(i > 0) {
            newEdges[index] = characterEdges[i - 1];
        }

        //Update character edges
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

    /**
     * Updates the offset the line number causes in the word
     * @param lineNumberOffset the new line number offset
     */
    public void setLineNumberOffset(float lineNumberOffset) {
        //Update each words position
        for(TextWord word : words){
            if(!(word instanceof LineNumberWord)){
                word.getPosition().x = word.getPosition().x + (lineNumberOffset - EditableFormattedTextLine.lineNumberOffset)*4;
            }
        }
        //Update the saved offset
        EditableFormattedTextLine.lineNumberOffset = lineNumberOffset;
    }

    /**
     * Used to set the position if line need to be changed
     * @param position the new position
     * @param changeLineNumbers
     */
    public void setPosition(Vector2f position, boolean changeLineNumbers){
        //Update positions of words
        for(TextWord word : words) {
            if (((word instanceof LineNumberWord) && changeLineNumbers || !(word instanceof LineNumberWord)) && word != null) {
                word.setPosition(new Vector2f(position.x + (word.getPosition().x - this.position.x), word.getPosition().y + position.y - this.position.y));
            }
        }
        //Update position
        this.position = position;
    }
}
