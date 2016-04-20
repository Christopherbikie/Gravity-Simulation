package simulation;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import simulation.graphics.Display;
import simulation.input.KeyboardHandler;

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

	/**
	 * Starts the Display thread
	 */
	private synchronized void start() {
		thread = new Thread(this, "Display");
		thread.start();
		stop();
	}

	/**
	 * Stops the Display thread
	 */
	private synchronized void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Runs the simulation. Called automatically when the Display thread starts.
	 */
	public void run() {
		System.out.println("Version: " + Version.getVersion());

		Display display = new Display(WIDTH, HEIGHT);
		display.run();
	}

	/**
	 * The simulation's entry point.
	 *
	 * @param args not used
	 */
	public static void main(String[] args) {
		Main main = new Main();
		main.start();
	}
}
