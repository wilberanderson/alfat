package gui.windows;


import controllers.gui.ButtonController;
import controllers.gui.GUIWindowController;
import gui.fontMeshCreator.FontType;
import gui.guiElements.GUIElement;
import gui.guiElements.TextField;
import loaders.Loader;
import main.EngineTester;
import main.GeneralSettings;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import rendering.renderEngine.GUIElementRenderer;
import utils.MyFile;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public abstract class GUIWindow {

    long window;

    private String title = "";
    private boolean deleteOnLostFocus = true;
    private GUIElementRenderer renderer;
    protected FontType fontType;
    Vector2f mousePosition = new Vector2f(-2, -2);
    private TextField selectedTextField;

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
        window = GLFW.glfwCreateWindow(width, height, "ALFAT", MemoryUtil.NULL, MemoryUtil.NULL);
        GLFW.glfwSetWindowTitle(window, "");

        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(window);
        GL11.glClearColor(GeneralSettings.USERPREF.getBackgroundColor3f().x, GeneralSettings.USERPREF.getBackgroundColor3f().y, GeneralSettings.USERPREF.getBackgroundColor3f().z, 1);

        //Allow rendering as fast as inputs are received
        GLFW.glfwSwapInterval(0);

        GLFW.glfwShowWindow(window);

        GUIWindow guiWindow = this;
        int[] xpos = new int[1];
        int[] xsize = new int[1];
        int[] ypos = new int[1];
        int[] ysize = new int[1];
        GLFW.glfwGetWindowPos(EngineTester.getWindow(), xpos, ypos);
        GLFW.glfwGetWindowSize(EngineTester.getWindow(), xsize, ysize);
        GLFW.glfwSetWindowPos(window, (xpos[0] + xpos[0] + xsize[0]) / 2 - width/2, (ypos[0] + ypos[0] + ysize[0]) / 2 - height/2);

        //Create callbacks

        //Callback that deletes the window on lost focus if desired
        GLFWWindowFocusCallback windowFocusCallback;
        GLFW.glfwSetWindowFocusCallback(window, windowFocusCallback = new GLFWWindowFocusCallback() {
            @Override
            public void invoke(long window, boolean focused) {
                if(focused == false && deleteOnLostFocus == true){
                    guiWindow.close();
                }
            }
        });

        //Callback that handles closing the window
        GLFWWindowCloseCallback windowCloseCallback;
        GLFW.glfwSetWindowCloseCallback(window, windowCloseCallback = new GLFWWindowCloseCallback() {
            @Override
            public void invoke(long window) {
                guiWindow.close();
            }
        });

        GLFWCursorPosCallback cursorPosCallback;
        GLFW.glfwSetCursorPosCallback(window, cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                int[] width = new int[1];
                int[] height = new int[1];
                GLFW.glfwGetWindowSize(window, width, height);
                mousePosition.x = (float)xpos / width[0]*2-1;
                mousePosition.y = 1 - (float)ypos / height[0]*2;
                ButtonController.hover(window, mousePosition);
            }
        });

        GLFWMouseButtonCallback mouseButtonCallback;
        GLFW.glfwSetMouseButtonCallback(window, mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if(button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE){
                    ButtonController.click(window, mousePosition);
                }
                if(button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {
                    for (GUIElement element : elementList) {
                        if (element instanceof TextField) {
                            TextField e = (TextField) element;
                            if (mousePosition.x > e.getPosition().x && mousePosition.y > e.getPosition().y && mousePosition.x < e.getPosition().x + e.getSize().x && mousePosition.y < e.getPosition().y + e.getSize().y) {
                                selectedTextField = e;
                            } else {
                                if(selectedTextField == e) {
                                    selectedTextField = null;
                                }
                            }
                        }
                    }
                }
            }
        });

        GLFWCharCallback charCallback;
        GLFW.glfwSetCharCallback(window, charCallback = new GLFWCharCallback() {
            @Override
            public void invoke(long window, int codepoint) {
                if(selectedTextField != null){
                    GLFW.glfwMakeContextCurrent(window);
                    selectedTextField.type((char) codepoint);
                }
            }
        });

        GLFWKeyCallback keyCallback;
        GLFW.glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if(selectedTextField != null){
                    if(key == GLFW_KEY_LEFT && (action == GLFW_PRESS || action == GLFW_REPEAT)){
                        selectedTextField.moveLeft();
                    }
                    else if(key == GLFW_KEY_RIGHT && (action == GLFW_PRESS || action == GLFW_REPEAT)){
                        selectedTextField.moveRight();
                    }
                    else if(key == GLFW_KEY_BACKSPACE && (action == GLFW_PRESS || action == GLFW_REPEAT)){
                        selectedTextField.delete(true);
                    }
                    else if(key == GLFW_KEY_DELETE && (action == GLFW_PRESS || action == GLFW_REPEAT)){
                        selectedTextField.delete(false);
                    }
                    else if(key == GLFW_KEY_ENTER && action == GLFW_PRESS){
                        onContinue();
                    }
                }
            }
        });

        //Initialize the renderer
        renderer = new GUIElementRenderer();

        //Add this window to the window controller
        GUIWindowController.add(this);

        //Initialize the font type
        fontType = new FontType(Loader.loadTexture(new MyFile(GeneralSettings.DEFAULT_FONT_LOCATION + ".png")), new MyFile(GeneralSettings.DEFAULT_FONT_LOCATION + ".fnt"));
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

    public void moveMouse(){

    }

    public void click(int button, int action){

    }

    protected void close(){
        renderer.cleanUp();
        GUIWindowController.remove(this);
    }

    abstract void onContinue();
}
