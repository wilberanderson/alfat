package gui;

import org.lwjgl.util.vector.Vector2f;

public abstract class HighlightableButton extends Button{

    public HighlightableButton(Vector2f position, Vector2f size){
        super(position, size);
    }

    public abstract void onHighlight();
}
