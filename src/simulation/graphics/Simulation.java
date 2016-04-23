package simulation.graphics;

import simulation.entity.Entity;
import simulation.entity.Planet;
import simulation.entity.Star;
import simulation.input.KeyboardHandler;
import simulation.utils.Clock;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;

/**
 * Created by Christopher on 20/04/2016.
 */
public class Simulation extends Display {

	private final Renderer renderer;

	private ArrayList<Entity> entities;

	/**
	 * Constructor for the Simulation class.
	 * Calls the Display constructor.
	 *
	 * @param width  Width for the display
	 * @param height Height for the display
	 */
	public Simulation(int width, int height) {
		super(width, height);
		renderer = new Renderer();
	}

	/**
	 * Start the Display
	 */
	@Override
	protected void init() {
		renderer.init(this);

		Entity star = new Star();
		star.setPosition(0, 0);
		star.setScale(1f);
//		Entity planet = new Planet();
//		planet.setPosition(-5, 0);
//		planet.setScale(0.25f);

		entities = new ArrayList<>();
		entities.add(star);
//		entities.add(planet);
	}

	/**
	 * Updates and renders
	 */
	@Override
	protected void tick() {
		// Update clock things
		Clock.update();
		delta = Clock.Delta();
		Clock.updateFPS();
		Clock.updateUPS();

		getInput();
		update(delta);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		render();

		// Sets the window title
		glfwSetWindowTitle(window, "Gravity Simulation | FPS: " + Clock.getFPS() + " UPS: " + Clock.getUPS());
	}

	/**
	 * Gets input for the simulation
	 */
	private void getInput() {
		// If the escape key is pressed, close the simulation.
		if (KeyboardHandler.isKeyDown(GLFW_KEY_ESCAPE))
			glfwSetWindowShouldClose(super.window, GLFW_TRUE);
	}

	/**
	 * Updates the simulation.
	 *
	 * @param delta The amount of time since the last update
	 */
	@Override
	protected void update(float delta) {
		for (Entity e : entities)
			e.update(delta);
	}

	/**
	 * Renders the simulation
	 */
	@Override
	protected void render() {
		renderer.render(this, entities);
	}

    /**
	 * Cleans up OpenGL objects
	 */
	@Override
	protected void cleanUp() {
		renderer.cleanUp(entities);

		// Clean up meshes
		for (Entity e : entities)
			e.getMesh().cleanUp();
	}
}
