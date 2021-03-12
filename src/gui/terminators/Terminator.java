package gui.terminators;

import dataStructures.RawModel;
import org.lwjgl.util.vector.Vector2f;

public abstract class Terminator{
    private Vector2f position;
    private boolean highlighted = false;

    public Terminator(Vector2f position){
        this.position = position;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public static RawModel getModel(){
        return null;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public boolean isHighlighted() {
        return highlighted;
    }
}
