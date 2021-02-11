package gui.buttons;

import org.lwjgl.util.vector.Vector2f;

public abstract class HighlightableButton extends Button{

    private boolean isHighlighted = false;

    public HighlightableButton(Vector2f position, Vector2f size){
        super(position, size);
    }
    public HighlightableButton(long window){
        super(window);
    }
    public abstract void onHighlight();
    public abstract void onUnhighlight();

    public void highlight(){
        isHighlighted = true;
        onHighlight();
    }

    public void unhighlight(){
        isHighlighted = false;
        onUnhighlight();
    }

    public boolean isHighlighted(){
        return  isHighlighted;
    }

}
