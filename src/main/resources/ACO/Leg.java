package main.resources.ACO;

/*
 * Object to store a leg in a Traveling Sales Person problem.
 * Contains two variables. 
 * The amount of pheremone on the leg and the distance of the leg
 */
public class Leg {
	

	
	private double[] pheromone;
	private double dist;
	
	private int color;
	private Node nodeA;
	private Node nodeB;
	
	/*
	 * constructor for a leg. Takes an initial amount of pheremone and a distance
	 */
	public Leg(double pheromone, Node nodeA ,Node nodeB, int colors) {
		this.pheromone = new double[colors];
		this.color = -1;
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
	public double getPheremone(int color) {
		return pheromone[color];
	}
	public void setPheremone(double pheremone, int color) {
		this.pheromone[color] = pheremone;
	}
	public double [] getPheromoneArray() {
		return this.pheromone;
	}
	public double getDist() {
		return dist;
	}
	public void setDist(double dist) {
		this.dist = dist;
	}
	public int getColor(){
		return this.color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	
	public Node getNodeA() {return nodeA;}
	public Node getNodeB() {return nodeB;}

	public double getTotalPheremone() {
		// TODO Auto-generated method stub
		double total = 0;
		for (int i = 0; i < this.pheromone.length; i++) {
			total += this.pheromone[i];
		}
		return total;
	}
}

