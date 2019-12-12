package main.resources.ACO;

import java.util.ArrayList;

public class Ant {
	
	private ArrayList<Node> tourNodes;
	private ArrayList<Integer> tourLegs;
	private Node currNode;
	private boolean stop;
	
	// default init ant
	public Ant(Node node) {
		this.setTour(new ArrayList<Node>());
		this.tourLegs = new ArrayList<Integer>();
		this.setCurrNode(node);
		this.stop = false;
	}
	
	// create ant with tour already performed -- used to combine ant tours
	@SuppressWarnings("unchecked")
	public Ant(ArrayList<Node> tourNodes, ArrayList<Integer> tourLegs) {
		this.tourNodes = (ArrayList<Node>) tourNodes.clone();
		this.currNode = tourNodes.get(tourNodes.size()-1);
		this.tourLegs = (ArrayList<Integer>) tourLegs.clone();
		this.stop = true;
	}
	
	// cloning
	@SuppressWarnings("unchecked")
	public Ant(Ant model) {
		this.currNode = model.currNode;
		this.tourNodes = (ArrayList<Node>) model.tourNodes.clone();
		this.tourLegs = (ArrayList<Integer>) model.tourLegs.clone();
		this.stop = model.stop;
	}
	public Ant clone() {return new Ant(this);}

	
	
	
	// getters and setters
	
	public int getTourLen() { return tourNodes.size(); }
	public Node getCurrNode() { return currNode; }
	public ArrayList<Node> getTourNodes() { return tourNodes; }
	public ArrayList<Integer> getTourLegs() { return tourLegs; }
	public void setCurrNode(Node node) { currNode = node; }
	public void setTour(ArrayList<Node> tour) {this.tourNodes = tour; }
	public void stop() { this.stop = true;}
	public boolean cont() { return !this.stop; }

	public boolean tourHasLeg(Leg leg) {
		return this.tourLegs.contains(leg.getId());
//		int next_index, prev_index;
//		int index_i = this.tourNodes.indexOf(leg.getNodeA());
//		if(index_i == 0) {
//			next_index = 1;
//			prev_index = this.tourNodes.size()-1;
//		}else if(index_i == this.tourNodes.size()-1) {
//			next_index = 0;
//			prev_index = this.tourNodes.size()-2;
//		}else {
//			next_index = index_i+1;
//			prev_index = index_i-1;
//		}
//		if (this.tourNodes.get(next_index).equals(leg.getNodeB()) || this.tourNodes.get(prev_index).equals(leg.getNodeB())) return true;
//		return false;
	}
	

	public void moveToNode(Node node, int legId, int color) {
		this.setCurrNode(node);
		this.tourNodes.add(node);
		this.tourLegs.add(legId);
	}
	
	
	public void reset(Node node){
		this.setCurrNode(node);
		this.tourNodes = new ArrayList<Node>();
		this.tourLegs = new ArrayList<Integer>();
		this.stop = false;
	}

	
	
	

}
