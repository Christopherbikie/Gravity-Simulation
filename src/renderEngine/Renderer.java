package renderEngine;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import simulation.Simulation;
import textures.ModelTexture;
import maths.Maths;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

/**
 * Created by Christopher on 23/04/2016.
 */
public class Renderer {

	/**
	 * The shader
	 */
	private StaticShader shader;

	/**
	 * Constructor for the renderer.
	 *
	 * @param shader The shader to use
	 * @param projectionMatrix Projection matrix to use
	 */
	public Renderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	/**
	 * Prepare for rendering.
	 * Enables depth test, clears colour and depth buffers and resets the clear colour.
	 */
	public void prepare() {
		glEnable(GL_DEPTH_TEST);
		glClearColor(0, 0, 0, 0.05f);
		if (!Simulation.drawTrails)
			glClear(GL_COLOR_BUFFER_BIT);
		glClear(GL_DEPTH_BUFFER_BIT);
	}

	/**
	 * Prepares then renders a HashMap of Textured models and Entities.
	 *
	 * @param entities HashMap of entities to be rendered.
	 *                 Entities should be grouped by TexturedModel.
	 */
	public void render(Map<TexturedModel, List<Entity>> entities) {
		for (TexturedModel model : entities.keySet()) {
			prepareTextureModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				prepareInstance(entity);
				glDrawElements(GL_TRIANGLES, model.getRawModel().getVertexCount(), GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}

	/**
	 * Prepares a TexturedModel for rendering.
	 *
	 * @param model The TexturedModel to prepare.
	 */
	private void prepareTextureModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		glBindVertexArray(rawModel.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		ModelTexture texture = model.getTexture();
		shader.loadShineVatiables(texture.getShineDamper(), texture.getReflectivity());
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, model.getTexture().getTextureID());
	}

	/**
	 * Disables all vertex attribute arrays and unbinds the vertex array.
	 */
	private void unbindTexturedModel() {
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
	}

	/**
	 * Prepares an entity for rendering.
	 *
	 * @param entity The Entity to prepare.
	 */
	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition3f(),
				entity.getRotation().x, entity.getRotation().y, entity.getRotation().z, entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadIsLightSource(entity.getType().isLightSource());
	}
}
