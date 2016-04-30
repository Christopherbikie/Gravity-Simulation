package entities;

/**
 * Created by Christopher on 30/04/2016.
 */
public enum EntityType {
	Star(true), Planet(false);

	public boolean isLightSource;

	EntityType(boolean isLightSource) {
		this.isLightSource = isLightSource;
	}
}
