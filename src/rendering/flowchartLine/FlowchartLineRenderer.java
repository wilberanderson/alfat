package rendering.flowchartLine;

import controllers.flowchartWindow.FlowchartWindowController;
import dataStructures.RawModel;
import controllers.flowchartWindow.FlowchartWindow;
import gui.FlowchartLine;
import loaders.Loader;
import main.GeneralSettings;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

public class FlowchartLineRenderer {

    private FlowchartLineShader shader;
    private static final float[] VERTICES = {
            0, 0,
            0, -1
    };
    private RawModel lineSegment;

    public FlowchartLineRenderer() {
        shader = new FlowchartLineShader();
        lineSegment = Loader.loadToVAO(VERTICES, 2);
    }


    public void render(List<FlowchartLine> flowchartLines, FlowchartWindowController flowchartWindowController, boolean doClipping, boolean isScreenshot){
        prepare();
        if(isScreenshot){
            shader.zoomTranslateMatrix.loadMatrix(GeneralSettings.SCREENSHOT_TRANSLATION);
        }else{
            shader.zoomTranslateMatrix.loadMatrix(flowchartWindowController.getZoomTranslateMatrix());
        }
        if(!isScreenshot) {
            shader.aspectRatio.loadMatrix(flowchartWindowController.getAspectRatio());
        }else{
            Matrix2f matrix = new Matrix2f();
            matrix.setIdentity();
            shader.aspectRatio.loadMatrix(matrix);
        }
        shader.windowPosition.loadVec2(-1, -1);
        shader.windowSize.loadVec2(2, 2);
        for(FlowchartLine line : flowchartLines) {
            shader.color.loadVec3(line.getColor());
            GL30.glBindVertexArray(lineSegment.getVaoID());
            GL20.glEnableVertexAttribArray(0);
            Vector2f position = line.getPositions().get(0);
            for(int i = 1; i < line.getPositions().size(); i++) {
                shader.startPosition.loadVec2(position);
                position = line.getPositions().get(i);
                shader.endPosition.loadVec2(position);
                if(doClipping) {
                    shader.windowPosition.loadVec2(flowchartWindowController.getPosition());
                    shader.windowSize.loadVec2(flowchartWindowController.getSize());
                }
                GL11.glDrawArrays(GL11.GL_LINES, 0, 2);
            }
            GL20.glDisableVertexAttribArray(0);
            GL30.glBindVertexArray(0);
        }
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