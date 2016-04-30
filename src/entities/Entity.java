package entities;

import models.RawModel;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import textures.ModelTexture;

/**
 * Created by Christopher on 24/04/2016.
 */
public class Entity {

	private EntityType type;
	private TexturedModel model;
	private Vector3f position;
	private Vector3f scale;
	private Vector3f rotation;
	private String modelPath, texturePath;

	public static Entity sun = new Entity("obj/sphere4096", "textures/star");
	public static Entity mercury = new Entity("obj/sphere4096", "textures/mercury");
	public static Entity venus = new Entity("obj/sphere4096", "textures/venus");
	public static Entity earth = new Entity("obj/sphere4096", "textures/earth");
	public static Entity mars = new Entity("obj/sphere4096", "textures/mars");
	public static Entity jupiter = new Entity("obj/sphere4096", "textures/jupiter");
	public static Entity saturn = new Entity("obj/sphere4096", "textures/saturn");
	public static Entity uranus = new Entity("obj/sphere4096", "textures/uranus");
	public static Entity neptune = new Entity("obj/sphere4096", "textures/neptune");
	public static Entity pluto = new Entity("obj/sphere4096", "textures/pluto");

	public Entity(String modelPath, String texturePath) {
		Loader loader = new Loader();
		RawModel model = OBJLoader.loadObjModel(modelPath, loader);
		this.model = new TexturedModel(model, new ModelTexture(loader.loadTexture(texturePath)));
		ModelTexture texture = this.model.getTexture();
		texture.setShineDamper(10);
		texture.setReflectivity(0.1f);
		this.modelPath = modelPath;
		this.texturePath = texturePath;
	}

	public Entity(EntityType type, Entity entity, Vector3f position, Vector3f scale, Vector3f rotation) {
		this.type = type;
		this.model = entity.getModel();
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.modelPath = entity.getModelPath();
		this.texturePath = entity.getTexturePath();
	}

	public void increasePosition(Vector3f amount) {
		this.position.x += amount.x;
		this.position.y += amount.y;
		this.position.z += amount.z;
	}

	public void increaseRotation(Vector3f amount) {
		this.rotation.x += amount.x;
		this.rotation.y += amount.y;
		this.rotation.z += amount.z;
	}

	public EntityType getType() {
		return type;
	}

	public String getModelPath() {
		return modelPath;
	}

	public String getTexturePath() {
		return texturePath;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition3f() {
		return position;
	}

	public Vector2f getPosition2f() {
		return new Vector2f(position.x, position.y);
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setPosition(Vector2f position) {
		this.position.x = position.x;
		this.position.y = position.y;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public Vector3f getScale() {
		return scale;
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}

	public void setScale(float scale) {
		this.scale.x = scale;
		this.scale.y = scale;
		this.scale.z = scale;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Entity {");
		sb.append("\n    model: ").append(getModelPath());
		sb.append("\n    texture: ").append(getTexturePath());
		sb.append("\n    position: ").append(position.toString());
		sb.append("\n    rotation: ").append(rotation.toString());
		sb.append("\n    scale: ").append(scale.toString());
		sb.append("\n}");
		return  sb.toString();
	}
}
