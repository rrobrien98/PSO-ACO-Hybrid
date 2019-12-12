package main.resources.PSO;
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
			Topology topology, 
			double[] posRange,
			double[] velRange,
			double chi,
			double phi,
			int swarmSize,
			int maxIter,
			int maxDim,
			Graph graph
		){
		this.topology = topology;
		this.posRange = posRange;
		this.velRange = velRange;
		this.chi = chi;
		this.phi = phi;
		this.swarmSize = swarmSize;
		this.maxIter = maxIter;
		this.maxDim = maxDim;
		this.ACO_Params.setGraph(graph);
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
