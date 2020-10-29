package gui;

import fontMeshCreator.FontType;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.w3c.dom.Text;
import utils.InputManager;

public class TextButton extends Button{
    private GUIFilledBox guiFilledBox;
    private GUIText text;


    public TextButton(Vector2f position, Vector2f size, String text, Vector3f backgroundColor, Vector3f textColor, FontType font, float fontSize, float width, float edge){
        super(position, size);
        this.text = new GUIText(text, fontSize, font, new Vector2f(position.x, position.y+fontSize*0.06f), width, edge, textColor, new Vector4f(-2, -2, -2, -2));
        this.guiFilledBox = new GUIFilledBox(position, size, backgroundColor);
    }

    @Override
    public void onPress() {
        System.out.println("Test Failed");

    }

    public GUIFilledBox getGuiFilledBox(){
        return  guiFilledBox;
    }


}
