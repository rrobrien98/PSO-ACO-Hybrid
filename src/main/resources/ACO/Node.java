package main.resources.ACO;

import java.util.ArrayList;
/*
 * Represents a node in the graph
 */
public class Node {
	
	private int id;
	//neighbors list contains all the nodes that are adjacent to this node
	private ArrayList<Node> neighbors = new ArrayList<Node>();

	public Node(int id) {
		this.id = id;
	}
	/*
	 * clones a model node
	 */
	public Node(Node model) {
		this.id = model.id;
		this.neighbors = model.neighbors;
	}
	public Node clone() {
		return new Node(this);
	}
	
	public boolean equals(Node node) {
		return this.id == node.getId(); // <=== edit
	}
	
	
	public void addNeighbor(Node node) {
		neighbors.add(node);
	}
	
	
	// getters and setters
	
	public int getId(){ return id;}
	public ArrayList<Node> getNeighbors(){
		return this.neighbors;
	}

	public void setNeighbors(ArrayList<Node> neighbors) {
		this.neighbors = neighbors;
	}
	
	
}
