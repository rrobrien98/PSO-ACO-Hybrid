package main;
import java.util.ArrayList;
import java.util.Random;
import main.resources.PSO.*;
import main.ACO;

public class PSO {
	
	//PARAMS DEF START
	public enum Topology { 
		gl, ri, vn, ra, _error;
		public static int RA_NHOOD_SIZE = 5;
		public static int VN_ARRAY_HEIGHT = 4; // description
		public static int VN_ARRAY_WIDTH = 4;  // same here..
	};
	public enum FinalSettings {;
		public static double CHI = 0.7298; // constriction Factor
		public static double PHI = 2.05; 
		public static int swarmSize = 10;
		public static int maxIter = Integer.MAX_VALUE;
		public static int maxDim = 3;
	}
	//PARAMS DEF END
	
	
	
	Params params;
	ArrayList <Particle> particles;
	Particle gBest; 
	Boolean bestFound = false;
	int iterationCount = 0;
	Random rand = new Random();
	long duration;

	
	public PSO(Params params){
		this.params = params;
		this.particles = new ArrayList<Particle>();
		runPSO();
	}
	
	// description
	private void runPSO() {
		long startTime = System.currentTimeMillis();
		duration = startTime;
		initParticles();
		setNeighbors();
		while(	iterationCount < params.getMaxIter() && 
				!bestFound &&
				duration < startTime+(long)Lab.RunDuration) {
			evalParticles();
			update_Pbests();
			update_Gbest();
			update_All_NHoodBest();
			update_AllVel();
			update_AllPos();
			System.out.println(this.gBest.getCurrVal());
			duration = System.currentTimeMillis() - startTime;
			iterationCount++;
		}
	}
	
	
	// description
	private void initParticles() {
		for (int i = 0; i < params.getSwarmSize(); i++) {
			particles.add(new Particle(params.getMaxDim()));
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
			int val = this.evalACO(particle);
			particle.setVal(val);
		}
	}


	
	
	
	/*
	 *  ---- ACO TEST FUNCTION  ----
	 */
	
	// description
	private int evalACO(Particle particle) {
		ACO.Result res = new ACO(
				new main.resources.ACO.Params(
						params.ACO_Params.getGraph(),
						params.ACO_Params.getNumOf_ants(), 
						params.ACO_Params.getNumOf_iterations(), 
						particle.getAlpha(),
						particle.getRho(),
						particle.getEFactor(),
						params.ACO_Params.getSatLimit(), 
						params.ACO_Params.getTimeLimit(), 
						params.ACO_Params.getOptDist())
				).get_res();
		return res.getBestPath().getTourLen();
	}
	
	
	
	
	/*
	 *  ---- UPDATE SEARCH STATES  ----
	 */
	
	// description
	private void update_Pbests() {
		for (int i = 0; i < this.particles.size(); i++) {
			Particle currParticle = this.particles.get(i);
			if (currParticle.getCurrVal() > currParticle.getPbestval()) {
				currParticle.setPbestval(currParticle.getCurrVal());
				currParticle.setPbest(currParticle.getCoords().clone());
			}
		}
	}
	private void update_Gbest() {
		for (int i = 0; i < this.particles.size(); i++) {
			Particle currParticle = particles.get(i);
			if (currParticle.getCurrVal() > gBest.getCurrVal()) {
				gBest = currParticle.clone();
				if(gBest.getCurrVal() >= params.ACO_Params.getGraph().getNumOf_edges()) bestFound = true;
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
		return new Result(gBest, iterationCount, params.getTopology(), duration);
	}
	class Result{
		private Particle gBest;
		private int iterationCount;
		private Topology topology;
		private long duration;
		public Result(Particle gBest, int iterationCount, Topology topology, long duration) {
			this.duration = duration;
			this.gBest = gBest;
			this.iterationCount = iterationCount;
			this.topology = topology;
		}
		public void print() {
			System.out.println("\n\n----  ** PSO RESULTS ** ----\n");
			System.out.println("Best Found: "+this.gBest.getCurrVal());
			System.out.println("Topology: "+this.topology);
			System.out.println("Iterations: "+this.iterationCount);
			System.out.println("Duration: "+this.duration);
		}
		//getters
		public Particle getGBest() {return gBest;}
		public int getIterCount() {return iterationCount;}
		public Topology getTopology() {return topology;}		
	}
	
	

			
}
