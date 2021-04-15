package gui.texts;

import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class WhiteSpaceWord extends TextWord {

    //private static Vector3f color = new Vector3f(0, 0, 0);
    private static Vector3f color = GeneralSettings.USERPREF.getSeparatorColor3f();

    private String text;

    public WhiteSpaceWord(String text, Vector2f position) {
        super(text, position);
        this.text = text;
    }


    public Vector3f getColor() {
        return color;
    }

    public static void setColor(Vector3f color) {
        WhiteSpaceWord.color = color;
    }

    public String getText(){
        return text;
    }
}
