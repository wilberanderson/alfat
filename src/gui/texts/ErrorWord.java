package gui.texts;

import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class ErrorWord extends TextWord{
    private static Vector3f color = GeneralSettings.errorColor;

    public ErrorWord(String text, Vector2f position, String separator) {
        super(text, position, separator);
    }

    public Vector3f getColor() {
        return color;
    }

    public static void setColor(Vector3f color) {
        ErrorWord.color = color;
    }
}
