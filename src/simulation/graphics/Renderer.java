package simulation.graphics;

import simulation.entity.Entity;
import simulation.math.Matrix4f;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Christopher on 22/04/2016.
 */
public class Renderer {

	private static final float Z_NEAR = 0.01f;
	private static final float Z_FAR = 1000.f;

	private Transformation transformation;

	private ShaderProgram shaderProgram;

	public Renderer() {
		transformation = new Transformation();
	}

	/**
	 * Initiate the renderer.
	 */
	public void init() {
		// Creates a new ShaderProgram
		shaderProgram = new ShaderProgram("shaders/vertex.vert", "shaders/fragment.frag");

		// Create uniforms for world and projection matrices
		shaderProgram.createUniform("projectionMatrix");
		shaderProgram.createUniform("worldMatrix");

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}

	/**
	 * Render to the display.
	 *
	 * @param display The dislay to be rendered to
	 * @param entities An array of entities to render
	 */
	public void render(Display display, Entity[] entities) {
		// Clear colour buffer
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// If resized, correct viewport
		if (display.resized) {
			glViewport(0, 0, display.WIDTH, display.HEIGHT);
			display.resized = false;
		}

		// Bind the shader program
		shaderProgram.enable();

//		float fovx = (float) Math.toRadians(60);
//		float fovy = fovx / display.WIDTH * display.HEIGHT;

		// Update the projection Matrix
		Matrix4f projectionMatrix = transformation.getProjectionMatrix(-10.0f, 10.0f, -10.0f * display.aspectRatio, 10.0f * display.aspectRatio, Z_NEAR, Z_FAR);
//		Matrix4f projectionMatrix = transformation.getProjectionMatrix(fovx, fovy, Z_NEAR, Z_FAR);
		shaderProgram.setUniformMatrix4fv("projectionMatrix", projectionMatrix);

		// Render each Entity
		for (Entity e : entities) {
			// Set world matrix for this Entity
			Matrix4f worldMatrix = transformation.getWorldMatrix(e.getPosition3f(), e.getRotation(), e.getScale());
			shaderProgram.setUniformMatrix4fv("worldMatrix", worldMatrix);
			// Render the mesh for this entity
			e.getMesh().render();
		}

		// Unbind the shader program
		shaderProgram.disable();
	}

	/**
	 * Clean up
	 */
	public void cleanUp() {
		if (shaderProgram != null)
			shaderProgram.cleanUp();
	}
}
