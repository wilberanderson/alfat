package utils;

import gui.Button;
import main.GeneralSettings;
import org.lwjgl.glfw.*;

import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class InputManager {

    public static boolean KEY_FORWARD = false;
    public static boolean KEY_BACKWARD = false;
    public static boolean KEY_LEFT = false;
    public static boolean KEY_RIGHT = false;
    public static boolean KEY_STRAFE_LEFT = false;
    public static boolean KEY_STRAFE_RIGHT = false;
    public static boolean KEY_OPEN_MENU = false;
    public static boolean KEY_JUMP = false;
    public static boolean KEY_ROTATE_ENTITY_CLOCKWISE = false;
    public static boolean KEY_ROTATE_ENTITY_COUNTERCLOCKWISE = false;
    public static boolean CHANGE_MODEL = false;
    public static boolean PLACE_MODEL = false;
    public static boolean LEFT_CLICK = false;
    public static boolean RIGHT_CLICK = false;
    public static boolean SELECT_ENTITY = false;
    public static boolean DELETE_ENTITY = false;
    public static double SCROLL_CHANGE = 0.0;
    public static double ENTITY_SCALE_CHANGE = 0.0;
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

    public static void init(long window, Map<String, Button> buttonList){
        buttons = buttonList;
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                    glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
                if(key == GLFW_KEY_W && action == GLFW_PRESS) {
                    KEY_FORWARD = true;
                }else if(key == GLFW_KEY_W && action == GLFW_RELEASE) {
                    KEY_FORWARD = false;
                }else if(key == GLFW_KEY_S && action == GLFW_PRESS) {
                    KEY_BACKWARD = true;
                }else if(key == GLFW_KEY_S && action == GLFW_RELEASE) {
                    KEY_BACKWARD = false;
                }else if(key == GLFW_KEY_A && action == GLFW_PRESS) {
                    KEY_LEFT = true;
                }else if(key == GLFW_KEY_A && action == GLFW_RELEASE) {
                    KEY_LEFT = false;
                }else if(key == GLFW_KEY_D && action == GLFW_PRESS) {
                    KEY_RIGHT = true;
                }else if(key == GLFW_KEY_D && action == GLFW_RELEASE) {
                    KEY_RIGHT = false;
                }else if(key == GLFW_KEY_Q && action == GLFW_PRESS) {
                    KEY_STRAFE_LEFT = true;
                }else if(key == GLFW_KEY_Q && action == GLFW_RELEASE) {
                    KEY_STRAFE_LEFT = false;
                }else if(key == GLFW_KEY_E && action == GLFW_PRESS) {
                    KEY_STRAFE_RIGHT = true;
                }else if(key == GLFW_KEY_E && action == GLFW_RELEASE) {
                    KEY_STRAFE_RIGHT = false;
                }else if(key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
                    KEY_JUMP = true;
                }else if(key == GLFW_KEY_SPACE && action == GLFW_RELEASE) {
                    KEY_JUMP = false;
                }else if(key == GLFW_KEY_TAB && action == GLFW_RELEASE) {
                    CHANGE_MODEL = true;
                }else if(key == GLFW_KEY_1 && action == GLFW_PRESS) {
                    KEY_ROTATE_ENTITY_CLOCKWISE = true;
                }else if(key == GLFW_KEY_1 && action == GLFW_RELEASE) {
                    KEY_ROTATE_ENTITY_CLOCKWISE = false;
                }else if(key == GLFW_KEY_2 && action == GLFW_PRESS) {
                    KEY_ROTATE_ENTITY_COUNTERCLOCKWISE = true;
                }else if(key == GLFW_KEY_2 && action == GLFW_RELEASE) {
                    KEY_ROTATE_ENTITY_COUNTERCLOCKWISE = false;
                }else if(key == GLFW_KEY_DELETE && action == GLFW_RELEASE) {
                    DELETE_ENTITY = true;
                }
            }
        });
        glfwSetScrollCallback(window, scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                SCROLL_CHANGE += yoffset;
                ENTITY_SCALE_CHANGE = yoffset;
            }
        });
        glfwSetMouseButtonCallback(window, mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if(button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
                    LEFT_CLICK = true;
                }else if(button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE) {
                    LEFT_CLICK = false;
                    SELECT_ENTITY = true;
//                    for(String buttonName : buttons.keySet()){
//                        if(buttons.get(buttonName).isHighlighted()){
//                            buttons.get(buttonName).setClicked(true);
//                        }
//                    }
                }else if(button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS) {
                    RIGHT_CLICK = true;
                }else if(button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_RELEASE) {
                    RIGHT_CLICK = false;
                    PLACE_MODEL = true;
                }
            }
        });
        glfwSetCursorPosCallback(window, cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                MOUSE_X = xpos;
                MOUSE_Y = ypos;
//                ,
            }
        });
        previousMouseX = MOUSE_X;
        previousMouseY = MOUSE_Y;
    }

    public static void processEvents(){
        MOUSE_X_CHANGE = MOUSE_X - previousMouseX;
        MOUSE_Y_CHANGE = MOUSE_Y - previousMouseY;
        previousMouseX = MOUSE_X;
        previousMouseY = MOUSE_Y;
    }

}
