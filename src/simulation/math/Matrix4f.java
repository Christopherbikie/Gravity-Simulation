package simulation.math;

import simulation.utils.BufferUtils;

import java.nio.FloatBuffer;

/**
 * Created by Christopher on 4/22/2016.
 */
public class Matrix4f {
    /**
     * Size of the matrix
     */
    public static final int SIZE = 4 * 4;

    /**
     * Array of elements of size SIZE
     */
    public float[] elements = new float[SIZE];

    /**
     * Creates a new empty matrix
     */
    public Matrix4f() {
        for (int i = 0; i < SIZE; i++)
            elements[i] = 0.0f;
    }

    /**
     * Creates the identity matrix
     *
     * @return the identity matrix
     */
    public static Matrix4f identity() {
        Matrix4f result = new Matrix4f();
        result.elements[0 + 0 * 4] = 1.0f;
        result.elements[1 + 1 * 4] = 1.0f;
        result.elements[2 + 2 * 4] = 1.0f;
        result.elements[3 + 3 * 4] = 1.0f;

        return result;
    }

    /**
     * Creates the orthographic projection matrix
     *
     * @param left Screen left bound
     * @param right Screen right bound
     * @param bottom Screen bottom bound
     * @param top Screen top bound
     * @param near Screen near bound
     * @param far Screen far bound
     * @return The orthographic projection matrix
     */
    public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
        Matrix4f result = identity();

	    result.elements[0 + 0 * 4] = 2.0f / (right - left);
	    result.elements[1 + 1 * 4] = 2.0f / (top - bottom);
	    result.elements[2 + 2 * 4] = -2.0f / (far - near);
	    result.elements[0 + 3 * 4] = (left + right) / (left - right);
	    result.elements[1 + 3 * 4] = (bottom + top) / (bottom - top);
	    result.elements[2 + 3 * 4] = (far + near) / (far - near);

        return result;
    }

    /**
     * Creates a new perspective projection matrix.
     *
     * @param fovx The screen's Field Of View on the x axis
     * @param fovy The screen's Field Of View on the y axis
     * @param near The screen's near bound
     * @param far The screen's far bound
     * @return The perspective matrix
     */
    public static Matrix4f perspective(float fovx, float fovy, float near, float far) {
        Matrix4f result = new Matrix4f();

        float zp = (far + near);
        float zm = (far - near);


        result.elements[0 + 0 * 4] = (float) (1.0f / (Math.atan(fovx / 2.0f)));
        result.elements[1 + 1 * 4] = (float) (1.0f / Math.atan(fovy / 2.0f));
        result.elements[2 + 2 * 4] = -zp / zm;
        result.elements[3 + 2 * 4] = -1.0f;
        result.elements[2 + 3 * 4] = -(2.0f * far * near) / zm;

        return result;
    }

	/**
	 * Creates a translation matrix.
	 *
	 * @param vector Vector to translate by
	 * @return The translation matrix
	 */
    public static Matrix4f translate(Vector3f vector) {
	    Matrix4f result = identity();
        result.elements[0 + 3 * 4] = vector.x;
        result.elements[1 + 3 * 4] = vector.y;
        result.elements[2 + 3 * 4] = vector.z;
	    return result;
    }

	/**
	 * Creates a rotation matrix.
	 * Rotates around x axis
	 *
	 * @param angle Angle in degrees to rotate by
	 * @return The rotation matrix
	 */
    public static Matrix4f rotate(float angle) {
	    Matrix4f result = identity();
        float r = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(r);
        float sin = (float) Math.sin(r);

	    result.elements[0 + 0 * 4] = cos;
	    result.elements[1 + 0 * 4] = sin;
	    result.elements[0 + 1 * 4] = -sin;
	    result.elements[1 + 1 * 4] = cos;
	    return result;
    }

	/**
	 * Creates a scale matrix.
	 *
	 * @param amount Amount to scale by
	 * @return The scale matrix
	 */
	public static Matrix4f scale(float amount) {
		Matrix4f result = identity();
		result.elements[0 + 0 * 4] = amount;
		result.elements[1 + 1 * 4] = amount;
		result.elements[2 + 2 * 4] = amount;
		return result;
	}

	/**
	 * Multiply this matrix with another one.
	 *
	 * @param matrix The matrix to multiply with
	 * @return The result of the multiplication
	 */
	public Matrix4f multiply(Matrix4f matrix) {
		Matrix4f result = new Matrix4f();
		for (int y = 0; y < 4; y++)
			for (int  x = 0; x < 4; x++) {
				float sum = 0.0f;
				for (int e = 0; e < 4; e++)
					sum += this.elements[x + e * 4] * matrix.elements[e + y * 4];
				result.elements[x + y * 4] = sum;
			}
		return result;
	}

	/**
	 * Creates a FloatBuffer out of this instance of Matrix4f
	 *
	 * @return FloatBuffer of this Matrix4f
	 */
    public FloatBuffer toFloatBuffer() {
        return BufferUtils.createFloatBuffer(elements);
    }
}
