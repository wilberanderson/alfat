package rendering.flowchartLine;

import main.GeneralSettings;
import rendering.shaders.ShaderProgram;
import rendering.shaders.uniforms.UniformFloat;
import rendering.shaders.uniforms.UniformMat3;
import rendering.shaders.uniforms.UniformVec2;
import rendering.shaders.uniforms.UniformVec3;

/**
 * Handles the interaction with the {@link gui.Cursor} vertex and fragment shaders.
 */
public class FlowchartLineShader extends ShaderProgram{


    protected UniformVec3 color = new UniformVec3("color");
    protected UniformVec2 startPosition = new UniformVec2("startPosition");
    protected UniformVec2 endPosition = new UniformVec2("endPosition");
    protected UniformVec2 windowPosition = new UniformVec2("windowPosition");
    protected UniformVec2 windowSize = new UniformVec2("windowSize");
    protected UniformMat3 zoomTranslateMatrix = new UniformMat3("zoomTranslateMatrix");

    /**
     * Performs setup for a {@link FlowchartLineShader}
     *  - Initializes the shader program
     *  - Binds position to attrib 0
     *  - Stores the locations for the color, mousePosition, and fontHeight for the {@link gui.Cursor}
     */
    public FlowchartLineShader() {
        super(GeneralSettings.FLOWCHART_VERTEX, GeneralSettings.FLOWCHART_FRAGMENT, "position");
        super.storeAllUniformLocations(color, startPosition, endPosition, windowPosition, windowSize, zoomTranslateMatrix);
    }





}
