package simulation.graphics;

import simulation.math.Matrix4f;
import simulation.math.Vector3f;

/**
 * Created by Christopher on 22/04/2016.
 */
public class Transformation {

	private Matrix4f projectionMatrix;

	private Matrix4f worldMatrix;

	/**
	 * Constructor for Transformation.
	 */
	public Transformation() {
		projectionMatrix = Matrix4f.identity();
		worldMatrix = Matrix4f.identity();
	}

	/**
	 * Returns an orthographic projection matrix.
	 *
	 * @param left Screen left bound
	 * @param right Screen right bound
	 * @param bottom Screen bottom bound
	 * @param top Screen top bound
	 * @param near Screen near bound
	 * @param far Screen far bound
	 * @return The orthographic projection matrix
	 */
	public final Matrix4f getProjectionMatrix(float left, float right, float bottom, float top, float near, float far) {
		projectionMatrix = Matrix4f.orthographic(left, right, bottom, top, near, far);
		return projectionMatrix;
	}

	/**
	 * Returns a perspective projection matrix.
	 *
	 * @param fovx The screen's Field Of View on the x axis
	 * @param fovy The screen's Field Of View on the y axis
	 * @param near The screen's near bound
	 * @param far The screen's far bound
	 * @return The perspective matrix
	 */
	public final Matrix4f getProjectionMatrix(float fovx, float fovy, float near, float far) {
		projectionMatrix = Matrix4f.perspective(fovx, fovy, near, far);
		return projectionMatrix;
	}

	/**
	 * Returns a world matrix based off an entity's offset, rotation and scale.
	 *
	 * @param offset The offset or position of the entity
	 * @param rotation The rotation on the x axis of the entity
	 * @param scale The scale of the entity.
	 * @return The world matrix
	 */
	public Matrix4f getWorldMatrix(Vector3f offset, float rotation, float scale) {
		return Matrix4f.translate(offset).multiply(Matrix4f.rotate(rotation)).multiply(Matrix4f.scale(scale));
	}
}
