package fontRendering;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import shaders.ShaderProgram;

public class FontShader extends ShaderProgram{

	private static final String VERTEX_FILE = "/fontRendering/fontVertex.vert";
	private static final String FRAGMENT_FILE = "/fontRendering/fontFragment.frag";

	private int colourLocation;
	private int translationLocation;

	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		colourLocation = super.getUniformLocation("colour");
		translationLocation = super.getUniformLocation("translation");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	protected void loadColour(Vector3f colour) {
		super.loadVector(colourLocation, colour);
	}

	protected void loadTranslation(Vector2f translation) {
		super.load2DVector(translationLocation, translation);
	}
}
