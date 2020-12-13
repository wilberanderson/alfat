package gui.textBoxes;

import gui.GUIFilledBox;
import gui.GUIText;
import gui.TextLine;
import gui.TextWord;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;
import rendering.text.TextMaster;

import java.util.ArrayList;
import java.util.List;

public class FlowchartTextBox extends TextBox{

    private float lineHeight = GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR;
    private List<String> registers = new ArrayList<>();
    private List<TextLine> textLines = new ArrayList<>();
    private String alert;

    public FlowchartTextBox(Vector2f position, List<TextLine> textLines, int lineNumber, List<String> registers, String alert){
        super();
        super.setPosition(position);
        super.setBackgroundColor(GeneralSettings.TEXT_BOX_BACKGROUND_COLOR);
        super.setBorderColor(GeneralSettings.TEXT_BOX_BORDER_COLOR);
        super.setTextColor(GeneralSettings.TEXT_COLOR);
        this.registers = registers;
        this.alert = alert;
//        String[] lines = content.split("\n");
        float minHeight = GeneralSettings.TEXT_BOX_BORDER_WIDTH;
        double greatestLength = 0;
        float longestLineNumber = 0;
//        for (String line : lines){
//            GUIText text = new GUIText(line, GeneralSettings.FONT_SIZE, GeneralSettings.FONT, new Vector2f(GeneralSettings.TEXT_BOX_BORDER_WIDTH + position.x-1,position.y-minHeight - 1 - GeneralSettings.TEXT_BOX_BORDER_WIDTH + lineHeight*lines.length), GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, GeneralSettings.TEXT_COLOR, null, false, true, false);
//            super.getTexts().add(text);
//            if (text.getLength() > greatestLength){
//                greatestLength = text.getLength();
//            }
//
//            GUIText lineNumberText = new GUIText(Integer.toString(lineNumber), GeneralSettings.FONT_SIZE, GeneralSettings.FONT, new Vector2f(GeneralSettings.TEXT_BOX_BORDER_WIDTH + position.x-1, text.getPosition().y), GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, GeneralSettings.LINE_NUMBER_COLOR, null, false, true, false);
//            super.getLineNumbers().add(lineNumberText);
//            if(lineNumberText.getLength() > longestLineNumber){
//                longestLineNumber = (float) lineNumberText.getLength();
//            }
//
//            minHeight+=lineHeight;
//            lineNumber++;
//        }
        for(TextLine line : textLines){
            if(line.getLength() > greatestLength){
                greatestLength = line.getLength();
            }

            line.getPosition().setX(GeneralSettings.TEXT_BOX_BORDER_WIDTH*2 + position.x-1);
            line.getPosition().setY(position.y-minHeight - 1);
            this.textLines.add(line);

            GUIText lineNumberText = new GUIText(Integer.toString(lineNumber), GeneralSettings.FONT_SIZE, GeneralSettings.FONT, new Vector2f(GeneralSettings.TEXT_BOX_BORDER_WIDTH + position.x-1, line.getPosition().y+GeneralSettings.TEXT_BOX_BORDER_WIDTH + lineHeight*textLines.size()), GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, GeneralSettings.LINE_NUMBER_COLOR, null, false, true, false);
            super.getLineNumbers().add(lineNumberText);
            if(lineNumberText.getLength() > longestLineNumber){
                longestLineNumber = (float) lineNumberText.getLength();
            }

            minHeight+=lineHeight;
            lineNumber++;

        }
        if(super.getLineNumbers().size() > 0) {
            float offset = longestLineNumber*2;
            System.out.println(offset);
            for(TextLine line : this.textLines){
                line.getPosition().setX(line.getPosition().x + offset);
            }
        }


        super.setTextNumberFilledBox(new GUIFilledBox(position, new Vector2f(longestLineNumber*2 + 2*GeneralSettings.TEXT_BOX_BORDER_WIDTH, minHeight), GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR));
        super.setSize(new Vector2f((float)greatestLength*2 + 4*GeneralSettings.TEXT_BOX_BORDER_WIDTH + super.getTextNumberFilledBox().getSize().x,lineHeight*textLines.size() + GeneralSettings.TEXT_BOX_BORDER_WIDTH));
        super.setGuiFilledBox(new GUIFilledBox(position, super.getSize(), GeneralSettings.TEXT_BOX_BACKGROUND_COLOR));
        for(GUIText text : super.getTexts()){
            text.setPosition(new Vector2f(super.getTextNumberFilledBox().getPosition().x+super.getTextNumberFilledBox().getSize().x-1, text.getPosition().y));
        }
        for(GUIText text : super.getTexts()){
            if(text.getPositionBounds() == null){
                text.setPositionBounds(new Vector4f(position.x, position.y, position.x+super.getSize().x, position.y+super.getSize().y));
            }
        }
    }

    @Override
    public void setPosition(Vector2f position){
        changeHorizontalPosition(super.getPosition().x - position.x);
        changeVerticalPosition(-(super.getPosition().y - position.y));
        super.setPosition(position);
        super.getTextNumberFilledBox().setPosition(position);
        super.getGuiFilledBox().setPosition(new Vector2f(position.x + super.getTextNumberFilledBox().getSize().x, position.y));
    }

    public List<String> getRegisters(){
        return registers;
    }

    public String getAlert(){
        return alert;
    }

    public List<TextLine> getTextLines() {
        return textLines;
    }

    public void clear(){
        for (GUIText text: super.getTexts()) {
            TextMaster.removeText(text);
        }
        for (GUIText text: super.getLineNumbers()) {
            TextMaster.removeText(text);
        }
        super.getTexts().clear();
        textLines.clear();
    }

}
