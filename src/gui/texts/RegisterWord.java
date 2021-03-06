package gui.texts;

import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class RegisterWord extends TextWord{
    //private static Vector3f color = GeneralSettings.registerColor;
    private static Vector3f color = GeneralSettings.USERPREF.getRegisterColor3f();

    public RegisterWord(String text, Vector2f position) {
        super(text, position);
    }

    public Vector3f getColor() {
        return color;
    }

    public static void setColor(Vector3f color) {
        RegisterWord.color = color;
    }
}
