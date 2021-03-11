package main;
import controllers.ApplicationController;
import controllers.flowchartWindow.FlowchartWindow;
import controllers.gui.GUIWindowController;
import gui.*;
import gui.textBoxes.TextBox;
import gui.windows.GUIWindow;
import gui.windows.PopupWindow;
import loaders.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import parser.GlobalParser;
import parser.ParserManager;
import rendering.renderEngine.GUIElementRenderer;
import rendering.renderEngine.MasterRenderer;
import controllers.GLFWEventController;

import java.nio.*;
import java.util.ArrayList;
import java.util.List;


public class EngineTester {

    // The window handle
    private static long window;

    //Temporary attributes
    private List<GuiTexture> guis;
    private ApplicationController applicationController;
    private GUIWindow guiWindow;
    /**
     * Used for all operations of the program
     *  - Initializes all relevant objects
     *  - Enters the main loop
     *  - If an exception occurs in the main loop the crash method is called
     */
    public void run(String[] args) {

        init(args);
        try {
            loop();
        }catch(Exception e){
            e.printStackTrace();
            crash();
        }
        exit();

    }

    /**
     * Initializes basic one time tasks
     *  - Creates the window
     *  - Initializes the openGL context
     *  - Updates the icon on taskbar and the window
     *
     */
    private void init(String[] args) {

        GeneralSettings.USERPREF = new UserPreferences();
        GlobalParser.PARSER_MANAGER = new ParserManager();


        //********************************Create the window************************************
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !GLFW.glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        GLFW.glfwDefaultWindowHints(); // optional, the current window hints are already the default
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE); // the window will stay hidden after creation
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE); // the window will be resizable
//        GLFW.glfwWindowHint(GLFW.)

        // Create the window
        window = GLFW.glfwCreateWindow(GeneralSettings.DEFAULT_WIDTH, GeneralSettings.DEFAULT_HEIGHT, "ALFAT", MemoryUtil.NULL, MemoryUtil.NULL);
        if ( window == MemoryUtil.NULL )
            throw new RuntimeException("Failed to create the GLFW window");


        // Get the thread stack and push a new frame
        try ( MemoryStack stack = MemoryStack.stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            GLFW.glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            // Center the window
            if(vidmode != null)
            GLFW.glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        //*********************************Initialize openGL context************************************
        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(window);

        //Allow rendering as fast as inputs are received
        GLFW.glfwSwapInterval(0);


        //********************************Change the icon***************************************
        try {
            IconLoader.setIcons("/res/icon/alfatlogo2.png", window,512);
        }catch(Exception e){
            e.printStackTrace();
        }
        // Make the window visible
        GLFW.glfwShowWindow(window);
        //Poll events to make the taskbar icon update
        GLFW.glfwPollEvents();

        //**********************************Initialize input manager**********************************

        //InputManager.init(window);

        //**********************************Initialize Rendering system****************************
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();


        //************************************Initialize guis************************************
        //Create the FontTypes in GeneralSettings, must happen before using any font
        GeneralSettings.initializeFonts();

        //Initialize the render engine for the main window and popup windows
        MasterRenderer.init();


        //************************************Initialize input*************************************
        applicationController = new ApplicationController();
        GLFWEventController.init(window, applicationController);


        // Set the clear color
        //GL11.glClearColor(GeneralSettings.base02.x, GeneralSettings.base02.y, GeneralSettings.base02.z, 1);
        GL11.glClearColor(GeneralSettings.USERPREF.getBackgroundColor3f().x, GeneralSettings.USERPREF.getBackgroundColor3f().y, GeneralSettings.USERPREF.getBackgroundColor3f().z, 1);


        //************************************Initialize the aspect ratio********************************
        GeneralSettings.updateAspectRatio(GeneralSettings.DEFAULT_WIDTH, GeneralSettings.DEFAULT_HEIGHT);

        //************************************Open file**************************************************

        if(args.length == 1) {
            applicationController.getHeader().openFile(args[0]);
        }

        //****************************************Perform temporary initializations****************************
        tempInit();
    }

    private void tempInit(){
        //********************************************Initialize guis************************************************************
        //Create list to store all gui elements on screen
        guis = new ArrayList<>();
        //guis.add(new GuiTexture(engine.getRenderer().getReflectionTexture(), new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f)));

        //guiWindow = new GUIWindow(200, 100);
//        guiWindow.title("Alfat popup");
//
//        PopupWindow popupWindow = new PopupWindow("Popup test", "This is a popup test");
    }

    /**
     * Initializes the rendering loop and begins looping
     */
    private void loop() {

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        do{
            //GLFW.glfwRequestWindowAttention(window);

            // Poll for window events. The event callbacks will be called when an event is received
            long startTime = System.currentTimeMillis();
            GLFW.glfwWaitEvents();
            //GLFW.glfwWaitEventsTimeout(0.5);
//            if(System.currentTimeMillis()-startTime < 500){
////                System.out.println("Event received");
//            }else{
////                System.out.println("Timed out");
//            }
            long eventTime = System.currentTimeMillis();

            //Render main window
            GLFW.glfwMakeContextCurrent(window);
            MasterRenderer.renderScene(guis, applicationController);

            //Render any open popups
            GUIWindowController.render();

            long renderTime = System.currentTimeMillis();

//            Print per frame timing info
            if((eventTime-startTime) > 0){
                System.out.println("Time to process events: " + (eventTime-startTime));
            }
//            if((renderTime-eventTime) > 0){
                System.out.println("Time to render: " + (renderTime - eventTime));
//            }
//            System.out.println("Ratio: " + ((eventTime-startTime)/(renderTime-startTime)*100) + ":" + ((renderTime-eventTime)/(renderTime/startTime)*100));

            // Memory usage:
//            Runtime runtime = Runtime.getRuntime();
//            runtime.gc();
//            System.out.println("Used memory is: " + (runtime.totalMemory()-runtime.freeMemory())/1024);

        }while( !GLFW.glfwWindowShouldClose(window));
    }


    /**
     * Save content from code editor to temp file.
     * */
    private void saveIfCrash() {
        if(applicationController.getCodeWindowController() != null) {
            TempFileManager tfm = new TempFileManager(GeneralSettings.TEMP_DIR);
            //TODO: Change to use the user preferences temp folder over TEMP DIR. Maybe make UserPref from header public or make a getter?
            tfm.saveCodeEditorTextToFile(applicationController.getCodeWindowController().getTexts(),GeneralSettings.FILE_PATH, GeneralSettings.TEMP_DIR);
        }
    }


    /**Used for a graceful crash of the program
     *  - TODO: Saves changes to files
     *  - Frees up memory
     *  - Exits with an error code
     */
    public void crash(){
        exit();
        saveIfCrash();
        System.exit(-1);
    }

    /**
     * Handles cleaning up all information used for the program
     *  - Destroys the window
     *  - Cleans up VAO's/VBO's
     *  - Destroys the render engine
     */
    private void exit(){
        // Free the window callbacks and destroy the window
        Callbacks.glfwFreeCallbacks(window);
        GLFW.glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        GLFW.glfwTerminate();
        try {
            GLFW.glfwSetErrorCallback(null).free();
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        //Clean up VAO's, VBO's, and textures from loader
        Loader.cleanUp();

        //Close the render engine
        MasterRenderer.cleanUp();
    }

    /**
     * Runs the program, if the program completes running without any errors then the exit code 0 will be used
     */
    public static void main(String[] args) {
        new EngineTester().run(args);
        System.exit(0);
    }

    public static long getWindow(){
        return window;
    }

}