package gui;

import gui.fontMeshCreator.FontType;
import gui.fontMeshCreator.TextMeshData;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;

public class TextLine {

    public String textString;
    private static float fontSize = GeneralSettings.FONT_SIZE;

    List<TextWord> words;
    Vector2f position = new Vector2f();
    float length = 0;


    public TextLine(List<TextWord> words){
        this.words = words;
    }

    public List<TextWord> getWords(){
        return words;
    }

    public Vector2f getPosition(){
        return position;
    }

    public void setLength(float length){
        this.length = length;
    }

    public float getLength(){
        if(length < 1){
            length = 0;
            int numberOfCharacters = 0;
            for(TextWord word : words){
                if(word.getSeparator().length() == 1 && word.getSeparator().charAt(0) == ' '){
                    length += word.getSpaceSize();
                }else if(word.getSeparator().length() == 1 && word.getSeparator().charAt(0) == '\t'){
                    length += word.getSpaceSize() * ((numberOfCharacters % GeneralSettings.CHARACTERS_PER_SPACE) == 0 ? GeneralSettings.CHARACTERS_PER_SPACE : numberOfCharacters % GeneralSettings.CHARACTERS_PER_SPACE);
                }
                length += word.getCharacterEdges()[word.getCharacterEdges().length-1];
                numberOfCharacters += word.getCharacterEdges().length-1;
            }
        }
        return length;
    }
}
