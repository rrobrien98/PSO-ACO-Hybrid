package main;

import main.Lab.Error;

public class Lab {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		//PSO
		//general params
		double CHI = 0.7298; // constriction Factor
		double PHI = 2.05; 
		double[] posRanges = {15.0, 30.0};
		double[] velRanges = {-2.0, 4.0};
		
		
		//---------------------------
		
		
		
		
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
