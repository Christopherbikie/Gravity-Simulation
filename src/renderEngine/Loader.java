package renderEngine;

import models.RawModel;
import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Created by Christopher on 23/04/2016.
 */
public class Loader {

	/**
	 * List of Vertex Array Objects
	 */
	private List<Integer> vaos = new ArrayList<>();
	/**
	 * List of Vertex Buffer Objects
	 */
	private List<Integer> vbos = new ArrayList<>();
	/**
	 * List of textures
	 */
	private List<Integer> textures = new ArrayList<>();

	/**
	 * Takes an array of positions, texture coordinates, normals and indices and creates a RawModel.
	 *
	 * @param positions Array of vertex coordinates
	 * @param textureCoords Array of texture coordinates
	 * @param normals Array of normal vectors
	 * @param indices Array of indices
	 * @return A RawModel made from the arrays given arrays
	 */
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		// Create a Vertex array object and store its location
		int vaoID = createVAO();
		// Create a Buffer array out of the indices
		bindIndicesBuffer(indices);
		// Add the vao location to the vao array
		vaos.add(vaoID);
		// Store the positions, texture coordinates and normals as a vbo
		storeDataInAtribList(0, 3, positions);
		storeDataInAtribList(1, 2, textureCoords);
		storeDataInAtribList(2, 3, normals);
		// Unbind the vertex array
		unbind();
		return new RawModel(vaoID, indices.length);
	}

	/**
	 * Load a texture from a file.
	 *
	 * @param fileName The path to and name of the file for the texture
	 * @return Location of the loaded texture
	 */
	public int loadTexture(String fileName) {
		// Create a new empty texture
		Texture texture = null;

		// There is currently a glitch in slick-util where it will spam the console with warnings about a an invalid png file.
		// However, the texture sill loads fine.
		// These few lines set the output stream to a new unused one so that these messages are not displayed.
		PrintStream oldOut = System.out;
		PrintStream newOut = new PrintStream(new ByteArrayOutputStream());
		System.setOut(newOut);

		// Load the texture. If the file is not found, print the stack trace.
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + fileName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Use the normal output stream
		System.setOut(oldOut);

		// Store the location of the texture
		int textureID = texture.getTextureID();
		// Add the texture to the texture array
		textures.add(textureID);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		return textureID;
	}

	/**
	 * Create a new VAO
	 *
	 * @return The new VAO
	 */
	private int createVAO() {
		int vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		return vaoID;
	}

	/**
	 * Store data in an attribute list.
	 */
	private void storeDataInAtribList(int attribNumber, int coordinateSize, float[] data) {
		int vboID = glGenBuffers();
		vbos.add(vboID);
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(attribNumber, coordinateSize, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	/**
	 * Create a new buffer out of the indices int array and bind to it.
	 *
	 * @param indices The indices to make the buffer out of
	 */
	public void bindIndicesBuffer(int[] indices) {
		int vboID = glGenBuffers();
		vbos.add(vboID);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
	}

	/**
	 * Store an int array in an int buffer.
	 *
	 * @param data An array of integers
	 * @return The IntBuffer created from the input
	 */
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data).flip();
		return buffer;
	}

	/**
	 * Store a float array in a float buffer.
	 *
	 * @param data An array of floats
	 * @return The FloatBuffer created from the input
	 */
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data).flip();
		return buffer;
	}

	/**
	 * Unbind the vertex array.
	 */
	private void unbind() {
		glBindVertexArray(0);
	}

	/**
	 * Clean up the loader class.
	 * Deletes all VAOs, VBOs, and textures.
	 */
	public void cleanUp() {
		for (int vao : vaos)
			glDeleteVertexArrays(vao);
		for (int vbo : vbos)
			glDeleteBuffers(vbo);
		for (int texture : vbos)
			glDeleteTextures(texture);
	}
}
