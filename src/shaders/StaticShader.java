package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import maths.Maths;

/**
 * Created by Christopher on 23/04/2016.
 */
public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/shaders/vertex.vert";
	private static final String FRAGMENT_FILE = "src/shaders/fragment.frag";

	private int transformationMatrixLocation;
	private int projectionMatrixLocation;
	private int viewMatrixLocation;
	private int lightPositionLocation;
	private int lightColourLocation;
	private int shineDamperLocation;
	private int reflectivityLocation;
	private int isLightSourceLocation;

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		transformationMatrixLocation = super.getUniformLocation("transformationMatrix");
		projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
		viewMatrixLocation = super.getUniformLocation("viewMatrix");
		lightPositionLocation = super.getUniformLocation("lightPosition");
		lightColourLocation = super.getUniformLocation("lightColour");
		shineDamperLocation = super.getUniformLocation("shineDamper");
		reflectivityLocation = super.getUniformLocation("reflectivity");
		isLightSourceLocation = super.getUniformLocation("isLightSource");
	}

	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(transformationMatrixLocation, matrix);
	}

	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(viewMatrixLocation, viewMatrix);
	}

	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(projectionMatrixLocation, matrix);
	}

	public void loadLight(Light light) {
		super.loadVector(lightPositionLocation, light.getPosition());
		super.loadVector(lightColourLocation, light.getColour());
	}

	public void loadShineVatiables(float damper, float reflectivity) {
		super.loadFloat(shineDamperLocation, damper);
		super.loadFloat(reflectivityLocation, reflectivity);
	}

	public void loadIsLightSource(boolean isLightSource) {
		super.loadBoolean(isLightSourceLocation, isLightSource);
	}
}
