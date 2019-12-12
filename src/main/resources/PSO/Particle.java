package main.resources.PSO;
import java.util.ArrayList;
import java.util.Random;



public class Particle {
	
	private ArrayList<Particle> neighbors;
	private double[] coords;
	private double[] pBest;
	private double pBestVal;
	private double[] lBest;
	private double lBestVal;
	private double[] velocities;
	private double currVal;
	private Random rand = new Random();
	
	
	public Particle(double posMin, double posMax, double velMin, double velMax, int maxDim) {
		neighbors = new ArrayList<Particle>();
		coords = new double[maxDim];
		velocities = new double[maxDim];
		for (int i = 0; i < maxDim; i++) {
			coords[i] = rand.nextDouble() * (posMax - posMin) + posMin;
			velocities[i] = rand.nextDouble() * (velMax - velMin) + velMin;
		}
		this.setPbest(coords);
		this.setNhoodBest(null);
		this.setVal(Double.MAX_VALUE);
		this.setPbestval(Double.MAX_VALUE);
		this.setNhoodBestval(Double.MAX_VALUE);
	}
	
	@SuppressWarnings("unchecked")
	public Particle(Particle model) {
		currVal = model.currVal;
		neighbors = (ArrayList<Particle>) model.neighbors.clone();
		coords = model.coords.clone();
		velocities = model.velocities.clone();
		pBest = model.pBest; lBest = model.lBest;
		pBestVal = model.pBestVal; lBestVal = model.lBestVal;
	}
	public Particle clone() {return new Particle(this);}
		
	
	// getters and setters
	public double[] getCoords() { return coords; }
	public double getPos_coorDim(int d) { return coords[d];}
	public void setPos_coorDim(int d, double pos) { coords[d] = pos;}
	
	public void setVel_coorDim(int d, double vel) { velocities[d] = vel;}
	public double getVel_coorDim(int d) { return velocities[d];}
	
	public double getCurrVal() {return currVal;}
	public void setVal(double val) { this.currVal = val;}

	public void addNb(Particle neighbor) { this.neighbors.add(neighbor);}
	public void clearNhood() { this.neighbors.clear(); }
	public ArrayList<Particle> getNhood(){return this.neighbors;}

	public double[] getNhoodBest() { return lBest;}
	public void setNhoodBest(double[] nbest) { this.lBest = nbest;}
	public double getNhoodBestval() { return lBestVal;}
	public void setNhoodBestval(double nbestval) {this.lBestVal = nbestval;}

	public double getPbestval() { return pBestVal;}
	public void setPbestval(double pbestval) { this.pBestVal = pbestval;}
	public double[] getPbest() {return pBest;}
	public double getPBest_coorDim(int d) { return pBest[d]; }
	public void setPbest(double[] pbest) {this.pBest = pbest;}

	
	// getters for vector dims
	public double getAlpha() {return coords[0];}
	public double getBeta() {return coords[1];}
	public double getRho() {return coords[2];}
	public double getEFactor() {return coords[3];}
	
	
}

