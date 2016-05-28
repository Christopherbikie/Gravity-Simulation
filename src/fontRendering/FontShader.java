package fontRendering;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import shaders.ShaderProgram;

public class FontShader extends ShaderProgram {

	/**
	 * Location of the vertex shader
	 */
	private static final String VERTEX_FILE = "/shaders/fontVertex.vert";
	/**
	 * Location of the fragment shader
	 */
	private static final String FRAGMENT_FILE = "/shaders/fontFragment.frag";

	/**
	 * Location of the colour uniform on the GPU
	 */
	private int colourLocation;
	/**
	 * Location of the translation uniform vector on the GPU
	 */
	private int translationLocation;

	/**
	 * Create a new FontShader
	 */
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	/**
	 * Get and store all the locations of the uniforms
	 */
	@Override
	protected void getAllUniformLocations() {
		colourLocation = super.getUniformLocation("colour");
		translationLocation = super.getUniformLocation("translation");
	}

	/**
	 * Bind all of the uniforms
	 */
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	/**
	 * Load the colour of the the font
	 * 
	 * @param colour Colour of the font
	 */
	protected void loadColour(Vector3f colour) {
		super.loadVector(colourLocation, colour);
	}

	/**
	 * Load the translation of the font
	 *
	 * @param translation Translation
	 */
	protected void loadTranslation(Vector2f translation) {
		super.load2DVector(translationLocation, translation);
	}
}
