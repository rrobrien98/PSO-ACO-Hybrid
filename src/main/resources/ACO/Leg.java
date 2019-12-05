package main.resources.ACO;

/*
 * Object to store a leg in a Traveling Sales Person problem.
 * Contains two variables. 
 * The amount of pheremone on the leg and the distance of the leg
 */
public class Leg {
	private double pheremone;
	private double dist;
	
	/*
	 * constructor for a leg. Takes an initial amount of pheremone and a distance
	 */
	public Leg(double pheremone, double dist) {
		this.setPheremone(pheremone);
		this.setDist(dist);
	}

	/*
	 * Getters and setters for leg variables.
	 */
	public double getPheremone() {
		return pheremone;
	}
	public void setPheremone(double pheremone) {
		this.pheremone = pheremone;
	}
	public double getDist() {
		return dist;
	}
	public void setDist(double dist) {
		this.dist = dist;
	}
}
