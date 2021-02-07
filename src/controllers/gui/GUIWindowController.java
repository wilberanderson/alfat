package controllers.gui;

import gui.windows.GUIWindow;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class GUIWindowController {
    private static List<GUIWindow> guiWindows= new ArrayList<>();

    public static void render(){
        for(GUIWindow window : guiWindows){
            window.render();
        }
    }

    public static void add(GUIWindow window){
        guiWindows.add(window);
    }

    public static void remove(GUIWindow window){
        GLFW.glfwDestroyWindow(window.getWindow());
        guiWindows.remove(window);
    }
}
