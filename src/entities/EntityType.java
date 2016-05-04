package entities;

/**
 * Created by Christopher on 30/04/2016.
 */
public enum EntityType {
	Star(true), Planet(true);

	/**
	 * True if the entity is a light source, false if it is not.
	 */
	public boolean isLightSource;

	EntityType(boolean isLightSource) {
		this.isLightSource = isLightSource;
	}
}
