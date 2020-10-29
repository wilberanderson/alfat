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


    public TextButton(Vector2f position, Vector2f size, String text, Vector3f backgroundColor, Vector3f highlightColor, Vector3f textColor, FontType font, float fontSize, float width, float edge){
        super(position, size);
        this.text = new GUIText(text, fontSize, font, new Vector2f(position.x, position.y+fontSize*0.06f), width, edge, textColor, new Vector4f(-2, -2, -2, -2), true);
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

}
