package main;
import java.util.ArrayList;
import java.util.Random;
import main.resources.ACO.Ant;
import main.resources.ACO.Graph;
import main.resources.ACO.Params;
import main.resources.ACO.Leg;
import main.resources.ACO.Node;

/*
 * Class in control of running the entire ACO algorithm, whether it is EAC or ACS.
 * The aco can be terminated after a certain number of iterations, a certain amount of time,
 * or if it reaches a certain percentage of the optimal solution.
 */
public class ACO {
	public enum Type {eac, acs};
	Random rand;
	Params params;
	Graph graph;
	Ant bestPath;
	int iterCount;
	long startTime;
	long duration;
	ArrayList<Ant> colony;
	
	/*
	 * Constructor for an ACO object.
	 * Stores necessary variables and objects, intitializes the colony of ants, 
	 * initializes a running time, and then calls runACO to actually run the algorithm.
	 */
	public ACO(Graph graph, Params params) {
		this.params = params;
		this.rand = new Random();
		this.graph = graph;
		this.iterCount = 0;
		this.colony = new ArrayList<Ant>();
		this.initialize_colony();
		runACO();
		duration = System.currentTimeMillis() - startTime;
	}
	
	/*
	 * Initializes a colony of ants and gives them a random starting city.
	 * Sets bestPath to a random ant so it can be compared.
	 */
	public void initialize_colony() {
		for (int i = 0; i < this.params.getNumOf_ants(); i++) {
			colony.add(new Ant(graph.getRandNode()));
		}
		this.bestPath = colony.get(0).clone();
	}
	
	
	/*
	 *  Runs the main loop of the ACO.
	 *  First it sends the ants on parallel tours.
	 *  Next it updates the best solution found.
	 *  After that it updates the Pheremone on the TSP.
	 *  Finally it resets all the ants and updates the iteration count and duration.
	 */
	public void runACO(){
		startTime = System.currentTimeMillis();
		duration = 0;
		while(	iterCount < params.getNumOf_iterations() && 
				bestPath.getTourLen() > params.getSatLimit() &&
				duration < params.getTimeLimit()){
			this.tour();
			this.updateBest();
			this.updatePheromone();
			this.resetAnts();
			iterCount++;//update iterations
			duration = System.currentTimeMillis() - startTime;//update duration
		}
	}
	
	/*
	 * Runs a single tour of the ACO algorithm. Iterates through the number of cities and moves every ant
	 * for each city. This allows the ants to build tours parallel to each other rather than one after the other.
	 */
	public void tour() {
		int tour_len = 0;
		//until the tour has had time to visit every city keep going		
		while (tour_len < this.graph.getNumOf_nodes()) {
			//Moves each ant
			for(Ant ant: colony) {
				if (ant.cont()) {
					this.selectNext(ant);
				}
			}
			tour_len++;
		}
	}
	


	
	
//	=============== Select Next City ===============
	
	

	
	/*
	 * Selects the next city to travel to for the input ant.
	 * First it makes an array of cities that the ant hasn't visited.
	 * Then it calculates a probability that the ant visits each city, and adds all 
	 * the probabilities together.
	 * Then takes a random percent of this total probability and keeps subtracting probabilities
	 * until the total probability goes below zero. It then takes the city corresponding to the
	 * last probability as the next city.
	 */
	public void selectNext(Ant ant) {
		//find all cities that haven't been visited.
		ArrayList<Node> clearNodes = graph.getClearNodes(ant);

		ArrayList<Double> probs = new ArrayList<Double>();
		double total_prob = 0;
		//calculate all probabilities and add to total probability.
		for (Node clearNode : clearNodes) {
			Leg leg = graph.getLeg(ant.getCurrNode(), clearNode);
			double leg_prob = Math.pow(leg.getTotalPheremone(), this.params.getAlpha()) * Math.pow((1/leg.getDist()), this.params.getBeta());
			total_prob += leg_prob;
			probs.add(leg_prob);
		}
		//use a random percent of total probability to set next city index.
		int index = -1;//city number
		total_prob *= rand.nextDouble();//random percent of total prob.
		while (total_prob >= 0) {
			index += 1;//next city
			total_prob -= probs.get(index);//subtract city prob from total prob.
		}
		//move the ant to the next city.
		Node nextNode = clearNodes.get(index);
		
		int color;
		if ((color = this.colorPath(ant, nextNode)) != -1) {
			ant.moveToNode(nextNode,color);
		}
		else {
			//stop the ants tour
		}
			
	}
	private int colorPath(Ant ant, Node nextNode) {
		// TODO method that loops through all colors that we could color this next leg, probabilistically selects one
		ArrayList<Integer> used_colors = new ArrayList<Integer>();
		for (Node neighbor : ant.getCurrNode().getNeighbors()) {
			Leg leg = this.graph.getLeg(ant.getCurrNode(), neighbor);
			if (leg.getColor() != -1) {
				used_colors.add(leg.getColor());
			}
		}
		for (Node neighbor : nextNode.getNeighbors()) {
			Leg leg = this.graph.getLeg(nextNode, neighbor);
			if (leg.getColor() != -1) {
				used_colors.add(leg.getColor());
			}
		}
		ArrayList<Double> probs = new ArrayList<Double>();
		ArrayList<Integer> colors = new ArrayList<Integer>();
		double total_prob = 0;
		Leg leg = graph.getLeg(ant.getCurrNode(), nextNode);
		//calculate all probabilities and add to total probability.
		for (int i = 0; i < this.params.getColors();i++) {
			if (!used_colors.contains(i)) {
				double color_prob = leg.getPheremone(i);//assuming an array of phereomones
				probs.add(color_prob);
				total_prob += color_prob;
				colors.add(i);
			}
		}
	//use a random percent of total probability to set next city index.
		if (!colors.isEmpty()) {
			int index = -1;//color number
			total_prob *= rand.nextDouble();//random percent of total prob.
			while (total_prob >= 0) {
				index += 1;//next city
				total_prob -= probs.get(index);//subtract city prob from total prob.
			}
			return colors.get(index);
		}
		
		return -1;
	
	
	
}
	
//	=============== Pheremone Updating ===============
	
	
	/*
	 * Updates the pheromone for the EAC type of ACO.
	 * First it uses nested for loops to iterate through every leg. In order to avoid extra work only 
	 * half of the legs are calculated because the TSP problems are symmetrical.
	 * Then it dissipates the pheremone for each leg and checks if the best path traveled the leg.
	 * If the best path did travel the leg it adds the elitist pheremone to the leg.
	 * Then updates both directions of the leg to the new pheremone level.
	 */
	public void updatePheromone() {
		//iterate through every city. Have to do this via the nodes because legs is a hashtable, not iteratable
		ArrayList<Leg> updatedLegs = new ArrayList<Leg>();
		for(Node node: graph.getNodes()) for(Node ne: node.getNeighbors()) {
			Leg leg = graph.getLeg(node, ne);
			if(!updatedLegs.contains(leg)){
				double[] pheromone = leg.getPheremoneArray();
				for (int i = 0; i < pheromone.length; i ++) {
					pheromone[i] *= (1-this.params.getRho());
				}
				
				//add pheromone for every ant that traveled the leg
				for(Ant ant: colony) {
					if(ant.tourHasLeg(leg)) {
						pheromone[leg.getColor()] += this.params.getElitism_factor()*ant.getTourLen(); // (1/ant.getDist)
					}
				}
				//if the best path travels the leg add pheremone.
				
				this.graph.setPheromone(leg, pheromone);//set the leg to the new pheromone.
				updatedLegs.add(leg);
			}
		}
	}

	

	
	
//	=============== Other functions ===============
	
	
	/*
	 * Updates the best found path so far. If an ant has found a better path than
	 * the current best it sets the bestPath object to a clone of that ant.
	 */
	public void updateBest() {
		for(Ant ant: colony) if(ant.getTourLen() > this.bestPath.getTourLen()) {
			this.bestPath = ant.clone();
		}
	}

	/*
	 * Resets the ants tour arrays and puts them at a random city.
	 */
	public void resetAnts() {
		//iterate through every ant.
		for(Ant ant: colony) ant.reset(graph.getRandNode());
	}
	
	/*
	 * Returns a result object storing all relevant information to a run of the ACO
	 * algorithm.
	 */
	public Result get_res() {
		return new Result(params, bestPath, iterCount, duration, graph);
	}
	
	
	
	
	
	/*
	 * Result object to store all important information about a run of the ACO
	 * algorithm. Contains a print function to format and print out all important info.
	 */
	class Result{
		private Params params;
		private Ant bestPath;
		private int iters;
		private long duration;
		private Graph graph;
		
		public Result(Params params, Ant bestPath, int iters, long duration, Graph graph) {
			this.params = params;
			this.bestPath = bestPath;
			this.iters = iters;
			this.duration = duration;
			this.graph = graph;
		}	

		/** Print out the results in a nice pretty way.*/
		public void print() {
			System.out.println("============ Results ============\n");
			System.out.println("Colored: "+bestPath.getTourLen());
		}
		
		public Params getParams() {return params;}
		public Ant getBestPath() {return bestPath;}
		public int getNumofIter() {return iters;}
		public long getDuration() {return duration;}
		public Graph getGraph() {return graph;}
	}
	

}
