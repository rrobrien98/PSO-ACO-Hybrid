package main;

public class Lab {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		//PSO
		//general params
		
		
		// start pso, get values from res, isolate max value
		
		
		
		
		
	}
	

	
	// error handling
	enum Error{illegalArgs, testFunc, topology}
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
