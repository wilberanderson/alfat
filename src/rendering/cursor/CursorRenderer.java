package rendering.cursor;

import dataStructures.RawModel;
import loaders.Loader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class CursorRenderer {

    private CursorShader shader;
    private static final float[] VERTICES = {
            0, 0,
            0, -1
    };
    private RawModel cursor;

    public CursorRenderer() {
        shader = new CursorShader();
        cursor = Loader.loadToVAO(VERTICES, 2);
        shader.start();
        shader.color.loadVec3(1,1,1);
    }


    public void render(Vector3f color, Vector2f mousePosition, float fontSize){
        prepare();

        GL30.glBindVertexArray(cursor.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        shader.color.loadVec3(color);
        shader.mousePosition.loadVec2(mousePosition);
        shader.fontHeight.loadFloat(0.05f*fontSize);
        GL11.glDrawArrays(GL11.GL_LINES, 0, 2);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);

        endRendering();


    }

    public void cleanUp(){
        shader.cleanUp();
    }

    private void prepare(){
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        shader.start();
    }


    private void endRendering(){
        shader.stop();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

}