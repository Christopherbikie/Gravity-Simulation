package UI;

import com.sun.javafx.binding.StringFormatter;
import entities.Entity;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import org.lwjgl.util.vector.Vector2f;
import renderEngine.DisplayManager;
import renderEngine.Loader;

import java.io.File;
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
	private List<GUIText> stats;
	/**
	 * Number formatter
	 */
	private DecimalFormat formatter = new DecimalFormat("0.0000");

	/**
	 * Constructor to create a UI
	 *
	 * @param loader Loader to use to load font files
	 */
	public UI(Loader loader) {
		segoeUI = new FontType(loader.loadTexture("/fonts/segoe_ui"), new File("res/fonts/segoe_ui.fnt"));
		TextMaster.init(loader);
	}

	/**
	 * Update statistics
	 *
	 * @param entities List of entities
	 */
	public void update(List<Entity> entities) {
		if (stats != null)
			stats.forEach(GUIText::remove);
		stats = new ArrayList<>();
		Entity entity = entities.get(1);
		ArrayList<String> strings = new ArrayList<>();
		strings.add("Entity type: " + entity.getType().getName());
		strings.add("Position: " + formatter.format(entity.getPosition2f().x) + ", " + formatter.format(entity.getPosition2f().y));
		strings.add("Mass: " + entity.getMass() + " kg");

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
