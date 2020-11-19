package rendering.text;

import java.util.List;
import java.util.Map;

import gui.textBoxes.CodeWindow;
import gui.FlowChartWindow;
import main.GeneralSettings;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import gui.fontMeshCreator.FontType;
import gui.GUIText;
import org.lwjgl.util.vector.Matrix3f;

public class FontRenderer {

	private FontShader shader;
	private Matrix3f zoomTranslateMatrix = new Matrix3f();

	public FontRenderer() {
		shader = new FontShader();
		zoomTranslateMatrix.setIdentity();
	}


	public void render(Map<FontType, List<GUIText>> texts, FlowChartWindow flowChartWindow, CodeWindow codeWindow, boolean doClipping){
		prepare();
		shader.aspectRatio.loadMatrix(GeneralSettings.ASPECT_RATIO);
		for(FontType font : texts.keySet()){
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureAtlas());
			for(GUIText text : texts.get(font)){
				renderText(text, flowChartWindow, codeWindow, doClipping);
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
	
	private void renderText(GUIText text, FlowChartWindow flowChartWindow, CodeWindow codeWindow, boolean doClipping){
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
			shader.windowPosition.loadVec2(flowChartWindow.getPosition());
			shader.windowSize.loadVec2(flowChartWindow.getSize());
			}else{
				shader.windowPosition.loadVec2(-1, -1);
				shader.windowSize.loadVec2(2, 2);
			}
			shader.zoomTranslateMatrix.loadMatrix(flowChartWindow.getZoomTranslateMatrix());
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
