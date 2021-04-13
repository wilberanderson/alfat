package gui.texts;

import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class ImmediateWord extends TextWord{
    //private static Vector3f color = GeneralSettings.immediateColor;
    private static Vector3f color = GeneralSettings.USERPREF.getImmediateColor3f();

    public ImmediateWord(String text, Vector2f position) {
        super(text, position);
    }

    public Vector3f getColor() {
        return color;
    }

    public static void setColor(Vector3f color) {
        ImmediateWord.color = color;
    }
}
