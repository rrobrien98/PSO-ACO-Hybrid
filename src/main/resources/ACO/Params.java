package main.resources.ACO;
import main.ACO.Type;

/*
 * Class to store all relevant parameters for a run of an ACO. not all parameters are initialized
 * for every run, as not all parameters are needed.
 */
public class Params {
	//parameters for stop cases
	private int iterLimit;
	private double satLimit = 0;//initialize to base case of zero incase it isn't changed
	private long timeLimit;
	//parameters controlling type of aco
	private double optDist;
	public Type type;
	private int numOf_colors;
	private int numOf_ants;
	private int numOf_iterations;
	//parameters for calculations
	private double alpha;
	private double beta;
	private double rho;
	private double elitism_factor;
	private double epsilon;
	private double q_0;
	
	private Graph graph;
	

	/*
	 * Initialize parameters object for the Elitist Ant Colony type of ACO
	 */
	public Params(Graph graph, int num_ants, int num_iterations, double alpha,
			double rho, double eFactor, double satLimit, long timeLimit, double optDist) {
		this.numOf_ants = num_ants;
		this.numOf_iterations = num_iterations;
		this.alpha = alpha;
		this.rho = rho;
		this.elitism_factor = eFactor;
		this.type = Type.eac;
		this.satLimit = satLimit;
		this.timeLimit = timeLimit;
		this.optDist = optDist;
		this.numOf_colors = graph.getNumOf_colors();
		this.graph = graph;
	}

	
	public void print() {
		System.out.print("ants-"+this.numOf_ants+ " iter-"+this.numOf_iterations+ " a-"+this.alpha+ " b-"+
				this.beta+ "rho-"+this.rho+ " epsi-"+this.epsilon+" qo-"+this.q_0+" sat-"+this.satLimit+
				"tMax-"+this.timeLimit+ " optDist-"+this.optDist);
	}
	
	/*
	 * getters and Setters for parameters
	 */
	public void setGraph(Graph graph) {
		this.graph = graph;
	}
	public int getNumOf_ants() {
		return numOf_ants;
	}
	public int getNumOf_iterations() {
		return numOf_iterations;
	}
	public double getAlpha() {
		return alpha;
	}
	public double getBeta() {
		return beta;
	}
	public double getRho() {
		return rho;
	}
	public double getElitism_factor() {
		return elitism_factor;
	}
	public double getEpsilon() {
		return epsilon;
	}
	public double getQ_0() {
		return q_0;
	}

	public double getSatLimit() {
		return satLimit;
	}
	public int getIterLimit() {
		return iterLimit;
	}
	public Type getType() {
		return this.type;
	}
	public long getTimeLimit() {
		return timeLimit;
	}
	public double getOptDist() {
		return optDist;
	}
	public Graph getGraph() {
		return graph;
	}



	public int getColors() {
		return numOf_colors;
	}
	
}
