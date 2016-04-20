package simulation.graphics;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
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
public class Display {

	// The window width and height
	private int width, height;

	// We need to strongly reference callback instances.
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;

	// The window handle
	private long window;

	/**
	 * Constructor for the Display class.
	 *
	 * @param width  Width for the display
	 * @param height Height for the display
	 */
	public Display(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void run() {
		try {
			// Initialise GLFW, OpenGL and the window
			init();
			// Start loop
			loop();

			// Destroy window and window callbacks
			glfwDestroyWindow(window);
			keyCallback.release();
		} finally {
			// Terminate GLFW and release the GLFWErrorCallback
			glfwTerminate();
			errorCallback.release();
		}
	}

	/**
	 * Initialises GLFW and OpenGL.
	 */
	public void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

		// Initialize GLFW
		if (glfwInit() != GLFW_TRUE)
			throw new IllegalStateException("Unable to initialise GLFW");

		// Configure our window. Using defaults, but sets visible and resizeable to false.
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

		// Create the window
		window = glfwCreateWindow(width, height, "Simulation", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		// For now it just closes when escape is released.
		glfwSetKeyCallback(window, keyCallback = new KeyboardHandler());

		// Get the resolution of the primary monitor
		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window
		glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
	}

	/**
	 * The game loop.
	 */
	private void loop() {
		float delta;

		// Creates GLCapabilities instance and makes OpenGL bindings available for use.
		GL.createCapabilities();

		// Prepare data
		float[] data = new float[]{
				-0.5f, -0.5f,
				-0.5f, 0.5f,
				0.5f, -0.5f,
				0.5f, 0.5f,
				-0.5f, 0.5f,
				0.5f, -0.5f
		};

		// Create a DataBuffer to put the data in. Position is at 0.
		FloatBuffer dataBuffer = BufferUtils.createFloatBuffer(data.length);
		// Put all the data in the buffer, position at the end of the data
		dataBuffer.put(data);
		// Sets the FloatBuffer to read
		dataBuffer.flip();

		// Sets buffer to be a pointer to a new Buffer on the GPU
		int buffer = GL15.glGenBuffers();
		// Binds the buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
		// Binds the buffer
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, dataBuffer, GL15.GL_STATIC_DRAW);
		// Put the data in the binded buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// Prepare the shader
		String vert =
				"#version = 330\n" +
						"in vec2 position;\n" +
						"void main() {\n" +
						"   gl_Position = vec4(position, 0.0f, 1.0f);\n" +
						"}\n";
		String frag =
				"#version 330\n" +
						"out vec4 out_color;\n" +
						"void main() {\n" +
						"   out_color = vec4(0.0f, 1.0f, 1.0f, 1.0f);\n" +
						"}/n";
		// Creates a shader program and keeps a pointer to it as an int
		int shader = createShaderProgram(new int[]{
				GL20.GL_VERTEX_SHADER, GL20.GL_FRAGMENT_SHADER
		}, new String[]{vert, frag});
		// Set the clear color
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while (glfwWindowShouldClose(window) == GL_FALSE) {
			Clock.update();
			delta = Clock.Delta();

			// Clear colour and depth buffer
			GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			// Use the shader Shader program (which was created earlier)
			GL20.glUseProgram(shader);

			// Binds the buffer
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
			// Set attribute's location to 0 and put the data in "position"
			GL20.glBindAttribLocation(shader, 0, "position");
			// Activate the attribute
			GL20.glEnableVertexAttribArray(0);
			// Use attribute binded to 0
			// vec2 position requires two floats
			// The type of data we are using are floats
			// The vertex data is tightly packed (no space in-between them)
			// Our first vertex is at the begining of our data, so the offset is 0
			GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 0, 0);

			// We are drawing triangles
			// Our first vertex is at the begining of our data, so the offset is 0
			// We are drawing 6 points (2 triangles)
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);

			// Clean up
			// Disable attribute at position 0
			GL20.glDisableVertexAttribArray(0);
			// Stop using the program
			GL20.glUseProgram(0);

			// Swap the colour buffers
			glfwSwapBuffers(window);

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();

			// Get input for the simulation
			getInput();

			// Update the simulation
			update(delta);
			Clock.updateFPS();

			// Render the simulation
			render();
			Clock.updateUPS();

			// Sets the winodw title
			glfwSetWindowTitle(window, "Gravity Simulation | FPS: " + Clock.getFPS() + " UPS: " + Clock.getUPS());
		}
	}

	int createShader(int shaderType, String shaderString) {
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
		}
	}

	/**
	 * Gets input for the simulation
	 */
	private void getInput() {
		// If the escape key is pressed, close the simulation.
		if (KeyboardHandler.isKeyDown(GLFW_KEY_ESCAPE))
			glfwSetWindowShouldClose(window, GLFW_TRUE);
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
}
