package main.resources.ACO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Graph {
	
	private Random rand = new Random();
	//private ArrayList<Leg> legs = new ArrayList<Leg>();
	private ArrayList<Node> nodes = new ArrayList<Node>();
	private HashMap<String, Leg> legs= new HashMap<String, Leg>(); // must populate via addLeg
	
	String filename;
	public Graph(String filename) {
		this.filename = filename;
	}
	public void constructGraph() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.filename));
			String line;
			Boolean firstEdge = false;
			Boolean readNodes = false;
			while(((line = br.readLine())!=null)) {
				String[] parts = line.trim().split(" ");
				//once it finds the first city start storing coordinates
				if (!line.trim().startsWith("%")) {
					readNodes = false;
					firstEdge = true;
				}
				else if(readNodes) {
					this.graph = new ArrayList[(int) Math.max(Double.parseDouble(parts[2]), Double.parseDouble(parts[2]))]
				}
				else {
					readNodes = true;
				}
				if(firstEdge) {
					if (this.graph[parts[0]] == null) {
						this.graph[parts[0]] = new ArrayList<Leg>();
					}
					//need to change leg initializer
					this.graph[parts[0]].add(new Leg());
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.print("Input File Not Found.");
		} catch (IOException e) {
			System.out.print("IO error in br.readline()");
			e.printStackTrace();
		}
	}
	
	// return total number of nodes in graph
	public Integer getNumOf_nodes(){
		return nodes.size();
	}
	
	// returns non-colored adjacent nodes, given current node for given ant.
	public ArrayList<Node> getClearNodes(Ant ant){
		ArrayList<Node> clearNodes = new ArrayList<Node>();
		Node currNode = ant.getCurrNode();
		for(Node adjNode: ant.getCurrNode().getNeighbors()) {
			if(this.getLeg(currNode, adjNode).getColor() == null) clearNodes.add(adjNode);
		}
		return clearNodes;
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
	
	
	// add new leg to leg collection. make sure that direction is impotent
	public void addLeg(Node nodeA, Node nodeB) {
		Leg leg = legs.get(Integer.toString(nodeA.getId()) + Integer.toString(nodeB.getId()) );
		if(leg != null) return;
		legs.put(Integer.toString(nodeA.getId()) + Integer.toString(nodeB.getId()), new Leg(0, nodeA, nodeB));
	}
	
	
	// return leg by using identifiers from each node - i.e. int node.id
	// order of params does not matter.. 
	public Leg getLeg(Node nodeA, Node nodeB) {
		Leg leg = legs.get(Integer.toString(nodeA.getId()) + Integer.toString(nodeB.getId()) );
		if(leg == null) legs.get(Integer.toString(nodeB.getId()) + Integer.toString(nodeA.getId()) );
		return leg;
	}
	
	
	public ArrayList<Node> getNodes(){
		return this.nodes;
	}
	
	
	// set pheromone to leg
	public void setPheromone(Leg leg, double pheromone) {
		this.getLeg(leg.getNodeA(), leg.getNodeB()).setPheremone(pheromone);
	}
	
	
	
	
	

}
