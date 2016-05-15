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
	 * Array of keys that were pressed during the most recent update.
	 */
	private static boolean[] keys = new boolean[org.lwjgl.input.Keyboard.KEYBOARD_SIZE];
	/**
	 * Array of keys that are currently held but were not pressed in the most recent update.
	 */
	private static boolean[] keysHolding = new boolean[org.lwjgl.input.Keyboard.KEYBOARD_SIZE];

	/**
	 * Update the array of keys that are currently pressed.
	 */
	public static void update() {
		// Update all the keys states in keysHeld
		while (org.lwjgl.input.Keyboard.next()) {
			boolean eventKeyState = org.lwjgl.input.Keyboard.getEventKeyState();
			keysHeld[org.lwjgl.input.Keyboard.getEventKey()] = eventKeyState;
		}

		// Clear the keys array
		keys = new boolean[org.lwjgl.input.Keyboard.KEYBOARD_SIZE];

		// Update all the keys states in keys
		for (int i = 0; i < keys.length; i++) {
			if (keysHeld[i] && !keysHolding[i]) {
				keys[i] = true;
				keysHolding[i] = true;
			}
			if (keysHolding[i] && !keysHeld[i])
				keysHolding[i] = false;
		}
	}

	/**
	 * Checks if a key is down.
	 * True indicates it is pressed, otherwise false.
	 *
	 * @param keyID Integer of the key being pressed.
	 *              see org.lwjgl.input.Keyboard for what each integers to use for a key.
	 * @return True if key is pressed, otherwise false.
	 */
	public static boolean getKeyDown(int keyID) {
		return keysHeld[keyID];
	}

	/**
	 * Checks if a key is down without repeat presses.
	 * This means this method will only return true after the first update a key is pressed.
	 *
	 * @param keyID Integer of the key being pressed.
	 *              see org.lwjgl.input.Keyboard for what each integers to use for a key.
	 * @return True if key is pressed, otherwise false.
	 */
	public static boolean getKeyDownNoRepeats(int keyID) {
		return keys[keyID];
	}
}
