package main;
import java.util.ArrayList;
import java.util.Random;
import main.resources.PSO.*;
import main.ACO;

public class PSO {
	
	public enum Topology { 
		gl, ri, vn, ra, _error;
		public static int RA_NHOOD_SIZE = 5;
		public static int VN_ARRAY_HEIGHT = 4; // description
		public static int VN_ARRAY_WIDTH = 4;  // same here..
	};
	double CHI = 0.7298; // constriction Factor
	double PHI = 2.05; 
	double[] posRanges = {15.0, 30.0};
	double[] velRanges = {-2.0, 4.0};
	
	
	Params params;
	ArrayList <Particle> particles;
	Particle gBest; 
	Boolean bestFound = false; // make sure we change this value
	int iterationCount = 0;
	Random rand = new Random();
	ArrayList<Integer> progress;
	
	public PSO(Params params){
		this.params = params;
		this.particles = new ArrayList<Particle>();
		this.progress = new ArrayList<Integer>();
		runPSO();
	}
	
	// description
	private void runPSO() {
		initParticles();
		setNeighbors();
		System.out.print("Progress: ");
		while(iterationCount < params.getMaxIter() && !bestFound) {
			_progressReport();
			evalParticles();
			update_Pbests();
			update_Gbest();
			update_All_NHoodBest();
			update_AllVel();
			update_AllPos();
			iterationCount++;
		}
	}
	
	
	// description
	private void initParticles() {
		for (int i = 0; i < params.getSwarmSize(); i++) {
			particles.add(new Particle(
					params.getPosRange()[0], params.getPosRange()[1], 
					params.getVelRange()[0], params.getVelRange()[1], 
					params.getMaxDim()));
		}
		gBest = particles.get(0).clone(); // set random particle as best
	}
	
	
	
	
	/*
	 *  ----  NEIGHBORHOOD INIT (Topologies) ----
	 */
	private void setNeighbors() {
		switch (params.getTopology()) {
		case ri: setRingNeighbors(); break;
		case vn: setVnNeighbors(); break;
		case ra: setRandNeighbors(); break;
		case gl: break;
		default: Lab.throwError(Lab.Error.topology);
		}
	}
	// description
	private void setRandNeighbors() {
		int k = Topology.RA_NHOOD_SIZE;
		for (int i = 0; i < particles.size(); i++) {
			while (particles.get(i).getNhood().size() < k-1) {
				Particle toAdd = particles.get(rand.nextInt(particles.size()));
				if (!particles.get(i).getNhood().contains(toAdd)) {
					particles.get(i).addNb(toAdd);
				}
			}
		}
	}
	// description
	private void setVnNeighbors() {
		int arrHeight = Topology.VN_ARRAY_HEIGHT;
		int arrWidth = Topology.VN_ARRAY_WIDTH;
		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).addNb(particles.get(((i-1) + arrHeight*arrWidth)%arrHeight*arrWidth));
			particles.get(i).addNb(particles.get(((i+1))%arrHeight*arrWidth));
			particles.get(i).addNb(particles.get(((i-arrWidth) + arrHeight*arrWidth)%arrHeight*arrWidth));
			particles.get(i).addNb(particles.get(((i+arrWidth))%arrHeight*arrWidth));
		}
	}
	//description
	private void setRingNeighbors() {
		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).addNb(particles.get(((i-1) + particles.size())%particles.size()));
			particles.get(i).addNb(particles.get(((i+1))%particles.size()));
		}
	}
	
	
	
	
	/*
	 *  ---- PARTICLES EVALUATIONS ----
	 */
	
	// description
	private void update_AllVel() {
		for (int i = 0; i < params.getSwarmSize(); i++) {
			for(int d = 0; d < params.getMaxDim(); d++) {
				// ****** compute the acceleration due to personal best
				double pBestAttract = particles.get(i).getPBest_coorDim(d) - particles.get(i).getPos_coorDim(d);
				pBestAttract *= rand.nextDouble() * params.getPhi();
	
			    // ****** compute the acceleration due to global best
				double gBestAttract = gBest.getPBest_coorDim(d) - particles.get(i).getPos_coorDim(d);
				gBestAttract *= rand.nextDouble() * params.getPhi();
				
			    // ****** constrict the new velocity and reset the current velocity
				particles.get(i).setVel_coorDim(d, params.getChi()*(pBestAttract + gBestAttract) );	
			}	
		}
	}
	// description
	private void update_AllPos() {
		for (int i = 0; i < params.getSwarmSize(); i++) {
			for(int d = 0; d < params.getMaxDim(); d++) {
				double pos = particles.get(i).getPos_coorDim(d);
				double vel = particles.get(i).getVel_coorDim(d);
				particles.get(i).setPos_coorDim(d, pos+vel);
			}
		}
	} 
	// description
	private void evalParticles() {
		for (int i = 0; i < params.getSwarmSize(); i++) {
			Particle particle = particles.get(i);
			Double val = null; 
			evalACO(particles);
			particle.setVal(val);
		}
	}


	
	
	
	/*
	 *  ---- TEST FUNCTIONS  ----
	 */
	
	// description
	private int evalACO(Particle particle) {
		ACO.Result res = new ACO(
				params.ACO_Params.getGraph(),
				new main.resources.ACO.Params(
						params.ACO_Params.getOptFile(), 
						params.ACO_Params.getNumOf_ants(), 
						params.ACO_Params.getNumOf_iterations(), 
						particle.getAlpha(),
						particle.getBeta(),
						particle.getRho(),
						particle.getEFactor(),
						params.ACO_Params.getSatPercent(), 
						params.ACO_Params.getTimeLimit(), 
						params.ACO_Params.getOptDist())
				).get_res();
		return res.getCount();
	}
	
	
	private double evalRosenbrock (Particle particle) {
		double retVal = 0;
		for (int i = 0; i < params.getMaxDim() - 1; i++) {
			double val = particle.getCoords()[i];
			double next_val = particle.getCoords()[i+1];
		    retVal += 100* Math.pow(next_val - val*val, 2.0) + Math.pow(val-1.0, 2.0);
		}
		return retVal;
	}
	// description
	private double evalRastrigin (Particle particle) {
	
		double retVal = 0;
		for (int i = 0; i < params.getMaxDim(); i++) {
			double val = particle.getCoords()[i];
			retVal+= val * val - 10.0*Math.cos(2.0*Math.PI*val) + 10.0;
		}
		return retVal;
	}
	// description
	private double evalSphere (Particle particle) {
		double retVal = 0;
		for (int i = 0; i < params.getMaxDim(); i++) {
			double val = particle.getCoords()[i];
			retVal += val * val;
		}
		return retVal;
	} 
	// description
	private double evalAckley(Particle particle) {
		   double firstSum = 0;
		   double secondSum = 0;
		   for (int i = 0; i < params.getMaxDim(); i++) {
			   firstSum += particle.getCoords()[i] * particle.getCoords()[i];
			   secondSum += Math.cos(2.0*Math.PI*particle.getCoords()[i]);
		   }
		   return -20.0 * Math.exp(-0.2 * Math.sqrt(firstSum/2.0)) - 
		     Math.exp(secondSum/2.0) + 20.0 + Math.E;
	}
	
	
	
	
	
	/*
	 *  ---- UPDATE SEARCH STATES  ----
	 */
	
	// description
	private void update_Pbests() {
		for (int i = 0; i < this.particles.size(); i++) {
			Particle currParticle = this.particles.get(i);
			if (currParticle.getCurrVal() < currParticle.getPbestval()) {
				currParticle.setPbestval(currParticle.getCurrVal());
				currParticle.setPbest(currParticle.getCoords().clone());
			}
		}
	}
	private void update_Gbest() {
		for (int i = 0; i < this.particles.size(); i++) {
			Particle currParticle = particles.get(i);
			if (currParticle.getCurrVal() < gBest.getCurrVal()) {
				gBest = currParticle.clone();
				if(gBest.getCurrVal() == 0.0) bestFound = true;
			}
		}
	}
	private void update_All_NHoodBest() {
		for (int i = 0; i < this.particles.size(); i++) {
			Particle currParticle = particles.get(i);
			for (int j = 0; i < currParticle.getNhood().size(); i++) {
				if (currParticle.getNhood().get(j).getCurrVal() < currParticle.getCurrVal()) {
					currParticle.setNhoodBestval(currParticle.getNhood().get(j).getCurrVal());
					currParticle.setNhoodBest(currParticle.getNhood().get(j).getCoords().clone());
				}
			}
		}
	}

	
	
	
	// descriptions
	public Result getResult() {
		return new Result(gBest, iterationCount, params.getTopology(), params.getTestFunc());
	}
	class Result{
		private Particle gBest;
		private int iterationCount;
		private Topology topology;
		private TestFunc testFunc;
		public Result(Particle gBest, int iterationCount, Topology topology, TestFunc testFunc) {
			this.gBest = gBest;
			this.iterationCount = iterationCount;
			this.topology = topology;
			this.testFunc = testFunc;
		}
		public void print() {
			System.out.println("\n\n----  ** PSO RESULTS ** ----\n");
			System.out.println("Best Found: "+this.gBest.getCurrVal());
			System.out.println("Topology: "+this.topology);
			System.out.println("Test Func: "+this.testFunc);
			System.out.println("Iterations: "+this.iterationCount);
		}
		//getters
		public Particle getGBest() {return gBest;}
		public int getIterCount() {return iterationCount;}
		public Topology getTopology() {return topology;}
		public TestFunc getTestFunc() {return testFunc;}
		
	}
	
	
	// shows progress in terminal
	private void _progressReport() {
		if(iterationCount != 0) {
			double progress = ((double)iterationCount/(double)params.getMaxIter())*100;
			for(int i=0; i<100; i+=5) {
				if(this.progress.contains(i)) continue;
				else if((int)progress > i) {
					this.progress.add(i);
					System.out.print(i+" ");
				}
			}
		}
	}
			
}
