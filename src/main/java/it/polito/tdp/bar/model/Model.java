package it.polito.tdp.bar.model;

public class Model {
	
	
	private Simulator sim;
	
	public Model() {
		sim = new Simulator();
		sim.run();
	}
	
	public int getClienti() {
		return sim.getClienti();
	}
	
	public int getClientiSoddisfatti() {
		return sim.getClientiSoddisfatti();
	}
	
	public int getClientiInsoddisfatti() {
		return sim.getClientiInsoddisfatti();
	}
	
}
