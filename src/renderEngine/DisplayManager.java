package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

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
			Display.setTitle("3DGameEngine");
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
		// Update the display
		Display.update();
	}

	/**
	 * Close the display
	 */
	public static void closeDisplay() {
		Display.destroy();
	}
}
