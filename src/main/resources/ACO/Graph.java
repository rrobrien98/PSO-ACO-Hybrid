package main.resources.ACO;

import java.util.ArrayList;
import java.util.Random;

public class Graph {
	
	Random rand = new Random();
	ArrayList<Leg> legs = new ArrayList<Leg>();
	ArrayList<Node> nodes = new ArrayList<Node>(); 
	
	
	public Graph() {
		// <<===== NEED SOME HELP HERE..  you can inspire yourself from tsp.java -- delete tsp.java afterward!
	}
	
	// return total number of nodes in graph
	public Integer getNumOf_nodes(){
		return nodes.size();
	}
	
	// returns non-colored adjacent nodes, given current node for given ant.
	public ArrayList<Node> getClearNodes(Ant ant){
		return null; // <<===== NEED SOME HELP HERE.. SHOULD NOT BE HARD! :)
	}
	
	// returns a random node from graph
	public Node getRandNode(){
		return nodes.get(rand.nextInt(nodes.size()));
	}
	
	
	// returns a new ant with tour equals to all inputed ants' combined tours.
	// note: tours are combined from low index to high index, assuming that inputs has ants arranged
	// 			so that last node from ant[i] is adjacent to first node from ant[i+1]
	public Ant mergeAntTours(ArrayList<Ant> ants) {
		ArrayList<Node> tour = new ArrayList<Node>();
		for(Ant ant: ants) tour.addAll(ant.getTour());
		Ant superAnt = new Ant(tour);
		return superAnt;
	}
	
	
	// return leg by using identifiers from each node - i.e. int node.id
	// orther of params should not matter
	public Leg getLeg(Node nodeA, Node nodeB) {
		for(Leg leg: legs) if(leg.hasNodes(nodeA, nodeB)) return leg;
		return null;
	}
	
	
	// returns all legs
	public ArrayList<Leg> getLegs(){
		return this.legs;
	}
	
	
	// set pheromone to leg
	public void setPheromone(Leg leg, double pheromone) {
		this.getLeg(leg.getNodeA(), leg.getNodeB()).setPheremone(pheromone);
	}
	
	
	
	
	

}
