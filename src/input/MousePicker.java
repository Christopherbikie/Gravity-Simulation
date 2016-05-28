package input;

import entities.Camera;
import maths.Maths;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Created by Christopher on 21/05/2016.
 */
public class MousePicker {

	/**
	 * Number of recursions to preform in the binary search
	 */
	private static final int RECURSION_COUNT = 200;
	/**
	 * Distance to check during the binary search.
	 * Objects further than this far away will not be found.
	 */
	private static final float RAY_RANGE = 600;

	/**
	 * The projection matrix being used
	 */
	private Matrix4f projectionMatrix;
	/**
	 * The view matrix being used
	 */
	private Matrix4f viewMatrix;
	/**
	 * The camera we are viewing from
	 */
	private Camera camera;

	/**
	 * The current location on the y-plane the mouse lines up with.
	 */
	private Vector3f currentYPlanePoint;

	/**
	 * Constuctor to create a new MousePicker
	 *
	 * @param camera The camera we are viewing from
	 * @param projectionMatrix The projection matrix being used
	 */
	public MousePicker(Camera camera, Matrix4f projectionMatrix) {
		this.camera = camera;
		this.projectionMatrix = projectionMatrix;
		this.viewMatrix = Maths.createViewMatrix(camera);
	}

	/**
	 * Update the mouse's current projected y-plane point
	 */
	public void update() {
		viewMatrix = Maths.createViewMatrix(camera);
		Vector3f currentRay = calculateMouseRay();
		if (intersectionInRange(0, RAY_RANGE, currentRay)) {
			currentYPlanePoint = binarySearch(0, 0, RAY_RANGE, currentRay);
		} else {
			currentYPlanePoint = null;
		}
	}

	/**
	 * Find the ray from the camera passing through the mouse on the screen
	 *
	 * @return The ray passing through the camera and mouse
	 */
	private Vector3f calculateMouseRay() {
		Vector2f normalisedCoords = getNormalisedDeviceCoordinates(Mouse.getX(), Mouse.getY());
		Vector4f clipCoords = new Vector4f(normalisedCoords.x, normalisedCoords.y, -1f, 1f);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		return toWorldCoords(eyeCoords);
	}

	/**
	 * Get the normalised device coordinates of the mouse from the viewport space
	 *
	 * @param mouseX x coordinate of the mouse
	 * @param mouseY y coordinate of the mouse
	 * @return The location of the mouse in normalised device coordinates
	 */
	private Vector2f getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
		float x = (2f * mouseX) / Display.getWidth() - 1;
		float y = (2f * mouseY) / Display.getHeight() - 1;
		return new Vector2f(x, y);
	}

	/**
	 * Convert normalised device coordinates to eye coordinates
	 *
	 * @param clipCoords Normalised device coordinates
	 * @return Eye coordinates for the given normalised coordinates
	 */
	private Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjectionMatrix = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjectionMatrix, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}

	/**
	 * Convert eye coordinates to world coordinates
	 *
	 * @param eyeCoords Eye coordinates to convert
	 * @return Converted world coordinates
	 */
	private Vector3f toWorldCoords(Vector4f eyeCoords){
		Matrix4f invertedWorldMatrix = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedWorldMatrix, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();
		return mouseRay;
	}

	/**
	 * Get the coordinates of a point on a ray at n distance
	 *
	 * @param ray Ray to find the point on
	 * @param distance Distance of the point on the ray from ray origin
	 * @return Coordinate of the point
	 */
	private Vector3f getPointOnRay(Vector3f ray, float distance) {
		Vector3f camPos = camera.getPosition();
		Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
		return Vector3f.add(start, scaledRay, null);
	}

	/**
	 * Search for the intersection of a ray with the y-plane
	 *
	 * @param count Number of recursions completed so far
	 * @param start Lower bound of the intersection
	 * @param finish Upper bound of the intersection
	 * @param ray Ray to find the intersection of
	 * @return Coordinates of the intersection
	 */
	private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
		float half = start + ((finish - start) / 2f);
		if (count >= RECURSION_COUNT) {
			return getPointOnRay(ray, half);
		}
		if (intersectionInRange(start, half, ray)) {
			return binarySearch(count + 1, start, half, ray);
		} else {
			return binarySearch(count + 1, half, finish, ray);
		}
	}

	/**
	 * Find if there is an intersection of a ray with the y-plane in a range
	 * 
	 * @param start Lower bound of the range
	 * @param finish Upper bound of the range
	 * @param ray Ray to check for intersections
	 * @return True if a intersection was found, otherwise false
	 */
	private boolean intersectionInRange(float start, float finish, Vector3f ray) {
		Vector3f startPoint = getPointOnRay(ray, start);
		Vector3f endPoint = getPointOnRay(ray, finish);
		return !isBellowYPlane(startPoint) && isBellowYPlane(endPoint);
	}

	/**
	 * Checks if a point is below the y-plane
	 * 
	 * @param testPoint point to test
	 * @return True if point is below y = 0, otherwise false
	 */
	private boolean isBellowYPlane(Vector3f testPoint) {
		return testPoint.y < 0;
	}

	/**
	 * Gets the coordinates of the mouse's intersection with the y-plane
	 * @return The mouse's intersection with the y-plane
	 */
	public Vector3f getCurrentYPlanePoint() {
		return currentYPlanePoint;
	}
}
