package rendering.cursor;

import rendering.shaders.ShaderProgram;
import main.GeneralSettings;
import rendering.shaders.uniforms.*;

public class CursorShader extends ShaderProgram{


    protected UniformVec3 color = new UniformVec3("color");
    protected UniformVec2 mousePosition = new UniformVec2("mousePosition");
    protected UniformFloat fontHeight = new UniformFloat("fontHeight");

    public CursorShader() {
        super(GeneralSettings.CURSOR_VERTEX, GeneralSettings.CURSOR_FRAGMENT, "position");
        super.storeAllUniformLocations(color, mousePosition, fontHeight);
    }





}
