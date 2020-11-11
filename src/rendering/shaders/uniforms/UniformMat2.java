package rendering.shaders.uniforms;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix2f;

import java.nio.FloatBuffer;

public class UniformMat2 extends Uniform{

	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(4);

	public UniformMat2(String name) {
		super(name);
	}
	
	public void loadMatrix(Matrix2f matrix){
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix2fv(super.getLocation(), false, matrixBuffer);
	}
	
	

}
