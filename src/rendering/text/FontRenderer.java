package rendering.text;

import controllers.flowchartWindow.FlowchartWindowController;
import gui.guiElements.TextField;
import gui.texts.*;
import gui.fontMeshCreator.FontType;
import gui.textBoxes.CodeWindow;
import gui.guiElements.GUIElement;
import main.GeneralSettings;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector2f;
import utils.Printer;

import java.util.List;
import java.util.Map;

public class FontRenderer {

	private FontShader shader;
	private Matrix3f zoomTranslateMatrix = new Matrix3f();

	/**
	 *
	 */
	public FontRenderer() {
		shader = new FontShader();
		zoomTranslateMatrix.setIdentity();
	}

	/**
	 * @param texts
	 * @param flowChartWindowController
	 * @param codeWindow
	 * @param doClipping
	 * @param isScreenshot
	 */
	public void render(Map<FontType, List<Text>> texts, FlowchartWindowController flowChartWindowController, CodeWindow codeWindow, boolean doClipping, boolean isScreenshot) {
		prepare();
		if (!isScreenshot) {
			shader.aspectRatio.loadMatrix(GeneralSettings.ASPECT_RATIO);
		} else {
			Matrix2f matrix = new Matrix2f();
			matrix.setIdentity();
			shader.aspectRatio.loadMatrix(matrix);
		}
		for (FontType font : texts.keySet()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureAtlas());
			for (Text text : texts.get(font)) {
				renderText(text, flowChartWindowController, codeWindow, doClipping, null, null);
			}
		}
		endRendering();
	}

	public void renderGUIElements(List<GUIElement> elementList){
		prepare();
		shader.aspectRatio.loadMatrix(GeneralSettings.IDENTITY2);
		for(GUIElement element : elementList){
			if(element.getGuiText() != null){
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, element.getGuiText().getFont().getTextureAtlas());
				if(element instanceof TextField) {
					renderText(element.getGuiText(), null, null, false, ((TextField) element).getPosition(), ((TextField) element).getSize());
				}else{
					renderText(element.getGuiText(), null, null, false, null, null);
				}

			}
		}
		endRendering();
	}

	/**
	 *
	 */
	public void cleanUp() {
//		Printer.print();
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
	 * @param text
	 * @param flowchartWindowController
	 * @param codeWindow
	 * @param doClipping
	 * TODO: Move window calculation outside of render text
	 */
	private void renderText(Text text, FlowchartWindowController flowchartWindowController, CodeWindow codeWindow, boolean doClipping, Vector2f position, Vector2f size) {
		GL30.glBindVertexArray(text.getMesh());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		shader.width.loadFloat(GeneralSettings.FONT_WIDTH);
		shader.edge.loadFloat(GeneralSettings.FONT_EDGE);
		shader.translation.loadVec2(text.getPosition());
		shader.windowPosition.loadVec2(-1, -1);
		shader.windowSize.loadVec2(2, 2);
		shader.doClipping.loadBoolean(true);
		if (text instanceof TextWord) {
			if (doClipping) {
				shader.windowPosition.loadVec2(flowchartWindowController.getPosition());
				shader.windowSize.loadVec2(flowchartWindowController.getSize());
			} else {
				shader.windowPosition.loadVec2(-1, -1);
				shader.windowSize.loadVec2(2, 2);
			}
			shader.zoomTranslateMatrix.loadMatrix(flowchartWindowController.getZoomTranslateMatrix());
			if (codeWindow == null) {
				shader.doClipping.loadBoolean(false);
				shader.zoomTranslateMatrix.loadMatrix(GeneralSettings.IMAGE_TRANSLATION);
				shader.color.loadVec3(((TextWord) text).getColor());
				GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
			}
		} else if (text instanceof GUIText) {
			shader.windowPosition.loadVec2(-1, -1);
			shader.windowSize.loadVec2(2, 2);
			shader.zoomTranslateMatrix.loadMatrix(zoomTranslateMatrix);
			if (codeWindow == null) {
				if(text instanceof DarkGUIText){
					shader.windowPosition.loadVec2(position);
					shader.windowSize.loadVec2(size);
					shader.color.loadVec3(DarkGUIText.getColor());
				}else {
					shader.color.loadVec3(GUIText.getColor());
				}
				GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
			}
		} else if (text instanceof CodeWindowText && codeWindow != null) {
			shader.windowPosition.loadVec2(codeWindow.getCodeWindowPosition().x, codeWindow.getCodeWindowPosition().y);
			shader.windowSize.loadVec2(codeWindow.getCodeWindowSize());
			shader.zoomTranslateMatrix.loadMatrix(zoomTranslateMatrix);
		} else {
			if (codeWindow != null) {
				shader.windowPosition.loadVec2(codeWindow.getPosition().x, codeWindow.getPosition().y);
				shader.windowSize.loadVec2(codeWindow.getSize());
				shader.zoomTranslateMatrix.loadMatrix(zoomTranslateMatrix);
			}
		}
		if (codeWindow != null) {
			//shader.color.loadVec3(text.getColor());
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
		}
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
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
