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
        result.elements[0 + 3 * 4] = -((right + left) / (right - left));
        result.elements[1 + 3 * 4] = -((top + bottom) / (top - bottom));
        result.elements[2 + 3 * 4] = -((far + near) / (far - near));

        return result;
    }

    /**
     * Creates a new perspectie projection matrix.
     *
     * @param a The screen's aspect ratio
     * @param fov The screen's Field Of View
     * @param near The screen's near bound
     * @param far The screen's far bound
     * @return The perspective matrix
     */
    public static Matrix4f perspective(float a, float fov, float near, float far) {
        Matrix4f result = new Matrix4f();

        float zp = (far + near);
        float zm = (far - near);


        result.elements[0 + 0 * 4] = (float) (1.0f / (Math.tan(fov / 2.0f))) / a;
        result.elements[1 + 1 * 4] = (float) (1.0f / Math.tan(fov / 2.0f));
        result.elements[2 + 2 * 4] = -zp / zm;
        result.elements[3 + 2 * 4] = -1.0f;
        result.elements[2 + 3 * 4] = -(2.0f * far * near) / zm;

        return result;
    }

    public FloatBuffer toFloatBuffer() {
        return BufferUtils.createFloatBuffer(elements);
    }
}
