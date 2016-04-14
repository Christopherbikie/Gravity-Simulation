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
public class Main implements Runnable{

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;

	private Thread thread;
	public boolean running = false;

	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;

	private long window;

	public void run() {
		System.out.println("Version: " + Version.getVersion());

		try {
			init();
		} finally {

		}
	}

	public void start() {
		running = false;
		thread = new Thread(this, "Simulation");
		thread.start();
	}

	public void init() {
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

		if (glfwInit() != GLFW_TRUE)
			throw new IllegalStateException("Unable to initialise GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

		window = glfwCreateWindow(WIDTH, HEIGHT, "Simulation", NULL, NULL);
	}

	public static void main(String[] args) {
		new Main().start();
	}
}
