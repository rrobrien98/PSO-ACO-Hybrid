package main.resources.ACO;

import java.util.Comparator;

/*
 * Object to store a leg in graph. 
 * Contains the vector of pheremones for each color on the leg, the nodes it bridges, and its id
 */
public class Leg {
	
	private int id;	
	private int color;
	private Node nodeA;
	private Node nodeB;
	
	private double[] pheromones;
	
	
	/*
	 * constructor for a leg. Takes the initial pheremone concentration, the end nodes, the number of colors in the graph, and its id
	 */
	public Leg(double startPheromone, Node nodeA ,Node nodeB, int numOf_colors, int id) {
		this.pheromones = new double[numOf_colors];
		for(int i=0; i<numOf_colors; i++) this.pheromones[i] = startPheromone; 
		this.color = -1;
		this.nodeA = nodeA;
		this.nodeB = nodeB;
		this.id = id;
	}
	
	/*
	 * Clones a leg object
	 */
	public Leg(Leg model) {
		this.id = model.getId();
		this.nodeA = model.getNodeA().clone();
		this.nodeB = model.getNodeB().clone();
		this.pheromones = model.getPheromoneArray().clone();
	}
	public Leg clone() { return new Leg(this);}
	
	
	public void clearColor() {
		this.color = -1;
	}
	
	/*
	 * Checks if a leg goes from nodeA to nodeB
	 */
	public boolean hasNodes(Node nodeA, Node nodeB) {
		if(this.nodeA.equals(nodeA) && this.nodeB.equals(nodeB) ||
			this.nodeB.equals(nodeA) && this.nodeA.equals(nodeB)) {
			return true;
		}
		return false;
	}
	
	/*
	 * Adds the pheremone vectors of two different legs
	 */
	public void mergePheromones(Leg leg) {
		for(int i=0; i<pheromones.length; i++) {
			this.pheromones[i] += leg.getPheromone(i);
		}
	}
	/*
	 * Calculates the average pheremone concentration across all graph dimensions
	 */
	public void averagePheromonesSum(int graphDims) {
		for(int i=0; i<pheromones.length; i++) {
			this.pheromones[i] /= graphDims;
		}
	}
	/*
	 * Changes this leg to match parameters of a model leg
	 */
	public void changeTo(Leg model) {
		this.id = model.getId();
		this.nodeA = model.getNodeA().clone();
		this.nodeB = model.getNodeB().clone();
		this.pheromones = model.getPheromoneArray().clone();
	}

	/*
	 * Getters and setters for leg variables.
	 */
	public double getPheromone(int color) {
		return color == -1? pheromones[0] : pheromones[color];
	}
	public double getMaxPheromone() {
		return color == -1? pheromones[0] : pheromones[color];
	}
	public void setPheromone(double pheromone, int color) {
		this.pheromones[color] = pheromone;
	}
	public void setPheromones(double[] pheromones) {
		this.pheromones = pheromones.clone();
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

	/*
	 * returns the sum of all components in pheromone vector
	 */
	public double getTotalPheremone() {
		double total = 0;
		for (int i = 0; i < this.pheromones.length; i++) {
			total += this.pheromones[i];
		}
		return total;
	}
	/*
	 * Comparator class used for sorting leg by pheremone concentration of the color they have been assigned
	 */
	static public class SortByPheromone implements Comparator<Leg>{
		@Override
		public int compare(Leg leg1, Leg leg2) {
			return leg1.getPheromone(leg1.getColor()) < leg2.getPheromone(leg2.getColor())?-1:
				(leg1.getPheromone(leg1.getColor()) == leg2.getPheromone(leg2.getColor())?0:1);
		}
	}
}


