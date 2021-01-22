package controllers;

import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;

public class GLFWEventController {

    private static GLFWKeyCallback keyCallback;
    private static GLFWScrollCallback scrollCallback;
    private static GLFWMouseButtonCallback mouseButtonCallback;
    private static GLFWCursorPosCallback cursorPosCallback;
    private static GLFWWindowFocusCallback windowFocusCallback;
    private static GLFWCharCallback charCallback;
    private static GLFWFramebufferSizeCallback framebufferSizeCallback;

    private static ApplicationController controller;

    /**
     * Creates callbacks for each window event which is used
     * @param window the GLFW window which can create events
     * @param controller the ApplicationController which will process the events
     */
    public static void init(long window, ApplicationController controller){
        GLFWEventController.controller = controller;
        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                GLFWEventController.controller.pressKey(key, action);
            }
        });

        glfwSetScrollCallback(window, scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                GLFWEventController.controller.scroll(yoffset);
            }
        });

        glfwSetMouseButtonCallback(window, mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                GLFWEventController.controller.click(button, action);
            }
        });

        glfwSetCursorPosCallback(window, cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                GLFWEventController.controller.moveMouse(xpos, ypos);
            }
        });

        glfwSetCharCallback(window, charCallback = new GLFWCharCallback() {
            @Override
            public void invoke(long window, int codepoint) {
                GLFWEventController.controller.type(codepoint);
            }
        });

        glfwSetFramebufferSizeCallback(window, framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                if(width > 0 && height > 0) {
                    GLFWEventController.controller.setFrameBufferSize(width, height);
                }
            }
        });
    }



}
