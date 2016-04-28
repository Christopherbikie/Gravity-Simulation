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

	public static final int WIDTH = 1600;
	public static final int HEIGHT = 900;
	public static final int FPS_CAP = 120;

	public static void createDisplay() {
		ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);

		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("3DGameEngine");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		glViewport(0, 0, WIDTH, HEIGHT);
	}

	public static void updateDisplay() {
		Display.sync(FPS_CAP);
		Display.update();
	}

	public static void closeDisplay() {
		Display.destroy();
	}
}
