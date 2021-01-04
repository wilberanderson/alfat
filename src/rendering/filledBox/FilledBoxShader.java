package rendering.filledBox;

import main.GeneralSettings;
import rendering.shaders.ShaderProgram;
import rendering.shaders.uniforms.*;


/**
 * Handles the interaction with the {@link gui.GUIFilledBox} vertex and fragment shaders.
 */
public class FilledBoxShader extends ShaderProgram {


    protected UniformVec3 color = new UniformVec3("color");
    protected UniformMat3 transformation = new UniformMat3("transformation");
    protected UniformVec2 windowPosition = new UniformVec2("windowPosition");
    protected UniformVec2 windowSize = new UniformVec2("windowSize");
    protected UniformMat3 zoomTranslateMatrix = new UniformMat3("zoomTranslateMatrix");
    protected UniformMat2 aspectRatio = new UniformMat2("aspectRatio");
    protected UniformBoolean doClipping = new UniformBoolean("doClipping");

    /**
     * Creates the shader program which is used for rendering a {@link gui.GUIFilledBox}.
     */
    public FilledBoxShader() {
        //A filled box's position is at attribute 0 and a filled box does not use any texture coordinates
        super(GeneralSettings.FILLED_BOX_VERTEX, GeneralSettings.FILLED_BOX_FRAGMENT, "position");
        //Activate all uniforms so that loading data to them loads to the shader program
        super.storeAllUniformLocations(color, transformation, windowPosition, windowSize, zoomTranslateMatrix, aspectRatio, doClipping);
    }


}
