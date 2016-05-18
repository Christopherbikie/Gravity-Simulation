package UI;

import entities.Entity;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import renderEngine.DisplayManager;
import renderEngine.Loader;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher on 17/05/2016.
 */
public class UI {

	/**
	 * The font to render with
	 */
	private FontType segoeUI;
	/**
	 * List of statistics to render
	 */
	private List<GUIText> stats = new ArrayList<>();
	/**
	 * Number formatterFourDecimals
	 */
	private List<String> strings = new ArrayList<>();
	/**
	 * Formatter for four decimal places
	 */
	private DecimalFormat formatterFourDecimals = new DecimalFormat("0.0000");
	/**
	 * Formatter for two decimal places
	 */
	private DecimalFormat formatterTwoDecimals = new DecimalFormat("0.00");
	/**
	 * Index of the currently selected entity
	 */
	private int entitySelection = 0;

	/**
	 * Constructor to create a UI
	 *
	 * @param loader Loader to use to load font files
	 */
	public UI(Loader loader) {
		segoeUI = new FontType(loader.loadTexture("/fonts/segoe_ui"), "/fonts/segoe_ui");
		TextMaster.init(loader);
	}

	/**
	 * Update statistics
	 *
	 * @param entities List of entities
	 */
	public void update(List<Entity> entities) {
		// Get keyboard input
		if (Keyboard.getKeyDownNoRepeats(org.lwjgl.input.Keyboard.KEY_RETURN))
			entitySelection++;
		if (entitySelection >= entities.size())
			entitySelection = 0;

		// Clear the current statistics
		stats.forEach(GUIText::remove);
		stats.clear();

		// Get the selected entity
		Entity entity = entities.get(entitySelection);

		// Clear and repopulate the array of strings
		strings.clear();
		strings.add("Name: " + entity.getName());
		strings.add("Entity type: " + entity.getType().getName());
		strings.add("Position (AU): " + formatterFourDecimals.format(entity.getPosition2f().x) + ", " + formatterFourDecimals.format(entity.getPosition2f().y));
		strings.add("Velocity (m/s): " + formatterFourDecimals.format(entity.getVelocity().x) + ", " + formatterFourDecimals.format(entity.getVelocity().y));
		strings.add("Mass: " + entity.getMass() + " kg");
		strings.add("Rotation period: " + (entity.getRotationPeriod() == 0 ? "Not rotating" : formatterTwoDecimals.format((float) entity.getRotationPeriod() / 3600)));

		// Create a GUIText for each line of text
		for (int i = 0; i < strings.size(); i++) {
			GUIText statistic =  new GUIText(strings.get(i), 0.8f, segoeUI, new Vector2f(0, (float) i / DisplayManager.HEIGHT * 20), 1f, false);
			statistic.setColour(0.8f, 0.8f, 0.8f);
			stats.add(statistic);
		}
	}

	/**
	 * Render UI
	 */
	public void render() {
		TextMaster.render();
	}

	/**
	 * Clean up UI
	 */
	public void cleanUp() {
		TextMaster.cleanUp();
	}
}
