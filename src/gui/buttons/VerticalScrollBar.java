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
        System.out.println(position.y);
        System.out.println(fullRange);
        System.out.println(height);
        System.out.println(currentPos);
        filledBox = new GUIFilledBox(new Vector2f(position.x, position.y + fullRange - height), new Vector2f(width, height), new Vector3f(0.3f, 0.3f, 0.3f));
        System.out.println(filledBox.getPosition());
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
        filledBox.getSize().y = windowHeight*factor;
        filledBox.getPosition().y -= filledBox.getSize().y;
    }
}
