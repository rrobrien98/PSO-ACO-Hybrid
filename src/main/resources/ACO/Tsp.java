package main.resources.ACO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/*
 * Object to store and update a Traveling Sales Person problem.
 * Contains functions to initialize a problem, functions to calculate important 
 * about the problem, and functions to get and set variables.
 */
public class Tsp {
	private Random rand = new Random();
	private int numOf_cities;
	private Leg[][] city_arr;
	private ArrayList<double[]> coords;
	private String filename;
	
	public ArrayList<Ant> ants;
	
	private double tau_0;

	/*
	 * Constructor. sets all variables and parameters and stores the file to be turned into
	 * the TSP.
	 */
	public Tsp(String filename){
		this.ants = new ArrayList<Ant>();
		this.filename = filename;
		this.city_arr = null;
		this.coords = new ArrayList<double[]>();
		this.extract_coords();
		this.create_array();
		this.numOf_cities = this.coords.size();
		this.tau_0 = this.calcTauNot();
		this.initPheremone();
	}
	
	/*
	 * gets all the coordinates from a TSP problem formatted as a list of a city number and two
	 * coordinates and must end with EOF. i.e.
	 * 1 12 24
	 * 2 32 8
	 * EOF
	 * Anything else will not be extracted correctly and will not work.
	 */
	public void extract_coords() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.filename));
			String line;
			Boolean firstCoord = false;
			while(!(line = br.readLine()).equals("EOF")) {
				String[] parts = line.trim().split(" ");
				//once it finds the first city start storing coordinates
				if (line.trim().startsWith("1")) {
					firstCoord = true;
				}
				if(firstCoord) {
					double[] coord = new double[2];
					coord[0] = Double.parseDouble(parts[1].trim());
					coord[1] = Double.parseDouble(parts[2].trim());
					this.coords.add(coord);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.print("Input File Not Found.");
		} catch (IOException e) {
			System.out.print("IO error in br.readline()");
			e.printStackTrace();
		}
	}
	
	/*
	 * Creates an array of legs based off of the coordinates read out of the input file.
	 * uses a nested for loop to iterate through every combo of cities.
	 */
	public void create_array() {
		//initialize the array of legs.
		this.city_arr = new Leg[this.coords.size()][this.coords.size()];
		//iterate through every city.
		for (int i = 0; i < this.coords.size(); i++) {
			//iterate through every city.
			for (int j = 0; j < this.coords.size(); j++) {
				//use pythagorean theorem to calculate distance.
				double distance =  Math.sqrt(
						Math.pow(this.coords.get(i)[0] - this.coords.get(j)[0], 2) 
						+ Math.pow(this.coords.get(i)[1] - this.coords.get(j)[1], 2)
				);
				this.city_arr[i][j] = new Leg(0, distance);//store leg
			}
		}
	}
	
	/*
	 * Calculates the TauNot for the given TSP problem. TauNot is simply making a tour by taking the path
	 * to the next closest city that hasn't been visited yet. Then dividing 1 by the number of cities
	 * multiplied by this tour length. 
	 */
	public double calcTauNot() {
		ArrayList<Integer> visited = new ArrayList<Integer>();//array to store visited cities
		int curr_city = 0; 
		double length = 0;//total length
		visited.add(curr_city);
		//build a complete tour
		System.out.print("\n				tour ("+this.getNumOf_cities()+"):	");
		while (visited.size() < this.numOf_cities) {
			int next = 0; // closest to current city
			double legDist = Double.MAX_VALUE; 
			//iterate through every city.
			for (int i = 0; i < this.numOf_cities; i++) {
				//check if it has already been visited and if it hasn't, if it's closer than the current closest.
				if (!visited.contains(i) && this.getDist(curr_city, i) < legDist) {
					next = i;
					legDist = this.getDist(curr_city, i);
				}
			}
			//add the closest city to the list of visited cities, and add its length.
			curr_city = next;
			visited.add(next);
			length += legDist;	
			if(visited.size()%400 == 0) System.out.print(visited.size()+" ");
		}
		//add the length of returning to the starting city.
		int first = visited.get(0);
		int last = visited.get(visited.size()-1);
		length += this.getDist(last, first);
		//calculate and return TauNot.
		System.out.print(" <- (init)");
		return 1/(this.numOf_cities * length);
	}
	
	/*
	 * Initialize the pheremone at every leg to tauNot.
	 */
	public void initPheremone() {
		//iterate through every city.
		for (int i = 0; i < this.numOf_cities; i++) {
			//iterate through every city.
			for (int j = 0; j < this.numOf_cities; j++) {
				this.city_arr[i][j].setPheremone(this.tau_0);
			}
		}
	}
	
	/*
	 * Calculate the length of an optimal tour from an input .opt.tour file
	 */
	public double calcOptimal(String filename) {
		//array to store the optimal tour.
		ArrayList<Integer> cities = new ArrayList<Integer>();
		double tour_len = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			Boolean firstCity = false;
			String line = br.readLine();
			while(!line.equals("EOF") && !line.equals("-1")) {//stop cases are EOF or -1
				//once the first city is reached start recording cities.
				if (line.trim().startsWith("1")) {
					firstCity = true;
				}
				if(firstCity) {//.opt.tour files use 1 based indexing so subtract 1 to change to 0 based.
					cities.add(Integer.parseInt(line) - 1);
				}
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.print("Input File Not Found.");
		} catch (IOException e) {
			System.out.print("IO error in br.readline()");
			e.printStackTrace();
		}
		//iterate through every city and calculate each leg
		for (int i = 0; i < cities.size(); i++) {

			int curr_city = cities.get(i);
			int next_city = cities.get((i+1) % cities.size());//mod allows us to calculate last to first
			tour_len += this.city_arr[curr_city][next_city].getDist();
		}
		return tour_len;
	}
	
	/*
	 * Getters and Setters for TSP variables
	 */
	public int getNumOf_cities() {
		return this.numOf_cities;
	}
	public Leg[][] getCityArr() {
		return this.city_arr;
	}
	public double getDist(int city1, int city2) {
		return this.city_arr[city1][city2].getDist();
	}
	public double getTau_0() {
		return tau_0;
	}
	public int getRandCity() {
		return rand.nextInt(this.numOf_cities);
	}
	
	public Leg getLeg(int city1, int city2) {
		return this.city_arr[city1][city2];
	}
	
	
}