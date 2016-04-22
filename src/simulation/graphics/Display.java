package simulation.graphics;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import simulation.input.KeyboardHandler;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by Christopher on 4/20/2016.
 */
public abstract class Display {

	/**
	 * The width of the window.
	 */
	protected int WIDTH;
	/**
	 * The height of the window.
	 */
	protected int HEIGHT;

    /**
     * The window's aspect ratio.
     */
	protected float aspectRatio;

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
        this.aspectRatio = (float) HEIGHT / WIDTH;
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
			cleanUp();
			// Terminate GLFW and release the GLFWErrorCallback
			glfwTerminate();
			errorCallback.release();
		}
	}

	private void resize() {
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
        aspectRatio = (float) HEIGHT / WIDTH;
		resized = false;
	}

	/**
	 * Updates the simulation.
	 *
	 * @param delta The amount of time since the last update
	 */
	protected abstract void update(float delta);

	/**
	 * Renders the simulation
	 */
	protected abstract void render(Mesh mesh);

	/**
	 * Start the Display
	 */
	protected abstract void start();

	/**
	 * Updates and renders
	 */
	protected abstract void tick();

	/**
	 * Cleans up OpenGL objects
	 */
	protected abstract void cleanUp();
}
