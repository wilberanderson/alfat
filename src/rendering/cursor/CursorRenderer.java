package rendering.cursor;

import controllers.codeWindow.CursorController;
import dataStructures.RawModel;
import loaders.Loader;
import main.GeneralSettings;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix2f;
import rendering.filledBox.FilledBoxShader;

/**
 * Controls rendering the {@link gui.Cursor cursor} which appears in the {@link gui.textBoxes.CodeWindow
 * code window}. As far as rendering is concerned a cursor consists of a position where the top of the
 * cursor should be rendered and a boolean which indicates whether or not the cursor is currently being
 * rendered. A cursor should not be rendered if the code window is not currently selected or if it is
 * currently hidden in the flash effect which increases visibility.
 */
public class CursorRenderer {

    //The vertices used by the cursor, the cursor is a scaled line which is scaled based on the font scaling factor which goes from the top of the screen to the bottom
    private static final float[] VERTICES = {
            0, 0,
            0, -1
    };
    //The model used for rendering which uses the vertices
    private RawModel cursor;
    //The shader program which is used to render the cursor
    private CursorShader shader;

    /**
     * Initializes for {@link gui.Cursor} rendering. Creates the shader program, loads the model to the GPU, and loads the color to the shader program.
     * TODO: If text color can be changed the cursor color needs to change too
     */
    public CursorRenderer() {
        shader = new CursorShader();
        cursor = Loader.loadToVAO(VERTICES, 2);
        shader.start();
        shader.color.loadVec3(GeneralSettings.CURSOR_COLOR);
        shader.stop();
    }

    /**
     * Renders the {@link gui.Cursor cursor} which is contained within the CursorController.
     * It will be visible if the {@link gui.textBoxes.CodeWindow code window} is selected,
     * the code window is visible, and the cursor is currently in the visible part of the
     * flashing animation.
     * @param cursorController the {@link CursorController cursor controller} which contains
     *                         the cursor to be rendered.
     */
    public void render(CursorController cursorController) {
        //Sets the OpenGL state to be appropriate for rendering the cursor
        prepare();

        //Bind the cursors model and enable the position to be used
        GL30.glBindVertexArray(this.cursor.getVaoID());
        GL20.glEnableVertexAttribArray(0);

        //Load the cursors aspect ratio to the shader
        //TODO: Use a general aspect ratio matrix
        Matrix2f aspectRatio = new Matrix2f();
        aspectRatio.setIdentity();
        aspectRatio.m00 = cursorController.getAspectRatio().x;
        aspectRatio.m11 = cursorController.getAspectRatio().y;
        shader.aspectRatio.loadMatrix(aspectRatio);

        //Load the cursors position and font height
        shader.mousePosition.loadVec2(cursorController.getCursor().getPosition());
        shader.fontHeight.loadFloat(GeneralSettings.FONT_HEIGHT);

        //Load the position and size of the code window for clipping operations
        shader.isVisible.loadBoolean(cursorController.isVisible());

        //Render the cursor
        GL11.glDrawArrays(GL11.GL_LINES, 0, 2);

        //Unbind the cursors model
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);

        //Restore the OpenGL state which was modified
        endRendering();
    }

    /**
     * Cleans up the memory of the {@link CursorShader cursor shader}. The cursor shader has
     * a GPU program used to render saved inside of GRAM. This will not be automatically cleaned up by
     * the Java garbage collector so it must be deleted manually when this renderer will no longer be used.
     * Called automatically when the program is closed.
     */
    public void cleanUp() {
        shader.cleanUp();
    }

    /**
     * Sets the OpenGL state to be appropriate for rendering the cursor.
     */
    private void prepare() {
        //Disable the depth test
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        //Set the shader to be the shader in active use so it does not try to use the previous shader to render
        shader.start();
    }

    /**
     * Restores the OpenGL state for another renderer to be called.
     */
    private void endRendering() {
        //Stop the shader program
        shader.stop();

        //Enable the depth test
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

}