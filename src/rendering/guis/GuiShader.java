package rendering.guis;

import rendering.shaders.ShaderProgram;
import main.GeneralSettings;
import rendering.shaders.uniforms.UniformBoolean;
import rendering.shaders.uniforms.UniformMatrix;

public class GuiShader extends ShaderProgram{
    protected UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
    protected UniformBoolean highlighted = new UniformBoolean("highlighted");

    /**
     * Creates the shader program for the {@link GuiRenderer} by
     * loading up the vertex and fragment shader code files. It also gets the
     * location of all the specified uniform variables, and also indicates that
     * the diffuse texture will be sampled from texture unit 0.
     */
    public GuiShader() {
        super(GeneralSettings.GUI_VERTEX, GeneralSettings.GUI_FRAGMENT, "position");
        super.storeAllUniformLocations(transformationMatrix, highlighted);
    }

}
     
     
 