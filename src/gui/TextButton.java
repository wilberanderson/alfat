package gui;

import fontMeshCreator.FontType;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.w3c.dom.Text;
import utils.InputManager;

public class TextButton extends Button{
    private GUIFilledBox guiFilledBox;
    private GUIText text;


    public TextButton(Vector2f position, Vector2f size, String text, Vector3f textColor, FontType font, float fontSize, float width, float edge){
        super(position, size);
        this.text = new GUIText(text, fontSize, font, position, width, edge, textColor, null);
    }

    @Override
    public void onPress() {
        System.out.println("Test Failed");

    }


}
