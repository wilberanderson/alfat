package rendering.flowchartLine;

import controllers.flowchartWindow.FlowchartWindowController;
import dataStructures.RawModel;
import gui.FlowchartLine;
import loaders.Loader;
import main.GeneralSettings;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 */
public class FlowchartLineRenderer {

    private static final float[] VERTICES = {
            0, 0,
            0, -1
    };
    private FlowchartLineShader shader;
    private RawModel lineSegment;

    /**
     *
     */
    public FlowchartLineRenderer() {
        shader = new FlowchartLineShader();
        lineSegment = Loader.loadToVAO(VERTICES, 2);
    }

    /**
     * @param flowchartWindowController
     */
    public void renderToScreen(FlowchartWindowController flowchartWindowController) {
        if(flowchartWindowController != null) {
            prepare();

            shader.zoomTranslateMatrix.loadMatrix(flowchartWindowController.getZoomTranslateMatrix());
            shader.aspectRatio.loadMatrix(flowchartWindowController.getAspectRatio());


            shader.windowPosition.loadVec2(flowchartWindowController.getPosition());
            shader.windowSize.loadVec2(flowchartWindowController.getSize());
            shader.doClipping.loadBoolean(true);

            for (FlowchartLine line : flowchartWindowController.getFlowchartLineList()) {
                renderLine(line);
            }
            endRendering();
        }
    }

    /**
     * @param flowchartWindowController
     */
    public void renderToImage(FlowchartWindowController flowchartWindowController) {
        prepare();

        shader.zoomTranslateMatrix.loadMatrix(GeneralSettings.IMAGE_TRANSLATION);
        shader.aspectRatio.loadMatrix(GeneralSettings.IDENTITY2);

        shader.doClipping.loadBoolean(false);

        for (FlowchartLine line : flowchartWindowController.getFlowchartLineList()) {
            renderLine(line);
        }
        endRendering();
    }

    private void renderLine(FlowchartLine line){
        shader.color.loadVec3(line.getColor());
        GL30.glBindVertexArray(lineSegment.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        Vector2f position = line.getPositions().get(0);
        if (line.isHighlighted()){
            GL11.glLineWidth(GeneralSettings.USERPREF.getHighlightedLineWidth());
        } else {
            GL11.glLineWidth(GeneralSettings.USERPREF.getDefaultLineWidth());
        }
        for (int i = 1; i < line.getPositions().size(); i++) {
            shader.startPosition.loadVec2(position);
            position = line.getPositions().get(i);
            shader.endPosition.loadVec2(position);
            GL11.glDrawArrays(GL11.GL_LINES, 0, 2);
        }
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    /**
     *
     */
    public void cleanUp() {
        shader.cleanUp();
    }

    /**
     *
     */
    private void prepare() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        shader.start();
    }

    /**
     *
     */
    private void endRendering() {
        shader.stop();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glLineWidth(1f);
    }

}