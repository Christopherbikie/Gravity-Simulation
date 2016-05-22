package UI;

import entities.Camera;
import entities.Entity;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import input.Keyboard;
import maths.Maths;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;

import java.util.List;

/**
 * Created by Christopher on 22/05/2016.
 */
public class EntityLabeler {

	/**
	 * List of entities
	 */
	private List<Entity> entities;
	/**
	 * The projection matrix
	 */
	private Matrix4f projectionMatrix;
	/**
	 * The camera
	 */
	private Camera camera;
	/**
	 * The font to render text in
	 */
	private FontType segoeUI;
	/**
	 * True if labels are to be rendered, otherwise false
	 */
	private boolean showLabels = true;

	/**
	 * Creates a new EntityLabelManager
	 *
	 * @param entities List of entities to make labels for
	 * @param camera The camera we are viewing from
	 * @param projectionMatrix The projection matrix
	 * @param loader Loader to load the font with
	 */
	public EntityLabeler(List<Entity> entities, Camera camera, Matrix4f projectionMatrix, Loader loader) {
		this.entities = entities;
		this.camera = camera;
		this.projectionMatrix = projectionMatrix;
		segoeUI = new FontType(loader.loadTexture("/fonts/segoe_ui"), "/fonts/segoe_ui");
	}

	/**
	 * Update the labels
	 */
	public void update() {
		// Toggle showLabels if key 'L' is pressed
		if (Keyboard.getKeyDownNoRepeats(org.lwjgl.input.Keyboard.KEY_L))
			showLabels = !showLabels;
		// If labels are to be shown, create labels for all entities in the screen
		if (showLabels)
			for (Entity entity : entities) {
				Vector3f screenCoords = Maths.convertToScreenSpace(entity.getPosition3f(), Maths.createViewMatrix(camera), projectionMatrix);
				if (screenCoords == null)
					continue;
				if (screenCoords.x >= 0 && screenCoords.x < Display.getWidth() && screenCoords.y >= 0 && screenCoords.y < Display.getHeight()) {
					GUIText label =  new GUIText(entity.getName(), 1f, segoeUI, new Vector2f(screenCoords.x, screenCoords.y), 1f, false);
					label.setColour(0.8f, 0.8f, 0.8f);
				}
			}
	}
}
