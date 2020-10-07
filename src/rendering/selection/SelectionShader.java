package rendering.selection;

import main.GeneralSettings;
import rendering.shaders.ShaderProgram;
import rendering.shaders.uniforms.UniformMat3;
import rendering.shaders.uniforms.UniformVec3;

public class SelectionShader extends ShaderProgram{


    protected UniformVec3 color = new UniformVec3("color");
    protected UniformMat3 transformation = new UniformMat3("transformation");
   // protected UniformVec2 translation = new UniformVec2("translation");

    public SelectionShader() {
        super(GeneralSettings.SELECTION_VERTEX, GeneralSettings.SELECTION_FRAGMENT, "position");
        super.storeAllUniformLocations(color, transformation);//, translation);
    }





}
