package simulation;

import maths.Maths;
import ui.UI;
import ui.EntityLabeler;
import entities.Camera;
import entities.Entity;
import entities.Light;
import input.MousePicker;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import util.Clock;
import util.XMLReader;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher on 23/04/2016.
 */
public class Simulation {

	/**
	 * True if we should draw trails, otherwise false
	 */
	public static boolean drawTrails = false;
	/**
	 * True if testing for min/max sun mass, otherwise false
	 */
	private static boolean isTestRunning = false;
	/**
	 * Scale of entity sizes.
	 * 1.0 represents a radius of 1 AU
	 */
	private static float scale = 0.1f;
	/**
	 * Array of all entities in the solar system
	 */
	private static List<Entity> entities;
	/**
	 * Array of all entities in the solar system at their state before the test started
	 */
	private static List<Entity> originalEntities;

	private static double currentMassTest;

	private static double currentTestStart = 0;

	private static List<Double> successfulTests = new ArrayList<>();

	/**
	 * The program's entry point, this method is executed when the simulation is run.
	 *
	 * @param args Not used
	 */
	public static void main(String[] args) {
		// Create a new display
		DisplayManager.createDisplay();
		// Create a new loader
		Loader loader = new Loader();

		// Create a list to store all entities currently in the simulation
		// Load the entities from an xml file
		entities = XMLReader.loadSystem("/xml/theSolarSystem.xml");

		// Create a light source at the location of the sun
		// Assumes sun is the first object of the array
		Light light = new Light(entities.get(0).getPosition3f(), new Vector3f(1, 1, 0.8f));

		// Create a camera i.e. the point from which we observe the simulation
		Camera camera = new Camera(new Vector3f(0, 5, 0), 90, 0, 0);

		// Create a renderer
		MasterRenderer renderer = new MasterRenderer();

		// Create a UI
		UI ui = new UI(loader, new MousePicker(camera, renderer.getProjectionMatrix()), new EntityLabeler(camera, renderer.getProjectionMatrix(), loader));

		// Loop to update and render all the entities
		while (!Display.isCloseRequested()) {
			float delta = Clock.delta();
			Clock.update();
			getInput(camera);

			// Update all the entities positions and rotations
			for (int i = 0; i < 1000; i++) {
				for (Entity entity : entities) {
					entity.update(delta / 1000, entities);
					Clock.updateUPS();
				}
			}
			// Update the UI
			ui.update();

			if (isTestRunning) {
				updateTest();
			}

			// Prepare all the entities for rendering
			entities.forEach(renderer::processEntity);
			// Render the entities
			renderer.render(light, camera);
			// Render the UI
			ui.render();
			Clock.updateFPS();

			// Update the display
			DisplayManager.updateDisplay();
		}

		// Clean up the UI
		ui.cleanUp();
		// Clean up the renderer (detach and delete shaders)
		renderer.cleanUp();
		// Clean up the loader (delete GPU objects)
		loader.cleanUp();
		// Close the display
		DisplayManager.closeDisplay();
	}

	/**
	 * Get user input
	 *
	 * @param camera The camera the user is controlling
	 */
	private static void getInput(Camera camera) {
		input.Keyboard.update();
		// Move the camera
		camera.move(entities);
		// Check if trails should be drawn
		if (input.Keyboard.getKeyDownNoRepeats(Keyboard.KEY_T))
			drawTrails = !drawTrails;

		// Update scale
		if (input.Keyboard.getKeyDown(Keyboard.KEY_PRIOR)) { // Page up
			scale *= 1 + Clock.deltaWithoutMultiplier(false);
			for (Entity entity : entities)
				entity.setScale(scale);
		}
		if (input.Keyboard.getKeyDown(Keyboard.KEY_NEXT)) { // Page down
			scale *= 1 - Clock.deltaWithoutMultiplier(false);
			for (Entity entity : entities)
				entity.setScale(scale);
		}

		// If 'O' is pressed, open an open file dialog to open and load an xml file
		if (input.Keyboard.getKeyDownNoRepeats(Keyboard.KEY_O)) {
			final JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(null);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				System.out.println(file);
				try {
					entities = XMLReader.loadSystem(file);
				} catch (Exception e) {
					System.err.println("Not a valid XML file!");
					e.printStackTrace();
				}
			}
		}

		// If the enter key is pressed, start a test
		if (input.Keyboard.getKeyDownNoRepeats(Keyboard.KEY_RETURN) && !isTestRunning) {
			entities = XMLReader.loadMostRecent();
			originalEntities = XMLReader.loadMostRecent();
			successfulTests.clear();
			Clock.setMultiplier(1576800000f);
			currentMassTest = 1e20;
			System.out.println("Started");
			isTestRunning = true;
		}
	}

	/**
	 * Update the current test
	 */
	private static void updateTest() {
		if (currentTestStart == 0) {
			currentTestStart = Clock.getTotalTime();
			entities.get(0).setMass(currentMassTest);
		}
		// Current mass is successful if it lasts 1576800000 seconds (50 years)
		if (Clock.getTotalTime() >= currentTestStart + 1576800000d) {
			entities = XMLReader.loadMostRecent();
			Clock.setTotalTime(currentTestStart);
			System.out.println("Successful test on mass " + currentMassTest);
			successfulTests.add(currentMassTest);
			// Increase tested mass by 5%
			currentMassTest *= 1.05f;
			entities.get(0).setMass(currentMassTest);
			// Finish after testing mass 1e40
			if (currentMassTest > 1e40)
				finishTest();
		}

		// Check if any entity has moved 50% closer or further from the sun
		for (int i = 1; i < entities.size(); i++) {
			float percentDifference = entities.get(i).getPosition2f().length() / originalEntities.get(i).getPosition2f().length();

			if ((percentDifference < 0.5f || percentDifference > 1.5f) && entities.get(i).getPosition2f().length() >= 0.1f) {
				entities = XMLReader.loadMostRecent();
				Clock.setTotalTime(currentTestStart);
				// Increase tested mass by 5%
				currentMassTest *= 1.05f;
				entities.get(0).setMass(currentMassTest);
				// Finish after testing mass 1e40
				if (currentMassTest > 1e40)
					finishTest();
			}
		}
	}

	/**
	 * Called to stop a test a print the results
	 */
	private static void finishTest() {
		isTestRunning = false;
		entities = XMLReader.loadMostRecent();
		Clock.setMultiplier(86400);

		// Calculate mean result
		double mean = 0;
		for (Double mass : successfulTests)
			mean += mass;
		mean /= successfulTests.size();

		// Print results
		System.out.println("Finished!");
		System.out.println("Minimum: " + successfulTests.get(0));
		System.out.println("Maximum: " + successfulTests.get(successfulTests.size() - 1));
		System.out.println("Range: " + (successfulTests.get(successfulTests.size() - 1) - successfulTests.get(0)));
		System.out.println("Max / Min: " + (successfulTests.get(successfulTests.size() - 1) / successfulTests.get(0)));
		System.out.println("Mean: " + mean);
	}

	public static List<Entity> getEntities() {
		return entities;
	}

	public static boolean isTestRunning() {
		return isTestRunning;
	}

	public static double getCurrentMassTest() {
		return currentMassTest;
	}
}
