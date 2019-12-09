package main.resources.ACO;

import java.util.ArrayList;

public class Ant {
	
	private ArrayList<Node> tour;
	private Node currNode;
	private boolean stop;
	
	// default init ant
	public Ant(Node node) {
		this.setTour(new ArrayList<Node>());
		this.setCurrNode(node);
		this.stop = false;
	}
	
	// create ant with tour already performed -- used to combine ant tours
	public Ant(ArrayList<Node> tour) {
		this.tour = tour;
		this.currNode = tour.get(tour.size()-1);
	}
	
	// cloning
	public Ant(Ant model) {
		this.tour = model.tour;
		this.currNode = model.currNode;
	}
	public Ant clone() {return new Ant(this);}

	
	
	
	// getters and setters
	
	public int getTourLen() { return tour.size(); }
	
	public Node getCurrNode() { return currNode; }
		
	public ArrayList<Node> getTour() { return tour; }
	
	public void setCurrNode(Node node) { currNode = node; }
	
	public void setTour(ArrayList<Node> tour) {this.tour = tour; }
	public void stop() {
		this.stop = true;
	}
	
	public boolean cont() {
		return !this.stop;
	}

	public boolean tourHasLeg(Leg leg) {
		int next_index, prev_index;
		int index_i = this.tour.indexOf(leg.getNodeA());
		if(index_i == 0) {
			next_index = 1;
			prev_index = this.tour.size()-1;
		}else if(index_i == this.tour.size()-1) {
			next_index = 0;
			prev_index = this.tour.size()-2;
		}else {
			next_index = index_i+1;
			prev_index = index_i-1;
		}
		if (this.tour.get(next_index).equals(leg.getNodeB()) || this.tour.get(prev_index).equals(leg.getNodeB())) return true;
		return false;
	}
	

	public void moveToNode(Node node,int color) {
		this.setCurrNode(node);
		this.tour.add(node);
	}
	
	
	public void reset(Node node){
		this.setTour(new ArrayList<Node>());
		this.setCurrNode(node);
	}

	
	
	

}
