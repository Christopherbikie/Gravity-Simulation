package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import util.Clock;

import static org.lwjgl.opengl.GL11.glViewport;

/**
 * Created by Christopher on 23/04/2016.
 */
public class DisplayManager {

	/**
	 * The screen's width
	 */
	public static final int WIDTH = 1600;
	/**
	 * The screen's height
	 */
	public static final int HEIGHT = 900;
	/**
	 * The maximum frame rate for the display
	 */
	public static final int FPS_CAP = 60;

	/**
	 * Create a new display
	 */
	public static void createDisplay() {
		// Tell OpenGL to use version 3.2 with forward compatibility and using core profile
		ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);

		try {
			// Specify the size of our window
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			// Create the window
			Display.create(new PixelFormat(), attribs);
			// Set the display's title
			Display.setTitle("Gravity Simulation");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		// Set the display's viewport
		glViewport(0, 0, WIDTH, HEIGHT);
	}

	/**
	 * Update the display
	 */
	public static void updateDisplay() {
		// Sync the display to our frame rate
		Display.sync(FPS_CAP);
		// Set the display's title
		Display.setTitle("Gravity Simulation | FPS: " + Clock.getFPS() + " UPS: " + Clock.getUPS() + " | Speed: " + getMultiplier());
		// Update the display
		Display.update();
	}

	private static String getMultiplier() {
		float multiplier = Clock.getMultiplier();
		float amount;
		String unit;
		if (multiplier < 60) {
			amount = multiplier;
			unit = " seconds per second";
		} else if (multiplier > 60 && multiplier < 3600) {
			amount = multiplier / 60;
			unit = " minutes / second";
		} else if (multiplier > 3600 && multiplier < 86400) {
			amount = multiplier / 3600;
			unit = " hours / second";
		} else if (multiplier > 86400 && multiplier < 31536000) {
			amount = multiplier / 86400;
			unit = " days / second";
		} else {
			amount = multiplier / 31536000;
			unit = " years / second";
		}
		return amount + unit;
	}

	/**
	 * Close the display
	 */
	public static void closeDisplay() {
		Display.destroy();
	}
}
