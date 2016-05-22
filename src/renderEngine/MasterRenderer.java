package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Christopher on 24/04/2016.
 */
public class MasterRenderer {

	/**
	 * Field Of View in degrees.
	 */
	private static final float FOV = 70;
	/**
	 * Closest distance to camera to be rendered.
	 */
	private static final float NEAR_PLANE = 0.1f;
	/**
	 * Furthest distance from camera to be rendered.
	 */
	private static final float FAR_PLANE = 1000;
	/**
	 * The projection matrix.
	 */
	private Matrix4f projectionMatrix = createProjectionMatrix();

	/**
	 * Create a StaticShader
	 */
	private StaticShader shader = new StaticShader();
	/**
	 * Create a Renderer
	 */
	private Renderer renderer = new Renderer(shader, projectionMatrix);

	/**
	 * A HashMap containing the models, and the entities using that model.
	 */
	private Map<TexturedModel, List<Entity>> entities = new HashMap<>();

	/**
	 * Renders a object to the screen.
	 *
	 * @param sun The light source.
	 * @param camera The camera from which we are viewing
	 */
	public void render(Light sun, Camera camera) {
		renderer.prepare();
		shader.start();
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		entities.clear();
	}

	/**
	 * Adds an entity to the entities HashMap.
	 * If an entity using the same model already exists, add the entity to that models list of entitys.
	 * Otherwise, add the model and the entity as a new entry.
	 *
	 * @param entity The entity to add to the array.
	 */
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null)
			batch.add(entity);
		else {
			List<Entity> newBatch = new ArrayList<>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}

	/**
	 * Creates a perspective projection matrix.
	 */
	private Matrix4f createProjectionMatrix() {
		Matrix4f result = new Matrix4f();
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV/2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		result.m00 = x_scale;
		result.m11 = y_scale;
		result.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		result.m23 = -1;
		result.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		result.m33 = 0;
		return result;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	/**
	 * Clean up the shaders.
	 */
	public void cleanUp() {
		shader.cleanUp();
	}
}
