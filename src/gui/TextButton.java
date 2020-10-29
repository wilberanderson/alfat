package gui;

import fontMeshCreator.FontType;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.w3c.dom.Text;
import utils.InputManager;

public class TextButton extends HighlightableButton{
    private GUIFilledBox guiFilledBox;
    private GUIText text;
    private Vector3f backgroundColor;
    private Vector3f highlightColor;
    private Vector2f position;
    private Vector2f size;

    public TextButton(Vector2f position, String text, Vector3f backgroundColor, Vector3f highlightColor, Vector3f textColor, FontType font, float fontSize, float width, float edge){
        super();
        this.text = new GUIText(text, fontSize, font, new Vector2f(position.x + GeneralSettings.TEXT_BUTTON_PADDING, position.y+fontSize*0.06f + GeneralSettings.TEXT_BUTTON_PADDING), width, edge, textColor, new Vector4f(-2, -2, -2, -2), true);
        Vector2f size = new Vector2f((float)this.text.getLength()*2 + 2*GeneralSettings.TEXT_BUTTON_PADDING, GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR + 2*GeneralSettings.TEXT_BUTTON_PADDING);
        this.position = position;
        this.size = size;
        this.guiFilledBox = new GUIFilledBox(position, size, backgroundColor);
        this.backgroundColor = backgroundColor;
        this.highlightColor = highlightColor;
    }

    public TextButton(Vector2f position, Vector2f size, String text, Vector3f backgroundColor, Vector3f highlightColor, Vector3f textColor, FontType font, float fontSize, float width, float edge){
        super();
        this.text = new GUIText(text, fontSize, font, new Vector2f(position.x + GeneralSettings.TEXT_BUTTON_PADDING, position.y+fontSize*0.06f + GeneralSettings.TEXT_BUTTON_PADDING), width, edge, textColor, new Vector4f(-2, -2, -2, -2), true);
        this.position = position;
        this.size = size;
        this.guiFilledBox = new GUIFilledBox(position, size, backgroundColor);
        this.backgroundColor = backgroundColor;
        this.highlightColor = highlightColor;
    }

    @Override
    public void onPress() {
        System.out.println("Test Failed");

    }

    public GUIFilledBox getGuiFilledBox(){
        return  guiFilledBox;
    }

    @Override
    public void onHighlight() {
        this.guiFilledBox.setColor(highlightColor);
    }

    @Override
    public void onUnhighlight() {
        this.guiFilledBox.setColor(backgroundColor);
    }

    @Override
    public Vector2f getPosition(){
        return position;
    }

    @Override
    public Vector2f getSize(){
        return size;
    }

}
