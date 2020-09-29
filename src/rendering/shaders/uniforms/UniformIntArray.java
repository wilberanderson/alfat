package rendering.shaders.uniforms;

public class UniformIntArray extends Uniform{

	private UniformInt[] intUniforms;

	public UniformIntArray(String name, int size) {
		super(name);
		intUniforms = new UniformInt[size];
		for(int i=0;i<size;i++){
			intUniforms[i] = new UniformInt(name + "["+i+"]");
		}
	}
	
	@Override
	public void storeUniformLocation(int programID) {
		for(UniformInt intUniform : intUniforms){
			intUniform.storeUniformLocation(programID);
		}
	}

	public void loadIntArray(int[] ints){
		for(int i=0;i<ints.length;i++){
			intUniforms[i].loadInt(ints[i]);
		}
	}
	
	

}
