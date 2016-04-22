package simulation.graphics;

import simulation.entity.Entity;
import simulation.entity.Test;
import simulation.input.KeyboardHandler;
import simulation.math.Matrix4f;
import simulation.utils.Clock;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

/**
 * Created by Christopher on 20/04/2016.
 */
public class Simulation extends Display {

	private final Renderer renderer;

	private Entity[] entities;

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
		renderer.init();

		// Array of vertices and indices to make a square
		float[] vertices = new float[]{
				-0.5f,  0.5f,  0.5f,
				-0.5f, -0.5f,  0.5f,
				0.5f, -0.5f,  0.5f,
				0.5f,  0.5f,  0.5f,
		};
		float[] colours = new float[]{
				0.5f, 0.0f, 0.0f,
				0.0f, 0.5f, 0.0f,
				0.0f, 0.0f, 0.5f,
				0.0f, 0.5f, 0.5f,
		};
		int[] indices = new int[]{
				0, 1, 3, 3, 1, 2,
		};

		// Create the Mesh
		Mesh mesh = new Mesh(vertices, colours, indices);
		Entity entity = new Entity(mesh);
		entity.setPosition(0, 0, 2);
		entity.setScale(10);
		entity.setRotation(30);
		entities = new Entity[] {entity};
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
		renderer.cleanUp();

		// Clean up meshes
		for (Entity e : entities)
			e.getMesh().cleanUp();
	}
}
