package simulation.graphics;

import simulation.utils.FileLoader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Christopher on 21/04/2016.
 */
public class ShaderProgram {

	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;

	/**
	 * Creates a new shader program.
	 *
	 * @param vertexPath Path to the vertex shader
	 * @param fragmentPath Path to the fragment shader
	 */
	public ShaderProgram(String vertexPath, String fragmentPath) {
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
