package gui;

import controllers.ApplicationController;
import gui.textBoxes.CodeWindow;
import main.GeneralSettings;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class Cursor {
    private Vector2f position;
    private Vector3f color;

    public Cursor(Vector3f color){
        this.position = new Vector2f(-2, -2);
        this.color = color;

    }
    public Vector2f getPosition(){
        return position;
    }

    public void setPosition(Vector2f position){
        this.position = position;
    }

    public Vector3f getColor(){
        return color;
    }
}
