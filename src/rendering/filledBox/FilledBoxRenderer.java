package rendering.filledBox;

import java.util.List;

import controllers.ApplicationController;
import dataStructures.RawModel;
import gui.*;
import gui.buttons.Button;
import gui.buttons.TextButton;
import gui.textBoxes.FlowChartTextBox;
import gui.textBoxes.TextBox;
import loaders.Loader;
import main.GeneralSettings;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Matrix3f;

public class FilledBoxRenderer {

    private FilledBoxShader shader;
    private static final float[] VERTICES = {
            0, 0,
            0, 2,
            2, 0,
            2, 2
    };
    private RawModel square;
    private Matrix3f zoomTranslateMatrix = new Matrix3f();
    private Matrix2f aspectRatio = new Matrix2f();


    public FilledBoxRenderer() {
        shader = new FilledBoxShader();
        square = Loader.loadToVAO(VERTICES, 2);
        zoomTranslateMatrix.setIdentity();
        aspectRatio.setIdentity();
    }


    public void render(List<FlowChartTextBox> textBoxes, FlowChartWindow flowChartWindow, Header header){
        prepare();

        GL30.glBindVertexArray(square.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        shader.doClipping.loadBoolean(true);

        if(header.getCodeWindow() != null) {
            shader.windowPosition.loadVec2(header.getCodeWindow().getCodeWindow().getPosition().x - 1, header.getCodeWindow().getCodeWindow().getPosition().y - 1);
            shader.aspectRatio.loadMatrix(aspectRatio);
            shader.windowSize.loadVec2(header.getCodeWindow().getCodeWindow().getSize());
            shader.zoomTranslateMatrix.loadMatrix(zoomTranslateMatrix);
            renderFilledBox(header.getCodeWindow().getCodeWindow().getTextNumberFilledBox());
            shader.windowPosition.loadVec2(header.getCodeWindow().getCodeWindow().getCodeWindowPosition().x - 1, header.getCodeWindow().getCodeWindow().getCodeWindowPosition().y - 1);
            shader.windowSize.loadVec2(header.getCodeWindow().getCodeWindow().getCodeWindowSize());
            renderFilledBox(header.getCodeWindow().getCodeWindow().getGuiFilledBox());
        }
        shader.zoomTranslateMatrix.loadMatrix(flowChartWindow.getZoomTranslateMatrix());
        for(TextBox textBox : textBoxes){
            if(textBox instanceof FlowChartTextBox){
                shader.windowPosition.loadVec2(flowChartWindow.getPosition());
                shader.windowSize.loadVec2(flowChartWindow.getSize());
                shader.aspectRatio.loadMatrix(GeneralSettings.ASPECT_RATIO);
                shader.color.loadVec3(textBox.getBackgroundColor());
            }else{
                System.out.println("Undefined box rendering behavior");
            }
            renderFilledBox(textBox.getGuiFilledBox());
            renderFilledBox(textBox.getTextNumberFilledBox());
        }

        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);

        endRendering();
    }

    public void renderScreenshot(List<FlowChartTextBox> textBoxes, FlowChartWindow flowChartWindow){
        prepare();

        GL30.glBindVertexArray(square.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        shader.zoomTranslateMatrix.loadMatrix(GeneralSettings.SCREENSHOT_TRANSLATION);
        Matrix2f aspectRatio = new Matrix2f();
        aspectRatio.setIdentity();
        shader.aspectRatio.loadMatrix(aspectRatio);
        shader.doClipping.loadBoolean(false);
        for(TextBox textBox : textBoxes){
            renderFilledBox(textBox.getGuiFilledBox());
            renderFilledBox(textBox.getTextNumberFilledBox());
        }

        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);

        endRendering();
    }

    public void renderGuis(Header header){
        prepare();

        GL30.glBindVertexArray(square.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        shader.aspectRatio.loadMatrix(aspectRatio);

        shader.zoomTranslateMatrix.loadMatrix(zoomTranslateMatrix);
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


        for(Button button : ApplicationController.buttons){
            if(button instanceof TextButton){
                shader.color.loadVec3(((TextButton)button).getGuiFilledBox().getColor());
                transformationMatrix = new Matrix3f();
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

    private void renderFilledBox(GUIFilledBox filledBox){
        shader.color.loadVec3(filledBox.getColor());
        Matrix3f transformationMatrix = new Matrix3f();
        transformationMatrix.m00 = filledBox.getSize().x/2;
        transformationMatrix.m11 = filledBox.getSize().y/2;
        transformationMatrix.m20 = filledBox.getPosition().x;
        transformationMatrix.m21 = filledBox.getPosition().y;
        shader.transformation.loadMatrix(transformationMatrix);
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