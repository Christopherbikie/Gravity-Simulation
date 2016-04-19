package simulation.math;

/**
 * Created by Christopher on 4/19/2016.
 */
public class Vector2f {
	private float x, y;

	public Vector2f() {
		this.x = 0;
		this.y = 0;
	}

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2f getUnitVector() {
		return new Vector2f(x / getLength(), y / getLength());
	}

	public Vector2f add(Vector2f vector) {
		return new Vector2f(x + vector.getX(), y + vector.getY());
	}

	public Vector2f dot(float scalar) {
		Vector2f result = new Vector2f();
		result.setX(x * scalar);
		result.setY(y * scalar);
		return result;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	private void setX(float x) {
		this.x = x;
	}

	private void setY(float y) {
		this.y = y;
	}

	public void set(Vector2f vector) {
		this.x = vector.getX();
		this.y = vector.getY();
	}

	public float getLength() {
		return (float) Math.sqrt(x * x + y * y);
	}
}
