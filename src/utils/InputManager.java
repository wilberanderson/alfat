package utils;

import gui.FlowChartWindow;
import gui.Header;
import gui.buttons.Button;
import gui.buttons.HeaderMenu;
import gui.buttons.HighlightableButton;
import main.GeneralSettings;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

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
    private static boolean LEFT_MOUSE_HELD = false;
    private static GLFWKeyCallback keyCallback;
    private static GLFWScrollCallback scrollCallback;
    private static GLFWMouseButtonCallback mouseButtonCallback;
    private static GLFWCursorPosCallback cursorPosCallback;
    private static GLFWWindowFocusCallback windowFocusCallback;
    public static List<Button> buttons;
    private static GLFWCharCallback charCallback;
    private static GLFWFramebufferSizeCallback framebufferSizeCallback;
    public static List<Character> codepoints = new ArrayList<>();
    private static HeaderMenu openMenu = null;
    private static FlowChartWindow flowChartWindow;
    private static Header header;
    private static Vector2f aspectRatio = new Vector2f(1, 1);

    public static void init(long window){
        buttons = new ArrayList<>();
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
                    LEFT_MOUSE_HELD = true;


                }else if(button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE){
                    LEFT_CLICK = false;
                    LEFT_MOUSE_HELD = false;

                    boolean buttonPressed = false;
                    List<HeaderMenu> toClose = new ArrayList<>();

                    for(Button b: buttons){
                        if(MOUSE_X >= b.getPosition().x && MOUSE_Y >= b.getPosition().y && MOUSE_X < b.getPosition().x+b.getSize().x && MOUSE_Y < b.getPosition().y+b.getSize().y){
                            if (b instanceof HeaderMenu){
                                if(openMenu != b && openMenu != null){
                                    toClose.add((HeaderMenu) openMenu);
                                }
                                openMenu = (HeaderMenu) b;
                            } else {
                                toClose.add(openMenu);
                                openMenu = null;
                            }
                            b.onPress();
                            buttonPressed = true;
                            break;
                        }
                    }

                    if (!buttonPressed){
                        for (Button b : buttons) {
                            if (b instanceof HeaderMenu) {
                                if (((HeaderMenu) b).isOpen) {
                                    toClose.add((HeaderMenu)b);
                                }
                            }
                        }
                        if(header.getCodeWindow() != null) {
                            if (MOUSE_X < header.getCodeWindow().getSize().x - 1 && MOUSE_Y < header.getCodeWindow().getSize().y - 1) {
                                header.getCodeWindow().setScrollable(true);
                            } else {
                                header.getCodeWindow().setScrollable(false);
                            }
                        }
                        if(header.getFlowChartWindow() != null) {
                            if (MOUSE_X > header.getFlowChartWindow().getPosition().x && MOUSE_Y < header.getFlowChartWindow().getSize().y - 1) {
                                header.getFlowChartWindow().setZoomable(true);
                            } else {
                                header.getFlowChartWindow().setZoomable(false);
                            }
                        }
                    }
                    for(HeaderMenu b : toClose){
                        b.close();
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
                previousMouseX = MOUSE_X;
                previousMouseY = MOUSE_Y;
                MOUSE_X = xpos/GeneralSettings.DISPLAY_WIDTH*2 - 1f;
                MOUSE_Y = 1-ypos/GeneralSettings.DISPLAY_HEIGHT*2;
                for(Button b: buttons){
                    if(b instanceof HighlightableButton) {
                        if (MOUSE_X >= b.getPosition().x && MOUSE_Y >= b.getPosition().y && MOUSE_X< b.getPosition().x + b.getSize().x && MOUSE_Y< b.getPosition().y + b.getSize().y) {
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

        glfwSetCharCallback(window, charCallback = new GLFWCharCallback() {
            @Override
            public void invoke(long window, int codepoint) {
                codepoints.add((char)codepoint);
            }
        });

        glfwSetFramebufferSizeCallback(window, framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                if(!GeneralSettings.SCREENSHOT_IN_PROGRESS) {
                    GeneralSettings.updateAspectRatio(width, height);
                    GL11.glViewport(0, 0, width, height);
                    header.setAspectRatio(new Vector2f(GeneralSettings.ASPECT_RATIO.m00, GeneralSettings.ASPECT_RATIO.m11));
                    flowChartWindow.setAspectRatio(GeneralSettings.ASPECT_RATIO);
                    aspectRatio = new Vector2f(GeneralSettings.ASPECT_RATIO.m00, GeneralSettings.ASPECT_RATIO.m11);
                }else{
                    GL11.glViewport(0, 0, width, height);
                }
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
        if(SCROLL_CHANGE != 0){
            flowChartWindow.updateZoom((float)SCROLL_CHANGE/10);
        }
        if(LEFT_MOUSE_HELD && (MOUSE_X_CHANGE != 0 || MOUSE_Y_CHANGE != 0)){
            flowChartWindow.updateTranslation(new Vector2f((float)MOUSE_X_CHANGE*flowChartWindow.getZoom()*2, (float)MOUSE_Y_CHANGE*flowChartWindow.getZoom()*2));
        }
    }

    public static void setFlowChartWindow(FlowChartWindow newFlowChartWindow){
        flowChartWindow = newFlowChartWindow;
    }

    public static void setHeader(Header newHeader){
        header = newHeader;
    }

}
