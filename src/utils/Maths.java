package utils;

import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector2f;


public class Maths {
	public static Matrix3f createTransformationMatrix(Vector2f scale, Vector2f translation){
		Matrix3f transformationMatrix = new Matrix3f();
		transformationMatrix.m00 = scale.x;
		transformationMatrix.m11 = scale.y;
		transformationMatrix.m20 = translation.x;
		transformationMatrix.m21 = translation.y;
		return transformationMatrix;
	}
}
