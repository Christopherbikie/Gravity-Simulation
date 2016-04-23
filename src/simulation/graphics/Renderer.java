package simulation.graphics;

import simulation.entity.Entity;
import simulation.math.Matrix4f;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Christopher on 22/04/2016.
 */
public class Renderer {

	private static final float Z_NEAR = 0.01f;
	private static final float Z_FAR = 1000.f;

	private Transformation transformation;

	private Mesh background;

	public Renderer() {
		transformation = new Transformation();
	}

	/**
	 * Initiate the renderer.
	 */
	public void init(Display display) {
		// Array of vertices and indices for the background
		float[] vertices = new float[]{
				-10.0f,  10.0f / display.WIDTH * display.HEIGHT, 10.0f,
				-10.0f, -10.0f / display.WIDTH * display.HEIGHT, 10.0f,
				10.0f, -10.0f / display.WIDTH * display.HEIGHT, 10.0f,
				10.0f,  10.0f / display.WIDTH * display.HEIGHT, 10.0f,
		};
		float[] colours = new float[]{
				1f, 1f, 0.0f,
				1f, 1f, 0.0f,
				1f, 1f, 0.0f,
				1f, 1f, 0.0f,
		};
		int[] indices = new int[]{
				0, 1, 3,
				3, 1, 2,
		};
		background = new Mesh(vertices, colours, indices);

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}

	/**
	 * Render to the display.
	 *
	 * @param display The display to be rendered to
	 * @param entities An array of entities to render
	 */
	public void render(Display display, ArrayList<Entity> entities) {
		// Clear colour buffer
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// If resized, correct viewport
		if (display.resized) {
			glViewport(0, 0, display.WIDTH, display.HEIGHT);
			display.resized = false;
		}

//		float fovx = (float) Math.toRadians(60);
//		float fovy = fovx / display.WIDTH * display.HEIGHT;

		// Update the projection Matrix
		Matrix4f projectionMatrix = transformation.getProjectionMatrix(-10.0f, 10.0f, -10.0f * display.aspectRatio, 10.0f * display.aspectRatio, Z_NEAR, Z_FAR);
//		Matrix4f projectionMatrix = transformation.getProjectionMatrix(fovx, fovy, Z_NEAR, Z_FAR);

		ShaderProgram.bg.enable();
		ShaderProgram.bg.setUniform2f("star", entities.get(0).getPosition2f().x / 10, entities.get(0).getPosition2f().y / 10);
		ShaderProgram.bg.setUniformMatrix4fv("projectionMatrix", projectionMatrix);
		ShaderProgram.bg.setUniform1f("scale", entities.get(0).getScale());
		background.render();
		ShaderProgram.bg.disable();

		// Render each Entity
		for (Entity e : entities) {
			// Set world matrix for this Entity
			Matrix4f worldMatrix = transformation.getWorldMatrix(e.getPosition3f(), e.getRotation(), e.getScale());
			// Render the mesh for this entity
			e.render(projectionMatrix, worldMatrix);
		}
	}

	/**
	 * Clean up
	 */
	public void cleanUp(ArrayList<Entity> entities) {
		ShaderProgram.bg.cleanUp();
		for (Entity e : entities)
			e.cleanUp();
	}
}
