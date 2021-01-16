package gui.texts;

import gui.fontMeshCreator.FontType;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import rendering.text.TextMaster;

public class TextWord extends Text {

    String separator;
    private static float width = GeneralSettings.FONT_WIDTH;
    private static float edge = GeneralSettings.FONT_EDGE;
    private static FontType font = GeneralSettings.FONT;

    public TextWord(String text, Vector2f position, String separator){
        super(text, GeneralSettings.FONT_SIZE, position);
        this.separator = separator;
        TextMaster.removeText(this);
    }

    public String getSeparator(){
        return separator;
    }

    public static float getWidth() {
        return width;
    }

    public static void setWidth(float width) {
        TextWord.width = width;
    }

    public static float getEdge() {
        return edge;
    }

    public static void setEdge(float edge) {
        TextWord.edge = edge;
    }

    public FontType getFont() {
        return font;
    }

    public static void setFont(FontType font) {
        TextWord.font = font;
    }

    public static float getSpaceSize(){
        return font.getSpaceSize();
    }

    public Vector3f getColor(){
        return null;
    }
}
