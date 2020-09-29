package rendering.shaders.uniforms;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Matrix3f;

import java.nio.FloatBuffer;

public class UniformMat3 extends Uniform{

	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(9);

	public UniformMat3(String name) {
		super(name);
	}
	
	public void loadMatrix(Matrix3f matrix){
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix3fv(super.getLocation(), false, matrixBuffer);
	}
	
	

}
