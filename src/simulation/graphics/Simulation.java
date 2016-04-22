package simulation.graphics;

import simulation.input.KeyboardHandler;
import simulation.math.Matrix4f;
import simulation.utils.Clock;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

/**
 * Created by Christopher on 20/04/2016.
 */
public class Simulation extends Display {

	private ShaderProgram shaderProgram;

	private static final float Z_NEAR = 0.01f;
	private static final float Z_FAR = 1000.f;
//    private static final float FOV = (float) Math.toRadians(60);

    private Matrix4f projectionMatrix;

	private Mesh mesh;

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
		// Creates a new ShaderProgram
		shaderProgram = new ShaderProgram("shaders/vertex.vert", "shaders/fragment.frag");

        projectionMatrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * aspectRatio, 10.0f * aspectRatio, Z_NEAR, Z_FAR);
//        projectionMatrix = Matrix4f.perspective(aspectRatio, FOV, Z_NEAR, Z_FAR);
        shaderProgram.createUniform("projectionMatrix");

        // Array of vertices and indices to make a square
        float[] positions = new float[]{
                -0.5f,  10.0f, -1.05f,
                -0.5f, -0.5f, -1.05f,
                0.5f, -0.5f, -1.05f,
                0.5f,  0.5f, -1.05f,
        };
        float[] colours = new float[]{
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
        };
        int[] indices = new int[]{
                0, 1, 3, 3, 1, 2,
        };
        mesh = new Mesh(positions, colours, indices);
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
		update(delta);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		render(mesh);

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

	/**
	 * Updates the simulation.
	 *
	 * @param delta The amount of time since the last update
	 */
	@Override
	protected void update(float delta) {
	}

	/**
	 * Renders the simulation
	 */
	@Override
	protected void render(Mesh mesh) {
		// Clear colour buffer
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// Enable the shader program
		shaderProgram.enable();
        shaderProgram.setUniformMatrix4fv("projectionMatrix", projectionMatrix);

        // Draw the mesh
        glBindVertexArray(mesh.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

		// Restore state
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shaderProgram.disable();
	}

    /**
	 * Cleans up OpenGL objects
	 */
	@Override
	protected void cleanUp() {
		if (shaderProgram != null)
			shaderProgram.cleanUp();

		// Clean up mesh
		mesh.cleanUp();
	}
}
