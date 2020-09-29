package rendering.shaders.uniforms;

import org.lwjgl.util.vector.Vector3f;


public class UniformVec3Array extends Uniform{

	private UniformVec3[] vecUniforms;

	public UniformVec3Array(String name, int size) {
		super(name);
		vecUniforms = new UniformVec3[size];
		for(int i=0;i<size;i++){
			vecUniforms[i] = new UniformVec3(name + "["+i+"]");
		}
	}
	
	@Override
	public void storeUniformLocation(int programID) {
		for(UniformVec3 vecUniform : vecUniforms){
			vecUniform.storeUniformLocation(programID);
		}
	}

	public void loadVec3Array(Vector3f[] vectors){
		for(int i=0;i<vectors.length;i++){
			vecUniforms[i].loadVec3(vectors[i]);
		}
	}
	
	

}
