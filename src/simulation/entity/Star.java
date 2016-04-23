package simulation.entity;

import simulation.graphics.Mesh;
import simulation.graphics.ShaderProgram;

/**
 * Created by Christopher on 22/04/2016.
 */
public class Star extends Entity {

	public Star() {
		super();

		// Array of vertices and indices to make a square
		float[] vertices = new float[]{
				-1.0f,  1.0f,  1.0f,
				-1.0f, -1.0f,  1.0f,
				1.0f, -1.0f,  1.0f,
				1.0f,  1.0f,  1.0f,
		};
		float[] colours = new float[]{
				1f, 1f, 0.0f,
				1f, 1f, 0.0f,
				1f, 1f, 0.0f,
				1f, 1f, 0.0f,
		};
		int[] indices = new int[]{
				0, 1, 3, 3, 1, 2,
		};
		mesh = new Mesh(vertices, colours, indices);
		shaderProgram = ShaderProgram.star;
	}

	/**
	 * Updates the entity.
	 *
	 * @param delta Time since the last update
	 */
	@Override
	public void update(float delta) {

	}

	/**
	 * Clean's up the entity's shaders
	 */
	@Override
	public void cleanUp() {

	}
}
