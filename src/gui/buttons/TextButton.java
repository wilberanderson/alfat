package gui.buttons;

import gui.GUIFilledBox;
import gui.texts.GUIText;
import gui.fontMeshCreator.FontType;
import main.EngineTester;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class TextButton extends HighlightableButton{
    private Vector3f backgroundColor;
    private Vector3f highlightColor;
    private Vector2f aspectRatio = new Vector2f(1,1);

    public void setBackgroundColor(Vector3f backgroundColor3f) {
        this.backgroundColor = backgroundColor3f;
        this.getGuiFilledBox().setColor(backgroundColor);
    }

    public void setHighlightColor(Vector3f highlightColor3f) {
        this.highlightColor = highlightColor3f;

    }


    public TextButton(Vector2f position, String text, Vector3f backgroundColor, Vector3f highlightColor, Vector3f textColor, FontType font, float fontSize, float width, float edge){
        super(EngineTester.getWindow());
        this.setGuiText(new GUIText(text, fontSize, new Vector2f(position.x + GeneralSettings.TEXT_BUTTON_PADDING, position.y+fontSize*0.06f + GeneralSettings.TEXT_BUTTON_PADDING), font));
        Vector2f size = new Vector2f((float)this.getText().getLength()*2 + 2*GeneralSettings.TEXT_BUTTON_PADDING, fontSize*GeneralSettings.FONT_SCALING_FACTOR + 2*GeneralSettings.TEXT_BUTTON_PADDING);
        super.setPosition(position);
        super.setSize(size);
        setFilledBox(new GUIFilledBox(position, size, backgroundColor));
        this.backgroundColor = backgroundColor;
        this.highlightColor = highlightColor;
    }

    public TextButton(Vector2f position, String text, Vector3f backgroundColor, Vector3f highlightColor, Vector3f textColor, FontType font, float fontSize, float width, float edge, long window){
        super(window);
        this.setGuiText(new GUIText(text, fontSize, new Vector2f(position.x + GeneralSettings.TEXT_BUTTON_PADDING, position.y+fontSize*0.06f + GeneralSettings.TEXT_BUTTON_PADDING), font));
        Vector2f size = new Vector2f((float)this.getText().getLength()*2 + 2*GeneralSettings.TEXT_BUTTON_PADDING, fontSize*GeneralSettings.FONT_SCALING_FACTOR + 2*GeneralSettings.TEXT_BUTTON_PADDING);
        super.setPosition(position);
        super.setSize(size);
        setFilledBox(new GUIFilledBox(position, size, backgroundColor));
        this.backgroundColor = backgroundColor;
        this.highlightColor = highlightColor;
    }

    public TextButton(Vector2f position, Vector2f size, String text, Vector3f backgroundColor, Vector3f highlightColor, Vector3f textColor, FontType font, float fontSize, float width, float edge){
        super(EngineTester.getWindow());
        setGuiText(new GUIText(text, fontSize, new Vector2f(position.x + GeneralSettings.TEXT_BUTTON_PADDING, position.y+fontSize*0.06f + GeneralSettings.TEXT_BUTTON_PADDING), font));
        super.setPosition(position);
        super.setSize(size);
        setFilledBox(new GUIFilledBox(position, size, backgroundColor));
        this.backgroundColor = backgroundColor;
        this.highlightColor = highlightColor;
    }

    public TextButton(String text){
        super(EngineTester.getWindow());
        this.setGuiText(new GUIText(text, GeneralSettings.FONT_SIZE, new Vector2f(0,0)));
        super.setSize(new Vector2f((float)this.getText().getLength(), GeneralSettings.FONT_SIZE* GeneralSettings.FONT_SCALING_FACTOR + 2*GeneralSettings.TEXT_BUTTON_PADDING));
        this.backgroundColor = GeneralSettings.TEXT_BUTTON_BACKGROUND_COLOR;
        this.highlightColor = GeneralSettings.HIGHLIGHT_COLOR;
    }

    public void initializePosition(Vector2f position, Vector2f size){
        setFilledBox(new GUIFilledBox(new Vector2f(position), new Vector2f(size), backgroundColor));
        this.getText().setPosition(new Vector2f(position.x + GeneralSettings.TEXT_BUTTON_PADDING, position.y + GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR + GeneralSettings.TEXT_BUTTON_PADDING));
        super.setPosition(new Vector2f(position));
        super.setSize(size);
    }

    @Override
    public void onPress() {
        System.out.println("Test Failed");

    }

    public GUIFilledBox getGuiFilledBox(){
        return getFilledBox();
    }

    @Override
    public void onHighlight() {
        this.getGuiFilledBox().setColor(highlightColor);

    }

    @Override
    public void onUnhighlight() {

        this.getGuiFilledBox().setColor(backgroundColor);
    }


    public GUIText getText(){
        return this.getGuiText();
    }

    public void setAspectRatio(Vector2f aspectRatio){
        Vector2f size = getGuiFilledBox().getSize();
        size.x /= this.aspectRatio.x;
        size.y /= this.aspectRatio.y;
        size.x *= aspectRatio.x;
        size.y *= aspectRatio.y;
        getGuiFilledBox().setSize(size);
        super.setPosition(new Vector2f(-1, 1-(1-this.getGuiFilledBox().getPosition().y)/this.aspectRatio.y*aspectRatio.y));
        super.setSize(size);
        this.getGuiFilledBox().setPosition(new Vector2f(-1, 1-(1-this.getGuiFilledBox().getPosition().y)/this.aspectRatio.y*aspectRatio.y));
        super.setPosition(this.getGuiFilledBox().getPosition());
        this.getText().setPosition(new Vector2f(this.getGuiFilledBox().getPosition().x/aspectRatio.x, this.getGuiFilledBox().getPosition().y/aspectRatio.y + GeneralSettings.FONT_SCALING_FACTOR*GeneralSettings.FONT_SIZE));
        this.aspectRatio = aspectRatio;
    }

    @Override
    public void setPosition(Vector2f position){
        super.setPosition(position);
        this.getGuiFilledBox().setPosition(position);
        this.getText().getPosition().x = (position.x/aspectRatio.x);
    }

}
