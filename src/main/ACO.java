package main;
import java.util.ArrayList;
import java.util.Random;
import main.resources.ACO.Ant;
import main.resources.ACO.Graph;
import main.resources.ACO.Params;
import main.resources.ACO.Leg;
import main.resources.ACO.Tsp;

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
			colony.add(new Ant(tsp.getRandCity()));
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
		while(iterCount < params.getNumOf_iterations() && bestPath.getDist() > params.getSatLimit()
				&& duration < params.getTimeLimit()){
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
		System.out.print("\n				tour ("+this.tsp.getNumOf_cities()+"):	");
		int tour_len = 0;
		//until the tour has had time to visit every city keep going
		while (tour_len < this.tsp.getNumOf_cities()) {
			if (this.params.getType() == Type.eac) this.eliteMoveAll();
			else this.acsMoveAll();
			tour_len++;
			if(tour_len%400 == 0) System.out.print(tour_len+" ");
		}
		//ants count the distance for the return to the city they started at.
		for (int i = 0; i < this.colony.size(); i++) {//iterate through each ant
			Ant ant = this.colony.get(i);
			ant.moveToCity(ant.getTour().get(0), this.tsp.getDist(ant.getCurrCity(), ant.getTour().get(0)));
			if (this.params.getType() == Type.acs) {//acs has to wear away pheremone when an ant travels a leg
				this.wearAwayPheremone(ant.getCurrCity(), ant.getTour().get(0));
			}
		}
		System.out.print(" -- ");
	}
	
	
//	=============== Moving Ants ===============
	
	
	/*
	 * Moves each ant for the EAC type of ACO algorithm.
	 */
	public void eliteMoveAll() {
		//iterate through each ant
		for (int i = 0; i < this.params.getNumOf_ants(); i++) {
			this.selectNext(this.colony.get(i));
		}
	}
	
	/*
	 * Moves each ant for the ACS type of ACO algorithm.
	 * Implements a chance for ants to simply select the best next city.
	 * Wears away pheremone after each ant travels each leg.
	 */
	public void acsMoveAll() {
		//iterate through each ant
		for (int i = 0; i < this.params.getNumOf_ants(); i++) {
			Ant ant = this.colony.get(i);
			//potentially choose the next best city for the tour.
			boolean choose_best = rand.nextDouble() < this.params.getQ_0();
			if (choose_best)  this.selectBest(ant);
			//otherwise choose another city normally.
			else  this.selectNext(ant);
			//wear away the pheremone on the most recent leg
			if (ant.getTour().size()>=2) {//make sure the ant has traveled a leg
				this.wearAwayPheremone(ant.getTour().get(ant.getTour().size() - 1), 
						ant.getTour().get(ant.getTour().size() - 2));
			}
					
		}
	}
	
	
//	=============== Select Next City ===============
	
	
	/*
	 * Selects the best city to travel to from the current city for the input ant.
	 * First it makes an array of cities that haven't been visited yet.
	 * Then use pheremone levels and distance to calculate how "good" each city that
	 * hasn't been visited is and travel to the best.
	 */
	public void selectBest(Ant ant) {
		//find all cities that haven't been visited.
		ArrayList<Integer> cities_nonVisited = new ArrayList<Integer>();
		for (int i = 0; i < tsp.getNumOf_cities(); i++) {
			if (!ant.getTour().contains(i)  && i != ant.getCurrCity()) cities_nonVisited.add(i);
		}
		double best_path = 0;
		int best_city = 0;
		//iterate through each unvisited city.
		for (int next : cities_nonVisited) {
				Leg leg = tsp.getCityArr()[ant.getCurrCity()][next];
				//calculate how "good" the city is.
				if (leg.getPheremone() * Math.pow((1/leg.getDist()), this.params.getBeta()) > best_path) {
					best_path = leg.getPheremone() * Math.pow((1/leg.getDist()), this.params.getBeta());
					best_city = next;
				}
		}
		ant.moveToCity(best_city, this.tsp.getDist(ant.getCurrCity(), best_city));
	}
	
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
		ArrayList<Integer> cities_nonVisited = new ArrayList<Integer>();
		for (int i = 0; i < tsp.getNumOf_cities(); i++) {
			if (!ant.getTour().contains(i) && i != ant.getCurrCity()) {
				cities_nonVisited.add(i);
			}
		}
		ArrayList<Double> probs = new ArrayList<Double>();
		double total_prob = 0;
		//calculate all probabilities and add to total probability.
		for (int next : cities_nonVisited) {
			Leg leg = tsp.getLeg(ant.getCurrCity(), next);
			double leg_prob = Math.pow(leg.getPheremone(), this.params.getAlpha()) * Math.pow((1/leg.getDist()), this.params.getBeta());
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
		int nextCity = cities_nonVisited.get(index);
		double distance = (this.tsp.getDist(ant.getCurrCity(), nextCity));
		ant.moveToCity(nextCity, distance);
	}
	
	
//	=============== Pheremone Updating ===============
	
	
	/*
	 * Function to pick how to update pheremone based on type of ACO algorithm.
	 */
	public void updatePheromone() {
		if (this.params.getType() == Type.eac) this.updateElitePheremone();
		else this.updateAcsPheremone();
	}
	
	/*
	 * Updates the pheremone for the EAC type of ACO.
	 * First it uses nested for loops to iterate through every leg. In order to avoid extra work only 
	 * half of the legs are calculated because the TSP problems are symmetrical.
	 * Then it dissipates the pheremone for each leg and checks if the best path traveled the leg.
	 * If the best path did travel the leg it adds the elitist pheremone to the leg.
	 * Then updates both directions of the leg to the new pheremone level.
	 */
	public void updateElitePheremone() {
		//iterate through every city.
		System.out.print("pher: ");
		for (int i = 0; i < this.tsp.getNumOf_cities(); i++) {
			if(i%400 == 0) System.out.print(i+" ");
			//iterate through every city with index lower than i.
			for (int j = 0; j < i; j++) {
				//store pheremone in variable and dissipate a bit.
				double pheremone = this.tsp.getLeg(i, j).getPheremone() * (1 - this.params.getRho());
				//add pheremone for every ant that traveled the leg
				pheremone += this.totalPheremone(i, j);
				if (this.bestPath.tourHasLeg(i, j)) {//if the best path travels the leg add pheremone.
					pheremone += this.params.getElitism_factor() * (1/this.bestPath.getDist());
				}
				//set both directions of the leg to the new pheremone.
				this.tsp.getLeg(i,j).setPheremone(pheremone);
				this.tsp.getLeg(j,i).setPheremone(pheremone);
			}
		}
	}
	
	/*
	 * Updates the pheremone for the ACS type of ACO.
	 * First it uses nested for loops to iterate through every leg. In order to avoid extra work only
	 * half of the lefs are calculated because hte TSP problems are symmetrical.
	 */
	public void updateAcsPheremone() {
		//iterate through every city.
		for (int i = 0; i < this.tsp.getNumOf_cities(); i++) {
			//iterate through every city with index lower than i.
			for (int j = 0; j < i; j++) {
				//store pheremone in variable and dissipate a bit.
				double pheremone = this.tsp.getCityArr()[i][j].getPheremone() * (1 - this.params.getRho());
				if (this.bestPath.tourHasLeg(i, j)) {//if the best path travels the leg add pheremone.
					pheremone += this.params.getRho() * (1/this.bestPath.getDist());
				}
				//set both directions of the leg to the new pheremone.
				this.tsp.getCityArr()[i][j].setPheremone(pheremone);
				this.tsp.getCityArr()[j][i].setPheremone(pheremone);
			}
		}
	}
	
	/*
	 * Function to wear away the pheremone when an ant travels a leg in ACS.
	 */
	public void wearAwayPheremone(int i, int j) {
		double pheremone = this.tsp.getCityArr()[i][j].getPheremone();
		pheremone *= (1 - this.params.getEpsilon());
		pheremone += this.params.getEpsilon() * this.tsp.getTau_0();
		//set both directions of leg to the new pheremone level.
		this.tsp.getCityArr()[i][j].setPheremone(pheremone);
		this.tsp.getCityArr()[j][i].setPheremone(pheremone);
	}
	
	/*
	 * Function to lay down pheremone for every ant that traveled the input leg
	 */
	public double totalPheremone(int i, int j) {
		double total_pheremone = 0;
		//iterate through each ant
		for (int a = 0; a < this.colony.size(); a++) {
			Ant ant = this.colony.get(a);
			if (ant.tourHasLeg(i, j)) {//if ant traveled the leg add pheremone
				total_pheremone += (1 / ant.getDist());
			}
		}
		return total_pheremone;
	}
	
	
//	=============== Other functions ===============
	
	
	/*
	 * Updates the best found path so far. If an ant has found a better path than
	 * the current best it sets the bestPath object to a clone of that ant.
	 */
	public void updateBest() {
		//iterate through every ant
		for (int i = 0; i < this.params.getNumOf_ants(); i++) {
			//if the current ant's total distance is shorter than the best distance
			if (this.colony.get(i).getDist() < this.bestPath.getDist()) {
				this.bestPath = this.colony.get(i).clone();	
			}
		}
	}

	/*
	 * Resets the ants tour arrays and puts them at a random city.
	 */
	public void resetAnts() {
		//iterate through every ant.
		for (int i = 0; i < this.params.getNumOf_ants(); i++) {
			this.colony.get(i).reset(tsp.getRandCity());
		}			
	}
	
	/*
	 * Returns a result object storing all relevant information to a run of the ACO
	 * algorithm.
	 */
	public Result get_res() {
		return new Result(params, bestPath, iterCount, duration, tsp);
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
		private long duration_median;
		private Tsp tsp;
		private double opt_len = 1;
		private double best_len;
		private double best_len_median;
		private int count;
		
		public Result(int count, Params params, Ant bestPath, int iters, long duration, Tsp tsp) {
			this.duration = duration;
			this.params = params;
			this.bestPath = bestPath;
			this.iters = iters;
			this.tsp = tsp;
			this.best_len = bestPath.getDist();
			this.count = count;
			if (params.getOptFile() != null) {
				opt_len = params.getOptDist()==Double.MAX_VALUE? tsp.calcOptimal(params.getOptFile()):params.getOptDist();
			}else {
				opt_len = params.getOptDist();
			}
		}	
		public int getCount() {
			return count;
		}
		/** Print out the results in a nice pretty way.*/
		public void print() {
			System.out.println("============ ACO Results ============\n");
			System.out.println("ACO type: " + params.type);
			System.out.println("Number of Ants: " + params.getNumOf_ants());
		    System.out.println("Iterations: " + iters);
			System.out.println("Duration: " + duration + " milliseconds");
			System.out.println("Number of Cities: " + tsp.getNumOf_cities());
			System.out.println("Optimal Tour Length: " + opt_len + "\n");
			System.out.println("Best Path Value: " + bestPath.getDist());
			System.out.println("Percent off from Optimal: " + (best_len/opt_len*100 - 100.0) + "%");
			System.out.print("Best Path:\n{ ");
			for (int i = 0; i < bestPath.getTour().size(); i++) {
				System.out.print(bestPath.getTour().get(i));
				if ((i+1) < bestPath.getTour().size()) System.out.print(" , ");
				if ((i + 1) % 25 == 0) System.out.println();
			}
			System.out.println(" }\n");
		}
		/*
		 * Setters and Getters
		 */
		public double getBestToOptRatio() {return (best_len/opt_len);}
		public double getBestToOptRatio_median() {return best_len_median/opt_len;}
		public int getIters() {return iters;}
		public long getDur() {return duration;}
		public void setDur(long time) {this.duration = time;}
		public int getBestDist() {return (int)best_len;}
		public int getOptDist() {return (int)opt_len;}
		public void setBestDist(double dist) {this.best_len = dist;}
		public void setDur_median(long time) {this.duration_median = time;}
		public long getDur_median() {return duration_median;}
		public void setBestDist_median(double dist) {this.best_len_median = dist;}
		public double getBestDist_median() {return this.best_len_median;}		
	}
	

}
