package rendering.textBox;

import rendering.shaders.ShaderProgram;
import main.GeneralSettings;
import rendering.shaders.uniforms.*;

public class TextBoxShader extends ShaderProgram{


    protected UniformVec3 color = new UniformVec3("color");
    protected UniformMat3 transformation = new UniformMat3("transformation");
   // protected UniformVec2 translation = new UniformVec2("translation");

    public TextBoxShader() {
        super(GeneralSettings.TEXT_BOX_VERTEX, GeneralSettings.TEXT_BOX_FRAGMENT, "position");
        super.storeAllUniformLocations(color, transformation);//, translation);
    }





}
