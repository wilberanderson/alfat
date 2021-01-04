package main;
import controllers.ApplicationController;
import controllers.flowchartWindow.FlowchartWindow;
import gui.*;
import gui.textBoxes.TextBox;
import loaders.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
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
    /**
     * Used for all operations of the program
     *  - Initializes all relevant objects
     *  - Enters the main loop
     *  - If an exception occurs in the main loop the crash method is called
     */
    public void run() {

        init();
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
    private void init() {
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
            IconLoader.setIcons("/res/icon/icon.png", window,512);
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

        // Set the clear color
        GL11.glClearColor(GeneralSettings.base02.x, GeneralSettings.base02.y, GeneralSettings.base02.z, 1);


        //************************************Initialize guis************************************
        //Create the FontTypes in GeneralSettings, must happen before using any font
        GeneralSettings.initializeFonts();

        //Initializes the render engine
        MasterRenderer.init();
        //************************************Initialize input*************************************
        applicationController = new ApplicationController();
        GLFWEventController.init(window, applicationController);


        //************************************Initialize the aspect ratio********************************
        GeneralSettings.updateAspectRatio(GeneralSettings.DEFAULT_WIDTH, GeneralSettings.DEFAULT_HEIGHT);


        //****************************************Perform temporary initializations****************************
        tempInit();
    }

    private void tempInit(){
        //********************************************Initialize guis************************************************************
        //Create list to store all gui elements on screen
        guis = new ArrayList<>();
        //guis.add(new GuiTexture(engine.getRenderer().getReflectionTexture(), new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f)));

    }

    /**
     * Initializes the rendering loop and begins looping
     *  - TODO: Move finalized initialization steps to init() method
     */
    private void loop() {

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !GLFW.glfwWindowShouldClose(window) ) {

            // Poll for window events. The event callbacks will be called when an event is received
//            GLFW.glfwPollEvents();
            long startTime = System.currentTimeMillis();
            GLFW.glfwWaitEventsTimeout(0.5);
            if(System.currentTimeMillis()-startTime < 500){
//                System.out.println("Event received");
            }else{
//                System.out.println("Timed out");
            }
            //Render
            MasterRenderer.renderScene(guis, applicationController);

        }
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
        new EngineTester().run();
        System.exit(0);
    }

    public static long getWindow(){
        return window;
    }

}