package main.resources.PSO;
import main.ACO;
import main.PSO;
import main.PSO.Topology;
import main.resources.ACO.Graph;
/*
 * holds the parameters for a PSO object
 */
public class Params {
	
	private Topology topology;
	private double chi; 
	private double phi;
	private int swarmSize;
	private int maxIter;
	private int maxDim;
		
	
	public main.resources.ACO.Params ACO_Params;

	public Params(String graphFileName){
		this.topology = PSO.Topology.ra;
		this.chi = PSO.FinalSettings.CHI;
		this.phi = PSO.FinalSettings.PHI;
		this.swarmSize = PSO.FinalSettings.swarmSize;
		this.maxIter = PSO.FinalSettings.maxIter;
		this.maxDim = PSO.FinalSettings.maxDim;
		this.initACO(graphFileName);
	}



	
	// construct initial ACO params
	private void initACO(String graphFileName){
		Graph graph = new Graph(graphFileName, ACO.FinalSettings.graphDims);
		ACO_Params = new main.resources.ACO.Params(
				graph,
				ACO.FinalSettings.numOf_ants,
				ACO.FinalSettings.numOf_iter,
				ACO.FinalSettings.alpha,
				ACO.FinalSettings.rho,
				ACO.FinalSettings.eFactor,
				ACO.FinalSettings.satLimit,
				ACO.FinalSettings.timeLimit,
				ACO.FinalSettings.optDist
				);
	}
	
	
	//getters
	public Topology getTopology() {return topology;}
	public double getChi() {return chi;}
	public double getPhi() {return phi;}
	public int getSwarmSize() {return swarmSize;}
	public int getMaxIter() {return maxIter;}
	public int getMaxDim() {return maxDim;}
	
}
