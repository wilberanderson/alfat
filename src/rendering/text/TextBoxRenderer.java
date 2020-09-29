package rendering.text;

import java.util.List;
import java.util.Map;

import dataStructures.RawModel;
import gui.TextBox;
import loaders.Loader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class TextBoxRenderer {

    private TextBoxShader shader;
    private static final float[] VERTICES = {
            0, 0,
            0, 2,
            2, 0,
            2, 2
    };
    private RawModel square;

    public TextBoxRenderer() {
        shader = new TextBoxShader();
        square = Loader.loadToVAO(VERTICES, 2);
    }


    public void render(List<TextBox> textBoxes){
        prepare();

        GL30.glBindVertexArray(square.getVaoID());
        GL20.glEnableVertexAttribArray(0);

        for(TextBox textBox : textBoxes) {
            shader.color.loadVec3(textBox.getBackgroundColor());
            Matrix3f transformationMatrix = new Matrix3f();
            transformationMatrix.m00 = textBox.getSize().x;
            transformationMatrix.m11 = textBox.getSize().y;
            transformationMatrix.m20 = textBox.getPosition().x;
            transformationMatrix.m21 = textBox.getPosition().y;
            shader.transformation.loadMatrix(transformationMatrix);
            //shader.translation.loadVec2(new Vector2f(textBox.getPosition().x-1, textBox.getPosition().y-1));
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
        }
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