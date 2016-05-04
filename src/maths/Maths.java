package maths;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Christopher on 24/04/2016.
 */
public class Maths {

	/**
	 * Creates a transformation matrix.
	 *
	 * @param translation Translation vector
	 * @param rx Rotation on x axis
	 * @param ry Rotation on y axis
	 * @param rz Rotation on z axis
	 * @param scale Scale vector
	 * @return The new transformation matrix
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, Vector3f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, 1, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(1, scale.y, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(1, 1, scale.z), matrix, matrix);
		return matrix;
	}

	/**
	 * Creates a view matrix
	 *
	 * @param camera The camera
	 * @return The new view matrix
	 */
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1,0,0), viewMatrix,
				viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1,0), viewMatrix,
				viewMatrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}

	public static Vector2f divide(Vector2f left, float divisor, Vector2f dest) {
		if (dest == null)
			return new Vector2f(left.x / divisor, left.y / divisor);
		else {
			dest.set(left.x / divisor, left.y / divisor);
			return dest;
		}
	}
}
