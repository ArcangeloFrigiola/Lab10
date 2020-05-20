package it.polito.tdp.bar.model;

public class Tavolo {

	private int tipoTavolo;
	private int numTavoli;
	/**
	 * @param tipoTavolo
	 * @param capacitaTavolo
	 */
	public Tavolo(int tipoTavolo, int capacitaTavolo) {
		super();
		this.tipoTavolo = tipoTavolo;
		this.numTavoli = capacitaTavolo;
	}
	
	public int getTipoTavolo() {
		return tipoTavolo;
	}
	public void setTipoTavolo(int tipoTavolo) {
		this.tipoTavolo = tipoTavolo;
	}
	public int getNumTavoli() {
		return numTavoli;
	}
	public void setNumTavoli(int numTavoli) {
		this.numTavoli = numTavoli;
	}
	
	
}
