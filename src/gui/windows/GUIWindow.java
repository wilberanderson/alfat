package gui.windows;


import controllers.gui.GUIWindowController;
import main.GeneralSettings;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import rendering.renderEngine.GUIElementRenderer;

import java.util.ArrayList;
import java.util.List;

public class GUIWindow {

    long window;

    private String title = "";
    private WindowDecorator windowDecorator = null;
    private boolean deleteOnLostFocus = false;
    private GUIElementRenderer renderer;

    //TODO: Switch to layouts containing elements
    List<GUIElement> elementList = new ArrayList<>();

    public GUIWindow(int width, int height){
        //********************************Create the window************************************
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
//        if ( !GLFW.glfwInit() )
//            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        GLFW.glfwDefaultWindowHints(); // optional, the current window hints are already the default
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE); // the window will stay hidden after creation
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE); // the window will be resizable
//        GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, decorated ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
        //        GLFW.glfwWindowHint(GLFW.)

        // Create the window
        window = GLFW.glfwCreateWindow(200, 100, "ALFAT", MemoryUtil.NULL, MemoryUtil.NULL);
        GLFW.glfwSetWindowTitle(window, "");

        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(window);

        //Allow rendering as fast as inputs are received
        GLFW.glfwSwapInterval(0);

        GLFW.glfwShowWindow(window);

        GUIWindow guiWindow = this;

        //Create callbacks

        //Callback that deletes the window on lost focus if desired
        GLFWWindowFocusCallback windowFocusCallback;
        GLFW.glfwSetWindowFocusCallback(window, windowFocusCallback = new GLFWWindowFocusCallback() {
            @Override
            public void invoke(long window, boolean focused) {
                if(focused == false && deleteOnLostFocus == true){
                    GUIWindowController.remove(guiWindow);
                }
            }
        });

        //Callback that handles closing the window
        GLFWWindowCloseCallback windowCloseCallback;
        GLFW.glfwSetWindowCloseCallback(window, windowCloseCallback = new GLFWWindowCloseCallback() {
            @Override
            public void invoke(long window) {
                renderer.cleanUp();
                GUIWindowController.remove(guiWindow);
            }
        });

        //Initialize the renderer
        renderer = new GUIElementRenderer();
    }

    public void title(String title){
        GLFW.glfwSetWindowTitle(window, title);
    }

    public void render(){
        renderer.renderGUIElements(elementList, window);
    }

    public void setSize(int width, int height){
        GLFW.glfwSetWindowSize(window, width, height);
        GLFW.glfwMakeContextCurrent(window);
        GL11.glViewport(0, 0, width, height);
    }

    public void setSize(Vector2f size){
        int width = (int)(GeneralSettings.DISPLAY_WIDTH*size.x);
        int height = (int)(GeneralSettings.DISPLAY_HEIGHT*size.y);
        GLFW.glfwSetWindowSize(window, width, height);
        GLFW.glfwMakeContextCurrent(window);
        GL11.glViewport(0, 0, width, height);
    }

    public void setDeleteOnLostFocus(boolean deleteOnLostFocus){
        this.deleteOnLostFocus = deleteOnLostFocus;
    }

    public long getWindow() {
        return window;
    }

    public void setColor(int r, int g, int b){
        GLFW.glfwMakeContextCurrent(window);
        GL11.glClearColor(r/255, g/255, b/255, 1);

    }

    public void setColor(float r, float g, float b){
        GLFW.glfwMakeContextCurrent(window);
        GL11.glClearColor(r, g, b, 1);
    }

    public void setColor(Vector3f color){
        GLFW.glfwMakeContextCurrent(window);
        GL11.glClearColor(color.x, color.y, color.z, 1);
    }

    public void addElement(GUIElement element){
        elementList.add(element);
    }

    public void removeElement(GUIElement element){
        elementList.remove(element);
    }
}
