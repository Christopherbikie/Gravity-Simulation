package simulation.graphics;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.*;
import simulation.Clock;
import simulation.input.KeyboardHandler;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by Christopher on 4/20/2016.
 */
public abstract class Display {

	/**
	 * The width of the window.
	 */
	int WIDTH;
	/**
	 * The height of the window.
	 */
	int HEIGHT;

	/**
	 * The error callback. Used for processing errors.
	 */
	private GLFWErrorCallback errorCallback;

	/**
	 * The keyboard callback. Used to process keystrokes.
	 */
	private GLFWKeyCallback keyCallback;

	/**
	 * The window size callback callback. Used to process window resizing.
	 */
	private GLFWWindowSizeCallback windowSizeCallback;

	/**
	 * The window handle
	 */
	protected long window;

	private boolean resized = false;

	/**
	 * The time since the last update
	 */
	protected float delta;

	/**
	 * Constructor for the Display class.
	 *
	 * @param width  Width for the display
	 * @param height Height for the display
	 */
	public Display(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
	}

	/**
	 * Runs the display.
	 */
	public void run() {
		try {
			// Setup an error callback. The default implementation
			// will print the error message in System.err.
			glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

			// Initialize GLFW
			if (glfwInit() != GLFW_TRUE)
				throw new IllegalStateException("Unable to initialise GLFW");

			// Configure our window. Using defaults, but sets visible and resizeable to false.
			glfwDefaultWindowHints();
			glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
			glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

			// Create the window
			window = glfwCreateWindow(WIDTH, HEIGHT, "Simulation", NULL, NULL);
			if (window == NULL)
				throw new RuntimeException("Failed to create the GLFW window");

			// Setup a key callback. It will be called every time a key is pressed, repeated or released.
			// For now it just closes when escape is released.
			glfwSetKeyCallback(window, keyCallback = new KeyboardHandler());

			// Get the resolution of the primary monitor
			GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			// Center our window
			glfwSetWindowPos(window, (vidMode.width() - WIDTH) / 2, (vidMode.height() - HEIGHT) / 2);

			// Create glfwWindowSizeCallback
			glfwSetWindowSizeCallback(window, windowSizeCallback = GLFWWindowSizeCallback.create((window, width, height) -> {
				resized = true;
				WIDTH = width;
				HEIGHT = height;
			}));

			// Make the OpenGL context current
			glfwMakeContextCurrent(window);
			// Enable v-sync
			glfwSwapInterval(1);

			// Make the window visible
			glfwShowWindow(window);

			// Creates GLCapabilities instance and makes OpenGL bindings available for use.
			GL.createCapabilities();

			// Start the display
			start();

			// Run the rendering loop until the user has attempted to close the window.
			while (glfwWindowShouldClose(window) == GL_FALSE) {
				// If window has been resized, resize
				if (resized)
					resize();
				// Update and render the display
				tick();
				// Swap the colour buffers
				glfwSwapBuffers(window);
				// Poll for window events. The key callback above will only be
				// invoked during this call.
				glfwPollEvents();
			}

			// Destroy window and window callbacks
			glfwDestroyWindow(window);
			keyCallback.release();
		} finally {
			// Terminate GLFW and release the GLFWErrorCallback
			glfwTerminate();
			errorCallback.release();
		}
	}

	private void resize() {
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		resized = false;
	}

	/**
	 * Method to create a shader program from an array of shader types and shader code.
	 *
	 * @param shaderTypes The types of shaders
	 * @param shaders The code for the shaders
	 * @return An int pointing to the program object on the GPU
	 */
	protected int createShaderProgram(int[] shaderTypes, String[] shaders) {
		// Creates an array of shader IDs
		int[] shaderIDs = new int[shaders.length];
		// Creates a shader for every shader passed to the method
		for (int i = 0; i < shaderIDs.length; i++)
			shaderIDs[i] = createShader(shaderTypes[i], shaders[i]);
		// Calls createShaderProgram with an array of shader IDs as a parameter and returns the program
		return createShaderProgram(shaderIDs);
	}

	/**
	 * Method to create a shader from a given type and string.
	 *
	 * @param shaderType The type of shader
	 * @param shaderString The code for the shader
	 * @return An int pointing to the shader object on the GPU
	 */
	private int createShader(int shaderType, String shaderString) {
		// Creates a shader object in the GPU of the specified type.
		// Creates a pointer as an int.
		int shader = GL20.glCreateShader((shaderType));
		// Uploads the source string to the GPU for the specified shader
		GL20.glShaderSource(shader, shaderString);
		// Compiles the shader
		GL20.glCompileShader(shader);
		// Checks whether the compilation was successful

		int status = GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS);
		// If the compilation was not successful, print the an error message
		if (status == GL_FALSE) {
			String error = GL20.glGetShaderInfoLog(shader);
			String shaderTypeString = null;
			switch (shaderType) {
				case GL20.GL_VERTEX_SHADER:
					shaderTypeString = "vertex";
					break;
				case GL32.GL_GEOMETRY_SHADER:
					shaderTypeString = "geometry";
					break;
				case GL20.GL_FRAGMENT_SHADER:
					shaderTypeString = "fragment";
					break;
			}
			System.err.println("Compile failure in " + shaderTypeString + " shader:\n" + error);
		}
		// Return the created shader
		return shader;
	}

	/**
	 * Method to create a shader program given an array of shaders.
	 *
	 * @param shaders Array of shaders to be made into a program
	 * @return The program made from the shaders
	 */
	private int createShaderProgram(int[] shaders) {
		// Creates a program object in the GPU
		int program = GL20.glCreateProgram();
		// Attaches the shaders to the program so that OpenGL knows what constitutes our program.
		for (int i = 0; i < shaders.length; i++)
			GL20.glAttachShader(program, shaders[i]);
		// Links the shaders we put in the program together so OpenGl knows which compiled
		// shaders to run on each GPU when rendering with this program.
		GL20.glLinkProgram(program);

		// Checks if the shaders are linked together, and if not prints an error.
		int status = GL20.glGetShaderi(program, GL20.GL_LINK_STATUS);
		if (status == GL_FALSE) {
			String error = GL20.glGetProgramInfoLog(program);
			System.err.println("Linker failure: " + error);
		}
		// Detaches and deletes the shaders from the program.
		for (int i = 0; i <shaders.length; i++) {
			GL20.glDetachShader(program, shaders[i]);
			GL20.glDeleteShader(shaders[i]);
		}
		return program;
	}

	/**
	 * Updates the simulation.
	 *
	 * @param delta The amount of time since the last update
	 */
	private void update(float delta) {

	}

	/**
	 * Renders the simulation
	 */
	private void render() {

	}

	/**
	 * Start the Display
	 */
	protected abstract void start();

	/**
	 * Updates and renders
	 */
	protected abstract void tick();
}
