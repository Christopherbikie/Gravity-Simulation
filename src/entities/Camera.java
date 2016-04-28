package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Christopher on 24/04/2016.
 */
public class Camera {

	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch, yaw, roll;
	private float speed = 0.1f;

	public Camera() {

	}

	public Camera(Vector3f position, float pitch, float yaw, float roll) {
		this.position = position;
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
	}

	public void move() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			position.x -= (float) (Math.sin(-yaw * Math.PI / 180) * speed);
			position.z -= (float) (Math.cos(-yaw * Math.PI / 180) * speed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.x += (float) (Math.sin(-yaw * Math.PI / 180) * speed);
			position.z += (float) (Math.cos(-yaw * Math.PI / 180) * speed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.x += (float) (Math.sin((-yaw - 90) * Math.PI / 180) * speed);
			position.z += (float) (Math.cos((-yaw - 90) * Math.PI / 180) * speed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.x += (float) (Math.sin((-yaw + 90) * Math.PI / 180) * speed);
			position.z += (float) (Math.cos((-yaw + 90) * Math.PI / 180) * speed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			position.y += speed;
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			position.y -= speed;

		if (Keyboard.isKeyDown(Keyboard.KEY_UP))
			pitch -= 1f;
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
			pitch += 1f;
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
			yaw -= 1f;
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
			yaw += 1f;
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
