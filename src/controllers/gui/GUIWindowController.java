package controllers.gui;

import gui.windows.GUIWindow;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;

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

    public static void click(long window, int button, int action){
        for(GUIWindow guiWindow : guiWindows){
            if(guiWindow.getWindow() == window){
                guiWindow.click(button, action);
                return;
            }
        }
    }
}
