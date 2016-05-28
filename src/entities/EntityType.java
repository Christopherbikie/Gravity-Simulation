package entities;

/**
 * Created by Christopher on 30/04/2016.
 */
public enum EntityType {
	Star("star", true), Planet("planet", false);

	/**
	 * The name of the entity type, used for UI
	 */
	private String name;
	/**
	 * True if the entity is a light source, false if it is not.
	 */
	private boolean isLightSource;

	EntityType(String name, boolean isLightSource) {
		this.name = name;
		this.isLightSource = isLightSource;
	}

	public String getName() {
		return name;
	}

	public boolean isLightSource() {
		return isLightSource;
	}
}
