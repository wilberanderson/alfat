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

//    public void processInputs(long window){
//        }else if(ApplicationController.PASTE){
//            String clipboard = GLFW.glfwGetClipboardString(window);
//            if(clipboard != null) {
//                paste(clipboard);
//            }
//            ApplicationController.PASTE = false;
//        }
//        if(codeWindow.getAspectRatio().x != aspectRatio.x || codeWindow.getAspectRatio().y != aspectRatio.y){
//            aspectRatio = new Vector2f(codeWindow.getAspectRatio());
//            updatePosition();
//        }else{
//            position.y = text.getPosition().y;
//        }
//    }

    public Vector2f getPosition(){
        return position;
    }



    public void setPosition(Vector2f position){
        this.position = position;
    }
}
