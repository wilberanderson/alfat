package gui.texts;

import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class CommentWord extends TextWord{
    //private static Vector3f color = GeneralSettings.commentColor;
    private static Vector3f color = GeneralSettings.USERPREF.getCommentColor3f();

    public CommentWord(String text, Vector2f position) {
        super(text, position);
    }

    public Vector3f getColor() {
        return color;
    }

    public static void setColor(Vector3f color) {
        CommentWord.color = color;
    }
}
