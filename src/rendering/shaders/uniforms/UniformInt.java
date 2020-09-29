package rendering.shaders.uniforms;

import org.lwjgl.opengl.GL20;

public class UniformInt extends Uniform{

	private float currentValue;
	private boolean used = false;

	public UniformInt(String name){
		super(name);
	}
	
	public void loadInt(int value){
		if(!used || currentValue!=value){
			GL20.glUniform1i(super.getLocation(), value);
			used = true;
			currentValue = value;
		}
	}

}
