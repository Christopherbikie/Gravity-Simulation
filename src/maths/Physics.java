package maths;

import org.lwjgl.util.vector.Vector2f;

/**
 * Created by Christopher on 3/05/2016.
 */
public class Physics {

	public static final double G = 6.67408e-11;
	public static final double METERS_PER_AU = 1.496e+11;

	public static Vector2f getForce(double mass1, double mass2, Vector2f r1, Vector2f r2) {
		Vector2f radius = Vector2f.sub(r1, r2, null);
		float radiusLength = radius.length();
		return (Vector2f) radius.normalise().scale((float) (-G * mass1 * mass2 / (radiusLength * METERS_PER_AU * radiusLength * METERS_PER_AU)));

//		return G * mass1 * mass2 / (distance * METERS_PER_AU * distance * METERS_PER_AU);
	}

	public static Vector2f getAcceleration(Vector2f force, double mass) {
		return new Vector2f(force.x / (float) mass, force.y / (float) mass);
	}

	public static double getMovement(double currentVelocity, double delta, double acceleration) {
		return currentVelocity * delta + acceleration * delta * delta / 2.0d;
	}
}
