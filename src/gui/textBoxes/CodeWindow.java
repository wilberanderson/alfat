package gui.textBoxes;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;


public class CodeWindow extends TextBox{

    private Vector4f positionBounds;

    public CodeWindow() {
        super();
    }

    public Vector2f getCodeWindowPosition(){
        return new Vector2f(super.getPosition().x + super.getTextNumberFilledBox().getSize().x, super.getPosition().y);
    }

    public Vector2f getCodeWindowSize(){
        return new Vector2f(super.getSize().x-super.getTextNumberFilledBox().getSize().x, super.getSize().y);
    }

    public Vector4f getPositionBounds() {
        return positionBounds;
    }

    public void setPositionBounds(Vector4f positionBounds) {
        this.positionBounds = positionBounds;
    }
}
