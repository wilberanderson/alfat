package gui.textBoxes;

import gui.GUIFilledBox;
import gui.GUIText;
import gui.fontMeshCreator.FontType;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class FlowChartTextBox extends TextBox{

    private float lineHeight = GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR;

    public FlowChartTextBox(Vector2f position, Vector3f backgroundColor, Vector3f textColor, Vector3f borderColor, String content, FontType font, float fontSize, float thickness, float borderWidth, float padding, int lineNumber){
        super();
        super.setPosition(position);
        super.setBackgroundColor(backgroundColor);
        super.setBorderColor(borderColor);
        super.setTextColor(textColor);
        String[] lines = content.split("\n");
        float minHeight = padding;
        double greatestLength = 0;
        float longestLineNumber = 0;
        for (String line : lines){
            GUIText text = new GUIText(line, fontSize, font, new Vector2f(padding + position.x-1,position.y-minHeight - 1 - padding + lineHeight*lines.length), thickness, borderWidth, textColor, null, false, true, false);
            super.getTexts().add(text);
            if (text.getLength() > greatestLength){
                greatestLength = text.getLength();
            }

            GUIText lineNumberText = new GUIText(Integer.toString(lineNumber), fontSize, font, new Vector2f(padding + position.x-1, text.getPosition().y), thickness, borderWidth, textColor, null, false, true, false);
            super.getLineNumbers().add(lineNumberText);
            if(lineNumberText.getLength() > longestLineNumber){
                longestLineNumber = (float) lineNumberText.getLength();
            }

            minHeight+=lineHeight;
            lineNumber++;
        }
        super.setTextNumberFilledBox(new GUIFilledBox(position, new Vector2f(longestLineNumber*2 + 2*padding, minHeight), GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR));
        super.setSize(new Vector2f((float)greatestLength*2 + 4*padding + super.getTextNumberFilledBox().getSize().x,lineHeight*lines.length + padding));
        super.setGuiFilledBox(new GUIFilledBox(position, super.getSize(), backgroundColor));
        for(GUIText text : super.getTexts()){
            text.setPosition(new Vector2f(super.getTextNumberFilledBox().getPosition().x+super.getTextNumberFilledBox().getSize().x-1, text.getPosition().y));
        }
        for(GUIText text : super.getTexts()){
            if(text.getPositionBounds() == null){
                text.setPositionBounds(new Vector4f(position.x, position.y, position.x+super.getSize().x, position.y+super.getSize().y));
            }
        }
    }
}
