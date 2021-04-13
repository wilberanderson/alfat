package gui.texts;

import gui.fontMeshCreator.FontType;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a piece of text in the game.
 * 
 * @author Karl
 *
 */
public class DarkGUIText extends GUIText{

    private static Vector3f darkColor = new Vector3f(0.5f, 0.5f, 0.5f);

    public DarkGUIText(String text, float fontSize, Vector2f position) {
        super(text, fontSize, position);
    }

    public DarkGUIText(String text, float fontSize, Vector2f position, FontType fontType) {
        super(text, fontSize, position, fontType);
    }

    public static Vector3f getColor() {
        return darkColor;
    }
}
