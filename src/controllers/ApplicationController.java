package controllers;

import controllers.codeWindow.CodeWindowController;
import controllers.flowchartWindow.FlowchartTextBoxController;
import controllers.flowchartWindow.FlowchartWindowController;
import controllers.gui.ButtonController;
import controllers.gui.GUIController;
import controllers.gui.GUIWindowController;
import gui.Header;
import gui.UserPreferences;
import gui.buttons.HeaderMenu;
import main.EngineTester;
import main.GeneralSettings;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class ApplicationController {

    //Temporary variables
    public static boolean CTRL_PRESSED = false;
    public static boolean PASTE = false;
    public static boolean LEFT_CLICK = false;
    public static boolean RIGHT_CLICK = false;
    public static double MOUSE_X = 0.0;
    public static double MOUSE_Y = 0.0;
    private static double previousMouseX = 0;
    private static double previousMouseY = 0;
    private static boolean LEFT_MOUSE_HELD = false;
    private static HeaderMenu openMenu = null;
    private static Header header;
    private static Vector2f aspectRatio = new Vector2f(1, 1);

    //Permanent variables
    CodeWindowController codeWindowController;
    FlowchartWindowController flowchartWindowController;
    GUIController guiController;

    public TextLineController getTextLineController() {
        return textLineController;
    }

    TextLineController textLineController = new TextLineController();
    int activeWindow = ControllerSettings.GUI_WINDOW;

    /**
     * Updates user preferences
     * */
    public void updateUserPref() {


        //Flowchart background color and flowchart line color
        if(flowchartWindowController != null && flowchartWindowController.getFlowchartTextBoxController() != null) {
            //flowchartWindowController.getFlowchartTextBoxController().changeTextBoxBackgroundcolor3f();
            FlowchartTextBoxController.setTextNumberBackgroundColor(GeneralSettings.USERPREF.getFlowchartBoxlinenumberBGColor3f());
            FlowchartTextBoxController.setBackgroundColor(GeneralSettings.USERPREF.getFlowchartBoxbackgroundColor3f());
            FlowchartTextBoxController.setHighlightedColor(GeneralSettings.USERPREF.getFlowchartBoxHighlightColor3f());
        }

        if(codeWindowController != null && codeWindowController.getCodeWindow() != null) {
            codeWindowController.changeCodewindowBGcolor3f(GeneralSettings.USERPREF.getTexteditorBGColor3f());
            codeWindowController.changeCodewindowLinenumberBGColor3f(GeneralSettings.USERPREF.getTexteditorLinenumberBGColor3f());
        }

        if(header != null) {
            header.changeHeadercolor();
            header.changeButtonColors();
            header.changeTempFileManagerLimit(GeneralSettings.USERPREF.getTempFileLimit());
        }

        //Background color
        GL11.glClearColor(GeneralSettings.USERPREF.getBackgroundColor3f().getX(), GeneralSettings.USERPREF.getBackgroundColor3f().getY(), GeneralSettings.USERPREF.getBackgroundColor3f().getZ(), 1);
    }


    public ApplicationController(){
        header = new Header(new Vector2f(-1, 1-(GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR + GeneralSettings.TEXT_BUTTON_PADDING*2)), new Vector2f(2f, GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR + GeneralSettings.TEXT_BUTTON_PADDING*2), this);
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
        if(codeWindowController != null){
            codeWindowController.updateAspectRatio(new Vector2f(GeneralSettings.ASPECT_RATIO.m00, GeneralSettings.ASPECT_RATIO.m11), header.getGuiFilledBox().getSize().y);
        }
        if(flowchartWindowController != null) {
            flowchartWindowController.updateAspectRatio(GeneralSettings.ASPECT_RATIO);
        }
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
        if(activeWindow == ControllerSettings.CODE_WINDOW) {
            codeWindowController.scroll((float) scrollChange / 10);
        }else if(flowchartWindowController != null){
            flowchartWindowController.updateZoom((float) scrollChange / 10);
        }
    }

    /**
     * Processes the events that should be executed when the mouse is either clicked or released
     * @param button the GLFW mouse button representation of which mouse button was pressed
     * @param action the GLFW action of the button press, either GLFW_PRESS or GLFW_RELEASE
     */
    public void click(long window, int button, int action){
        org.lwjgl.glfw.GLFW.glfwMakeContextCurrent(EngineTester.getWindow());
        if(button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
            LEFT_CLICK = true;
            LEFT_MOUSE_HELD = true;


            if(window == EngineTester.getWindow()) {
                if (codeWindowController != null) {
                    //Process the mouse click in code window
                    if (codeWindowController.mouseLeftClick()) {
                        activeWindow = ControllerSettings.CODE_WINDOW;
                    } else {
                        activeWindow = ControllerSettings.FLOWCHART_WINDOW;
                    }
                }
            }
        }else if(button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE){
            LEFT_CLICK = false;
            LEFT_MOUSE_HELD = false;

            if(window == EngineTester.getWindow()) {
                if (codeWindowController != null) {
                    //Process the mouse release in code window
                    codeWindowController.mouseLeftRelease();
                }
                if(flowchartWindowController != null){
                    flowchartWindowController.click(button, action);
                }
            }

            ButtonController.click(window, new Vector2f((float)MOUSE_X, (float)MOUSE_Y));


        }else if(button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS) {
            RIGHT_CLICK = true;
        }else if(button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_RELEASE) {
            RIGHT_CLICK = false;
        }
        if(window != EngineTester.getWindow()) {
            GUIWindowController.click(window, button, action);
        }
    }

    /**
     * Moves the mouse to a new position
     * @param xPosition the mouses new x position in pixels
     * @param yPosition the mouses new y position in pixels
     */
    public void moveMouse(long window, double xPosition, double yPosition) {
        //Save the old mouse position for processing position changes
        previousMouseX = MOUSE_X;
        previousMouseY = MOUSE_Y;

        //Convert xPosition and yPosition and convert them into the standard coordinate system
        MOUSE_X = xPosition / GeneralSettings.DISPLAY_WIDTH * 2 - 1f;
        MOUSE_Y = 1 - yPosition / GeneralSettings.DISPLAY_HEIGHT * 2;

        if (codeWindowController != null) {
            //Process the mouse movement in the code window
            codeWindowController.moveMouse(new Vector2f((float) MOUSE_X, (float) MOUSE_Y));
        }

        ButtonController.hover(window, new Vector2f((float) MOUSE_X, (float) MOUSE_Y));
        if (flowchartWindowController != null){
            if (LEFT_MOUSE_HELD) {
                float xChange = (float) (MOUSE_X - previousMouseX);
                float yChange = (float) (MOUSE_Y - previousMouseY);
                flowchartWindowController.updateTranslation(new Vector2f((float) xChange * flowchartWindowController.getZoom(), (float) yChange * flowchartWindowController.getZoom()));
            }
            flowchartWindowController.moveMouse(MOUSE_X, MOUSE_Y);
        }
    }


    public Header getHeader(){
        return header;
    }

    public void setCodeWindowController(CodeWindowController codeWindowController){
        this.codeWindowController = codeWindowController;
        activeWindow = ControllerSettings.CODE_WINDOW;
    }

    public CodeWindowController getCodeWindowController(){
        return codeWindowController;
    }

    public FlowchartWindowController getFlowchartWindowController() {
        return flowchartWindowController;
    }

    public void setFlowchartWindowController(FlowchartWindowController flowchartWindowController) {
        this.flowchartWindowController = flowchartWindowController;
    }
    
    public void textEditorView(){
        if(codeWindowController != null){
            codeWindowController.maximize();
            activeWindow = ControllerSettings.CODE_WINDOW;
            if(flowchartWindowController != null) {
                flowchartWindowController.minimize();
            }
        }
    }
    
    public void flowchartView(){
        if(flowchartWindowController != null) {
            codeWindowController.minimize();
            flowchartWindowController.maximize();
            activeWindow = ControllerSettings.FLOWCHART_WINDOW;
        }
    }
    
    public void splitScreen(){
        if(codeWindowController != null){
            codeWindowController.goSplitScreen();
            if(flowchartWindowController != null) {
                flowchartWindowController.goSplitScreen();
            }
        }
    }

    public GUIController getGuiController() {
        return guiController;
    }
}
