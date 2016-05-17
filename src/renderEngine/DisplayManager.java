package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import util.Clock;

import java.text.DecimalFormat;

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
			Display.setTitle("Gravity simulation.Simulation");
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
		Display.setTitle("Gravity simulation.Simulation | FPS: " + Clock.getFPS() + " UPS: " + Clock.getUPS() + " | Time: " + getTime() + " | Speed: " + getMultiplier());
		// Update the display
		Display.update();
	}

	/**
	 * Returns the time since the simulation started as a String
	 * Format: #y ###d ##:##:##
	 *
	 * @return the current time since the simulation started
	 */
	private static String getTime() {
		// Create number formats for two and three digits
		DecimalFormat twoDigits = new DecimalFormat("#00");
		DecimalFormat threeDigits = new DecimalFormat("#000");
		// Get the current time
		double time = Clock.getTotalTime();
		// Format and return the time
		return String.valueOf((long) time / 31536000) + "y " +
				threeDigits.format((long) time / 86400 % 365) + "d " +
				twoDigits.format((long) time / 3600 % 24) + ':' +
				twoDigits.format((long) time / 60 % 60) + ':' +
				twoDigits.format((long) (time % 60));
	}

	/**
	 * Get the current multiplier as a formatted String.
	 *
	 * @return the current multiplier
	 */
	private static String getMultiplier() {
		// Get the current multiplier
		float multiplier = Clock.getMultiplier();
		// Create variables for the unit and amount.
		float amount;
		String unit;
		// Get the unit based on the size of the multiplier and adjust the amount accordingly
		// If amount is 1, unit is "second per second" rather than "seconds"
		if (multiplier < 60) {
			amount = multiplier;
			unit = amount == 1 ? " second per second" : " seconds per second";
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
		// Format and return the result
		return new DecimalFormat("#.00").format(amount) + unit;
	}

	/**
	 * Close the display
	 */
	public static void closeDisplay() {
		Display.destroy();
	}
}
