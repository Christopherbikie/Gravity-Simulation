package input;

/**
 * Created by Christopher on 14/05/2016.
 */
public class Keyboard {

	/**
	 * Array of keys currently held.
	 * True indicates pressed.
	 */
	private static boolean[] keysHeld = new boolean[org.lwjgl.input.Keyboard.KEYBOARD_SIZE];
	/**
	 * Array of
	 */
	private static boolean[] keys = new boolean[org.lwjgl.input.Keyboard.KEYBOARD_SIZE];
	private static boolean[] keysHolding = new boolean[org.lwjgl.input.Keyboard.KEYBOARD_SIZE];

	public static void update() {
		while (org.lwjgl.input.Keyboard.next()) {
			boolean eventKeyState = org.lwjgl.input.Keyboard.getEventKeyState();

			keysHeld[org.lwjgl.input.Keyboard.getEventKey()] = eventKeyState;
		}

		keys = new boolean[org.lwjgl.input.Keyboard.KEYBOARD_SIZE];

		for (int i = 0; i < keys.length; i++) {
			if (keysHeld[i] && !keysHolding[i]) {
				keys[i] = true;
				keysHolding[i] = true;
			}
			if (keysHolding[i] && !keysHeld[i])
				keysHolding[i] = false;
		}
	}

	public static boolean getKeyDown(int keyID) {
		return keysHeld[keyID];
	}

	public static boolean getKeyDownNoRepeats(int keyID) {
		return keys[keyID];
	}
}
