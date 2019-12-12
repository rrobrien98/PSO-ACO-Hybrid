package main.resources.ACO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import main.Lab;
import main.Lab.Error;

public class Graph {
	private int colors;
	private Random rand = new Random();
	//private ArrayList<Leg> legs = new ArrayList<Leg>();
	private ArrayList<Node> nodes = new ArrayList<Node>();
	private double numOf_nodes;
	private double numOf_edges = 0;
	
	private int numOf_legsDims;
	// stacked legCollections for aco pheromone update
	private ArrayList<HashMap<Integer, Leg>> legsDims = new  ArrayList<HashMap<Integer, Leg>>();
	
	String filename;
	public Graph(String filename, int dims, int colors) {
		this.filename = filename;
		this.colors = colors;
		this.numOf_legsDims = dims;
		for(int i=0; i<dims; i++)this.legsDims.add(i,new HashMap<Integer, Leg>());
		this.constructGraph();
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
				if (!line.trim().startsWith("%")) firstEdge = true;
				else if(readNodes) {
					for (int i = 0; i < Integer.parseInt(parts[2]); i++) {
						nodes.add(new Node(i));
					}
					this.numOf_nodes = Double.parseDouble(parts[2]);
					this.numOf_edges = Double.parseDouble(parts[1]);
				}
				else readNodes = true;
				if(firstEdge) {
					Node nodeA = this.nodes.get(Integer.parseInt(parts[0])-1);
					Node nodeB = this.nodes.get(Integer.parseInt(parts[1])-1);
					this.addLeg(nodeA, nodeB, 0); // initialize on dim 0 only, then duplicate to others
					nodeA.addNeighbor(nodeB);
					nodeB.addNeighbor(nodeA);
				}
			}
			br.close();
			for(int i=1; i<legsDims.size(); i++) legsDims.set(i, legsDims.get(0));//duplicate legsDim[0] through other dims
		} catch (FileNotFoundException e) {
			System.out.print("Input File Not Found.");
			Lab.throwError(Error.illegalArgs);
		} catch (IOException e) {
			System.out.print("IO error in br.readline()");
			Lab.throwError(Error.illegalArgs);
			e.printStackTrace();
		}
	}
	
	// return total number of nodes in graph
	public double getNumOf_nodes(){
		return this.numOf_nodes;
	}
	
	public double getNumOf_edges() {
		return this.numOf_edges;
	}
	
	public int getNumOf_legsDims() {
		return this.numOf_legsDims;
	}
	
	// returns non-colored adjacent nodes, given current node for given ant.
	public ArrayList<Node> getClearNodes(Ant ant, int graphDim){
		ArrayList<Node> clearNodes = new ArrayList<Node>();
		Node currNode = ant.getCurrNode();
		for(Node adjNode: ant.getCurrNode().getNeighbors()) {
			if(this.getLeg(currNode, adjNode, graphDim).getColor() == -1) clearNodes.add(adjNode);
		}
		return clearNodes;
	}
	
	// returns a random node from graph
	public Node getRandNode(){
		return nodes.get(rand.nextInt(nodes.size()));
	}
	
	
	
	
	// returns graph from merged of previous iteration
	public HashMap<Integer, Leg> mergeDims(){
		for(int i=1; i<this.numOf_legsDims; i++) {
			for(int key: this.getLegKeys(i)) { // get all legs from dim
				Leg leg = legsDims.get(i).get(key);
				legsDims.get(0).get(key).mergePheromones(leg); // add pheromones from other dims to dim[0]
			}
		}
		for(int i=1; i<this.numOf_legsDims; i++) legsDims.set(i, legsDims.get(0)); // update other graph dims to dim[0]
		return this.legsDims.get(0);
	}

	
	// returns a new ant with tour equals to all inputed ants' combined tours.
	// note: tours are combined from low index to high index, assuming that inputs has ants arranged
	// 			so that last node from ant[i] is adjacent to first node from ant[i+1]
	public Ant mergeAntTours(ArrayList<Ant> colony) {
		ArrayList<Node> tour = new ArrayList<Node>();
		for(Ant ant: colony) tour.addAll(ant.getTour());
		Ant superAnt = new Ant(tour);
		return superAnt;
	}
	
	
	// add new leg to leg collection. make sure that direction is impotent
	public void addLeg(Node nodeA, Node nodeB, int graphDim) {
		int key = Integer.parseInt((""+nodeA.getId()) + (""+nodeB.getId()));
		Leg leg = legsDims.get(graphDim).get(key);
		if(leg != null) return;
		legsDims.get(graphDim).put(key, new Leg(1/this.getNumOf_edges(), nodeA, nodeB,this.colors,key));
	}
	
	
	// return leg by using identifiers from each node - i.e. int node.id
	// order of params does not matter.. 
	public Leg getLeg(Node nodeA, Node nodeB, int graphDim) {
		int key1 = Integer.parseInt((""+nodeA.getId()) + (""+nodeB.getId()));
		int key2 = Integer.parseInt((""+nodeB.getId()) + (""+nodeA.getId()));
		Leg leg = legsDims.get(graphDim).get(key1);
		if(leg == null) leg = legsDims.get(graphDim).get(key2);
		return leg;
	}
	
	
	//returns keys for all legs
	public ArrayList<Integer> getLegKeys(int graphDim){
		ArrayList<Integer> visited = new ArrayList<Integer>();
		for(Node node: this.getNodes()) for(Node ne: node.getNeighbors()) {
			Leg leg = this.getLeg(node, ne, graphDim);
			if(!visited.contains(leg.getKey())) visited.add(leg.getKey());
		}
		return visited;
	}
	
	public ArrayList<Node> getNodes(){
		return this.nodes;
	}
	
	
	// set pheromone to leg
	public void setPheromone(Leg leg, double[] pheromone) {
		for (int i = 0; i < pheromone.length;i++) {
			leg.setPheremone(pheromone[i], i);
		}
	}
	
	
	

}
