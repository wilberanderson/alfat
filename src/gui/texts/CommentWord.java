package gui.texts;

import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class CommentWord extends TextWord{
    private static Vector3f color = GeneralSettings.commentColor;

    public CommentWord(String text, Vector2f position, String separator) {
        super(text, position, separator);
    }

    public Vector3f getColor() {
        return color;
    }

    public static void setColor(Vector3f color) {
        CommentWord.color = color;
    }
}
