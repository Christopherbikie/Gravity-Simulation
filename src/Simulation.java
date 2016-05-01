import entities.Camera;
import entities.Entity;
import entities.EntityType;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import textures.ModelTexture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher on 23/04/2016.
 */
public class Simulation {

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
		List<Entity> entities = new ArrayList<>();

		// Populate the list with new entities.
		Entity sun = new Entity(EntityType.Star, Entity.sun, new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new Vector3f(0, 0, 0));
		entities.add(sun);
		Entity earth = new Entity(EntityType.Planet, Entity.earth, new Vector3f(6, 0, 0), new Vector3f(1, 1, 1), new Vector3f(0, 0, 0));
		entities.add(earth);
		Entity mars = new Entity(EntityType.Planet, Entity.mars, new Vector3f(0, 0, 6), new Vector3f(1, 1, 1), new Vector3f(0, 0, 0));
		entities.add(mars);

		// Create a light source at the location of the sun
		Light light = new Light(sun.getPosition3f(), new Vector3f(1, 1, 0.8f));

		// Create a camera i.e. the point from which we observe the simulation
		Camera camera = new Camera(new Vector3f(0, 50, 0), 90, 0, 0);

		// Create a renderer
		MasterRenderer renderer = new MasterRenderer();

		// Loop to update and render all the entities
		while (!Display.isCloseRequested()) {
			// Rotate all the entities
			for (Entity entity : entities) {
				entity.increaseRotation(new Vector3f(0, 0.05f, 0));
			}
			// Move the camera according to user input
			camera.move();
			// Prepare all the entities for rendering
			entities.forEach(renderer::processEntity);
			// Render the entities
			renderer.render(light, camera);
			// Update the display
			DisplayManager.updateDisplay();
		}

		// Clean up the renderer (detach and delete shaders)
		renderer.cleanUp();
		// Clean up the loader (delete GPU objects)
		loader.cleanUp();
		// Close the display
		DisplayManager.closeDisplay();
	}
}