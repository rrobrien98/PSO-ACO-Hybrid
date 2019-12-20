package main;

public class Lab {
	
	
	public static enum PosRanges {;
		public static double[] alpha = {0,4};
		public static double[] rho = {0,1};
		public static double[] eFactor = {0,100};
	}
	
	public static enum VelRanges {;
		public static double[] alpha = {-1,1};
		public static double[] rho = {-.5,.5};
		public static double[] eFactor = {-40,40};	
	}
	
	public static int RunDuration = 1000 * 60 * 5; //ms

	public static void main(String[] args) {
		
		
		String graphFileName = "./src/inf-euroroad.edges";		
		
		
		// initialize PSO instance
		System.out.println("Initializing Project ...");
		PSO pso = new PSO(new main.resources.PSO.Params(graphFileName));
		
		// pso terminated
		PSO.Result res = pso.getResult();
		res.print();
				
		
		
	}
	

	
	// error handling
	public enum Error{illegalArgs, testFunc, topology}
	public static void throwError(Error err) {
		String generalMsg = 
				"\n------------------------------------------------\n"+
				"arg(s) Not Recognized. Please use format below:\n\n"+
				">> Lab.java topology swarmsize maxIteration testFunc maxDimension\n\n"+
				". topology args: gl, ri, vn, ra\n"+
				". testFunc args: rok, ack, ras, sphere\n"+
				". where: maxIteration, swarmsize, & maxDimension are integers \n\n";
		if(err == Error.illegalArgs) throw new IllegalArgumentException(generalMsg);
		else if(err == Error.testFunc) throw new IllegalArgumentException("\n\narg error: Topology"+generalMsg);
		else if(err == Error.topology) throw new IllegalArgumentException("\n\narg error: TestFunc"+generalMsg);
	}

}
