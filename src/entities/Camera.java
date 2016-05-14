package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import util.Clock;

/**
 * Created by Christopher on 24/04/2016.
 */
public class Camera {

	/**
	 * Vector for the camera's position
	 */
	private Vector3f position = new Vector3f(0, 0, 0);
	/**
	 * Floats for the camera's rotation  in degrees
	 */
	private float pitch, yaw, roll;

	/**
	 * Constructor for the camera.
	 *
	 * @param position The position for the camera to start at
	 * @param pitch The camera's pitch in degrees
	 * @param yaw The camera's yaw in degrees
	 * @param roll The camera's roll in degrees
	 */
	public Camera(Vector3f position, float pitch, float yaw, float roll) {
		this.position = position;
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
	}

	/**
	 * Move the camera according to user keyboard input
	 */
	public void move() {
		// Get the time since the last frame
		// Do not take the speed of time into account
		float delta = Clock.deltaWithoutMultiplier();
		// Make speed proportional to the distance from the y plane
		float speed = Math.abs(position.y) + 1;
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			position.x -= (float) (Math.sin(-yaw * Math.PI / 180) * speed * delta);
			position.z -= (float) (Math.cos(-yaw * Math.PI / 180) * speed * delta);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.x += (float) (Math.sin(-yaw * Math.PI / 180) * speed * delta);
			position.z += (float) (Math.cos(-yaw * Math.PI / 180) * speed * delta);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.x += (float) (Math.sin((-yaw - 90) * Math.PI / 180) * speed * delta);
			position.z += (float) (Math.cos((-yaw - 90) * Math.PI / 180) * speed * delta);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.x += (float) (Math.sin((-yaw + 90) * Math.PI / 180) * speed * delta);
			position.z += (float) (Math.cos((-yaw + 90) * Math.PI / 180) * speed * delta);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			position.y += speed * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			position.y -= speed * delta;

		if (Mouse.isButtonDown(0)) {
			float angleChange = Mouse.getDX() * 0.3f;
			yaw += angleChange;
		}
		if (Mouse.isButtonDown(0)) {
			float angleChange = Mouse.getDY() * 0.1f;
			pitch -= angleChange;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP))
			pitch -= 60f * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
			pitch += 60f * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
			yaw -= 60f * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
			yaw += 60f * delta;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
}
