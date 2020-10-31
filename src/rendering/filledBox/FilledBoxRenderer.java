package rendering.filledBox;

import java.util.List;

import dataStructures.RawModel;
import gui.*;
import loaders.Loader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Matrix3f;
import utils.InputManager;

public class FilledBoxRenderer {

    private FilledBoxShader shader;
    private static final float[] VERTICES = {
            0, 0,
            0, 2,
            2, 0,
            2, 2
    };
    private RawModel square;

    public FilledBoxRenderer() {
        shader = new FilledBoxShader();
        square = Loader.loadToVAO(VERTICES, 2);
    }


    public void render(List<TextBox> textBoxes, Header header, FlowChartWindow flowChartWindow){
        prepare();

        GL30.glBindVertexArray(square.getVaoID());
        GL20.glEnableVertexAttribArray(0);


        for(int i = 0; i < textBoxes.size(); i++) {
            if(i == 0){
                renderFilledBox(textBoxes.get(0).getGuiFilledBox(), null);
            }else{
                renderFilledBox(textBoxes.get(i).getGuiFilledBox(), flowChartWindow);
            }
        }
        shader.color.loadVec3(header.getGuiFilledBox().getColor());
        Matrix3f transformationMatrix = new Matrix3f();
        transformationMatrix.m00 = header.getGuiFilledBox().getSize().x/2;
        transformationMatrix.m11 = header.getGuiFilledBox().getSize().y/2;
        transformationMatrix.m20 = header.getGuiFilledBox().getPosition().x+1;
        transformationMatrix.m21 = header.getGuiFilledBox().getPosition().y+1;
        shader.transformation.loadMatrix(transformationMatrix);
        shader.windowPosition.loadVec2(-1, -1);
        shader.windowSize.loadVec2(2, 2);
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);

        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);

        endRendering();


    }

    public void renderGuis(){
        prepare();

        GL30.glBindVertexArray(square.getVaoID());
        GL20.glEnableVertexAttribArray(0);

        for(Button button : InputManager.buttons){
            if(button instanceof TextButton){
                shader.color.loadVec3(((TextButton)button).getGuiFilledBox().getColor());
                Matrix3f transformationMatrix = new Matrix3f();
                transformationMatrix.m00 = ((TextButton)button).getGuiFilledBox().getSize().x/2;
                transformationMatrix.m11 = ((TextButton)button).getGuiFilledBox().getSize().y/2;
                transformationMatrix.m20 = ((TextButton)button).getGuiFilledBox().getPosition().x+1;
                transformationMatrix.m21 = ((TextButton)button).getGuiFilledBox().getPosition().y+1;
                shader.transformation.loadMatrix(transformationMatrix);
                shader.windowPosition.loadVec2(-1, -1);
                shader.windowSize.loadVec2(2, 2);
                GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
            }
        }



        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);

        endRendering();


    }

    private void renderFilledBox(GUIFilledBox filledBox, FlowChartWindow window){
        shader.color.loadVec3(filledBox.getColor());
        Matrix3f transformationMatrix = new Matrix3f();
        transformationMatrix.m00 = filledBox.getSize().x/2;
        transformationMatrix.m11 = filledBox.getSize().y/2;
        transformationMatrix.m20 = filledBox.getPosition().x;
        transformationMatrix.m21 = filledBox.getPosition().y;
        shader.transformation.loadMatrix(transformationMatrix);
        if(window != null){
            shader.windowPosition.loadVec2(window.getPosition());
            shader.windowSize.loadVec2(window.getSize());
        }else {
            shader.windowPosition.loadVec2(-1, -1);
            shader.windowSize.loadVec2(2, 2);
        }
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
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