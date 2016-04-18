package simulation;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Created by Christopher on 4/14/2016.
 */
public class Main implements Runnable {

	// Window height and width
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;

	// Create thread
	private Thread thread;

	// We need to strongly reference callback instances.
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;

	// The window handle
	private long window;

	// Starts the Display thread
	private synchronized void start() {
		thread = new Thread(this, "Display");
		thread.start();
		stop();
	}

	//Stops the Display thread
	private synchronized void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Runs the simulation
	public void run() {
		System.out.println("Version: " + Version.getVersion());

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

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

		// Initialize GLFW
		if (glfwInit() != GLFW_TRUE)
			throw new IllegalStateException("Unable to initialise GLFW");

		// Configure our window
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

		// Create the window
		window = glfwCreateWindow(WIDTH, HEIGHT, "Simulation", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		// For now it just closes when escape is released.
		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
					glfwSetWindowShouldClose(window, GLFW_TRUE);
			}
		});

		// Get the resolution of the primary monitor
		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window
		glfwSetWindowPos(window, (vidMode.width() - WIDTH) / 2, (vidMode.height() - HEIGHT) / 2);

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
	}

	private void loop() {
		float delta;

		// Creates GLCapabilities instance and makes OpenGL bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while (glfwWindowShouldClose(window) == GL_FALSE) {
			Clock.update();
			delta = Clock.Delta();

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			// Swap the colour buffers
			glfwSwapBuffers(window);

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();

			update(delta);
			Clock.updateFPS();

			render();
			Clock.updateUPS();

			glfwSetWindowTitle(window, "Gravity Simulation | FPS: " + Clock.getFPS() + " UPS: " + Clock.getUPS());
		}
	}

	private void update(float delta) {
	}

	private void render() {
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.start();
	}
}
