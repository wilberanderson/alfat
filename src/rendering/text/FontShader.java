package rendering.text;

import rendering.shaders.ShaderProgram;
import main.GeneralSettings;
import rendering.shaders.uniforms.*;

/**
 * Handles the interaction with the {@link gui.Selection} vertex and fragment shaders.
 */
public class FontShader extends ShaderProgram{

	
	protected UniformVec3 color = new UniformVec3("color");
	protected UniformVec2 translation = new UniformVec2("translation");
	protected UniformFloat width = new UniformFloat("width");
	protected UniformFloat edge = new UniformFloat("edge");
	protected UniformVec2 windowPosition = new UniformVec2("windowPosition");
	protected UniformVec2 windowSize = new UniformVec2("windowSize");
	protected UniformMat3 zoomTranslateMatrix = new UniformMat3("zoomTranslateMatrix");
	protected UniformMat2 aspectRatio = new UniformMat2("aspectRatio");
	protected UniformBoolean doClipping = new UniformBoolean("doClipping");

	public FontShader() {
		super(GeneralSettings.FONT_VERTEX, GeneralSettings.FONT_FRAGMENT, "position", "textureCoords");
		super.storeAllUniformLocations(color, translation, width, edge, windowPosition, windowSize, zoomTranslateMatrix, aspectRatio, doClipping);
	}


	


}
