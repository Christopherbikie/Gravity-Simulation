package simulation.math;

/**
 * Created by Christopher on 22/04/2016.
 */
public class Vector3f {
	public float x, y, z;

	public Vector3f() {
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3f(Vector2f vector, float z) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = z;
	}
}
