package main.resources.ACO;

/*
 * Object to store a leg in a Traveling Sales Person problem.
 * Contains two variables. 
 * The amount of pheremone on the leg and the distance of the leg
 */
public class Leg {
	private double pheremone;
	private double dist;
	
	private Node nodeA;
	private Node nodeB;
	
	/*
	 * constructor for a leg. Takes an initial amount of pheremone and a distance
	 */
	public Leg(double pheromone, Node nodeA ,Node nodeB) {
		this.setPheremone(pheremone);
		this.nodeA = nodeA;
		this.nodeB = nodeB;
	}
	
	public boolean hasNodes(Node nodeA, Node nodeB) {
		if(this.nodeA.equals(nodeA) && this.nodeB.equals(nodeB) ||
			this.nodeB.equals(nodeA) && this.nodeA.equals(nodeB)) {
			return true;
		}
		return false;
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
	
	public Node getNodeA() {return nodeA;}
	public Node getNodeB() {return nodeB;}
}

