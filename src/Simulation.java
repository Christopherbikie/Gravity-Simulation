import entities.Camera;
import entities.Entity;
import entities.EntityType;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import textures.ModelTexture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher on 23/04/2016.
 */
public class Simulation {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader = new Loader();

		List<Entity> entities = new ArrayList<>();

		Entity sun = new Entity(EntityType.Star, Entity.sun, new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new Vector3f(0, 0, 0));
		entities.add(sun);
		Entity earth = new Entity(EntityType.Planet, Entity.earth, new Vector3f(6, 0, 0), new Vector3f(1, 1, 1), new Vector3f(0, 0, 0));
		entities.add(earth);
		Entity mars = new Entity(EntityType.Planet, Entity.mars, new Vector3f(0, 0, 6), new Vector3f(1, 1, 1), new Vector3f(0, 0, 0));
		entities.add(mars);

		System.out.println(earth.toString());

		Light light = new Light(sun.getPosition3f(), new Vector3f(1, 1, 0.8f));

		Camera camera = new Camera(new Vector3f(0, 50, 0), 90, 0, 0);

		MasterRenderer renderer = new MasterRenderer();

		float time = 0;
		while (!Display.isCloseRequested()) {
			for (Entity entity : entities) {
				entity.increaseRotation(new Vector3f(0, 0.05f, 0));
			}
			camera.move();
			entities.forEach(renderer::processEntity);
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
			time++;
		}

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
