package simulation.math;

/**
 * Created by Christopher on 4/19/2016.
 */
public class Vector2f {
	private float x, y;

	/**
	 * Creates an instance of Vector2f with 0 magnitude.
	 */
	public Vector2f() {
		this.x = 0;
		this.y = 0;
	}

	/**
	 * Creates an instance of Vector2f with specified x and y components.
	 *
	 * @param x X component
	 * @param y Y component
	 */
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the unit vector of the current instance.
	 *
	 * @return the unit vector of the current instance
	 */
	public Vector2f getUnitVector() {
		return new Vector2f(x / getLength(), y / getLength());
	}

	/**
	 * Adds two vectors and returns the result.
	 *
	 * @param vector The vector to add to the current instance
	 * @return The result of the operation
	 */
	public Vector2f add(Vector2f vector) {
		return new Vector2f(x + vector.getX(), y + vector.getY());
	}

	/**
	 * Subtracts two vectors and returns the result.
	 *
	 * @param vector The vector to subtract from the current instance
	 * @return The result of the operation
	 */
	public Vector2f sub(Vector2f vector) {
		return new Vector2f(x - vector.getX(), y - vector.getY());
	}

	/**
	 * Multiplies (or scales) the current instance by a given value.
	 *
	 * @param scalar The amount to scale by
	 * @return The result of the operation
	 */
	public Vector2f mul(float scalar) {
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

	/**
	 * Calculates the length of the current vector.
	 *
	 * @return The result of the operation
	 */
	public float getLength() {
		return (float) Math.sqrt(x * x + y * y);
	}

	/**
	 * For debugging. Returns a String representation of the vector.
	 *
	 * @return String representation of the vector
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		Vector2f unitVector = this.getUnitVector();

		result.append(this.getClass().getName()).append(" Object {\n");
		result.append("    X component: ").append(x).append("\n");
		result.append("    Y component: ").append(y).append("\n");
		result.append("    Length: ").append(getLength()).append("\n");
		result.append("    Unit Vector {\n");
		result.append("        X component: ").append(unitVector.getX()).append("\n");
		result.append("        Y component: ").append(unitVector.getY()).append("\n");
		result.append("        Length: ").append(unitVector.getLength()).append("\n    }\n}");

		return result.toString();
	}
}
