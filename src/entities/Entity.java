package entities;

import maths.Physics;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import util.OBJLoader;
import textures.ModelTexture;

import java.util.List;

/**
 * Created by Christopher on 24/04/2016.
 */
public class Entity {

	/**
	 * The type of entity eg planet, star...
	 */
	private EntityType type;
	/**
	 * The entity's model
	 */
	private TexturedModel model;
	/**
	 * The entity's mass
	 */
	private double mass;
	/**
	 * Time in seconds for the object to complete one rotation on it's axis
	 */
	private int rotationPeriod;
	/**
	 * The entity's position
	 */
	private Vector3f position;
	/**
	 * The entity's scale (size)
	 */
	private Vector3f scale;
	/**
	 * The entity's rotation
	 */
	private Vector3f rotation;
	/**
	 * The entity's velocity
	 */
	private Vector2f velocity = new Vector2f(0, 0);
	/**
	 * The path to the entity's model and texture
	 */
	private String modelPath, texturePath;

	public static Entity sun = new Entity("/obj/sphere4096", "/textures/star");
//	public static Entity mercury = new Entity("/obj/sphere4096", "/textures/mercury");
//	public static Entity venus = new Entity("/obj/sphere4096", "/textures/venus");
	public static Entity earth = new Entity("/obj/sphere4096", "/textures/earth");
	public static Entity mars = new Entity("/obj/sphere4096", "/textures/mars");
//	public static Entity jupiter = new Entity("/obj/sphere4096", "/textures/jupiter");
//	public static Entity saturn = new Entity("/obj/sphere4096", "/textures/saturn");
//	public static Entity uranus = new Entity("/obj/sphere4096", "/textures/uranus");
//	public static Entity neptune = new Entity("/obj/sphere4096", "/textures/neptune");
//	public static Entity pluto = new Entity("/obj/sphere4096", "/textures/pluto");

	/**
	 * Create a new entity using a path to the entity's model and texture
	 *
	 * @param modelPath Path to the OBJ model
	 * @param texturePath Path to the PNG image
	 */
	public Entity(String modelPath, String texturePath) {
		// Create a new loader
		Loader loader = new Loader();
		// Load the entity's model using the OBJ file loader
		RawModel model = OBJLoader.loadObjModel(modelPath, loader);
		this.model = new TexturedModel(model, new ModelTexture(loader.loadTexture(texturePath)));
		ModelTexture texture = this.model.getTexture();
		texture.setShineDamper(10);
		texture.setReflectivity(0.1f);
		this.modelPath = modelPath;
		this.texturePath = texturePath;
	}

	/**
	 * Create a new Entity based off a entity already created and adding a type, position, scale and rotation
	 *
	 * @param type The type of entity to create
	 * @param entity The entity to extract the model from
	 * @param position The position for the new entity
	 * @param scale The scale for the new entity
	 * @param rotation The rotation for the new entity in degrees
	 */
	public Entity(EntityType type, Entity entity, Vector3f position, Vector3f scale, Vector3f rotation) {
		this.type = type;
		this.model = entity.getModel();
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.modelPath = entity.getModelPath();
		this.texturePath = entity.getTexturePath();
	}

	/**
	 * Increase the entity's position
	 *
	 * @param amount Amount to increase the entities position by
	 */
	public void increasePosition(Vector3f amount) {
		this.position.x += amount.x;
		this.position.y += amount.y;
		this.position.z += amount.z;
	}

	/**
	 * Increase the entity's position
	 *
	 * @param amount Amount to increase the entities position by
	 */
	public void increasePosition(Vector2f amount) {
		this.position.x += amount.x;
		this.position.y += amount.y;
	}

	/**
	 * Increase the entity's rotation
	 *
	 * @param amount Amount to rotate in degrees
	 */
	public void increaseRotation(Vector3f amount) {
		this.rotation.x += amount.x;
		this.rotation.y += amount.y;
		this.rotation.z += amount.z;
	}

	/**
	 * Updates the entities's position & rotation
	 *
	 * @param delta Time since last update in seconds
	 * @param entities List of all entities to interact with
	 */
	public void update(float delta, List<Entity> entities) {
		// Update the entity's position
		// Iterate through list of entities and calculate the force, acceleration and movement towards the entity
		for (Entity entity : entities) {
			// If the current entity is this entity, skip to the next in the list
			if (entity == this) continue;

			// Get the force towards the current entity
			Vector2f force = Physics.getForce(mass, entity.getMass(), getPosition2f(), entity.getPosition2f());
			// Get the acceleration towards the current entity
			Vector2f acceleration = Physics.getAcceleration(force, mass);
			// Change this entity's velocity based off the acceleration and time passed
			velocity.x += acceleration.x * delta;
			velocity.y += acceleration.y * delta;
		}

		// Change this entity's position based off it's velocity and the time passed, converting from meters to AU
		position.x += velocity.x * delta / Physics.METERS_PER_AU;
		position.z += velocity.y * delta / Physics.METERS_PER_AU;

		// If the rotation period has been set, rotate
		if (rotationPeriod != 0)
			increaseRotation(new Vector3f(0, (360f / rotationPeriod) * delta, 0));
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

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public Vector3f getPosition3f() {
		return position;
	}

	public Vector2f getPosition2f() {
		return new Vector2f(position.x, position.z);
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setPosition(Vector2f position) {
		this.position.x = position.x;
		this.position.z = position.y;
	}

	public Vector2f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2f velocity) {
		this.velocity = velocity;
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

	public int getRotationPeriod() {
		return rotationPeriod;
	}

	public void setRotationPeriod(int rotationPeriod) {
		this.rotationPeriod = rotationPeriod;
	}

	/**
	 * Returns a string representation of the entity
	 *
	 * @return A string representation of the entity
	 */
	public String toString() {
		return "Entity {" +
				"\n    type: " + getType() +
				"\n    model: " + getModelPath() +
				"\n    texture: " + getTexturePath() +
				"\n    position: " + position.toString() +
				"\n    rotation: " + rotation.toString() +
				"\n    scale: " + scale.toString() +
				"\n}";
	}
}
