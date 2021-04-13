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
import org.lwjgl.opengl.GL31;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 */
public class TerminatorRenderer {

    private TerminatorShader shader;

    /**
     *
     */
    public TerminatorRenderer() {
        shader = new TerminatorShader();
    }

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

            renderTerminators(flowChartWindowController.numberOfArrowHeads, flowChartWindowController.numberOfJunctions);

//            for (FlowchartLine line : flowChartWindowController.getFlowchartLineList()) {
////                shader.color.loadVec3(line.getColor());
//                GL30.glBindVertexArray(line.getTerminator().getModel().getVaoID());
//                GL20.glEnableVertexAttribArray(0);
//                if (line.getTerminator() instanceof ArrowHead) {
////                    shader.transformation.loadVec2(line.getTerminator().getPosition());
//                    GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
//                } else if (line.getTerminator() instanceof Junction) {
////                    shader.transformation.loadVec2(line.getTerminator().getPosition());
//                    GL11.glDrawArrays(GL11.GL_POINTS, 0, 1);
//                }
//                GL20.glDisableVertexAttribArray(0);
//                GL30.glBindVertexArray(0);
//            }
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

        renderTerminators(flowChartWindowController.numberOfArrowHeads, flowChartWindowController.numberOfJunctions);

//        for (FlowchartLine line : flowChartWindowController.getFlowchartLineList()) {
//            if (line.getTerminator().isHighlighted()){
////              enbiggen terminator (should be highlighted)
//            } else {
//                // normal terminator size
//                GL11.glPointSize(pointSize);
//            }
//            renderTerminator(line);
//        }
        GL11.glPointSize(1);
        GL11.glDisable(GL11.GL_POINT_SMOOTH);
        endRendering();
    }

    private void renderTerminators(int numberOfArrowHeads, int numberOfJunctions){
        GL30.glBindVertexArray(ArrowHead.getModel().getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        GL31.glDrawArraysInstanced(GL11.GL_TRIANGLES, 0, 3, numberOfArrowHeads);


        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(Junction.getModel().getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        GL31.glDrawArraysInstanced(GL11.GL_POINTS, 0, 1, numberOfJunctions);

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

//    private void renderTerminator(FlowchartLine line){
//        shader.color.loadVec3(line.getColor());
//        GL30.glBindVertexArray(line.getTerminator().getModel().getVaoID());
//        GL20.glEnableVertexAttribArray(0);
//        if (line.getTerminator() instanceof ArrowHead) {
//            shader.transformation.loadVec2(line.getTerminator().getPosition());
//            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
//        } else if (line.getTerminator() instanceof Junction) {
//            shader.transformation.loadVec2(line.getTerminator().getPosition());
//            GL11.glDrawArrays(GL11.GL_POINTS, 0, 1);
//        }
//        GL20.glDisableVertexAttribArray(0);
//        GL30.glBindVertexArray(0);
//    }

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
