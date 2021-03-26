package rendering.rulers;

import main.GeneralSettings;
import rendering.shaders.ShaderProgram;
import rendering.shaders.uniforms.*;

/**
 * Handles the interaction with the {@link gui.Cursor} vertex and fragment shaders.
 */
public class RulerShader extends ShaderProgram{


//    protected UniformVec3 color = new UniformVec3("color");
//    protected UniformVec4 endPositions = new UniformVec4("endPositions");
//    protected UniformVec2 startPosition = new UniformVec2("startPosition");
//    protected UniformVec2 endPosition = new UniformVec2("endPosition");
    protected UniformVec2 windowPosition = new UniformVec2("windowPosition");
    protected UniformVec2 windowSize = new UniformVec2("windowSize");
    protected UniformMat3 zoomTranslateMatrix = new UniformMat3("zoomTranslateMatrix");
    protected UniformMat2 aspectRatio = new UniformMat2("aspectRatio");
//    protected UniformBoolean doClipping = new UniformBoolean("doClipping");

    /**
     * Performs setup for a {@link RulerShader}
     *  - Initializes the shader program
     *  - Binds position to attrib 0
     *  - Stores the locations for the color, mousePosition, and fontHeight for the {@link gui.Cursor}
     */
    public RulerShader() {
        super(GeneralSettings.RULER_VERTEX, GeneralSettings.RULER_FRAGMENT, "position", "endPositions", "color");
        super.storeAllUniformLocations(windowPosition, windowSize, zoomTranslateMatrix, aspectRatio);//, color, endPositions);
    }





}
