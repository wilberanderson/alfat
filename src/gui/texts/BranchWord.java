package gui.texts;

import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class BranchWord extends TextWord{
    //private static Vector3f color = GeneralSettings.branchColor;
    private static Vector3f color = GeneralSettings.USERPREF.getBranchTextColor3f();

    public BranchWord(String text, Vector2f position) {
        super(text, position);
    }

    public Vector3f getColor() {
        return color;
    }

    public static void setColor(Vector3f color) {
        BranchWord.color = color;
    }
}
