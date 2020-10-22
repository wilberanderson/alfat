package main;
import fontMeshCreator.FontType;
import gui.Cursor;
import gui.TextBox;
import loaders.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import parser.JsonReader;
import parser.LC3Syntax;
import rendering.renderEngine.RenderEngine;
import gui.GuiTexture;
import utils.InputManager;
import utils.MyFile;

import java.io.File;
import java.nio.*;
import java.util.ArrayList;
import java.util.List;

//Can you see this???
import com.fasterxml.jackson.databind.ObjectMapper;

public class EngineTester {

    // The window handle
    private long window;
    private RenderEngine engine;

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
        InputManager.init(window, null);
    }

    /**
     * Initializes the rendering loop and begins looping
     *  - TODO: Move finalized initialization steps to init() method
     */
    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        GL11.glClearColor(0.2f, 0.2f, 0.2f, 0.0f);

        //Initializes the render engine
        engine = new RenderEngine();

        //********************************************Initialize guis************************************************************
        //Create list to store all gui elements on screen
        List<GuiTexture> guis = new ArrayList<>();
		//guis.add(new GuiTexture(engine.getRenderer().getReflectionTexture(), new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f)));
        float fontSize = 1;

        //*********************************************Initialize text boxes*****************************************************
        //Create a font to use for rendering files
        FontType tacoma = new FontType(Loader.loadTexture(new MyFile("/res/fonts/arial/arial.png")), new MyFile("/res/fonts/arial/arial.fnt"));
        //Create list to store all text boxes
        List<TextBox> textBoxes = new ArrayList<>();
        //Create sample text boxes
        TextBox codeFile = new TextBox(new Vector2f(0f,0f), new Vector2f(1f, 2f), new Vector3f(0.1f,0.1f,0.1f), new Vector3f(1,1,1), new Vector3f(0,0,0), "    .ORIG    x3000\n" +
                ";Start\n" +
                "        LEA    R0, PROMPT1\n" +
                "        PUTS            ;Display beginning prompt\n" +
                "        LD    R2, WORD        \n" +
                "        LD    R3, GUESS    ;Load address's\n" +
                "        LD    R4, BLANK    ;Load \"_\"\n" +
                "        AND    R5, R5, #0    ;Initialized to 0    \n" +
                ";Word Input\n" +
                "INPUT        GETC\n" +
                "        LD    R1, BACKSP\n" +
                "        NOT    R1, R1\n" +
                "        ADD    R1, R1, #1\n" +
                "        ADD    R1, R0, R1\n" +
                "        BRz    INPUT        ;Goto [INPUT] backspace read skip it\n" +
                "        LD     R1, NEWLN\n" +
                "        NOT    R1, R1\n" +
                "        ADD    R1, R1, #1\n" +
                "        ADD     R1, R0, R1\n" +
                "        BRz     INPUT_DONE    ;Goto [INPUT_DONE] if a new line was entered\n" +
                "        STR    R0, R2, #0    ;Store char in word string\n" +
                "        STR    R4, R3, #0    ;Store \"_\" in guess string\n" +
                "        LD    R0, ASCII\n" +
                "        ADD    R0, R0, #-6    \n" +
                "        OUT            ;Echo \"*\"\n" +
                "        ADD    R2, R2, #1    ;Increment word\n" +
                "        ADD    R3, R3, #1    ;Increment guess\n" +
                "        LD    R0, SPACE\n" +
                "        STR    R0, R3, #0\n" +
                "        ADD    R3, R3, #1    ;Put a space in between each char GUI use\n" +
                "        ADD    R5, R5, #1    ;Increment word counter\n" +
                "        BRnzp    INPUT        ;Goto [INPUT] we're not done\n" +
                ";\n" +
                "; Registers:\n" +
                ";    R0 = Input/Output\n" +
                ";    R1 = Temp variable\n" +
                ";    R2 = Address of word\n" +
                ";    R3 = Address of guess\n" +
                ";    R4 = Length\n" +
                ";    R5 = Address of Used\n" +
                ";\n" +
                ";Prepare word guessing\n" +
                "INPUT_DONE    LEA    R0, PROMPT2    ;Display second prompt\n" +
                "        PUTS\n" +
                "        ST    R5, LENGTH\n" +
                "        LD    R5, USED    ;Load address of used string\n" +
                "        AND    R0, R0, #0\n" +
                "        STR    R0, R2, #0\n" +
                "        STR    R0, R3, #0\n" +
                "        STR    R0, R5, #0    ;Null terminate the strings\n" +
                "TOP        LD    R2, WORD    \n" +
                "        LD    R3, GUESS    ;Load starting words\n" +
                "        LD    R5, USED\n" +
                "        LEA    R0, PROMPT3\n" +
                "        PUTS\n" +
                "        LD    R0, USED\n" +
                "        PUTS\n" +
                "        LD    R0, NEWLN\n" +
                "        OUT\n" +
                "        LD    R0, GUESS\n" +
                "        PUTS\n" +
                "        LD    R0, NEWLN\n" +
                "        OUT\n" +
                ";Word guessing\n" +
                "        GETC\n" +
                "        LD    R4, LENGTH\n" +
                "        BRnzp    WORD1        ;Goto [WORD1] to get rid of pre changes.\n" +
                ";Is char in the word?\n" +
                "WORD0        ADD    R4, R4, #-1    ;Decrement counter\n" +
                "        ADD    R2, R2, #1    ;Increment word\n" +
                "        ADD    R3, R3, #2    ;Increment guess\n" +
                "WORD1        ADD    R4, R4, #0\n" +
                "        BRz    IN_USED        ;Goto [IN_USED] done going through word\n" +
                "        LDR    R1, R2, #0\n" +
                "        NOT    R1, R1\n" +
                "        ADD    R1, R1, #1\n" +
                "        ADD    R1, R0, R1    ;Subtract word char from input\n" +
                "        BRnp    WORD0        ;Goto [WORD0] if char does not match\n" +
                "        STR    R0, R3, #0\n" +
                ";Is char everywhere in word?\n" +
                "WORD2        ADD    R4, R4, #0\n" +
                "        BRz    CHECK        ;Goto [CHECK] Done with input char checking\n" +
                "        ADD    R4, R4, #-1    ;Decrement counter\n" +
                "        ADD    R2, R2, #1    ;Increment word\n" +
                "        ADD    R3, R3, #2    ;Increment guess\n" +
                "        LDR    R1, R2, #0\n" +
                "        NOT    R1, R1\n" +
                "        ADD    R1, R1, #1    ;Inverse\n" +
                "        ADD    R1, R0, R1    ;Subtract word char from\n" +
                "        Brnp    WORD2        ;Goto [WORD2] Not a match, continue\n" +
                "        STR    R0, R3, #0    ;Store the char matched into \"Guess\"\n" +
                "        BRnzp    WORD2        ;Goto [WORD2] Is a match, continue\n" +
                ";has char been in \"used\" already?\n" +
                "IN_USED        LDR    R1, R5, #0\n" +
                "        BRnp    IN_CHAR        ;Goto [CHAR] Not end, so check for char\n" +
                "        STR    R0, R5, #0    ;Store char into used\n" +
                "        ADD    R5, R5, #1    ;Increment used\n" +
                "        AND    R0, R0, #0\n" +
                "        STR    R0, R5, #0    ;Null terminate used\n" +
                "        BRnzp    CHECK        ;Goto [CHECK] char stored\n" +
                "IN_CHAR        NOT    R1, R1\n" +
                "        ADD    R1, R1, #1\n" +
                "        ADD    R1, R0, R1    ;Subtract Used value from char\n" +
                "        BRz    CHECK        ;Goto [CHECK] char was already there\n" +
                "        ADD    R5, R5, #1    ;Increment address of used\n" +
                "        BRnzp    IN_USED        ;Goto [IN_USED] no match found try next.\n" +
                ";Check to see if strings match\n" +
                "CHECK        LD    R2, WORD    \n" +
                "        LD    R3, GUESS\n" +
                "        LD    R4, LENGTH\n" +
                "CHECK_AGAIN    LDR    R0, R2, #0\n" +
                "        LDR    R1, R3, #0\n" +
                "        NOT    R1, R1\n" +
                "        ADD    R1, R1, #1    ;Inverse R1\n" +
                "        ADD    R1, R0, R1    ;subtract chars from word and guess\n" +
                "        BRnp    TOP        ;Goto [TOP] They don't match\n" +
                "        ADD    R4, R4, #0\n" +
                "        Brz    DONE        ;Goto [Done] All chars match!\n" +
                "        ADD    R4, R4, #-1    ;Decrement length\n" +
                "        ADD    R2, R2, #1    ;Increment word\n" +
                "        ADD    R3, R3, #2    ;Increment guess\n" +
                "        BRnzp    CHECK_AGAIN    ;Goto [CHECK_AGAIN] chars so far have been equal\n" +
                ";End\n" +
                "DONE        LEA    R0, PROMPT2\n" +
                "        PUTS\n" +
                "        LEA    R0, PROMPT3\n" +
                "        PUTS\n" +
                "        LD    R0, USED\n" +
                "        PUTS\n" +
                "        LD    R0, NEWLN\n" +
                "        OUT\n" +
                "        LD    R0, GUESS\n" +
                "        PUTS        \n" +
                "        LEA    R0, PROMPT4\n" +
                "        PUTS        \n" +
                "        HALT\n" +
                ";Data\n" +
                "ASCII    .FILL    x0030\n" +
                "SPACE    .FILL    x0020\n" +
                "BACKSP    .FILL    x0008\n" +
                "TAB    .FILL    x0009\n" +
                "NEWLN    .FILL    x000A\n" +
                "BLANK    .FILL    x005F\n" +
                "LENGTH    .FILL    x0000\n" +
                "PROMPT1    .STRINGZ    \"[Enter your word]\\n\"\n" +
                "PROMPT2    .STRINGZ    \"\\n[Guess the word!]\\n\"\n" +
                "PROMPT3    .STRINGZ    \"[Letters used] > \"\n" +
                "PROMPT4    .STRINGZ    \"\\n[You got it right!]\\n\"\n" +
                "WORD    .FILL    x3200\n" +
                "GUESS    .FILL    x3240\n" +
                "USED    .FILL    x3260\n" +
                ";\n" +
                "    .END", tacoma, fontSize, 0.25f, 0.5f, GeneralSettings.TEXT_BOX_BORDER_WIDTH);
        TextBox flowChart1 = new TextBox(new Vector2f(1.15f,1.5f), new Vector3f(0.1f,0.1f,0.1f), new Vector3f(1,1,1), new Vector3f(0,0,0), "Sample automatically sized textbox\nThis text box automatically sizes itself to match it's input", tacoma, fontSize, 0.25f, 0.5f, GeneralSettings.TEXT_BOX_BORDER_WIDTH);
        //        MousePicker picker = new MousePicker(scene.getCamera(), scene.getTerrains());
        textBoxes.add(codeFile);
        textBoxes.add(flowChart1);

        //Initialize cursor to be null
        Cursor cursor = null;


        //Testing stuff

        System.out.println("hello world ");

//        ObjectMapper objectMapper = new ObjectMapper();
//        File file0 = new File("CodeSyntax\\LC3-Operators.json");
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

//        JsonReader j = new JsonReader();
//        LC3Syntax s = j.mapJson(new File("CodeSyntax\\LC3-Operators.json"));
//        System.out.println(s);








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
                Vector2f newPosition = new Vector2f((float)InputManager.MOUSE_X/GeneralSettings.DISPLAY_WIDTH*2 - 1f, 1-(float)InputManager.MOUSE_Y/GeneralSettings.DISPLAY_HEIGHT*2);
                for (TextBox textBox : textBoxes){
                    if(newPosition.x > textBox.getPosition().x - 1 && newPosition.x < (textBox.getPosition().x + textBox.getSize().x)-1 && newPosition.y > textBox.getPosition().y - 1 && newPosition.y < (textBox.getPosition().y + textBox.getSize().y)-1) {
                        cursor = new Cursor(newPosition, textBox);
                        break;
                    }
                }
                //Reset left click value to avoid checking for new position multiple times per click
                InputManager.LEFT_CLICK = false;
            }

            //If there is a cursor process all of the events
            if(cursor != null) {
                cursor.processInputs(window);
            }

            //Render
            engine.renderScene(guis, textBoxes, new Vector3f(1,1,1), cursor, fontSize);

            //Temporarily make changes for scrolling
            codeFile.changeContentsVerticalPosition((float)InputManager.SCROLL_CHANGE/10);
            InputManager.SCROLL_CHANGE = 0;

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
        engine.close();
    }

    /**
     * Runs the program, if the program completes running without any errors then the exit code 0 will be used
     */
    public static void main(String[] args) {
        new EngineTester().run();
        System.exit(0);
    }

}