package main.resources.ACO;

public class Node {
	
	private int id;

	public Node(int id) {
		this.id = id;
	}
	
	public boolean equals(Node node) {
		return this.id == node.getId(); // <=== edit
	}
	
	
	
	// getters and setters
	
	public int getId(){ return id;}
	
}
