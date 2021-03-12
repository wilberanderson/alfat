package controllers;

import controllers.codeWindow.CodeWindowController;
import controllers.flowchartWindow.FlowchartTextBoxController;
import controllers.flowchartWindow.FlowchartWindowController;
import controllers.gui.ButtonController;
import gui.Header;
import gui.Mouse;
import gui.Notifications.Notifications;
import gui.buttons.HeaderMenu;
import gui.texts.*;
import main.EngineTester;
import main.GeneralSettings;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class ApplicationController {

    //Temporary variables
    public static boolean CTRL_PRESSED = false;
    public static boolean ALT_PRESSED = false;
    public static boolean SHIFT_PRESSED = false;
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

    private boolean hoveringBoundary = false;
    private boolean draggingBoundary = false;


    //Permanent variables
    CodeWindowController codeWindowController;
    FlowchartWindowController flowchartWindowController;
    public static Notifications notification = new Notifications();

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
            FlowchartTextBoxController.setTextNumberBackgroundColor(GeneralSettings.USERPREF.getFlowchartBoxlinenumberBGColor3f());
            FlowchartTextBoxController.setBackgroundColor(GeneralSettings.USERPREF.getFlowchartBoxbackgroundColor3f());
            FlowchartTextBoxController.setHighlightedColor(GeneralSettings.USERPREF.getFlowchartBoxHighlightColor3f());
        }

        if(codeWindowController != null && codeWindowController.getCodeWindow() != null) {
            codeWindowController.changeCodewindowBGcolor3f(GeneralSettings.USERPREF.getTexteditorBGColor3f());
            codeWindowController.changeCodewindowLinenumberBGColor3f(GeneralSettings.USERPREF.getTexteditorLinenumberBGColor3f());
            codeWindowController.changeScrollBarsColor3f(GeneralSettings.USERPREF.getScrollBarColor3f());

            //Colors For Keywords
            BranchWord.setColor(GeneralSettings.USERPREF.getBranchTextColor3f());
            CommandWord.setColor(GeneralSettings.USERPREF.getCommandTextColor3f());
            CommentWord.setColor(GeneralSettings.USERPREF.getCommentColor3f());
            ErrorWord.setColor(GeneralSettings.USERPREF.getErrorColor3f());
            ImmediateWord.setColor(GeneralSettings.USERPREF.getImmediateColor3f());
            LabelWord.setColor(GeneralSettings.USERPREF.getLabelColor3f());
            LineNumberWord.setColor(GeneralSettings.USERPREF.getLineNumberColor3f());
            RegisterWord.setColor(GeneralSettings.USERPREF.getRegisterColor3f());
            SeparatorWord.setColor(GeneralSettings.USERPREF.getSeparatorColor3f());
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
        if(codeWindowController != null) {
            codeWindowController.type((char) codepoint);
        }
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
        //Processes cursor control keys
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
        }else if(key == GLFW_KEY_ENTER && action == GLFW_PRESS) {
            codeWindowController.type('\n');
        }
        else if(key == GLFW_KEY_TAB && action == GLFW_PRESS) {
            codeWindowController.type('\t');
        }
        //Process shortcut keys
        else if((key == GLFW_KEY_LEFT_CONTROL || key == GLFW_KEY_RIGHT_CONTROL) && action == GLFW_PRESS) {
            CTRL_PRESSED = true;
        }else if((key == GLFW_KEY_LEFT_CONTROL || key == GLFW_KEY_RIGHT_CONTROL) && action == GLFW_RELEASE) {
            CTRL_PRESSED = false;
        }else if((key == GLFW_KEY_LEFT_SHIFT || key == GLFW_KEY_RIGHT_SHIFT) && action == GLFW_PRESS) {
            SHIFT_PRESSED = true;
        }else if((key == GLFW_KEY_LEFT_SHIFT || key == GLFW_KEY_RIGHT_SHIFT) && action == GLFW_RELEASE) {
            SHIFT_PRESSED = false;
        }else if((key == GLFW_KEY_LEFT_ALT || key == GLFW_KEY_RIGHT_ALT) && action == GLFW_PRESS) {
            ALT_PRESSED = true;
        }else if((key == GLFW_KEY_LEFT_ALT || key == GLFW_KEY_RIGHT_ALT) && action == GLFW_RELEASE) {
            ALT_PRESSED = false;
        }
//        else if(key == GLFW_KEY_V && CTRL_PRESSED && action == GLFW_PRESS){
//            PASTE = true;
//        }
        //If a shortcut may have been performed process shortcut options
        if(CTRL_PRESSED && action == GLFW_PRESS){
            processShortcuts(ALT_PRESSED, SHIFT_PRESSED, key);
        }
    }

    /**
     * Processes keyboard shortcuts
     * @param alt boolean that is true if the alt key is pressed
     * @param shift boolean that is true if the shift key is pressed
     * @param key the GLFW int representation of which key was just pressed
     */
    private void processShortcuts(boolean alt, boolean shift, int key){
        //*******************File shortcuts****************
        //Open options
        if(key == GLFW_KEY_O){
            header.openFile();
            return;
        }
        //Save options
        if(key == GLFW_KEY_S){
            if(shift){
                header.saveAs();
                return;
            }
            if(alt){
                header.saveFlowchart();
                return;
            }
            header.save();
            return;
        }
        //Open menu
        if(key == GLFW_KEY_COMMA){
            header.settings();
            return;
        }

        //****************Flowchart shortcuts*******************
        //Initial generation
        if(key == GLFW_KEY_G){
            header.generate(GeneralSettings.OPEN_PARTIAL_FILE);
            return;
        }
        //Regeneration
        if(key == GLFW_KEY_R){
            if(shift){
                header.regenerateFromSource(GeneralSettings.OPEN_PARTIAL_FILE);
                return;
            }
            header.regenerateFromEditor(GeneralSettings.OPEN_PARTIAL_FILE);
            return;
        }
        //*******************View shortcuts***************
        if(key == GLFW_KEY_1){
            header.textEditorView();
            return;
        }
        if(key == GLFW_KEY_2){
            header.flowchartView();
            return;
        }
        if(key == GLFW_KEY_3){
            header.splitScreenView();
            return;
        }
        if(key == GLFW_KEY_0){
            header.resetZoom();
            return;
        }
        //**********************Analysis shortcuts**********
        if(key == GLFW_KEY_I){
            if (shift){
                header.setPartialTagClosing();
            } else {
                header.setPartialTag();
            }
        }


        if(key == GLFW_KEY_L){
            if(alt){
                header.invalidLabels();
                return;
            }
            if(shift){
                header.clearRegisters();
                return;
            }
            header.registers();
            return;
        }
    }

    /**
     * Performs on scroll actions
     * @param scrollChange the amount by which the cursor was scrolled
     */
    public void scroll(double scrollChange){
        //Scrolling is processed on frame, hold the net value of the scrolling
        if(activeWindow == ControllerSettings.CODE_WINDOW) {
            codeWindowController.scroll((float) -scrollChange / 10);
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
        //Ensure context is appropriate for processing the action
        org.lwjgl.glfw.GLFW.glfwMakeContextCurrent(window);
        //When left mouse button pressed
        if(button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
            LEFT_CLICK = true;
            LEFT_MOUSE_HELD = true;

            //Mark the appropriate window to be active for processing scrolling and panning events
            if(window == EngineTester.getWindow()) {
                if(hoveringBoundary){
                    draggingBoundary = true;
                }
                if (!hoveringBoundary && codeWindowController != null) {
                    //Process the mouse click in code window
                    if (codeWindowController.mouseLeftClick()) {
                        activeWindow = ControllerSettings.CODE_WINDOW;
                    } else {
                        activeWindow = ControllerSettings.FLOWCHART_WINDOW;
                    }
                }
            }
        }
        //When left mouse button released
        else if(button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE){
            LEFT_CLICK = false;
            LEFT_MOUSE_HELD = false;
            draggingBoundary = false;

            //Process events for CodeWindow or FlowchartWindow as appropriate
            if(window == EngineTester.getWindow()) {
                if(activeWindow == ControllerSettings.FLOWCHART_WINDOW && flowchartWindowController != null){
                   flowchartWindowController.click(button, action);
                }
                if (codeWindowController != null) {
                    //Process the mouse release in code window
                    codeWindowController.mouseLeftRelease();
                }
            }

            //A button may have been clicked, allow the button controller to test and see
            ButtonController.click(window, new Vector2f((float)MOUSE_X, (float)MOUSE_Y));
        }
        //Ability to handle right clicks
        //TODO: Determine if these are needed
        else if(button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS) {
            RIGHT_CLICK = true;
        }else if(button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_RELEASE) {
            RIGHT_CLICK = false;
        }
        //If a different window was selected then a subwindow may need to process the action, send the event to the GUIWindowController
//        if(window != EngineTester.getWindow()) {
//            GUIWindowController.click(window, button, action);
//        }
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

        //Code window needs to know mouse position for cursor movements and scrollbars
        if (codeWindowController != null) {
            //Process the mouse movement in the code window
            codeWindowController.moveMouse(new Vector2f((float) MOUSE_X, (float) MOUSE_Y));
        }


        //Flowchart window processing
        if (!draggingBoundary && flowchartWindowController != null){
            //Panning is based on the change in mouse position adjusted for the zoom level of the application
            if (LEFT_MOUSE_HELD && !hoveringBoundary && activeWindow == ControllerSettings.FLOWCHART_WINDOW) {
                float xChange = (float) (MOUSE_X - previousMouseX);
                float yChange = (float) (MOUSE_Y - previousMouseY);
                flowchartWindowController.updateTranslation(new Vector2f((float) xChange * flowchartWindowController.getZoom(), (float) yChange * flowchartWindowController.getZoom()));
            }
            //Selecting text boxes is based on the raw position
            flowchartWindowController.moveMouse(MOUSE_X, MOUSE_Y);
        }

        //Have button controller test to see if any highlightable buttons should be highlighted
        ButtonController.hover(window, new Vector2f((float) MOUSE_X, (float) MOUSE_Y));

        if(flowchartWindowController != null) {
            if (!codeWindowController.isHoveringScroll() && MOUSE_X > flowchartWindowController.getPosition().x - 0.01 && MOUSE_X < flowchartWindowController.getPosition().x + 0.01 && MOUSE_Y < -1 + flowchartWindowController.getSize().y) {
                Mouse.setResize();
                hoveringBoundary = true;
            }else{
                hoveringBoundary = false;
            }
        }

        if(draggingBoundary){
            float xChange = (float) (MOUSE_X - previousMouseX);
            codeWindowController.moveBoundary(xChange);
            flowchartWindowController.moveBoundary(xChange);
        }
    }

    //TODO: Make sure it's ok to make this static...
    public static Header getHeader(){
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

    /**
     * Makes the text editor full screen
     */
    public void textEditorView(){
        //If a code window exists maximize it and make it active
        if(codeWindowController != null){
            codeWindowController.maximize();
            activeWindow = ControllerSettings.CODE_WINDOW;

            //If a flowchart window exists it needs to be minimized
            if(flowchartWindowController != null) {
                flowchartWindowController.minimize();
            }
        }
    }

    /**
     * Makes the flowchart window full screen
     */
    public void flowchartView(){
        //If a flowchart window exists
        if(flowchartWindowController != null) {
            //The text editor needs to be minimized
            codeWindowController.minimize();
            //The flowchart needs to be maximized
            flowchartWindowController.maximize();
            //Make the flowchart window the active window for scrolling events
            activeWindow = ControllerSettings.FLOWCHART_WINDOW;
        }
    }

    /**
     * Makes the program split screen between text editor and flowchart
     * TODO: Determine whether to make it possible to go splitscreen before generating flowchart
     */
    public void splitScreen(){
        //If a code window has been created have it go split screen
        if(codeWindowController != null ){
            codeWindowController.goSplitScreen();

            //If a flowchart window has been created have it go split screen
            if(flowchartWindowController != null) {
                flowchartWindowController.goSplitScreen();
            }
        }
    }
}
