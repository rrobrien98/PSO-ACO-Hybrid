package main.resources.PSO;
import main.ACO;
import main.PSO.Topology;
import main.resources.ACO.Graph;

public class Params {
	
	private Topology topology;
	private double[] posRange;
	private double[] velRange;
	private double chi; // constriction Factor
	private double phi;
	private int swarmSize;
	private int maxIter;
	private int maxDim;
	
	
	public main.resources.ACO.Params ACO_Params;

	public Params(
			String graphFileName,
			Topology topology, 
			double[] posRange,
			double[] velRange,
			double chi,
			double phi,
			int swarmSize,
			int maxIter,
			int maxDim
		){
		this.topology = topology;
		this.posRange = posRange;
		this.velRange = velRange;
		this.chi = chi;
		this.phi = phi;
		this.swarmSize = swarmSize;
		this.maxIter = maxIter;
		this.maxDim = maxDim;
		this.initACO(graphFileName);
	}



	
	// construct initial ACO params
	private void initACO(String graphFileName){
		Graph graph = new Graph(graphFileName, ACO.FinalSettings.numOf_colors);
		ACO_Params = new main.resources.ACO.Params(
				graph,
				ACO.FinalSettings.numOf_ants,
				ACO.FinalSettings.numOf_iter,
				ACO.FinalSettings.alpha,
				ACO.FinalSettings.beta,
				ACO.FinalSettings.rho,
				ACO.FinalSettings.eFactor,
				ACO.FinalSettings.satLimit,
				ACO.FinalSettings.timeLimit,
				ACO.FinalSettings.optDist,
				ACO.FinalSettings.numOf_colors
				);
	}
	
	
	//getters
	public Topology getTopology() {return topology;}
	public double[] getPosRange() {return posRange;}
	public double[] getVelRange() {return velRange;}
	public double getChi() {return chi;}
	public double getPhi() {return phi;}
	public int getSwarmSize() {return swarmSize;}
	public int getMaxIter() {return maxIter;}
	public int getMaxDim() {return maxDim;}
	
}
