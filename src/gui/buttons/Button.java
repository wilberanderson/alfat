package gui.buttons;

import gui.guiElements.GUIElement;
import org.lwjgl.util.vector.Vector2f;

public abstract class Button extends GUIElement {

    private Vector2f position;
    private Vector2f size;
    private long window;

    public Button(Vector2f position, Vector2f size) {
        this.position = position;
        this.size = size;
    }

    public Button(long window){
        this.window = window;
    }

    public abstract void onPress();

    public Vector2f getPosition(){
        return position;
    }

    public Vector2f getSize(){
        return size;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void setSize(Vector2f size) {
        this.size = size;
    }


    public long getWindow() {
        return window;
    }
}
