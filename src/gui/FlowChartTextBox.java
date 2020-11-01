package gui;

import fontMeshCreator.FontType;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class FlowChartTextBox extends TextBox{

    private float lineHeight = GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR;

    public FlowChartTextBox(Vector2f position, Vector3f backgroundColor, Vector3f textColor, Vector3f borderColor, String content, FontType font, float fontSize, float thickness, float borderWidth, float padding){
        super();
        super.setPosition(position);
        super.setBackgroundColor(backgroundColor);
        super.setBorderColor(borderColor);
        super.setTextColor(textColor);
        String[] lines = content.split("\n");
        float minHeight = padding;
        double greatestLength = 0;

        for (String line : lines){
            GUIText text = new GUIText(line, fontSize, font, new Vector2f(padding + position.x-1,position.y-minHeight - 1 - padding + lineHeight*lines.length), thickness, borderWidth, textColor, null, false, true);
            super.getTexts().add(text);
            if (text.getLength() > greatestLength){
                greatestLength = text.getLength();
            }
            minHeight+=lineHeight;
        }
        super.setSize(new Vector2f((float)greatestLength*2 + 4*padding,lineHeight*lines.length + padding));
        for(GUIText text : super.getTexts()){
            if(text.getPositionBounds() == null){
                text.setPositionBounds(new Vector4f(position.x, position.y, position.x+super.getSize().x, position.y+super.getSize().y));
            }
        }
        super.setGuiFilledBox(new GUIFilledBox(position, super.getSize(), backgroundColor));
    }
}
