package main.resources.ACO;

/*
 * Object to store a leg in a Traveling Sales Person problem.
 * Contains two variables. 
 * The amount of pheremone on the leg and the distance of the leg
 */
public class Leg {
	

	
	private double[] pheremone;
	private double dist;
	
	private int colors;
	private int color;
	private Node nodeA;
	private Node nodeB;
	
	/*
	 * constructor for a leg. Takes an initial amount of pheremone and a distance
	 */
	public Leg(double pheromone, Node nodeA ,Node nodeB, int colors) {
		this.pheremone = new double[colors];
		this.color = -1;
		this.nodeA = nodeA;
		this.nodeB = nodeB;
		this.colors = colors;
		
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
		return pheremone[color];
	}
	public void setPheremone(double pheremone, int color) {
		this.pheremone[color] = pheremone;
	}
	public double [] getPheremoneArray() {
		return this.pheremone;
	}
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
		for (int i = 0; i < this.pheremone.length; i++) {
			total += this.pheremone[i];
		}
		return total;
	}
}

