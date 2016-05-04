package maths;

/**
 * Created by Christopher on 3/05/2016.
 */
public class Physics {

	public static final double G = 6.67408e-11;
	public static final double METERS_PER_AU = 1.496e+11;

	public static double getForce(double mass1, double mass2, double distance) {
		return G * mass1 * mass2 / (distance * METERS_PER_AU * distance * METERS_PER_AU);
	}

	public static double getAcceleration(double force, double mass) {
		return force / mass;
	}

	public static double getMovement(double currentVelocity, double delta, double acceleration) {
		return currentVelocity * delta + acceleration * delta * delta / 2.0d;
	}
}
