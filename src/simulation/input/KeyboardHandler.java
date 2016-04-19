package simulation.input;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/**
 * Created by Christopher on 4/19/2016.
 */
public class KeyboardHandler extends GLFWKeyCallback {

	/**
	 * A boolean array to store the which keys are pressed.
	 */
	private static boolean[] keys = new boolean[65536];

	/**
	 * Called by GLFW when a key is pressed.
	 *
	 * @param window The window that recieved the event
	 * @param key The keyboard key that was pressed or released
	 * @param scancode The system-specific scancode of the key
	 * @param action <code>GLFW_PRESS</code>, <code>GLFW_RELEASE</code> or <code>GLFW_REPEAT</code>.
	 * @param mods Bit field describing which modifier keys were held down
	 */
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		keys[key] = action == GLFW_RELEASE;
	}

	/**
	 * Boolean method that returns true if a given key is pressed.
	 *
	 * @param keyCode The key to check if pressed
	 * @return
	 */
	public static boolean isKeyDown(int keyCode) {
		return keys[keyCode];
	}
}
