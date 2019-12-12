package main;
import main.resources.ACO.Graph;
import main.resources.PSO.Params;

public class Lab {

	public static void main(String[] args) {
		
		// graph settings
		String graphFileName = "";
		int numOf_colors = 10;
		
		
		// graph parsing
		Graph graph = new Graph(graphFileName, numOf_colors);
		
		
		// initialize PSO instance
		System.out.println("Initializing PSO ...");
		main.resources.PSO.Params psoParams = new Params(
				PSO.Topology.ra,
				PSO.FinalSettings.posRanges,
				PSO.FinalSettings.velRanges,
				PSO.FinalSettings.CHI,
				PSO.FinalSettings.PHI,
				PSO.FinalSettings.swarmSize,
				PSO.FinalSettings.maxIter,
				PSO.FinalSettings.maxDim,
				graph
		);
		PSO pso = new PSO(psoParams);
		
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
