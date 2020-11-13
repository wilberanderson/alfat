package rendering.cursor;

import dataStructures.RawModel;
import gui.Cursor;
import loaders.Loader;
import main.GeneralSettings;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix2f;

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
        shader.color.loadVec3(GeneralSettings.CURSOR_COLOR);
        shader.stop();
    }


    public void render(Cursor cursor){
        prepare();

        GL30.glBindVertexArray(this.cursor.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        Matrix2f aspectRatio = new Matrix2f();
        aspectRatio.setIdentity();
        aspectRatio.m00 = cursor.getCodeWindow().getAspectRatio().x;
        aspectRatio.m11 = cursor.getCodeWindow().getAspectRatio().y;
        shader.aspectRatio.loadMatrix(aspectRatio);
        shader.mousePosition.loadVec2(cursor.getPosition());
        shader.fontHeight.loadFloat(GeneralSettings.FONT_SCALING_FACTOR*GeneralSettings.FONT_SIZE);
        shader.windowPosition.loadVec2(cursor.getCodeWindow().getPosition().x-1, cursor.getCodeWindow().getPosition().y-1);
        shader.windowSize.loadVec2(cursor.getCodeWindow().getSize());
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