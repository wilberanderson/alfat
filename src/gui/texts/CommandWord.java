package gui.texts;

import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class CommandWord extends TextWord{
    //private static Vector3f color = GeneralSettings.commandColor;
    private static Vector3f color = GeneralSettings.USERPREF.getCommandTextColor3f();


    public CommandWord(String text, Vector2f position) {
        super(text, position);
    }

    public Vector3f getColor() {
        return color;
    }

    public static void setColor(Vector3f color) {
        CommandWord.color = color;
    }
}
