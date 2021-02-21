package gui;

import main.EngineTester;
import org.lwjgl.glfw.GLFW;

public class Mouse {
    private static long window = EngineTester.getWindow();
    private static long pointer = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
    private static long hand = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR);
    private static long iPointer = GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR);
    private static long horizontalArrow = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HRESIZE_CURSOR);
    private static long selected = pointer;

    /**
     * Sets the cursor to the default arrow pointer
     */
    public static void setPointer(){
        GLFW.glfwSetCursor(window, pointer);
    }

    /**
     * Sets the cursor to the hand which indicates that an element may be interacted with
     */
    public static void setHand(){
        GLFW.glfwSetCursor(window, hand);

    }

    /**
     * Sets the cursor to a horizontal resize bar
     */
    public static void setResize(){
        GLFW.glfwSetCursor(window, horizontalArrow);

    }

    /**
     * Sets the cursor to the default I-beam which appears when hovering over text
     */
    public static void setIBeam(){
        GLFW.glfwSetCursor(window, iPointer);

    }
}
