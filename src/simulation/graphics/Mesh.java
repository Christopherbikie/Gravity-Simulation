package simulation.graphics;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by Christopher on 21/04/2016.
 */
public class Mesh {

	private final int vaoID;
	private final int vboID;
	private final int iboID;
	private final int colourVBOID;
	private final int vertexCount;

	/**
	 * Takes a float array as an input and creates the VBO and VAO objects needed to load that model into the graphics card.
	 *
	 * @param vertices Float array of vertices
	 */
	public Mesh(float[] vertices, float[] colours, int[] indices) {
		// Because each vertex has 3 values, divide by three
		vertexCount = indices.length;

		// Create a VAO and bind to it
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);

		// Create a VBO, bind us to it, put our FloatBuffer data in
		// it, and define the data structure of our data
		vboID = glGenBuffers();
		// Create a FloatBuffer from the float array positions then flip
		FloatBuffer vbo = BufferUtils.createFloatBuffer(vertices.length);
		vbo.put(vertices).flip();
		glBindBuffer(GL_ARRAY_BUFFER, vaoID);
		glBufferData(GL_ARRAY_BUFFER, vbo, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

		// Colour VBO
		colourVBOID = glGenBuffers();
		FloatBuffer colourVBO = BufferUtils.createFloatBuffer(colours.length);
		colourVBO.put(colours).flip();
		glBindBuffer(GL_ARRAY_BUFFER, colourVBOID);
		glBufferData(GL_ARRAY_BUFFER, colourVBO, GL_STATIC_DRAW);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

		// Create an index buffer object
		iboID = glGenBuffers();
		// Create an int buffer, put in indices and flip
		IntBuffer ibo = BufferUtils.createIntBuffer(indices.length);
		ibo.put(indices).flip();
		// Bind to IBO and put out IntBuffer data in it
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, ibo, GL_STATIC_DRAW);

		// Unbind VAO
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	/**
	 * Returns the ID of the VAO.
	 *
	 * @return The ID of the VAO
	 */
	public int getVaoID() {
		return vaoID;
	}

	/**
	 * Returns vertexCount
	 *
	 * @return vertexCount
	 */
	public int getVertexCount() {
		return vertexCount;
	}

	public void render() {
		// Draw the mesh
		glBindVertexArray(getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

		// Restore state
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
	}

	/**
	 * Cleans up OpenGL Objects
	 */
	public void cleanUp() {
		glDisableVertexAttribArray(0);

		// Delete the VBO
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(vboID);
		glDeleteBuffers(colourVBOID);
		glDeleteBuffers(iboID);

		// Delete the VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoID);
	}
}
