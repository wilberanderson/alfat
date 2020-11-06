package utils;

import gui.buttons.Button;
import gui.buttons.HeaderMenu;
import gui.buttons.HighlightableButton;
import main.GeneralSettings;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

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
    public static List<Button> buttons;
    private static GLFWCharCallback charCallback;
    private static GLFWFramebufferSizeCallback framebufferSizeCallback;
    public static List<Character> codepoints = new ArrayList<>();
    private static HeaderMenu openMenu = null;

    public static void init(long window){
        buttons = new ArrayList<>();

//        TextButton button = new TextButton(new Vector2f(-1f, 1-GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR - 2*GeneralSettings.TEXT_BUTTON_PADDING), "Text creation test", new Vector3f(0, 0, 0), GeneralSettings.HIGHLIGHT_COLOR, new Vector3f(1, 1, 1), GeneralSettings.TACOMA, GeneralSettings.FONT_SIZE, GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE) {
//            @Override
//            public void onPress() {
//                System.out.println("Test success");
//            }
//        };
//        buttons.add(button);
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

                    for(Button b: buttons){
                        if(MOUSE_X >= b.getPosition().x && MOUSE_Y >= b.getPosition().y && MOUSE_X < b.getPosition().x+b.getSize().x && MOUSE_Y < b.getPosition().y+b.getSize().y){
                            b.onPress();
                            if(openMenu != null){
                                openMenu.close();
                                openMenu = null;
                            }
                            if(b instanceof HeaderMenu){
                                openMenu = (HeaderMenu) b;
                            }
                            break;
                        }
                    }
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
                MOUSE_X = xpos/GeneralSettings.DISPLAY_WIDTH*2 - 1f;
                MOUSE_Y = 1-ypos/GeneralSettings.DISPLAY_HEIGHT*2;
                for(Button b: buttons){
                    if(b instanceof HighlightableButton) {
                        if (MOUSE_X >= b.getPosition().x && MOUSE_Y >= b.getPosition().y && MOUSE_X < b.getPosition().x + b.getSize().x && MOUSE_Y < b.getPosition().y + b.getSize().y) {
                            if(!((HighlightableButton) b).isHighlighted()) {
                                ((HighlightableButton) b).highlight();
                            }
                        }else{
                            if(((HighlightableButton) b).isHighlighted()) {
                                ((HighlightableButton) b).unhighlight();
                            }
                        }
                    }
                }
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

        glfwSetFramebufferSizeCallback(window, framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                GeneralSettings.DISPLAY_WIDTH = width;
                GeneralSettings.DISPLAY_HEIGHT = height;
                GL11.glViewport(0, 0, GeneralSettings.DISPLAY_WIDTH, GeneralSettings.DISPLAY_HEIGHT);
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
