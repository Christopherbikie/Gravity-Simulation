package simulation;

import UI.UI;
import UI.EntityLabeler;
import entities.Camera;
import entities.Entity;
import entities.Light;
import input.MousePicker;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import util.Clock;
import util.XMLReader;

import java.util.List;

/**
 * Created by Christopher on 23/04/2016.
 */
public class Simulation {

	/**
	 * True if we should draw trails, otherwise false
	 */
	public static boolean drawTrails = false;
	/**
	 * Scale of entity sizes.
	 * 1.0 represents a radius of 1 AU
	 */
	public static float scale = 0.1f;

	/**
	 * The program's entry point, this method is executed when the simulation is run.
	 *
	 * @param args Not used
	 */
	public static void main(String[] args) {
		// Create a new display
		DisplayManager.createDisplay();
		// Create a new loader
		Loader loader = new Loader();

		// Create a list to store all entities currently in the simulation
		// Load the entities from an xml file
		List<Entity> entities = XMLReader.loadSystem("/xml/theSolarSystem.xml");

		// Create a light source at the location of the sun
		// Assumes sun is the first object of the array
		Light light = new Light(entities.get(0).getPosition3f(), new Vector3f(1, 1, 0.8f));

		// Create a camera i.e. the point from which we observe the simulation
		Camera camera = new Camera(new Vector3f(0, 5, 0), 90, 0, 0);

		// Create a renderer
		MasterRenderer renderer = new MasterRenderer();

		// Create a UI
		UI ui = new UI(loader, new MousePicker(camera, renderer.getProjectionMatrix()), new EntityLabeler(entities, camera, renderer.getProjectionMatrix(), loader));

		// Loop to update and render all the entities
		while (!Display.isCloseRequested()) {
			float delta = Clock.delta();
			Clock.update();
			getInput(camera, entities);

			// Update all the entities positions and rotations
			for (int i = 0; i < 1000; i++) {
				for (Entity entity : entities) {
					entity.update(delta / 1000, entities);
					Clock.updateUPS();
				}
			}
			// Update the UI
			ui.update(entities);

			// Prepare all the entities for rendering
			entities.forEach(renderer::processEntity);
			// Render the entities
			renderer.render(light, camera);
			// Render the UI
			ui.render();
			Clock.updateFPS();

			// Update the display
			DisplayManager.updateDisplay();
		}

		// Clean up the UI
		ui.cleanUp();
		// Clean up the renderer (detach and delete shaders)
		renderer.cleanUp();
		// Clean up the loader (delete GPU objects)
		loader.cleanUp();
		// Close the display
		DisplayManager.closeDisplay();
	}

	/**
	 * Get user input
	 *
	 * @param camera The camera the user is controlling
	 */
	private static void getInput(Camera camera, List<Entity> entities) {
		input.Keyboard.update();
		// Move the camera
		camera.move(entities);
		// Check if trails should be drawn
		if (input.Keyboard.getKeyDownNoRepeats(Keyboard.KEY_T))
			drawTrails = !drawTrails;

		// Update scale
		if (input.Keyboard.getKeyDown(Keyboard.KEY_PRIOR)) {
			scale *= 1 + Clock.delta();
			for (Entity entity : entities)
				entity.setScale(scale);
		}
		if (input.Keyboard.getKeyDown(Keyboard.KEY_NEXT)) {
			scale *= 1 - Clock.delta();
			for (Entity entity : entities)
				entity.setScale(scale);
		}
	}
}
