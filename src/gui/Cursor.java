package gui;

import controllers.ApplicationController;
import gui.textBoxes.CodeWindow;
import main.GeneralSettings;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

public class Cursor {
    private Vector2f position;

    public Cursor(){
        this.position = new Vector2f(-2, -2);

    }
    public Vector2f getPosition(){
        return position;
    }

    public void setPosition(Vector2f position){
        this.position = position;
    }
}
