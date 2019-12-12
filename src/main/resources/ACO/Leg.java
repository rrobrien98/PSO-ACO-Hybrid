package main.resources.ACO;

/*
 * Object to store a leg in a Traveling Sales Person problem.
 * Contains two variables. 
 * The amount of pheremone on the leg and the distance of the leg
 */
public class Leg {
	
	private int id;
	
	private double[] pheromones;
	
	private int color;
	private Node nodeA;
	private Node nodeB;
	
	/*
	 * constructor for a leg. Takes an initial amount of pheremone and a distance
	 */
	public Leg(double startPheromone, Node nodeA ,Node nodeB, int numOf_colors, int id) {
		this.pheromones = new double[numOf_colors];
		for(int i=0; i<numOf_colors; i++) this.pheromones[i] = startPheromone; 
		this.color = -1;
		this.nodeA = nodeA;
		this.nodeB = nodeB;
		this.id = id;
	}
	
	
	public Leg(Leg model) {
		Leg leg = new Leg(0,null,null,0,0);
		leg.id = model.id;
		leg.color = model.color;
		leg.pheromones = model.pheromones;
		leg.nodeA = model.nodeA;
		leg.nodeB = model.nodeB;
	}
	public Leg clone() { return new Leg(this);}
	
	
	public void clearColor() {
		this.color = -1;
	}
	
	public boolean hasNodes(Node nodeA, Node nodeB) {
		if(this.nodeA.equals(nodeA) && this.nodeB.equals(nodeB) ||
			this.nodeB.equals(nodeA) && this.nodeA.equals(nodeB)) {
			return true;
		}
		return false;
	}
	
	public void mergePheromones(Leg leg) {
		for(int i=0; i<pheromones.length; i++) {
			this.pheromones[i] += leg.getPheromone(i);
		}
	}

	/*
	 * Getters and setters for leg variables.
	 */
	public double getPheromone(int color) {
		return pheromones[color];
	}
	public void setPheromone(double pheromone, int color) {
		this.pheromones[color] = pheromone;
	}
	public void resetPheromone(double startPheromone, int numOf_colors) {
		for(int i=0; i<numOf_colors; i++) this.pheromones[i] = startPheromone;
	}
	public double [] getPheromoneArray() {
		return this.pheromones;
	}

	public int getColor(){
		return this.color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	
	public int getKey() {return id;}
	public int getId() {return id;}
	
	public Node getNodeA() {return nodeA;}
	public Node getNodeB() {return nodeB;}

	public double getTotalPheremone() {
		// TODO Auto-generated method stub
		double total = 0;
		for (int i = 0; i < this.pheromones.length; i++) {
			total += this.pheromones[i];
		}
		return total;
	}
}

