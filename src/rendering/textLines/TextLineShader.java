package rendering.textLines;

import gui.texts.TextLine;
import main.GeneralSettings;
import rendering.shaders.ShaderProgram;
import rendering.shaders.uniforms.*;

/**
 * Handles the interaction with the {@link TextLine} vertex and fragment shaders.
 */
public class TextLineShader extends ShaderProgram {


	protected UniformVec3 color = new UniformVec3("color");
	protected UniformVec2 translation = new UniformVec2("translation");
	protected UniformFloat width = new UniformFloat("width");
	protected UniformFloat edge = new UniformFloat("edge");
	protected UniformVec2 windowPosition = new UniformVec2("windowPosition");
	protected UniformVec2 windowSize = new UniformVec2("windowSize");
	protected UniformMat3 zoomTranslateMatrix = new UniformMat3("zoomTranslateMatrix");
	protected UniformMat2 aspectRatio = new UniformMat2("aspectRatio");

	/**
	 * Creates the shader program which is used for rendering a {@link TextLine}.
	 */
	public TextLineShader() {
		//A text line's position is at attribute 0 and texture coordinates are at attribute 1
		super(GeneralSettings.TEXT_LINE_VERTEX, GeneralSettings.TEXT_LINE_FRAGMENT, "position", "textureCoords");
		//Activate all uniforms so that loading data to them loads to the shader program
		super.storeAllUniformLocations(color, translation,  width, edge, windowPosition, windowSize, zoomTranslateMatrix, aspectRatio);
	}


}
