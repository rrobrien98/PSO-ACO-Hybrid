package main.resources.ACO;

import java.util.ArrayList;

public class Ant {
	
	private ArrayList<Integer> tour;
	private double dist;
	private int currCity;
	
	
	public Ant(int city) {
		this.setTour(new ArrayList<Integer>());
		this.setCurrCity(city);
		this.setDist(Double.MAX_VALUE);
	}
	
	public Ant(Ant model) {
		this.tour = model.tour;
		this.dist = model.dist;
		this.currCity = model.currCity;
	}
	public Ant clone() {return new Ant(this);}

	
	public double getDist() {
		return dist;
	}

	public void setDist(double dist) {
		this.dist = dist;
	}

	public int getCurrCity() {
		return currCity;
	}

	public void setCurrCity(int currCity) {
		this.currCity = currCity;
	}

	public ArrayList<Integer> getTour() {
		return tour;
	}

	public void setTour(ArrayList<Integer> tour) {
		this.tour = tour;
	}


	public boolean tourHasLeg(int i, int j) {
		int index_i = this.tour.indexOf(i);
		int next_index; int prev_index;
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
		if (this.tour.get(next_index) == j || this.tour.get(prev_index) == j) return true;
		return false;
	}
	

	public void moveToCity(int city, double dist) {
		this.dist = (this.dist == Double.MAX_VALUE) ? dist : this.dist+dist;
		this.setCurrCity(city);
		this.tour.add(city);
	}
	
	
	public void reset(int city){
		this.setTour(new ArrayList<Integer>());
		this.setCurrCity(city);
		this.setDist(0);
	}

	
	
	

}
