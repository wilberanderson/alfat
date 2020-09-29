package gui;

import org.lwjgl.util.vector.Vector2f;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Button extends GuiTexture {

    private boolean clicked = false;

    public Button(int texture, Vector2f position, Vector2f scale, boolean highlightable) {
        super(texture, position, scale, highlightable);
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
}
