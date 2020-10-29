package rendering.selection;

import main.GeneralSettings;
import rendering.shaders.ShaderProgram;
import rendering.shaders.uniforms.UniformMat3;
import rendering.shaders.uniforms.UniformVec3;

/**
 * Handles the interaction with the {@link gui.Selection} vertex and fragment shaders.
 */
public class SelectionShader extends ShaderProgram{


    protected UniformVec3 color = new UniformVec3("color");
    protected UniformMat3 transformation = new UniformMat3("transformation");

    /**
     * Performs setup for a {@link SelectionShader}
     *  - Initializes the shader program
     *  - Binds position to attrib 0
     *  - Stores the locations for the transformationMatrix and whether the {@link gui.Selection} should be highlighted
     */
    public SelectionShader() {
        super(GeneralSettings.SELECTION_VERTEX, GeneralSettings.SELECTION_FRAGMENT, "position");
        super.storeAllUniformLocations(color, transformation);
    }





}
