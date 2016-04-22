package simulation.entity;

import simulation.graphics.Mesh;
import simulation.math.Vector2f;
import simulation.math.Vector3f;

/**
 * Created by Christopher on 4/19/2016.
 */
public class Entity {

	private final Mesh mesh;

	private final Vector3f position;

	private float scale;

	private float rotation;

	public Entity(Mesh mesh) {
		this.mesh = mesh;
		position = new Vector3f(0.0f, 0.0f, 0.0f);
		scale = 1;
		rotation = 0;
	}

//	public Vector2f getPosition2f() {
//		return position;
//	}

	public Vector3f getPosition3f() {
		return position;
	}

	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public void update(float delta) {

	}
}
