package rendering.guis;

import rendering.shaders.ShaderProgram;
import main.GeneralSettings;
import rendering.shaders.uniforms.UniformBoolean;
import rendering.shaders.uniforms.UniformMatrix;

/**
 * Handles the interaction with the {@link gui.GuiTexture} vertex and fragment shaders.
 */
public class GuiShader extends ShaderProgram{
    protected UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
    protected UniformBoolean highlighted = new UniformBoolean("highlighted");

    /**
     * Handles the interaction with the {@link gui.GuiTexture} vertex and fragment shaders.
     *  - Initializes the shader program
     *  - Binds position to attrib 0
     *  - Stores the locations for the transformationMatrix and whether the {@link gui.GuiTexture} should be highlighted
     */
    public GuiShader() {
        super(GeneralSettings.GUI_VERTEX, GeneralSettings.GUI_FRAGMENT, "position");
        super.storeAllUniformLocations(transformationMatrix, highlighted);
    }

}
     
     
 