package simulation;

import UI.UI;
import entities.Camera;
import entities.Entity;
import entities.EntityType;
import entities.Light;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import util.Clock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher on 23/04/2016.
 */
public class Simulation {

	public static boolean drawTrails = false;

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
		// Create a UI
		UI ui = new UI(loader);

		// Create a list to store all entities currently in the simulation
		List<Entity> entities = loadEntities();

		// Create a light source at the location of the sun
		// Assumes sun is the first object of the array
		Light light = new Light(entities.get(0).getPosition3f(), new Vector3f(1, 1, 0.8f));

		// Create a camera i.e. the point from which we observe the simulation
		Camera camera = new Camera(new Vector3f(0, 5, 0), 90, 0, 0);

		// Create a renderer
		MasterRenderer renderer = new MasterRenderer();

		// Loop to update and render all the entities
		while (!Display.isCloseRequested()) {
			float delta = Clock.delta();
			Clock.update();
			getInput(camera);
			// Update all the entities positions and rotations
			entities.forEach(entity -> entity.update(delta, entities));
			// Update the UI
			ui.update(entities);
			Clock.updateUPS();

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
	private static void getInput(Camera camera) {
		input.Keyboard.update();
		// Move the camera
		camera.move();
		// Check if trails should be drawn
		if (input.Keyboard.getKeyDownNoRepeats(Keyboard.KEY_T))
			drawTrails = !drawTrails;
	}

	private static List<Entity> loadEntities() {
		// Create a list to store all entities currently in the simulation
		List<Entity> entities = new ArrayList<>();

		// Populate the list with new entities.
		Entity sun = new Entity(EntityType.Star, Entity.sun, new Vector3f(0, 0, 0), new Vector3f(0.1f, 0.1f, 0.1f), new Vector3f(0, 0, 0));
		sun.setMass(1.9885e+30);
		sun.setRotationPeriod(2192832);
		sun.setName("Sun");
		entities.add(sun);

		Entity mercury = new Entity(EntityType.Planet, Entity.mercury, new Vector3f(-0.38709893f, 0, 0), new Vector3f(0.1f, 0.1f, 0.1f), new Vector3f(0, 0, 0));
		mercury.setMass(3.3011e+23);
		mercury.setVelocity(new Vector2f(0, -47872));
		mercury.setRotationPeriod(15201360);
		mercury.setName("Mercury");
		entities.add(mercury);

		Entity venus = new Entity(EntityType.Planet, Entity.venus, new Vector3f(-0.72333199f, 0, 0), new Vector3f(0.1f, 0.1f, 0.1f), new Vector3f(0, 0, 0));
		venus.setMass(4.8675e+24);
		venus.setVelocity(new Vector2f(0, -35020.7f));
		venus.setRotationPeriod(10087200);
		venus.setName("Venus");
		entities.add(venus);

		Entity earth = new Entity(EntityType.Planet, Entity.earth, new Vector3f(-1, 0, 0), new Vector3f(0.1f, 0.1f, 0.1f), new Vector3f(0, 0, 0));
		earth.setMass(5.9723e+24);
		earth.setVelocity(new Vector2f(0, -29781));
		earth.setRotationPeriod(86164);
		earth.setName("Earth");
		entities.add(earth);

		Entity mars = new Entity(EntityType.Planet, Entity.mars, new Vector3f(-1.527f, 0, 0), new Vector3f(0.1f, 0.1f, 0.1f), new Vector3f(0, 0, 0));
		mars.setMass(6.4171e+23);
		mars.setVelocity(new Vector2f(0, -24131.9f));
		mars.setRotationPeriod(88642);
		mars.setName("Mars");
		entities.add(mars);

		Entity jupiter = new Entity(EntityType.Planet, Entity.jupiter, new Vector3f(-5.2043f, 0, 0), new Vector3f(0.1f, 0.1f, 0.1f), new Vector3f(0, 0, 0));
		jupiter.setMass(1.89819e+27);
		jupiter.setVelocity(new Vector2f(0, -13056f));
		jupiter.setRotationPeriod(35730);
		jupiter.setName("Jupiter");
		entities.add(jupiter);

		Entity saturn = new Entity(EntityType.Planet, Entity.saturn, new Vector3f(-9.5824f, 0, 0), new Vector3f(0.1f, 0.1f, 0.1f), new Vector3f(0, 0, 0));
		saturn.setMass(5.6834e+26);
		saturn.setVelocity(new Vector2f(0, -9621.77f));
		saturn.setRotationPeriod(38361);
		saturn.setName("Saturn");
		entities.add(saturn);

		Entity uranus = new Entity(EntityType.Planet, Entity.uranus, new Vector3f(-19.1912f, 0, 0), new Vector3f(0.1f, 0.1f, 0.1f), new Vector3f(0, 0, 0));
		uranus.setMass(8.6813e+25);
		uranus.setVelocity(new Vector2f(0, -6797.22f));
		uranus.setRotationPeriod(62064);
		uranus.setName("Uranus");
		entities.add(uranus);

		Entity neptune = new Entity(EntityType.Planet, Entity.neptune, new Vector3f(-30.069f, 0, 0), new Vector3f(0.1f, 0.1f, 0.1f), new Vector3f(0, 0, 0));
		neptune.setMass(1.02413e+26);
		neptune.setVelocity(new Vector2f(0, -5433.68f));
		neptune.setRotationPeriod(57996);
		neptune.setName("Neptune");
		entities.add(neptune);

		Entity pluto = new Entity(EntityType.Planet, Entity.pluto, new Vector3f(-49.3043f, 0, 0), new Vector3f(0.1f, 0.1f, 0.1f), new Vector3f(0, 0, 0));
		pluto.setMass(1.303e+22);
		pluto.setVelocity(new Vector2f(0, -3676));
		pluto.setRotationPeriod(551815);
		pluto.setName("Pluto");
		entities.add(pluto);

		return entities;
	}
}
