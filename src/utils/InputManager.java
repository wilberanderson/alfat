package utils;

import gui.Button;
import main.GeneralSettings;
import org.lwjgl.glfw.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class InputManager {

    public static boolean UP_PRESSED = false;
    public static boolean DOWN_PRESSED = false;
    public static boolean LEFT_PRESSED = false;
    public static boolean RIGHT_PRESSED = false;
    public static boolean BACKSPACE_PRESSED = false;
    public static boolean DELETE_PRESSED = false;
    public static boolean CTRL_PRESSED = false;
    public static boolean PASTE = false;
    public static float UP_DURATION = 0;
    public static float DOWN_DURATION = 0;
    public static float LEFT_DURATION = 0;
    public static float RIGHT_DURATION = 0;
    public static float BACKSPACE_DURATION = 0;
    public static float DELETE_DURATION = 0;
    public static boolean LEFT_CLICK = false;
    public static boolean RIGHT_CLICK = false;
    public static double SCROLL_CHANGE = 0.0;
    public static double MOUSE_X = 0.0;
    public static double MOUSE_Y = 0.0;
    public static double MOUSE_X_CHANGE = 0.0;
    public static double MOUSE_Y_CHANGE = 0.0;
    private static double previousMouseX = 0;
    private static double previousMouseY = 0;
    private static GLFWKeyCallback keyCallback;
    private static GLFWScrollCallback scrollCallback;
    private static GLFWMouseButtonCallback mouseButtonCallback;
    private static GLFWCursorPosCallback cursorPosCallback;
    private static Map<String, Button> buttons;
    private static GLFWCharCallback charCallback;
    public static List<Character> codepoints = new ArrayList<>();

    public static void init(long window, Map<String, Button> buttonList){
        buttons = buttonList;
        // Setup a key callback. It will be called every time a key is pressed, repeated or released
        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                    glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
                if(key == GLFW_KEY_UP && action == GLFW_PRESS) {
                    UP_PRESSED = true;
                }else if(key == GLFW_KEY_UP && action == GLFW_RELEASE) {
                    UP_PRESSED = false;
                    UP_DURATION = 0;
                }else if(key == GLFW_KEY_DOWN && action == GLFW_PRESS) {
                    DOWN_PRESSED = true;
                }else if(key == GLFW_KEY_DOWN && action == GLFW_RELEASE) {
                    DOWN_PRESSED = false;
                    DOWN_DURATION = 0;
                }else if(key == GLFW_KEY_LEFT && action == GLFW_PRESS) {
                    LEFT_PRESSED = true;
                }else if(key == GLFW_KEY_LEFT && action == GLFW_RELEASE) {
                    LEFT_PRESSED = false;
                    LEFT_DURATION = 0;
                }else if(key == GLFW_KEY_RIGHT && action == GLFW_PRESS) {
                    RIGHT_PRESSED = true;
                }else if(key == GLFW_KEY_RIGHT && action == GLFW_RELEASE) {
                    RIGHT_PRESSED = false;
                    RIGHT_DURATION = 0;
                }else if(key == GLFW_KEY_BACKSPACE && action == GLFW_PRESS) {
                    BACKSPACE_PRESSED = true;
                }else if(key == GLFW_KEY_BACKSPACE && action == GLFW_RELEASE) {
                    BACKSPACE_PRESSED = false;
                    BACKSPACE_DURATION = 0;
                }else if(key == GLFW_KEY_DELETE && action == GLFW_PRESS) {
                    DELETE_PRESSED = true;
                }else if(key == GLFW_KEY_DELETE && action == GLFW_RELEASE) {
                    DELETE_PRESSED = false;
                    DELETE_DURATION = 0;
                }else if((key == GLFW_KEY_LEFT_CONTROL || key == GLFW_KEY_RIGHT_CONTROL) && action == GLFW_PRESS) {
                    CTRL_PRESSED = true;
                }else if((key == GLFW_KEY_LEFT_CONTROL || key == GLFW_KEY_RIGHT_CONTROL) && action == GLFW_RELEASE) {
                    CTRL_PRESSED = false;
                }else if(key == GLFW_KEY_V && CTRL_PRESSED && action == GLFW_PRESS){
                    PASTE = true;
                }else if(key == GLFW_KEY_ENTER && action == GLFW_PRESS) {
                    codepoints.add('\n');
                }
            }
        });
        glfwSetScrollCallback(window, scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                SCROLL_CHANGE += yoffset;
            }
        });
        glfwSetMouseButtonCallback(window, mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if(button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
                    LEFT_CLICK = true;
                }else if(button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS) {
                    RIGHT_CLICK = true;
                }else if(button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_RELEASE) {
                    RIGHT_CLICK = false;
                }
            }
        });
        glfwSetCursorPosCallback(window, cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                MOUSE_X = xpos;
                MOUSE_Y = ypos;
            }
        });
        previousMouseX = MOUSE_X;
        previousMouseY = MOUSE_Y;

        glfwSetCharCallback(window, charCallback = new GLFWCharCallback() {
            @Override
            public void invoke(long window, int codepoint) {
                codepoints.add((char)codepoint);
            }
        });
    }

    public static void processEvents(){
        if(LEFT_PRESSED){
            LEFT_DURATION += GeneralSettings.getFrameTime();
        }
        if(RIGHT_PRESSED){
            RIGHT_DURATION += GeneralSettings.getFrameTime();
        }
        if(UP_PRESSED){
            UP_DURATION += GeneralSettings.getFrameTime();
        }
        if(DOWN_PRESSED){
            DOWN_DURATION += GeneralSettings.getFrameTime();
        }
        if(BACKSPACE_PRESSED){
            BACKSPACE_DURATION += GeneralSettings.getFrameTime();
        }
        if(DELETE_PRESSED){
            DELETE_DURATION += GeneralSettings.getFrameTime();
        }
        MOUSE_X_CHANGE = MOUSE_X - previousMouseX;
        MOUSE_Y_CHANGE = MOUSE_Y - previousMouseY;
        previousMouseX = MOUSE_X;
        previousMouseY = MOUSE_Y;
    }

}
