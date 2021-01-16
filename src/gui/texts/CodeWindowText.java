package gui.texts;

import gui.fontMeshCreator.FontType;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class CodeWindowText extends Text{
    private String textString;
    private Vector3f color = GeneralSettings.TEXT_COLOR;
    private static FontType font = GeneralSettings.FONT;

    public CodeWindowText(String text, float fontSize, Vector2f position) {
        super(text, fontSize, position);
        this.textString = text;
    }

    public CodeWindowText(String textString, Text text, boolean deleteText){
        super(textString, text, deleteText);
        this.textString = textString;
    }

    public String getTextString(){
        return textString;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public FontType getFont(){
        return font;
    }
}
