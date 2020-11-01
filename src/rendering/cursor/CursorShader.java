package rendering.cursor;

import rendering.selection.SelectionShader;
import rendering.shaders.ShaderProgram;
import main.GeneralSettings;
import rendering.shaders.uniforms.*;

/**
 * Handles the interaction with the {@link gui.Cursor} vertex and fragment shaders.
 */
public class CursorShader extends ShaderProgram{


    protected UniformVec3 color = new UniformVec3("color");
    protected UniformVec2 mousePosition = new UniformVec2("mousePosition");
    protected UniformFloat fontHeight = new UniformFloat("fontHeight");
    protected UniformVec2 windowPosition = new UniformVec2("windowPosition");
    protected UniformVec2 windowSize = new UniformVec2("windowSize");

    /**
     * Performs setup for a {@link CursorShader}
     *  - Initializes the shader program
     *  - Binds position to attrib 0
     *  - Stores the locations for the color, mousePosition, and fontHeight for the {@link gui.Cursor}
     */
    public CursorShader() {
        super(GeneralSettings.CURSOR_VERTEX, GeneralSettings.CURSOR_FRAGMENT, "position");
        super.storeAllUniformLocations(color, mousePosition, fontHeight, windowPosition, windowSize);
    }





}
