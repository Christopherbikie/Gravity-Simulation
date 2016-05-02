package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
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
	 * Create a StaticShader
	 */
	private StaticShader shader = new StaticShader();
	/**
	 * Create a Renderer
	 */
	private Renderer renderer = new Renderer(shader);

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
	 * Clean up the shaders.
	 */
	public void cleanUp() {
		shader.cleanUp();
	}
}
