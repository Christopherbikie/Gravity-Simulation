package maths;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

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

	/**
	 * Convert world coordinates to screen coordinates
	 *
	 * @param position World coordinates to convert
	 * @param viewMatrix The current view matrix
	 * @param projMatrix The projection matrix
	 * @return Coordinates as screen coordinates
	 *         x and y are pixels on the screen, z is distance from camera
	 */
	public static Vector3f convertToScreenSpace(Vector3f position, Matrix4f viewMatrix, Matrix4f projMatrix) {
		Vector4f coords = new Vector4f(position.x, position.y, position.z, 1f);
		Matrix4f.transform(viewMatrix, coords, coords);
		Matrix4f.transform(projMatrix, coords, coords);
		return clipSpaceToScreenSpace(coords);
	}

	/**
	 * Convert homogeneous clip space to screen space
	 *
	 * @param coords Coordinates to convert
	 * @return Converted coordinates
	 */
	private static Vector3f clipSpaceToScreenSpace(Vector4f coords) {
		if (coords.w < 0)
			return null;
		return new Vector3f(((coords.x / coords.w) + 1) / 2f, 1 - (((coords.y / coords.w) + 1) / 2f), coords.z);
	}
}
