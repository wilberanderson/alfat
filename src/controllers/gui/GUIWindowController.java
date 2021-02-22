package controllers.gui;

import gui.guiElements.TextField;
import gui.windows.GUIWindow;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class GUIWindowController {
    private static List<GUIWindow> guiWindows= new ArrayList<>();
    private static List<TextField> textFields = new ArrayList<>();
    private static List<GUIWindow> toRemove = new ArrayList<>();
    private static boolean removing = false;

    public static void render(){
        //Remove any windows marked to be removed
        if(removing){
            removing = false;
            for(GUIWindow window: toRemove){
                GLFW.glfwDestroyWindow(window.getWindow());
                guiWindows.remove(window);
            }
        }
        for(GUIWindow window : guiWindows){
            window.render();
        }
    }

    public static void add(GUIWindow window){
        guiWindows.add(window);
    }

    public static void remove(GUIWindow window){
        //May cause crashes under certain conditions if removing directly, instead remove when the render call is made
        //Caused because allowing enter to return input causes a function to try to return to a structure in native code which has already been deleted
        toRemove.add(window);
        removing = true;
    }

//    public static void click(long window, int button, int action){
//        for(GUIWindow guiWindow : guiWindows){
//            if(guiWindow.getWindow() == window){
//                guiWindow.click(button, action);
//                return;
//            }
//        }
//    }

}
