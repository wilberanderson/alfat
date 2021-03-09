package rendering.textLines;

import controllers.codeWindow.CodeWindowController;
import controllers.flowchartWindow.FlowchartWindowController;
import controllers.TextLineController;
import gui.texts.*;
import gui.textBoxes.CodeWindow;
import main.GeneralSettings;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector2f;

/**
 * Controls rendering lines of {@link FormattedTextLine "formatted text"}, such as inside {@link gui.textBoxes.FlowchartTextBox flowchart text boxes} and the {@link CodeWindow}'s TextBox.
 */
public class TextLineRenderer {

	private TextLineShader shader;

	/**
	 * Creates a new TextLineRenderer. Initializes a {@link TextLineShader} to control the shader with will be used to render on the GPU and intitializes a local ZoomTranslateMatrix which is updated every render call.
	 */
	public TextLineRenderer() {
		shader = new TextLineShader();
	}

	/**
	 * Renders the formatted text lines used for lines of code
	 * @param textLineController
	 * @param flowChartWindowController
	 */
	public void renderToScreen(TextLineController textLineController, FlowchartWindowController flowChartWindowController, CodeWindowController codeWindowController) {
		prepare();

		shader.aspectRatio.loadMatrix(GeneralSettings.ASPECT_RATIO);



		for(FormattedTextLine line : textLineController.getLoadedTexts()){
			if(line.getWords().length > 0) {
				if (line instanceof EditableFormattedTextLine) {
					GL13.glActiveTexture(GL13.GL_TEXTURE0);
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, line.getWords()[0].getFont().getTextureAtlas());
					for (TextWord text : line.getWords()) {
						if (text instanceof LineNumberWord) {
							renderText(text, codeWindowController.getCodeWindow().getPosition(), codeWindowController.getCodeWindow().getSize(), GeneralSettings.IDENTITY3);
						} else {
							renderText(text, codeWindowController.getCodeWindow().getCodeWindowPosition(), codeWindowController.getCodeWindow().getCodeWindowSize(), GeneralSettings.IDENTITY3);
						}
					}
				} else {
					GL13.glActiveTexture(GL13.GL_TEXTURE0);
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, line.getWords()[0].getFont().getTextureAtlas());
					for (TextWord text : line.getWords()) {
						renderText(text, flowChartWindowController.getPosition(), flowChartWindowController.getSize(), flowChartWindowController.getZoomTranslateMatrix());
					}
				}
			}
		}

//		for (FormattedTextLine formattedTextLine : textLineController.getCodeWindowTextLines()) {
//			if (formattedTextLine.getWords().length > 0) {
//				GL13.glActiveTexture(GL13.GL_TEXTURE0);
//				GL11.glBindTexture(GL11.GL_TEXTURE_2D, formattedTextLine.getWords()[0].getFont().getTextureAtlas());
//				for (TextWord text : formattedTextLine.getWords()) {
//					if(text instanceof LineNumberWord){
//						renderText(text, codeWindowController.getCodeWindow().getPosition(), codeWindowController.getCodeWindow().getSize(), GeneralSettings.IDENTITY3);
//					}else{
//						renderText(text, codeWindowController.getCodeWindow().getCodeWindowPosition(), codeWindowController.getCodeWindow().getCodeWindowSize(), GeneralSettings.IDENTITY3);
//					}
//				}
//			}
//		}
//		for (FormattedTextLine formattedTextLine : textLineController.getFlowchartTextLines()) {
//			if (formattedTextLine.getWords().length > 0) {
//				GL13.glActiveTexture(GL13.GL_TEXTURE0);
//				GL11.glBindTexture(GL11.GL_TEXTURE_2D, formattedTextLine.getWords()[0].getFont().getTextureAtlas());
//				for (TextWord text : formattedTextLine.getWords()) {
//					renderText(text, flowChartWindowController.getPosition(), flowChartWindowController.getSize(), flowChartWindowController.getZoomTranslateMatrix());
//				}
//			}
//		}
		endRendering();
	}

	/**
	 * Renders the formatted text lines used for lines of code
	 * @param textLineController
	 * @param flowChartWindowController
	 */
	public void renderToImage(TextLineController textLineController, FlowchartWindowController flowChartWindowController) {
		prepare();

		Matrix2f matrix = new Matrix2f();
		matrix.setIdentity();
		shader.aspectRatio.loadMatrix(matrix);


		if(textLineController.getFlowchartTextLines() != null && textLineController.getFlowchartTextLines().size() > 0 && textLineController.getFlowchartTextLines().get(0) != null && textLineController.getFlowchartTextLines().get(0).getWords().length > 0) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D,textLineController.getFlowchartTextLines().get(0).getWords()[0].getFont().getTextureAtlas());
		}else{
			endRendering();
			return;
		}

		for (FormattedTextLine formattedTextLine : textLineController.getFlowchartTextLines()) {
			if (formattedTextLine.getWords().length > 0) {
				for (TextWord text : formattedTextLine.getWords()) {
					renderText(text, new Vector2f(-1, -1), new Vector2f(2, 2), GeneralSettings.IMAGE_TRANSLATION);
				}
			}
		}

		endRendering();
	}

	/**
	 * Cleans up the memory of the {@link TextLineShader}. The TextLineShader has a GPU program used to render saved inside of GRAM. This will not be automatically cleaned up by the garbage collector so it must be deleted manually when this renderer will no longer be used.
	 */
	public void cleanUp() {
		shader.cleanUp();
	}

	/**
	 * Sets the OpenGL state to be appropriate for rendering lines of formatted text.
	 */
	private void prepare() {
		//Enable alpha blending
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		//Disable the depth test
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		//Set the shader to be the shader in active use so it does not try to use the previous shader to render
		shader.start();
	}

	/**
	 * Renders a single {@link GUIText} onto the screen. Also accounts for what region of the screen clipping should be evaluated for.
	 * @param text the GUIText to be rendered. For a formatted {@link FormattedTextLine "line of text"} it is specifically a {@link TextWord}.
	 */
	private void renderText(TextWord text, Vector2f windowPosition, Vector2f windowSize, Matrix3f zoomTranslateMatrix) {
		//Bind the vertex array which represents the quads behind each letter in the text
		GL30.glBindVertexArray(text.getMesh());
		//This vertex array has positions stored in attribute 0 and texture coordinates stored in attribute 1
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);

		//Load the width of the letter and the added width of an edge(between 0 and 1 where 0 is no width and 1 is full width. Width + edge should not be greater than 1 or it will result in a sharp edge)
		shader.width.loadFloat(GeneralSettings.FONT_WIDTH);
		shader.edge.loadFloat(GeneralSettings.FONT_EDGE);

		//Load the position of the text
		shader.translation.loadVec2(text.getPosition());

		//Update the window size and location
		shader.windowPosition.loadVec2(windowPosition);
		shader.windowSize.loadVec2(windowSize);

		//Load the matrix which positions the text with the correct level of zoom and position
		shader.zoomTranslateMatrix.loadMatrix(zoomTranslateMatrix);

		//Load the color used to render this text
		shader.color.loadVec3(text.getColor());

		//Render the text
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());

		//Unbind the model used for the text
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}

	/**
	 * Restores the OpenGL state for another renderer to be called.
	 */
	private void endRendering() {
		//Stop the shader program
		shader.stop();

		//Disable the blend function and depth test
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

}
