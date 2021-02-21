package rendering.renderEngine;

import controllers.ApplicationController;
import gui.GuiTexture;
import gui.guiElements.GUIElement;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import rendering.cursor.CursorRenderer;
import rendering.filledBox.FilledBoxRenderer;
//import rendering.guis.GuiRenderer;
import rendering.text.FontRenderer;

import java.util.List;

/**
 * The MasterRenderer controls rendering everything contained within the application. Anything that appears on the screen is put there by one of the Renderer's which the MasterRenderer controls. This contains a Renderer for each category of opject that may appear on the screen. Currently support objects to be rendered are graphical images, filled in rectangles, a cursor to display in the text box, a line which connects boxes in the flowchart, text for the gui, and lines of formatted text. The MasterRenderer also control general OpenGL state to ensure that all rendering is performed properly.
 */
public class GUIElementRenderer {

    //private static GuiRenderer guiRenderer;
    private FilledBoxRenderer filledBoxRenderer;
    private FontRenderer fontRenderer;
    private CursorRenderer cursorRenderer;

    /**
     * Initializes the MasterRenderer to be able to perform all tasks that it needs to. Initializes the various renderers that are needed to be used.
     * TODO: Remove the TextMaster
     */
    public GUIElementRenderer() {
        filledBoxRenderer = new FilledBoxRenderer();
        fontRenderer = new FontRenderer();
        cursorRenderer = new CursorRenderer();
    }

    /**
     * Renders all objects which are to be rendered to the screen.
     * @param guis the list of {@link GuiTexture} which are to be rendered to the screen. These are images which will be rendered to the screen directly.
     * @param controller the {@link ApplicationController} which controls all operations in the application. Each controller contains the models which will be rendered.
     */
    public void renderGUIElements(List<GUIElement> elementList, long window) {
        //Set the rendering context
        GLFW.glfwMakeContextCurrent(window);

        //Clear the framebuffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        //Render the filled boxes.
        filledBoxRenderer.renderGUIElements(elementList);

        //Render the texts
        fontRenderer.renderGUIElements(elementList);

        //Render any cursors
        cursorRenderer.renderGUIElements(elementList);

        //Swap the color buffers to update the screen
        GLFW.glfwSwapBuffers(window);
    }

    /**
     * Clean up the renderers when the game is closed. OpenGL does not have a garbage collector so various things need to be cleaned up as the game closes. Each renderer has a shader program saved in the GPU that needs to be deleted when the application is closed.
     */
    public void cleanUp(){
        fontRenderer.cleanUp();
        filledBoxRenderer.cleanUp();
        cursorRenderer.cleanUp();
    }
}
