package UI;

import entities.Entity;
import entities.EntityType;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import input.Keyboard;
import input.MousePicker;
import maths.Physics;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import renderEngine.DisplayManager;
import renderEngine.Loader;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher on 17/05/2016.
 */
public class UI {

	/**
	 * Array representing which statistics are editable
	 */
	private final boolean[] EDITABLE_STATS = {true, true, true, true, true, true};

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
	 * The mouse picker for selecting entities
	 */
	private MousePicker mousePicker;
	/**
	 * The entity label manager
	 */
	private EntityLabeler entityLabeler;
	/**
	 * The currently selected entity
	 */
	private Entity selectedEntity;
	/**
	 * Number representing the line of the currently selected statistic
	 */
	private int statSelection = 0;

	/**
	 * Constructor to create a UI
	 *
	 * @param loader Loader to use to load font files
	 * @param mousePicker The mouse picker to select objects
	 */
	public UI(Loader loader, MousePicker mousePicker, EntityLabeler entityLabeler) {
		segoeUI = new FontType(loader.loadTexture("/fonts/segoe_ui"), "/fonts/segoe_ui");
		this.mousePicker = mousePicker;
		this.entityLabeler = entityLabeler;
		TextMaster.init(loader);
		// Make sure the selected statistic is valid
		while (!EDITABLE_STATS[statSelection]) {
			statSelection++;
			if (statSelection >= EDITABLE_STATS.length)
				statSelection = 0;
		}
		// Use the operating system's look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update statistics
	 *
	 * @param entities List of entities in the universe
	 */
	public void update(List<Entity> entities) {
		// If the left mouse button is down, cycle through all entities and check if the mouse is over it.
		// If it is, select the entity and stop cycling
		mousePicker.update();
		if (Mouse.isButtonDown(0))
			try {
				for (Entity entity : entities)
					if (Physics.isIntersecting(mousePicker.getCurrentYPlanePoint(), entity)) {
						selectedEntity = entity;
						break;
					}
			} catch (NullPointerException ignored) {
			}
		// If no entity is selected, select the first entity of the array (should be the sun)
		if (selectedEntity == null)
			selectedEntity = entities.get(0);

		// Get keyboard input
		if (Keyboard.getKeyDownNoRepeats(org.lwjgl.input.Keyboard.KEY_DOWN)) {
			statSelection++;
			if (statSelection >= EDITABLE_STATS.length)
				statSelection = 0;
			while (!EDITABLE_STATS[statSelection]) {
				statSelection++;
				if (statSelection >= EDITABLE_STATS.length)
					statSelection = 0;
			}
		}
		if (Keyboard.getKeyDownNoRepeats(org.lwjgl.input.Keyboard.KEY_UP)) {
			statSelection--;
			if (statSelection < 0)
				statSelection = EDITABLE_STATS.length - 1;
			while (!EDITABLE_STATS[statSelection]) {
				statSelection--;
				if (statSelection < 0)
					statSelection = EDITABLE_STATS.length - 1;
			}
		}

		// Get changes to statistics
		if (Keyboard.getKeyDownNoRepeats(org.lwjgl.input.Keyboard.KEY_RIGHT)) {
			// Switch to the currently selected statistic
			switch (statSelection) {
				case 0:
					// Show a dialog box asking for text input, store the answer
					String input = JOptionPane.showInputDialog(null, "Enter a name for " + selectedEntity.getName(), "Rename dialog", JOptionPane.PLAIN_MESSAGE);
					// As long as input is not null, set the entities name to the input
					if (input != null)
						selectedEntity.setName(input);
					break;
				case 1:
					// If the entity is a star make it a planet, otherwise make it a star
					if (selectedEntity.getType() == EntityType.Star)
						selectedEntity.setType(EntityType.Planet);
					else
						selectedEntity.setType(EntityType.Star);
					break;
				case 2:
					// Create text fields
					JTextField xField = new JTextField(5);
					JTextField yField = new JTextField(5);

					// Create the dialog box
					JPanel inputPanel = new JPanel();
					inputPanel.add(new JLabel("x:"));
					inputPanel.add(xField);
					inputPanel.add(Box.createHorizontalStrut(15)); // a spacer
					inputPanel.add(new JLabel("y:"));
					inputPanel.add(yField);

					// Show the dialog box
					JOptionPane.showMessageDialog(null, inputPanel, "Set the position for " + selectedEntity.getName(), JOptionPane.PLAIN_MESSAGE);
					// Change the position to the input, ignore number formatting errors
					try {
						selectedEntity.setPosition(new Vector2f(Float.parseFloat(xField.getText()), Float.parseFloat(yField.getText())));
					} catch (NumberFormatException ignored) {
					}
					break;
				case 3:
					// Create text fields
					xField = new JTextField(8);
					yField = new JTextField(8);

					// Create the dialog box
					inputPanel = new JPanel();
					inputPanel.add(new JLabel("x:"));
					inputPanel.add(xField);
					inputPanel.add(Box.createHorizontalStrut(15)); // a spacer
					inputPanel.add(new JLabel("y:"));
					inputPanel.add(yField);

					// Show the dialog box
					JOptionPane.showMessageDialog(null, inputPanel, "Set the velocity for " + selectedEntity.getName(), JOptionPane.PLAIN_MESSAGE);
					// Change the velocity to the input, ignore number formatting errors
					try {
						selectedEntity.setVelocity(new Vector2f(Float.parseFloat(xField.getText()), Float.parseFloat(yField.getText())));
					} catch (NumberFormatException ignored) {
					}
					break;
				case 4:
					// Create text fields
					xField = new JTextField(8);
					yField = new JTextField(2);

					// Create the dialog box
					inputPanel = new JPanel();
					inputPanel.add(xField);
					inputPanel.add(new JLabel("* 10 ^"));
					inputPanel.add(yField);
					inputPanel.add(new JLabel("kg"));

					// Show the dialog box
					JOptionPane.showMessageDialog(null, inputPanel, "Set the mass for " + selectedEntity.getName(), JOptionPane.PLAIN_MESSAGE);
					// Calculate and change the mass to the input, ignore number formatting errors
					try {
						selectedEntity.setMass(Double.parseDouble(xField.getText()) * Math.pow(10, Double.parseDouble(yField.getText())));
					} catch (NumberFormatException ignored) {
					}
					break;
				case 5:
					// Show a dialog box asking for text input, store the answer
					input = JOptionPane.showInputDialog(null, "Enter the rotation period for " + selectedEntity.getName(), "Change rotation period", JOptionPane.PLAIN_MESSAGE);
					// As long as input is not null, set the entities rotation period to the input
					// Ignore number formatting errors
					try {
						if (input != null)
							selectedEntity.setRotationPeriod(Integer.parseInt(input));
					} catch (NumberFormatException ignored) {
					}
					break;
			}
		}

		// Clear the current statistics
		TextMaster.removeAllTexts();
		stats.clear();

		// Clear and repopulate the array of strings
		strings.clear();
		strings.add(generateStatistic(0, "Name", selectedEntity.getName()));
		strings.add(generateStatistic(1, "Entity type", selectedEntity.getType().getName()));
		strings.add(generateStatistic(2, "Position (AU)", formatterFourDecimals.format(selectedEntity.getPosition2f().x) + ", " + formatterFourDecimals.format(selectedEntity.getPosition2f().y)));
		strings.add(generateStatistic(3, "Velocity (m/s)", formatterFourDecimals.format(selectedEntity.getVelocity().x) + ", " + formatterFourDecimals.format(selectedEntity.getVelocity().y)));
		strings.add(generateStatistic(4, "Mass", selectedEntity.getMass() + " kg"));
		strings.add(generateStatistic(5, "Rotation period", (selectedEntity.getRotationPeriod() == 0 ? "Not rotating" : formatterTwoDecimals.format((float) selectedEntity.getRotationPeriod() / 3600))));

		// Create a GUIText for each line of text
		for (int i = 0; i < strings.size(); i++) {
			GUIText statistic =  new GUIText(strings.get(i), 0.8f, segoeUI, new Vector2f(0.005f, (float) i / DisplayManager.HEIGHT * 20), 1f, false);
			statistic.setColour(0.8f, 0.8f, 0.8f);
			stats.add(statistic);
		}

		entityLabeler.update();
	}

	/**
	 * Generate a formatted line showing a statistic and its label, and a '>' if the statistic is selected.
	 *
	 * @param number Number of the statistic, used to work out whether the statistic is selected
	 * @param label Label or name of the statistic
	 * @param value Value of the statistic
	 * @return Formatted string representing the statistic
	 */
	private String generateStatistic(int number, String label, String value) {
		return (number == statSelection ? "> " : "") + label + ": " + value;
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
