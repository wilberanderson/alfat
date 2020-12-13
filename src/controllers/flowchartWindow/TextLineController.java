package controllers.flowchartWindow;

import gui.TextLine;
import gui.TextWord;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;
import rendering.text.TextMaster;

import java.util.ArrayList;
import java.util.List;

public class TextLineController {

    public TextLineController(){

    }

    List<TextLine> textLines = new ArrayList<>();

    public TextLine add(TextLine line){
        float offset = 0;
        int numberOfCharacters = 0;
        for(TextWord word : line.getWords()){
            if(word.getSeparator().length() == 1 && word.getSeparator().charAt(0) == ' '){
                offset += word.getSpaceSize();
            }else if(word.getSeparator().length() == 1 && word.getSeparator().charAt(0) == '\t'){
                offset += word.getSpaceSize() * ((numberOfCharacters % GeneralSettings.CHARACTERS_PER_SPACE) == 0 ? GeneralSettings.CHARACTERS_PER_SPACE : numberOfCharacters % GeneralSettings.CHARACTERS_PER_SPACE);
            }
            word.setPosition(new Vector2f(line.getPosition().x+offset, line.getPosition().y));
            offset += word.getCharacterEdges()[word.getCharacterEdges().length-1]*2;
            numberOfCharacters += word.getCharacterEdges().length-1;
        }
//        line.setLength(offset);
        textLines.add(line);
        return line;
    }

    public List<TextLine> getTextLines(){
        return textLines;
    }

    public void clear(){
//        for(TextLine text : textLines){
//            for(TextWord word : text.getWords()){
//                TextMaster.removeText(word);
//            }
//        }
        textLines.clear();
    }




}
