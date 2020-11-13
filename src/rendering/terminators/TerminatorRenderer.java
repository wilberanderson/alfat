package rendering.terminators;

import dataStructures.RawModel;
import gui.FlowChartWindow;
import gui.FlowchartLine;
import gui.terminators.ArrowHead;
import gui.terminators.Junction;
import gui.terminators.Terminator;
import loaders.Loader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

public class TerminatorRenderer {

    private TerminatorShader shader;
    private static final float[] VERTICES = {
            0, 0,
            0, -1
    };
    private RawModel lineSegment;

    public TerminatorRenderer() {
        shader = new TerminatorShader();
        lineSegment = Loader.loadToVAO(VERTICES, 2);
    }


    public void render(List<FlowchartLine> flowchartLines, FlowChartWindow flowChartWindow){
        prepare();
        shader.zoomTranslateMatrix.loadMatrix(flowChartWindow.getZoomTranslateMatrix());
        shader.aspectRatio.loadMatrix(flowChartWindow.getAspectRatio());
        shader.windowPosition.loadVec2(flowChartWindow.getPosition());
        shader.windowSize.loadVec2(flowChartWindow.getSize());
        GL11.glPointSize(5f);
        GL11.glEnable(GL11.GL_POINT_SMOOTH);
        Matrix3f transformationMatrix = new Matrix3f();
        for(FlowchartLine line : flowchartLines) {
            shader.color.loadVec3(line.getColor());
            GL30.glBindVertexArray(line.getTerminator().getModel().getVaoID());
            GL20.glEnableVertexAttribArray(0);
            if(line.getTerminator() instanceof ArrowHead){
                transformationMatrix.setIdentity();
                if(((ArrowHead) line.getTerminator()).isUpwards()){
                    transformationMatrix.m11 = 1;
                }else{
                    transformationMatrix.m11 = -1;
                }
                transformationMatrix.m20 = line.getTerminator().getPosition().x;
                transformationMatrix.m21 = line.getTerminator().getPosition().y;
                shader.transformationMatrix.loadMatrix(transformationMatrix);
                GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
            }else if(line.getTerminator() instanceof Junction){
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