package gui;

import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import rendering.text.TextMaster;

public class TextWord extends GUIText {

    String separator;

    public TextWord(String text, Vector3f color, boolean isFlowchartText, boolean isCodeWindowText, Vector2f position, String separator){
        super(text, GeneralSettings.FONT_SIZE, GeneralSettings.FONT, position, GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, color, new Vector4f(-1, -1, 1, 1), false, isFlowchartText, isCodeWindowText);
        this.separator = separator;
        TextMaster.removeText(this);
    }

    public String getSeparator(){
        return separator;
    }
}
