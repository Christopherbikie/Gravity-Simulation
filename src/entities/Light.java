package entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Christopher on 24/04/2016.
 */
public class Light {

	/**
	 * The light's position
	 */
	private Vector3f position;
	/**
	 * The light's colour
	 * Vector values should be red, green then blue.
	 */
	private Vector3f colour;

	public Light(Vector3f position, Vector3f colour) {
		this.position = position;
		this.colour = colour;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColour() {
		return colour;
	}

	public void setColour(Vector3f colour) {
		this.colour = colour;
	}
}
