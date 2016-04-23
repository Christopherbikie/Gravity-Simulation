package simulation.entity;

import simulation.graphics.Mesh;
import simulation.graphics.ShaderProgram;
import simulation.math.Matrix4f;
import simulation.math.Vector2f;
import simulation.math.Vector3f;

/**
 * Created by Christopher on 4/19/2016.
 */
public abstract class Entity {

	protected Mesh mesh;

	protected ShaderProgram shaderProgram;

	private final Vector2f position;

	private float scale;

	private float rotation;

	public Entity() {
		position = new Vector2f(0.0f, 0.0f);
		scale = 1;
		rotation = 0;
	}

	public Vector2f getPosition2f() {
		return position;
	}

	public Vector3f getPosition3f() {
		return new Vector3f(position.x, position.y, 2);
	}

	public void setPosition(float x, float y) {
		this.position.x = x;
		this.position.y = y;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public Mesh getMesh() {
		return mesh;
	}

	/**
	 * Updates the entity.
	 *
	 * @param delta Time since the last update
	 */
	public abstract void update(float delta);

	/**
	 * Renders the entity
	 */
	public void render(Matrix4f projectionMatrix, Matrix4f worldMatrix) {
		// Bind the shader program
		shaderProgram.enable();

		shaderProgram.setUniformMatrix4fv("projectionMatrix", projectionMatrix);
		shaderProgram.setUniformMatrix4fv("worldMatrix", worldMatrix);

		mesh.render();

		// Unbind the shader program
		shaderProgram.disable();
	}

	/**
	 * Clean's up the entity's shaders
	 */
	public abstract void cleanUp();
}
