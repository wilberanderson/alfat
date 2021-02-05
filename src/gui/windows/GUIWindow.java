package gui.windows;


import main.GeneralSettings;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class GUIWindow {

    long window;

    private String title = "";
    private WindowDecorator windowDecorator = null;
    private boolean open = true;

    public GUIWindow(int width, int height){
        //********************************Create the window************************************
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
//        if ( !GLFW.glfwInit() )
//            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        GLFW.glfwDefaultWindowHints(); // optional, the current window hints are already the default
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE); // the window will stay hidden after creation
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE); // the window will be resizable
//        GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, decorated ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
        //        GLFW.glfwWindowHint(GLFW.)

        // Create the window
        window = GLFW.glfwCreateWindow(200, 100, "ALFAT", MemoryUtil.NULL, MemoryUtil.NULL);
        GLFW.glfwSetWindowTitle(window, "");

        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(window);

        //Allow rendering as fast as inputs are received
        GLFW.glfwSwapInterval(0);

        GLFW.glfwShowWindow(window);
    }

    public void title(String title){
        GLFW.glfwSetWindowTitle(window, title);
    }

    public void render(){

        if(open) {
            if(GLFW.glfwWindowShouldClose(window)){
                GLFW.glfwDestroyWindow(window);
                open = false;
                return;
            }

            GLFW.glfwMakeContextCurrent(window);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            GLFW.glfwSwapBuffers(window);
        }
    }
}
