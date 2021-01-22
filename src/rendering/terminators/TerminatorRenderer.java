package rendering.terminators;

import controllers.flowchartWindow.FlowchartWindowController;
import dataStructures.RawModel;
import gui.FlowchartLine;
import gui.terminators.ArrowHead;
import gui.terminators.Junction;
import loaders.Loader;
import main.GeneralSettings;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Matrix3f;

/**
 *
 */
public class TerminatorRenderer {

    private static final float[] VERTICES = {
            0, 0,
            0, -1
    };
    private TerminatorShader shader;
    private RawModel lineSegment;

    /**
     *
     */
    public TerminatorRenderer() {
        shader = new TerminatorShader();
        lineSegment = Loader.loadToVAO(VERTICES, 2);
    }


    Matrix3f transformationMatrix = new Matrix3f();

    /**
     * @param flowChartWindowController
     */
    public void renderToScreen(FlowchartWindowController flowChartWindowController) {
        if(flowChartWindowController != null) {
            prepare();

            shader.zoomTranslateMatrix.loadMatrix(flowChartWindowController.getZoomTranslateMatrix());
            shader.aspectRatio.loadMatrix(flowChartWindowController.getAspectRatio());

            shader.windowPosition.loadVec2(flowChartWindowController.getPosition());
            shader.windowSize.loadVec2(flowChartWindowController.getSize());
            shader.doClipping.loadBoolean(true);

            float pointSize = 10f * flowChartWindowController.getZoomTranslateMatrix().m00;
            if (pointSize < 2) {
                pointSize = 2;
            }
            GL11.glPointSize(pointSize);
            GL11.glEnable(GL11.GL_POINT_SMOOTH);

            Matrix3f transformationMatrix = new Matrix3f();
            for (FlowchartLine line : flowChartWindowController.getFlowchartLineList()) {
                shader.color.loadVec3(line.getColor());
                GL30.glBindVertexArray(line.getTerminator().getModel().getVaoID());
                GL20.glEnableVertexAttribArray(0);
                if (line.getTerminator() instanceof ArrowHead) {
                    transformationMatrix.setIdentity();
                    if (((ArrowHead) line.getTerminator()).isUpwards()) {
                        transformationMatrix.m11 = 1;
                    } else {
                        transformationMatrix.m11 = -1;
                    }
                    transformationMatrix.m20 = line.getTerminator().getPosition().x;
                    transformationMatrix.m21 = line.getTerminator().getPosition().y;
                    shader.transformationMatrix.loadMatrix(transformationMatrix);
                    GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
                } else if (line.getTerminator() instanceof Junction) {
                    transformationMatrix.setIdentity();
                    transformationMatrix.m20 = line.getTerminator().getPosition().x;
                    transformationMatrix.m21 = line.getTerminator().getPosition().y;
                    shader.transformationMatrix.loadMatrix(transformationMatrix);
                    GL11.glDrawArrays(GL11.GL_POINTS, 0, 1);
                }
                GL20.glDisableVertexAttribArray(0);
                GL30.glBindVertexArray(0);
            }
            GL11.glPointSize(1);
            GL11.glDisable(GL11.GL_POINT_SMOOTH);
            endRendering();
        }
    }

    /**
     * @param flowChartWindowController
     */
    public void renderToImage(FlowchartWindowController flowChartWindowController) {
        prepare();

        shader.zoomTranslateMatrix.loadMatrix(GeneralSettings.IMAGE_TRANSLATION);
        shader.aspectRatio.loadMatrix(GeneralSettings.IDENTITY2);

        shader.doClipping.loadBoolean(false);

        float pointSize = 10f * flowChartWindowController.getZoomTranslateMatrix().m00;
        if (pointSize < 2) {
            pointSize = 2;
        }
        GL11.glPointSize(pointSize);
        GL11.glEnable(GL11.GL_POINT_SMOOTH);

        for (FlowchartLine line : flowChartWindowController.getFlowchartLineList()) {
            renderTerminator(line);
        }
        GL11.glPointSize(1);
        GL11.glDisable(GL11.GL_POINT_SMOOTH);
        endRendering();
    }

    private void renderTerminator(FlowchartLine line){
        shader.color.loadVec3(line.getColor());
        GL30.glBindVertexArray(line.getTerminator().getModel().getVaoID());
        GL20.glEnableVertexAttribArray(0);
        if (line.getTerminator() instanceof ArrowHead) {
            transformationMatrix.setIdentity();
            if (((ArrowHead) line.getTerminator()).isUpwards()) {
                transformationMatrix.m11 = 1;
            } else {
                transformationMatrix.m11 = -1;
            }
            transformationMatrix.m20 = line.getTerminator().getPosition().x;
            transformationMatrix.m21 = line.getTerminator().getPosition().y;
            shader.transformationMatrix.loadMatrix(transformationMatrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
        } else if (line.getTerminator() instanceof Junction) {
            transformationMatrix.setIdentity();
            transformationMatrix.m20 = line.getTerminator().getPosition().x;
            transformationMatrix.m21 = line.getTerminator().getPosition().y;
            shader.transformationMatrix.loadMatrix(transformationMatrix);
            GL11.glDrawArrays(GL11.GL_POINTS, 0, 1);
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
    }

}
