package main;
import fontMeshCreator.FontType;
import gui.*;
import loaders.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import parser.JsonReader;
import parser.LC3Syntax;
import rendering.renderEngine.MasterRenderer;
import utils.InputManager;
import utils.MyFile;

import java.io.File;
import java.nio.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

//Can you see this???


public class EngineTester {

    // The window handle
    private long window;
    private MasterRenderer renderer;

    //Temporary attributes
    private Cursor cursor;
    private List<TextBox> textBoxes;
    private List<GuiTexture> guis;
    private Header header;
    private List<FlowchartLine> flowchartLines;
    private FlowChartWindow flowChartWindow;
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
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE); // the window will not be resizable

        // Create the window
        window = GLFW.glfwCreateWindow(GeneralSettings.DISPLAY_WIDTH, GeneralSettings.DISPLAY_HEIGHT, "ALFAT", MemoryUtil.NULL, MemoryUtil.NULL);
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

        //Restrict fps to 60 fps
        GLFW.glfwSwapInterval(1);


        //********************************Change the icon***************************************
        try {
            IconLoader.setIcons("/res/icon/", window);
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
        GL11.glClearColor(0.2f, 0.2f, 0.2f, 0.0f);

        //Initializes the render engine
        renderer = new MasterRenderer();

        //************************************Initialize guis************************************
        //Create the FontTypes in GeneralSettings, must happen before using any font
        GeneralSettings.initializeFonts();

        //************************************Initialize input*************************************
        InputManager.init(window);




        //****************************************Perform temporary initializations****************************
        tempInit();
    }

    private void tempInit(){


        //********************************************Initialize guis************************************************************
        //Create list to store all gui elements on screen
        guis = new ArrayList<>();
        //guis.add(new GuiTexture(engine.getRenderer().getReflectionTexture(), new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f)));

        //*********************************************Initialize text boxes*****************************************************
        //Create a font to use for rendering files


        header = new Header(new Vector2f(-1, 1-(GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR + GeneralSettings.TEXT_BUTTON_PADDING*2)), new Vector2f(2f, GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR + GeneralSettings.TEXT_BUTTON_PADDING*2));


        //Create list to store all text boxes
        textBoxes = new ArrayList<>();
        //Create sample text boxes
        //codeWindow = new CodeWindow(new Vector2f(0f,0f), new Vector2f(1f, header.getPosition().y+1), new Vector3f(0.1f,0.1f,0.1f), new Vector3f(1,1,1), new Vector3f(0,0,0), "", GeneralSettings.TACOMA, GeneralSettings.FONT_SIZE, GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, GeneralSettings.TEXT_BOX_BORDER_WIDTH);
        FlowChartTextBox flowChartTextBox = new FlowChartTextBox(new Vector2f(0.5f,1.5f), new Vector3f(1f,0f,0f), new Vector3f(1,1,1), new Vector3f(0,0,0), "Sample automatically sized textbox\nThis text box automatically sizes itself to match it's input", GeneralSettings.TACOMA, GeneralSettings.FONT_SIZE, GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, GeneralSettings.TEXT_BOX_BORDER_WIDTH);
        //textBoxes.add(codeWindow);
        textBoxes.add(flowChartTextBox);

        //Testing stuff



//        ObjectMapper objectMapper = new ObjectMapper();
//        File file0 = new File("CodeSyntax\\LC3.json");
//        try {
//
//           // String jsonFile = objectMapper.writeValueAsString(file0);
//
//            LC3Syntax s = objectMapper.readValue(file0, LC3Syntax.class);
//            //System.out.println(jsonFile);
//            System.out.println(s);
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }

//        JsonReader j = new JsonReader(new File("CodeSyntax\\LC3.json"));
//        LC3Syntax s = j.mapJsonLC3Syntax();
//        System.out.println(s);
//        String []foo = s.getJumps();
//
//        for(int i = 0; i < foo.length; i++) {
//            System.out.println(foo[i]);
//        }
//





        flowchartLines = new ArrayList<>();
        List<Vector2f> positions1 = new ArrayList<>();
        List<Vector2f> positions2 = new ArrayList<>();
        List<Vector2f> positions3 = new ArrayList<>();
        Vector2f position = new Vector2f(0.5f, 0);
        positions1.add(position);
        position = new Vector2f(0.5f, 0.5f);
        positions1.add(position);
        position = new Vector2f(0.75f, 0.5f);
        positions1.add(position);

        position = new Vector2f(0.5f, -0.9f);
        positions2.add(position);
        position = new Vector2f(0.75f, -0.9f);
        positions2.add(position);
        position = new Vector2f(0.75f, 0);
        positions2.add(position);

        position = new Vector2f(-1, -1);
        positions3.add(position);
        position = new Vector2f(0.5f, 10);
        positions3.add(position);
        position = new Vector2f(1f, -1f);
        positions3.add(position);

        FlowchartLine flowchartLine = new FlowchartLine(positions1);
        flowchartLines.add(flowchartLine);
        flowchartLine = new FlowchartLine(positions2);
        flowchartLines.add(flowchartLine);
        flowchartLine = new FlowchartLine(positions3);
        flowchartLines.add(flowchartLine);

        flowChartWindow = new FlowChartWindow(null, null);

        header.setFlowChartWindow(flowChartWindow);
        //header.setCodeWindow(codeWindow);
    }

    /**
     * Initializes the rendering loop and begins looping
     *  - TODO: Move finalized initialization steps to init() method
     */
    private void loop() {

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !GLFW.glfwWindowShouldClose(window) ) {
            //Clear the framebuffer
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            //Update the frame time
            GeneralSettings.update();

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            GLFW.glfwPollEvents();
            InputManager.processEvents();

            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            //If the user is clicking and their mouse is in a text box recreate the cursor at the new position
            if(InputManager.LEFT_CLICK){
                Vector2f newPosition = new Vector2f((float)InputManager.MOUSE_X, (float)InputManager.MOUSE_Y);

                if(header.getCodeWindow() != null) {
                    if (newPosition.x > header.getCodeWindow().getPosition().x - 1 && newPosition.x < (header.getCodeWindow().getPosition().x + header.getCodeWindow().getSize().x) - 1 && newPosition.y > header.getCodeWindow().getPosition().y - 1 && newPosition.y < (header.getCodeWindow().getPosition().y + header.getCodeWindow().getSize().y) - 1) {
                        header.setCursor(new Cursor(newPosition, header.getCodeWindow()));
                    }
                }

                //Reset left click value to avoid checking for new position multiple times per click
                InputManager.LEFT_CLICK = false;
            }

            //If there is a cursor process all of the events
            if(header.getCursor() != null) {
                header.getCursor().processInputs(window);
            }

            //Render
            renderer.renderScene(guis, textBoxes, new Vector3f(1,1,1), header.getCursor(), GeneralSettings.FONT_SIZE, header, flowchartLines, flowChartWindow, header.getCodeWindow());

            //Temporarily make changes for scrolling
            if(InputManager.SCROLL_CHANGE != 0) {
                header.getCodeWindow().scroll((float) -InputManager.SCROLL_CHANGE / 10);
            }
            //Swap the color buffers to update the screen
            GLFW.glfwSwapBuffers(window);





        }
    }

    /**Used for a graceful crash of the program
     *  - TODO: Saves changes to files
     *  - Frees up memory
     *  - Exits with an error code
     */
    public void crash(){
        exit();
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
        renderer.cleanUp();
    }

    /**
     * Runs the program, if the program completes running without any errors then the exit code 0 will be used
     */
    public static void main(String[] args) {
        new EngineTester().run();
        System.exit(0);
    }

}