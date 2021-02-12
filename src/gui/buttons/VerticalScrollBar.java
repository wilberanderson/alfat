package gui.buttons;

import gui.GUIFilledBox;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class VerticalScrollBar {
    Vector2f position;
    float width;
    float height;
    float fullRange;
    float factor;
    float currentPos;
    float windowHeight;
    float contentsHeight;
    GUIFilledBox filledBox;

    public VerticalScrollBar(Vector2f position, float width, float fullRange, float windowHeight, float contentsHeight, float currentPos) {
        this.position = position;
        this.width = width;
        this.fullRange = fullRange;
        this.windowHeight = windowHeight;
        this.contentsHeight = contentsHeight;
        if (windowHeight < contentsHeight) {
            factor = windowHeight / contentsHeight;
        } else {
            factor = 1;
        }
        height = fullRange * factor;
        filledBox = new GUIFilledBox(new Vector2f(position.x, position.y + fullRange - height), new Vector2f(width, height), new Vector3f(0.3f, 0.3f, 0.3f));
    }

    public void updateAspectRatio(float width, float windowHeight, float fullRange, float contentsHeight){
        position.x = position.x + this.width - width;
        position.y = -1 + windowHeight - fullRange;
        filledBox.getSize().x = width;
        filledBox.getPosition().x = position.x;
        this.width = width;
        this.windowHeight = windowHeight;
        this.fullRange = fullRange;
        this.contentsHeight = contentsHeight;
        if(windowHeight < contentsHeight){
            factor = windowHeight / contentsHeight;
        }else{
            factor = 1;
        }
        height = fullRange * factor;
        filledBox.getSize().y = height;
        filledBox.getPosition().y = position.y + fullRange - height;
    }

    public void changePosition(float change) {
        filledBox.getPosition().y -= change * factor;
    }

    public GUIFilledBox getFilledBox() {
        return filledBox;
    }

    public float getFactor() {
        return factor;
    }

    public void changeContentsHeight(float change) {
        contentsHeight += change;
        if(windowHeight < contentsHeight){
            factor = windowHeight / contentsHeight;
        }else{
            factor = 1;
        }
        filledBox.getPosition().y += filledBox.getSize().y;
        filledBox.getSize().y = fullRange*factor;
        filledBox.getPosition().y -= filledBox.getSize().y;
    }

    public void goSplitScreen(){
        position.x = 0f-width;
        filledBox.getPosition().x = 0f-width;
    }

    public void minimize(){
        position.x = -2f;
        filledBox.getPosition().x = -2f;
    }

    public void maximize(){
        position.x = 1f-width;
        filledBox.getPosition().x = 1f-width;
    }
}
