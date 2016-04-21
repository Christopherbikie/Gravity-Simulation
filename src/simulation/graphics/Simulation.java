package simulation.graphics;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import simulation.Clock;
import simulation.input.KeyboardHandler;
import simulation.utils.FileLoader;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;

/**
 * Created by Christopher on 20/04/2016.
 */
public class Simulation extends Display {

	private int glProgram;
	private int vbo;

	/**
	 * Constructor for the Simulation class.
	 * Calls the Display constructor.
	 *
	 * @param width  Width for the display
	 * @param height Height for the display
	 */
	public Simulation(int width, int height) {
		super(width, height);
	}

	/**
	 * Start the Display
	 */
	@Override
	protected void start() {
		// Create vertex shader source
		String SourceVertex = FileLoader.loadAsString("shaders/vertex.vert");
		// Create frag shader source
		String sourceFragment = FileLoader.loadAsString("shaders/fragment.frag");
		// Create a new shader program using the sources we just made
		glProgram = createShaderProgram(new int[] {
				GL20.GL_VERTEX_SHADER, GL20.GL_FRAGMENT_SHADER
		}, new String[] {
				SourceVertex, sourceFragment
		});
		// Create a DataBuffer to put the data in. Position is at 0.
		FloatBuffer vertices = BufferUtils.createFloatBuffer(16);
		// Put all the data in the buffer, position at the end of the data
		// the sets the FloatBuffer to read
		vertices.put(new float[] {
				0.9f,   -0.9f,  1, 0,
				-0.9f,  -0.9f,  0, 0,
				0.9f,   0.9f,   1, 1,
				-0.9f,  0.9f,   0, 1,
		}).flip();
		// Sets vbo to be a pointer to a new Buffer on the GPU
		vbo = GL15.glGenBuffers();
		// Binds the buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		// Put our data in the buffer we are bound to
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
		// Unbind the buffer by binding to 0
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
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

		// Clear colour buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		// Binds the buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		// Use the glShader program (which was created earlier)
		GL20.glUseProgram(glProgram);

		// Position of the vectors
		int position = GL20.glGetAttribLocation(glProgram, "position");
		// Activate the attribute
		GL20.glEnableVertexAttribArray(position);
		// Use attribute bound to position
		// vec2 position requires two floats
		// The type of data we are using are floats
		// There is vec2 every 4 floats, and each float is 4 bytes, so 4 * 4 bytes between every vertex
		// Our first vertex is at the beginning of our data, so the offset is 0
		GL20.glVertexAttribPointer(position, 2, GL11.GL_FLOAT, false, 4 * 4, 0);

		// Corners of the objects on the screen
		int corner = GL20.glGetAttribLocation(glProgram, "corner");
		// Activate the attribute
		GL20.glEnableVertexAttribArray(corner);
		// See above
		// Data for corner starts at the third float, so offset by 2 * 4 bytes
		GL20.glVertexAttribPointer(corner, 2, GL11.GL_FLOAT, false, 4 * 4, 2 * 4);

		// Zoom constant
		int zoom = GL20.glGetUniformLocation(glProgram, "zoom");
		GL20.glUniform2f(zoom, HEIGHT * 1f / WIDTH, 1f);

		// We are drawing triangles
		// Our first vertex is at the beginning of our data, so the offset is 0
		// We are drawing 6 points (2 triangles)
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		// Disable attribute at position 0
		GL20.glDisableVertexAttribArray(0);

		// Stop using the program
		GL20.glUseProgram(0);

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
}
