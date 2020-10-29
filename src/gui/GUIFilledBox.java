package gui;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class GUIFilledBox {
    Vector2f position;
    Vector2f size;
    Vector3f color;

    public GUIFilledBox(Vector2f position, Vector2f size, Vector3f color){
        this.position = position;
        this.size = size;
        this.color = color;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getSize() {
        return size;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void setSize(Vector2f size) {
        this.size = size;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }
}
