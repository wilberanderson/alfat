package rendering.textLines;

import controllers.flowchartWindow.FlowchartWindowController;
import controllers.flowchartWindow.TextLineController;
import gui.GUIText;
import gui.TextLine;
import gui.textBoxes.CodeWindow;
import main.GeneralSettings;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Matrix3f;

public class TextLineRenderer {

	private TextLineShader shader;
	private Matrix3f zoomTranslateMatrix = new Matrix3f();

	public TextLineRenderer() {
		shader = new TextLineShader();
		zoomTranslateMatrix.setIdentity();
	}


	public void render(TextLineController textLineController, FlowchartWindowController flowChartWindowController, CodeWindow codeWindow, boolean doClipping, boolean isScreenshot){
		prepare();
		if(!isScreenshot){
			shader.aspectRatio.loadMatrix(GeneralSettings.ASPECT_RATIO);
		}else{
			Matrix2f matrix = new Matrix2f();
			matrix.setIdentity();
			shader.aspectRatio.loadMatrix(matrix);
		}

		for(TextLine textLine : textLineController.getTextLines()) {
			if(textLine.getWords().size() > 0) {
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, textLine.getWords().get(0).getFont().getTextureAtlas());
				for (GUIText text : textLine.getWords()) {
					renderText(text, flowChartWindowController, codeWindow, doClipping);
				}
			}
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
	
	private void renderText(GUIText text, FlowchartWindowController flowchartWindowController, CodeWindow codeWindow, boolean doClipping){
		GL30.glBindVertexArray(text.getMesh());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		shader.width.loadFloat(text.getWidth());
		shader.edge.loadFloat(text.getEdge());
		shader.borderWidth.loadFloat(text.getBorderWidth());
		shader.borderEdge.loadFloat(text.getBorderEdge());
		shader.offset.loadVec2(text.getOffset());
		shader.outlineColor.loadVec3(text.getOutlineColor());
		shader.translation.loadVec2(text.getPosition());
		shader.windowPosition.loadVec2(-1, -1);
		shader.windowSize.loadVec2(2, 2);
		shader.doClipping.loadBoolean(true);
		if(text.isInFlowchart()){
			if(doClipping){
			shader.windowPosition.loadVec2(flowchartWindowController.getPosition());
			shader.windowSize.loadVec2(flowchartWindowController.getSize());
			}else{
				shader.windowPosition.loadVec2(-1, -1);
				shader.windowSize.loadVec2(2, 2);
			}
			shader.zoomTranslateMatrix.loadMatrix(flowchartWindowController.getZoomTranslateMatrix());
			if(codeWindow == null){
				shader.doClipping.loadBoolean(false);
				shader.zoomTranslateMatrix.loadMatrix(GeneralSettings.SCREENSHOT_TRANSLATION);
				shader.color.loadVec3(text.getColor());
				GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
			}
		}else if(text.isGuiText()){
			shader.windowPosition.loadVec2(-1, -1);
			shader.windowSize.loadVec2(2, 2);
			shader.zoomTranslateMatrix.loadMatrix(zoomTranslateMatrix);
			if(codeWindow == null) {
				shader.color.loadVec3(text.getColor());
				GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
			}
		}else if(text.isCodeWindowText() && codeWindow != null){
			shader.windowPosition.loadVec2(codeWindow.getCodeWindowPosition().x-1, codeWindow.getCodeWindowPosition().y-1);
			shader.windowSize.loadVec2(codeWindow.getCodeWindowSize());
			shader.zoomTranslateMatrix.loadMatrix(zoomTranslateMatrix);
		}else{
			if(codeWindow != null) {
				shader.windowPosition.loadVec2(codeWindow.getPosition().x - 1, codeWindow.getPosition().y - 1);
				shader.windowSize.loadVec2(codeWindow.getSize());
				shader.zoomTranslateMatrix.loadMatrix(zoomTranslateMatrix);
			}
		}
		if(codeWindow != null) {
			shader.color.loadVec3(text.getColor());
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
		}
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	private void endRendering(){
		shader.stop();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

}
