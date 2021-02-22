package gui.texts;

import gui.fontMeshCreator.FontType;
import gui.fontMeshCreator.TextMeshData;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import org.lwjgl.util.vector.Vector4f;
import rendering.text.TextMaster;

/**
 * Represents a piece of text in the game.
 * 
 * @author Karl
 *
 */
public class GUIText extends Text{

    String textString;
    private static Vector3f color = GeneralSettings.TEXT_COLOR;
    private FontType font;

    public GUIText(String text, float fontSize, Vector2f position) {
        super(text, fontSize, position);
        this.textString = text;
        this.font = GeneralSettings.FONT;
    }

    public GUIText(String text, float fontSize, Vector2f position, FontType fontType) {
        super(text, fontSize, position, fontType);
        this.textString = text;
        this.font = fontType;
    }

    public String getTextString() {
        return textString;
    }

    public static Vector3f getColor() {
        return color;
    }

    public FontType getFont(){
        return font;
    }
}
