package rendering.cursor;

import main.GeneralSettings;
import rendering.shaders.ShaderProgram;
import rendering.shaders.uniforms.*;

/**
 * Handles the interaction with the {@link gui.Cursor} vertex and fragment shaders.
 */
public class CursorShader extends ShaderProgram {


    protected UniformVec3 color = new UniformVec3("color");
    protected UniformVec2 mousePosition = new UniformVec2("mousePosition");
    protected UniformFloat fontHeight = new UniformFloat("fontHeight");
    protected UniformMat2 aspectRatio = new UniformMat2("aspectRatio");
    protected UniformBoolean isVisible = new UniformBoolean("isVisible");

    /**
     * Creates the shader program which is used for rendering a {@link gui.Cursor}.
     */
    public CursorShader() {
        //A cursor's position is at attribute 0 and a cursor does not use any texture coordinates
        super(GeneralSettings.CURSOR_VERTEX, GeneralSettings.CURSOR_FRAGMENT, "position");
        //Activate all uniforms so that loading data to them loads to the shader program
        super.storeAllUniformLocations(color, mousePosition, fontHeight, aspectRatio, isVisible);
    }


}
