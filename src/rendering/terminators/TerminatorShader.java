package rendering.terminators;

import main.GeneralSettings;
import rendering.shaders.ShaderProgram;
import rendering.shaders.uniforms.UniformMat2;
import rendering.shaders.uniforms.UniformMat3;
import rendering.shaders.uniforms.UniformVec2;
import rendering.shaders.uniforms.UniformVec3;

/**
 * Handles the interaction with the {@link gui.Cursor} vertex and fragment shaders.
 */
public class TerminatorShader extends ShaderProgram{


    protected UniformVec3 color = new UniformVec3("color");
    protected UniformVec2 windowPosition = new UniformVec2("windowPosition");
    protected UniformVec2 windowSize = new UniformVec2("windowSize");
    protected UniformMat3 zoomTranslateMatrix = new UniformMat3("zoomTranslateMatrix");
    protected UniformMat2 aspectRatio = new UniformMat2("aspectRatio");
    protected UniformMat3 transformationMatrix = new UniformMat3("transformationMatrix");

    /**
     * Performs setup for a {@link TerminatorShader}
     *  - Initializes the shader program
     *  - Binds position to attrib 0
     *  - Stores the locations for the color, mousePosition, and fontHeight for the {@link gui.Cursor}
     */
    public TerminatorShader() {
        super(GeneralSettings.TERMINATOR_VERTEX, GeneralSettings.TERMINATOR_FRAGMENT, "position");
        super.storeAllUniformLocations(color, windowPosition, windowSize, zoomTranslateMatrix, aspectRatio, transformationMatrix);
    }





}
