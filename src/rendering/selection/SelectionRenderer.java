package rendering.selection;

import dataStructures.RawModel;
import gui.Selection;
import loaders.Loader;
import main.GeneralSettings;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix3f;

public class SelectionRenderer {

    private SelectionShader shader;
    private static final float[] VERTICES = {
            0, 0,
            0, 2,
            2, 0,
            2, 2
    };
    private RawModel square;

    public SelectionRenderer() {
        shader = new SelectionShader();
        square = Loader.loadToVAO(VERTICES, 2);
        shader.start();
        shader.color.loadVec3(GeneralSettings.HIGHLIGHT_COLOR);
        shader.stop();
    }


    public void render(Selection selection){
        prepare();

        GL30.glBindVertexArray(square.getVaoID());
        GL20.glEnableVertexAttribArray(0);

        if(selection.getNumberOfLines() == 1){
            Matrix3f transformationMatrix = new Matrix3f();
            //transformationMatrix.m00 = textBox.getSize().x/2;
            //transformationMatrix.m11 = textBox.getSize().y/2;
            //transformationMatrix.m20 = textBox.getPosition().x;
            //transformationMatrix.m21 = textBox.getPosition().y;

        }
        Matrix3f transformationMatrix = new Matrix3f();
        //transformationMatrix.m00 = textBox.getSize().x/2;
        //transformationMatrix.m11 = textBox.getSize().y/2;
        //transformationMatrix.m20 = textBox.getPosition().x;
        //transformationMatrix.m21 = textBox.getPosition().y;
        shader.transformation.loadMatrix(transformationMatrix);
        //shader.translation.loadVec2(new Vector2f(textBox.getPosition().x-1, textBox.getPosition().y-1));
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);

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