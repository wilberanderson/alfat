package gui.buttons;

import gui.GUIFilledBox;
import gui.GUIText;
import gui.fontMeshCreator.FontType;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class TextButton extends HighlightableButton{
    private GUIFilledBox guiFilledBox;
    private GUIText text;
    private Vector3f backgroundColor;
    private Vector3f highlightColor;
    private Vector2f aspectRatio = new Vector2f(1,1);

    public TextButton(Vector2f position, String text, Vector3f backgroundColor, Vector3f highlightColor, Vector3f textColor, FontType font, float fontSize, float width, float edge){
        super();
        this.text = new GUIText(text, fontSize, font, new Vector2f(position.x + GeneralSettings.TEXT_BUTTON_PADDING, position.y+fontSize*0.06f + GeneralSettings.TEXT_BUTTON_PADDING), width, edge, textColor, new Vector4f(-2, -2, -2, -2), true, false, false);
        Vector2f size = new Vector2f((float)this.text.getLength()*2 + 2*GeneralSettings.TEXT_BUTTON_PADDING, GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR + 2*GeneralSettings.TEXT_BUTTON_PADDING);
        super.setPosition(position);
        super.setSize(size);
        this.guiFilledBox = new GUIFilledBox(position, size, backgroundColor);
        this.backgroundColor = backgroundColor;
        this.highlightColor = highlightColor;
    }

    public TextButton(Vector2f position, Vector2f size, String text, Vector3f backgroundColor, Vector3f highlightColor, Vector3f textColor, FontType font, float fontSize, float width, float edge){
        super();
        this.text = new GUIText(text, fontSize, font, new Vector2f(position.x + GeneralSettings.TEXT_BUTTON_PADDING, position.y+fontSize*0.06f + GeneralSettings.TEXT_BUTTON_PADDING), width, edge, textColor, new Vector4f(-2, -2, -2, -2), true, false, false);
        super.setPosition(position);
        super.setSize(size);
        this.guiFilledBox = new GUIFilledBox(position, size, backgroundColor);
        this.backgroundColor = backgroundColor;
        this.highlightColor = highlightColor;
    }

    public TextButton(String text){
        super();
        this.text = new GUIText(text, GeneralSettings.FONT_SIZE, GeneralSettings.FONT, new Vector2f(0,0), GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, GeneralSettings.TEXT_COLOR, new Vector4f(-2, -2, -2, -2), true, false, false);
        super.setSize(new Vector2f((float)this.text.getLength(), GeneralSettings.FONT_SIZE* GeneralSettings.FONT_SCALING_FACTOR + 2*GeneralSettings.TEXT_BUTTON_PADDING));
        this.backgroundColor = GeneralSettings.TEXT_BUTTON_BACKGROUND_COLOR;
        this.highlightColor = GeneralSettings.HIGHLIGHT_COLOR;
    }

    public void initializePosition(Vector2f position, Vector2f size){
        this.guiFilledBox = new GUIFilledBox(new Vector2f(position), new Vector2f(size), backgroundColor);
        this.text.setPosition(new Vector2f(position.x + GeneralSettings.TEXT_BUTTON_PADDING, position.y + GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR + GeneralSettings.TEXT_BUTTON_PADDING));
        super.setPosition(new Vector2f(position));
        super.setSize(size);
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


    public GUIText getText(){
        return text;
    }

    public void setAspectRatio(Vector2f aspectRatio){
        Vector2f size = getGuiFilledBox().getSize();
        size.x /= this.aspectRatio.x;
        size.y /= this.aspectRatio.y;
        size.x *= aspectRatio.x;
        size.y *= aspectRatio.y;
        getGuiFilledBox().setSize(size);
        super.setPosition(new Vector2f(-1, 1-(1-guiFilledBox.getPosition().y)/this.aspectRatio.y*aspectRatio.y));
        super.setSize(size);
        guiFilledBox.setPosition(new Vector2f(-1, 1-(1-guiFilledBox.getPosition().y)/this.aspectRatio.y*aspectRatio.y));
        super.setPosition(guiFilledBox.getPosition());
        text.setPosition(new Vector2f(guiFilledBox.getPosition().x/aspectRatio.x, guiFilledBox.getPosition().y/aspectRatio.y + GeneralSettings.FONT_SCALING_FACTOR*GeneralSettings.FONT_SIZE));
        this.aspectRatio = aspectRatio;
    }

    @Override
    public void setPosition(Vector2f position){
        super.setPosition(position);
        this.getGuiFilledBox().setPosition(position);
        this.getText().getPosition().x = (position.x/aspectRatio.x);
    }

}
