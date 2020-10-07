package rendering.text;

import rendering.shaders.ShaderProgram;
import main.GeneralSettings;
import rendering.shaders.uniforms.UniformFloat;
import rendering.shaders.uniforms.UniformVec2;
import rendering.shaders.uniforms.UniformVec3;
import rendering.shaders.uniforms.UniformVec4;

public class FontShader extends ShaderProgram{

	
	protected UniformVec3 color = new UniformVec3("color");
	protected UniformVec3 outlineColor = new UniformVec3("outlineColor");
	protected UniformVec2 translation = new UniformVec2("translation");
	protected UniformVec2 offset = new UniformVec2("offset");
	protected UniformFloat width = new UniformFloat("width");
	protected UniformFloat edge = new UniformFloat("edge");
	protected UniformFloat borderWidth = new UniformFloat("borderWidth");
	protected UniformFloat borderEdge = new UniformFloat("borderEdge");
	protected UniformVec4 positionBounds = new UniformVec4("positionBounds");

	public FontShader() {
		super(GeneralSettings.FONT_VERTEX, GeneralSettings.FONT_FRAGMENT, "position", "textureCoords");
		super.storeAllUniformLocations(color, outlineColor, translation, offset, width, edge, borderWidth, borderEdge, positionBounds);
	}


	


}
