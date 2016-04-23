package simulation.graphics;

import simulation.math.Matrix4f;
import simulation.math.Vector2f;
import simulation.utils.FileLoader;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Christopher on 21/04/2016.
 */
public class ShaderProgram {

	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;

	public static ShaderProgram bg = new ShaderProgram("shaders/bg.vert", "shaders/bg.frag");
	public static ShaderProgram star = new ShaderProgram("shaders/star.vert", "shaders/star.frag");
	public static ShaderProgram planet = new ShaderProgram("shaders/planet.vert", "shaders/planet.frag");

	static {
		// Create uniforms for world and projection matrices
		bg.createUniform("star");
		bg.createUniform("projectionMatrix");
		bg.createUniform("scale");
		star.createUniform("projectionMatrix");
		star.createUniform("worldMatrix");
		planet.createUniform("projectionMatrix");
		planet.createUniform("worldMatrix");
	}

	private final Map<String, Integer> uniforms;

	/**
	 * Creates a new shader program.
	 *
	 * @param vertexPath Path to the vertex shader
	 * @param fragmentPath Path to the fragment shader
	 */
	public ShaderProgram(String vertexPath, String fragmentPath) {
        uniforms = new HashMap<>();
		// Create a new shader program
		programID = glCreateProgram();

		// Load the vertex and fragment shader source code, then give OpenGL the codes
		String vertexSource = FileLoader.loadAsString(vertexPath);
		vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
		String fragmentSource = FileLoader.loadAsString(fragmentPath);
		fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);

		// Gives OpenGL the sources for the shaders
		glShaderSource(vertexShaderID, vertexSource);
		glShaderSource(fragmentShaderID, fragmentSource);

		// Compiles the vertex shader and prints and error if it fails
		glCompileShader(vertexShaderID);
		if (glGetShaderi(vertexShaderID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Failed to compile vertex shader!");
			System.err.println(glGetShaderInfoLog(vertexShaderID));
		}

		// Compiles the fragment shader and prints and error if it fails
		glCompileShader(fragmentShaderID);
		if (glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Failed to compile fragment shader!");
			System.err.println(glGetShaderInfoLog(fragmentShaderID));
		}

		// Attach both shaders to the program
		glAttachShader(programID, vertexShaderID);
		glAttachShader(programID, fragmentShaderID);
		// Links the shaders in the program together so OpenGL knows to run the together
		glLinkProgram(programID);
		glValidateProgram(programID);

		// Deletes the shaders
		glDeleteShader(fragmentShaderID);
		glDeleteShader(vertexShaderID);
	}

    /**
     * Creates a new uniform variable.
     *
     * @param uniformName The name for the new variable
     */
    public void createUniform(String uniformName) {
        int uniformLocation = glGetUniformLocation(programID, uniformName);
        if (uniformLocation < 0)
            System.err.println("Could not find uniform \"" + uniformName + "\"");
        else
            uniforms.put(uniformName, uniformLocation);
    }

    /**
     * Sets the uniform variable to a Matrix4f.
     *
     * @param uniformName The name of the uniform variable
     * @param matrix The matrix to set the variable to
     */
    public void setUniformMatrix4fv(String uniformName, Matrix4f matrix) {
        glUniformMatrix4fv(uniforms.get(uniformName), false, matrix.toFloatBuffer());
    }

	/**
	 * Sets a uniform variable to a Vector2f.
	 *
	 * @param uniformName The name of the uniform variable
	 * @param vector The vector to set the variable to
	 */
	public void setUniform2f(String uniformName, Vector2f vector) {
		glUniform2f(uniforms.get(uniformName), vector.x, vector.y);
	}

	/**
	 * Sets a uniform variable to a Vector2f.
	 *
	 * @param uniformName The name of the uniform variable
	 * @param x The x value
	 * @param y The y value
	 */
	public void setUniform2f(String uniformName, float x, float y) {
		glUniform2f(uniforms.get(uniformName), x, y);
	}

	/**
	 * Sets a uniform variable to a float.
	 *
	 * @param uniformName The name of the uniform variable
	 * @param f The float to set the variable to
	 */
	public void setUniform1f(String uniformName, float f) {
		glUniform1f(uniforms.get(uniformName), f);
	}

	/**
	 * Enable this shader
	 */
	public void enable() {
		glUseProgram(programID);
	}

	/**
	 * Disable this shader
	 */
	public void disable() {
		glUseProgram(0);
	}

	/**
	 * Disable and delete this shader
	 */
	public void cleanUp() {
		disable();
		if (programID != 0) {
			if (vertexShaderID != 0)
				glDetachShader(programID, vertexShaderID);
			if (fragmentShaderID != 0)
				glDetachShader(programID, fragmentShaderID);
			glDeleteProgram(programID);
		}
	}
}
