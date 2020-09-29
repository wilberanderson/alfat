package gui;

import org.lwjgl.util.vector.Vector2f;

public class GuiTexture {
    
    private int texture;
    private Vector2f position;
    private Vector2f scale;
    private boolean highlightable = false;
    private boolean highlighted;

    public GuiTexture(int texture, Vector2f position, Vector2f scale) {
        this.texture = texture;
        this.position = position;
        this.scale = scale;
    }

    public GuiTexture(int texture, Vector2f position, Vector2f scale, boolean highlightable) {
        this.texture = texture;
        this.position = position;
        this.scale = scale;
        this.highlightable = highlightable;
    }


    public int getTexture() {
        return texture;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }


    public boolean isHighlightable() {
        return highlightable;
    }

    public void setHighlightable(boolean highlightable) {
        this.highlightable = highlightable;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }
 
}