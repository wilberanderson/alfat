package rendering.textLines;

import main.GeneralSettings;
import rendering.shaders.ShaderProgram;
import rendering.shaders.uniforms.*;

/**
 * Handles the interaction with the {@link gui.Selection} vertex and fragment shaders.
 */
public class TextLineShader extends ShaderProgram{

	
	protected UniformVec3 color = new UniformVec3("color");
	protected UniformVec3 outlineColor = new UniformVec3("outlineColor");
	protected UniformVec2 translation = new UniformVec2("translation");
	protected UniformVec2 offset = new UniformVec2("offset");
	protected UniformFloat width = new UniformFloat("width");
	protected UniformFloat edge = new UniformFloat("edge");
	protected UniformFloat borderWidth = new UniformFloat("borderWidth");
	protected UniformFloat borderEdge = new UniformFloat("borderEdge");
	protected UniformVec2 windowPosition = new UniformVec2("windowPosition");
	protected UniformVec2 windowSize = new UniformVec2("windowSize");
	protected UniformMat3 zoomTranslateMatrix = new UniformMat3("zoomTranslateMatrix");
	protected UniformMat2 aspectRatio = new UniformMat2("aspectRatio");
	protected UniformBoolean doClipping = new UniformBoolean("doClipping");

	public TextLineShader() {
		super(GeneralSettings.FONT_VERTEX, GeneralSettings.FONT_FRAGMENT, "position", "textureCoords");
		super.storeAllUniformLocations(color, outlineColor, translation, offset, width, edge, borderWidth, borderEdge, windowPosition, windowSize, zoomTranslateMatrix, aspectRatio, doClipping);
	}


	


}
