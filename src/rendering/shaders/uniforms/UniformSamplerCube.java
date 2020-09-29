package rendering.shaders.uniforms;

import org.lwjgl.opengl.GL20;

public class UniformSamplerCube extends Uniform {

	private int currentValue;
	private boolean used = false;

	public UniformSamplerCube(String name) {
		super(name);
	}

	public void loadCubeMap(int cubeMap) {
		if (!used || currentValue != cubeMap) {
			GL20.glUniform1i(super.getLocation(), cubeMap);
			used = true;
			currentValue = cubeMap;
		}
	}

}
