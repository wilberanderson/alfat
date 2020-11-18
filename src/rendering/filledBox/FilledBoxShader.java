package rendering.filledBox;

import rendering.shaders.ShaderProgram;
import main.GeneralSettings;
import rendering.shaders.uniforms.*;

public class FilledBoxShader extends ShaderProgram{


    protected UniformVec3 color = new UniformVec3("color");
    protected UniformMat3 transformation = new UniformMat3("transformation");
    protected UniformVec2 windowPosition = new UniformVec2("windowPosition");
    protected UniformVec2 windowSize = new UniformVec2("windowSize");
    protected UniformMat3 zoomTranslateMatrix = new UniformMat3("zoomTranslateMatrix");
    protected UniformMat2 aspectRatio = new UniformMat2("aspectRatio");
    protected UniformBoolean doClipping = new UniformBoolean("doClipping");

    /**
     * Handles the interaction with the TextBox vertex and fragment shaders.
     *  - Initializes the shader program
     *  - Binds position to attrib 0
     *  - Stores the locations for the color and transformation of the TextBox
     */
    public FilledBoxShader() {
        super(GeneralSettings.FILLED_BOX_VERTEX, GeneralSettings.FILLED_BOX_FRAGMENT, "position");
        super.storeAllUniformLocations(color, transformation, windowPosition, windowSize, zoomTranslateMatrix, aspectRatio, doClipping);
    }





}
