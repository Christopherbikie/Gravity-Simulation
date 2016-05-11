package util;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;

public class Clock {
	private static boolean paused = false;
	private static long lastFrame;
	private static float d = 0, multiplier = 1, timeCount = 0;
	private static double totalTime = 0;
	private static int fps, fpsCount, ups, upsCount;

	/**
	 * Returns time since <code>glfwInit()</code> in milliseconds.
	 *
	 * @return time since <code>glfwInit()</code> in milliseconds
	 */
	public static long getTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}

	/**
	 * Used to calculate the delta, without taking the multiplier or pause state into account.
	 *
	 * @return delta in milliseconds
	 */
	private static float getDelta() {
		long currentTime = getTime();
		int delta = (int) (currentTime - lastFrame);
		lastFrame = getTime();
		if (delta * 0.001f > 0.05f)
			return 0.05f;
		return delta * 0.001f;
	}

	/**
	 * Returns the time since the last update, times the multiplier.
	 *
	 * @return delta in milliseconds
	 */
	public static float delta() {
		if (paused)
			return 0;
		else
			return d * multiplier;
	}

	/**
	 * Returns the time since the last update, without the multiplier.
	 *
	 * @return delta in milliseconds
	 */
	public static float deltaWithoutMultiplier() {
		if (paused)
			return 0;
		else
			return d;
	}

	/**
	 * Returns the current simulation speed multiplier.
	 *
	 * @return current multiplier
	 */
	public static float getMultiplier() {
		return multiplier;
	}

	/**
	 * Updates the clock. Must be run every loop.
	 */
	public static void update() {
		if (Keyboard.isKeyDown(Keyboard.KEY_PERIOD))
			changeMultiplier(1.1f);
		if (Keyboard.isKeyDown(Keyboard.KEY_COMMA))
			changeMultiplier(0.9f);
		if (Keyboard.isKeyDown(Keyboard.KEY_P) && Keyboard.getEventKeyState())
			pause();

		d = getDelta();
		timeCount += d;
		totalTime += d * multiplier;

		if (timeCount > 1) {
			fps = fpsCount;
			fpsCount = 0;

			ups = upsCount;
			upsCount = 0;

			timeCount -= 1f;
		}
		updateFPS();
		updateUPS();
	}

	/**
	 * Changes the simulation speed by a specified amount.
	 *
	 * @param change Amount to change the speed by.
	 */
	public static void changeMultiplier(float change) {
		multiplier *= change;
	}

	/**
	 * Changes the simulation speed to a specified amount.
	 *
	 * @param value Value to set the speed to.
	 */
	public static void setMultiplier(float value) {
		multiplier *= value;
	}

	/**
	 * Pauses the Clock, makes <code>delta()</code> return 0.
	 */
	public static void pause() {
		paused = !paused;
	}

	/**
	 * Updates the FPS counter.
	 */
	public static void updateFPS() {
		fpsCount++;
	}

	/**
	 * Updates the UPS counter.
	 */
	public static void updateUPS() {
		upsCount++;
	}

	/**
	 * Getter for the FPS.
	 *
	 * @return Frames per second
	 */
	public static int getFPS() {
		return fps > 0 ? fps : fpsCount;
	}

	/**
	 * Getter for the UPS.
	 *
	 * @return Updates per second
	 */
	public static int getUPS() {
		return ups > 0 ? ups : upsCount;
	}

	/**
	 * Get the time since the simulation started.
	 *
	 * @return Time since the simulation started
	 */
	public static double getTotalTime() {
		return totalTime;
	}
}