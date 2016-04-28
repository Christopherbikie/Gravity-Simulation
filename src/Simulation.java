import entities.Camera;
import entities.Entity;
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

		Entity sun = new Entity(Entity.sun, new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new Vector3f(0, 0, 0));
		entities.add(sun);
		Entity mercury = new Entity(Entity.mercury, new Vector3f(2, 0, 0), new Vector3f(1, 1, 1), new Vector3f(0, 0, 0));
		entities.add(mercury);
		Entity venus = new Entity(Entity.venus, new Vector3f(4, 0, 0), new Vector3f(1, 1, 1), new Vector3f(0, 0, 0));
		entities.add(venus);
		Entity earth = new Entity(Entity.earth, new Vector3f(6, 0, 0), new Vector3f(1, 1, 1), new Vector3f(0, 0, 0));
		entities.add(earth);
		Entity mars = new Entity(Entity.mars, new Vector3f(8, 0, 0), new Vector3f(1, 1, 1), new Vector3f(0, 0, 0));
		entities.add(mars);
		Entity jupiter = new Entity(Entity.jupiter, new Vector3f(10, 0, 0), new Vector3f(1, 1, 1), new Vector3f(0, 0, 0));
		entities.add(jupiter);
		Entity saturn = new Entity(Entity.saturn, new Vector3f(12, 0, 0), new Vector3f(1, 1, 1), new Vector3f(0, 0, 0));
		entities.add(saturn);
		Entity uranus = new Entity(Entity.uranus, new Vector3f(14, 0, 0), new Vector3f(1, 1, 1), new Vector3f(0, 0, 0));
		entities.add(uranus);
		Entity neptune = new Entity(Entity.neptune, new Vector3f(16, 0, 0), new Vector3f(1, 1, 1), new Vector3f(0, 0, 0));
		entities.add(neptune);
		Entity pluto = new Entity(Entity.pluto, new Vector3f(18, 0, 0), new Vector3f(1, 1, 1), new Vector3f(0, 0, 0));
		entities.add(pluto);

		System.out.println(earth.toString());

		Light light = new Light(new Vector3f(-20, 0, 0), new Vector3f(1, 1, 1));

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
