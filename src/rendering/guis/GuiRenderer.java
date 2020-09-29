package rendering.guis;

import java.util.List;

import gui.GuiTexture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import dataStructures.RawModel;
import loaders.Loader;
import utils.Maths;

public class GuiRenderer {
	 
    private final RawModel quad;
    private GuiShader shader;

    /**
     * Sets up the quad to be used for the gui and also creates the {@link GuiShader} to be used
     */
    public GuiRenderer(){
        float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
        quad = Loader.loadToVAO(positions, 2);
        shader = new GuiShader();
    }

    /**
     * Renders all guis in a scene.
     * @param guis
     *            - the guis to be rendered.
     */
    public void render(List<GuiTexture> guis){
        prepare();
        for(GuiTexture gui: guis){
            prepareGui(gui);
            shader.highlighted.loadBoolean(gui.isHighlighted());
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        }
        finish();
    }

    /**
     * Binds the vertex array and sets attributes for rendering guis.
     */
    private void prepare(){
        shader.start();
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    /**
     * Prepares rendering for each {@link GuiTexture}
     * @param gui
     *            - the gui to be prepared to render.
     */
    private void prepareGui(GuiTexture gui){
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
        Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale());
        shader.transformationMatrix.loadMatrix(matrix);

    }

    /**
     * Resets attributes and stops the shader.
     */
    private void finish(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    /**
     * Deletes the shader.
     */
    public void cleanUp(){
        shader.cleanUp();
    }
}
