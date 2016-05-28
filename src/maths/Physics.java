package maths;

import entities.Entity;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Christopher on 3/05/2016.
 */
public class Physics {

	/**
	 * The Gravitational Constant
	 */
	public static final double G = 6.67408e-11;
	/**
	 * The number or meters in one astronomical unit
	 */
	public static final double METERS_PER_AU = 1.496e+11;

	/**
	 * Calculates the force between two objects in newtons.
	 * Uses Newton's Law of Universal Gravitation.
	 * F = G * m1 * m2 / (r * r) * normalise(r)
	 *
	 * @param mass1 Mass in kg of one of the objects
	 * @param mass2 Mass in  kg of the other object
	 * @param r1 The position of one of the objects in Astronomical Units (AU)
	 * @param r2 The position of the other object in AU
	 *           The position should be relative to the same point as the first.
	 * @return The force between the two objects in newtons.
	 */
	public static Vector2f getForce(double mass1, double mass2, Vector2f r1, Vector2f r2) {
		// Create a new vector representing the radius between the two objects
		Vector2f radius = Vector2f.sub(r1, r2, null);
		// Get the length of the radius
		float radiusLength = radius.length();
		// Use newtons law of gravitation to get the force between the two objects.
		return (Vector2f) radius.normalise().scale((float) (-G * mass1 * mass2 / (radiusLength * METERS_PER_AU * radiusLength * METERS_PER_AU)));
	}

	/**
	 * Calculates the acceleration of an object.
	 * Uses Newton's Second Law of Motion.
	 * F = ma
	 *
	 * @param force The force applied on the object in newtons
	 * @param mass The mass of the object in kg
	 * @return The acceleration of the object in meters per second squared.
	 */
	public static Vector2f getAcceleration(Vector2f force, double mass) {
		return new Vector2f(force.x / (float) mass, force.y / (float) mass);
	}

	/**
	 * Check if a point is intersecting an entity
	 *
	 * @param point The point
	 * @param entity The entity
	 * @return True if intersecting, otherwise false
	 */
	public static boolean isIntersecting(Vector3f point, Entity entity) {
		return Vector3f.sub(point, entity.getPosition3f(), null).length() <= 0.1f;
	}
}
