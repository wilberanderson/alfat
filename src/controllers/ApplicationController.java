package controllers;

import controllers.codeWindow.CodeWindowController;
import controllers.flowchartWindow.FlowchartWindowController;
import controllers.gui.ButtonController;
import controllers.gui.GUIController;
import gui.Header;
import gui.buttons.Button;
import gui.buttons.HeaderMenu;
import gui.buttons.HighlightableButton;
import main.GeneralSettings;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class ApplicationController {

    //Temporary variables
    public static boolean CTRL_PRESSED = false;
    public static boolean PASTE = false;
    public static boolean LEFT_CLICK = false;
    public static boolean RIGHT_CLICK = false;
    public static double SCROLL_CHANGE = 0.0;
    public static double MOUSE_X = 0.0;
    public static double MOUSE_Y = 0.0;
    private static double previousMouseX = 0;
    private static double previousMouseY = 0;
    private static boolean LEFT_MOUSE_HELD = false;
//    public static List<Button> buttons;
    private static HeaderMenu openMenu = null;
    private static Header header;
    private static Vector2f aspectRatio = new Vector2f(1, 1);

    //Permanent variables
    CodeWindowController codeWindowController;
    FlowchartWindowController flowChartWindowController;
    GUIController guiController;



    public ApplicationController(){
//        ApplicationController.buttons = new ArrayList<>();
    }

    /**
     * Types a single character
     * @param codepoint the 32 bit UTF codepoint for the character
     * TODO: Consider supporting more characters than just ascii characters
     */
    public void type(int codepoint){
        //Process the typed character
        codeWindowController.type((char)codepoint);
    }

    /**
     * Sets the framebuffer to the new width and height
     * @param width the new width of the window in pixels
     * @param height the new height of the window in pixels
     */
    public void setFrameBufferSize(int width, int height){
        //Resize the windows viewport to cause the application to fill the full window
        GL11.glViewport(0, 0, width, height);

        //Update the aspect ratio
        GeneralSettings.updateAspectRatio(width, height);
        header.setAspectRatio(new Vector2f(GeneralSettings.ASPECT_RATIO.m00, GeneralSettings.ASPECT_RATIO.m11));
        codeWindowController.updateAspectRatio(new Vector2f(GeneralSettings.ASPECT_RATIO.m00, GeneralSettings.ASPECT_RATIO.m11), header.getGuiFilledBox().getSize().y);
        flowChartWindowController.updateAspectRatio(GeneralSettings.ASPECT_RATIO);
        aspectRatio = new Vector2f(GeneralSettings.ASPECT_RATIO.m00, GeneralSettings.ASPECT_RATIO.m11);
    }

    /**
     * Updates internal parameters about whether a key should currently be pressed
     * @param key the GLFW key representation of which key on the keyboard is pressed
     * @param action the GLFW action of the key, is either GLFW_PRESS, GLFW_RELEASE, or GLFW_REPEAT
     */
    public void pressKey(int key, int action){
        if(key == GLFW_KEY_UP && (action == GLFW_PRESS || action == GLFW_REPEAT)) {
            codeWindowController.keyPress(ControllerSettings.CURSOR_UP);
        }else if(key == GLFW_KEY_DOWN && (action == GLFW_PRESS || action == GLFW_REPEAT)) {
            codeWindowController.keyPress(ControllerSettings.CURSOR_DOWN);
        }else if(key == GLFW_KEY_LEFT && (action == GLFW_PRESS || action == GLFW_REPEAT)) {
            codeWindowController.keyPress(ControllerSettings.CURSOR_LEFT);
        }else if(key == GLFW_KEY_RIGHT && (action == GLFW_PRESS || action == GLFW_REPEAT)) {
            codeWindowController.keyPress(ControllerSettings.CURSOR_RIGHT);
        }else if(key == GLFW_KEY_BACKSPACE && (action == GLFW_PRESS || action == GLFW_REPEAT)) {
            codeWindowController.keyPress(ControllerSettings.CURSOR_BACKSPACE);
        }else if(key == GLFW_KEY_DELETE && (action == GLFW_PRESS || action == GLFW_REPEAT)) {
            codeWindowController.keyPress(ControllerSettings.CURSOR_DELETE);
        }else if((key == GLFW_KEY_LEFT_CONTROL || key == GLFW_KEY_RIGHT_CONTROL) && action == GLFW_PRESS) {
            CTRL_PRESSED = true;
        }else if((key == GLFW_KEY_LEFT_CONTROL || key == GLFW_KEY_RIGHT_CONTROL) && action == GLFW_RELEASE) {
            CTRL_PRESSED = false;
        }else if(key == GLFW_KEY_V && CTRL_PRESSED && action == GLFW_PRESS){
            PASTE = true;
        }else if(key == GLFW_KEY_ENTER && action == GLFW_PRESS) {
            codeWindowController.type('\n');
        }
    }

    /**
     * Performs on scroll actions
     * @param scrollChange the amount by which the cursor was scrolled
     */
    public void scroll(double scrollChange){
        //Scrolling is processed on frame, hold the net value of the scrolling
        SCROLL_CHANGE += scrollChange;
        codeWindowController.scroll((float)scrollChange/10);
        flowChartWindowController.updateZoom((float)scrollChange/10);
    }

    /**
     * Processes the events that should be executed when the mouse is either clicked or released
     * @param button the GLFW mouse button representation of which mouse button was pressed
     * @param action the GLFW action of the button press, either GLFW_PRESS or GLFW_RELEASE
     */
    public void click(int button, int action){

        if(button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
            LEFT_CLICK = true;
            LEFT_MOUSE_HELD = true;

            if(codeWindowController != null) {
                //Process the mouse click in code window
                codeWindowController.mouseLeftClick();
            }
        }else if(button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE){
            LEFT_CLICK = false;
            LEFT_MOUSE_HELD = false;

            if(codeWindowController != null) {
                //Process the mouse release in code window
                codeWindowController.mouseLeftRelease();
            }

            ButtonController.click(new Vector2f((float)MOUSE_X, (float)MOUSE_Y));



        }else if(button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS) {
            RIGHT_CLICK = true;
        }else if(button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_RELEASE) {
            RIGHT_CLICK = false;
        }
    }

    /**
     * Moves the mouse to a new position
     * @param xPosition the mouses new x position in pixels
     * @param yPosition the mouses new y position in pixels
     */
    public void moveMouse(double xPosition, double yPosition){
        //Save the old mouse position for processing position changes
        previousMouseX = MOUSE_X;
        previousMouseY = MOUSE_Y;

        //Convert xPosition and yPosition and convert them into the standard coordinate system
        MOUSE_X = xPosition/GeneralSettings.DISPLAY_WIDTH*2 - 1f;
        MOUSE_Y = 1-yPosition/GeneralSettings.DISPLAY_HEIGHT*2;

        if(codeWindowController != null){
            //Process the mouse movement in the code window
            codeWindowController.moveMouse(new Vector2f((float)MOUSE_X, (float)MOUSE_Y));
        }

        ButtonController.hover(new Vector2f((float)MOUSE_X, (float)MOUSE_Y));

        if(LEFT_MOUSE_HELD && flowChartWindowController != null) {
            float xChange = (float)(MOUSE_X - previousMouseX);
            float yChange = (float)(MOUSE_Y - previousMouseY);
            flowChartWindowController.updateTranslation(new Vector2f((float) xChange, (float) yChange));
        }
    }

    public static void setHeader(Header newHeader){
        header = newHeader;
    }

    public void setCodeWindowController(CodeWindowController codeWindowController){
        this.codeWindowController = codeWindowController;
    }

    public CodeWindowController getCodeWindowController(){
        return codeWindowController;
    }

    public FlowchartWindowController getFlowChartWindowController() {
        return flowChartWindowController;
    }

    public void setFlowChartWindowController(FlowchartWindowController flowChartWindowController) {
        this.flowChartWindowController = flowChartWindowController;
    }
}
