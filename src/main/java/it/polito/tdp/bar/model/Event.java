package it.polito.tdp.bar.model;

import java.time.Duration;


public class Event implements Comparable<Event>{
	
	/*
	 * In questo scenario, posso avere due tipi di eventi:
	 *   1. Arriva un nuovo gruppo di clienti (ARRIVO_GRUPPO_CLIENTI)
	 *   2. Un gruppo va via, liberando un tavolo (TAVOLO_LIBERATO)
	 */
	
	public enum EventType{
		ARRIVO_GRUPPO_CLIENTI, TAVOLO_LIBERATO;
	}
	
	//ATTRIBUTI DELLA CLASSE EVENTO
	private int time; 
	private EventType type;
	private int numeroPersone;
	private boolean tolleranza;
	private int durata;
	private int idTavoloOccupato;
	
	
	/**
	 * @param time: istante di arrivo del gruppo al bar, indicativo
	 * @param type: tipo di evento, il gruppo arriva, o il tavolo si libera
	 * @param numeroPersone: numero di pesone di cui è composto il gruppo, necessario per l'assegnazione dei tavoli
	 * @tolleranza tolleranza: valore boolenao che indica se il cliente è tollerante (TRUE) o meno (FALSE)
	 * @durata durata: tempo di pemanenza dei clienti al tavolo
	 * @idTavoloOccupato id del tavolo occupato: può assumere valori 4, 6, 8, o 10, e indica il tipo di tavolo occupato dal gruppo
	 */
	public Event(int time, EventType type, int numeroPersone, boolean tolleranza, int durata, int idTavoloOccupato) {
		super();
		this.time = time;
		this.type = type;
		this.numeroPersone = numeroPersone;
		this.tolleranza = tolleranza;
		this.durata = durata;
		this.idTavoloOccupato = idTavoloOccupato;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public int getNumeroPersone() {
		return numeroPersone;
	}

	public void setNumeroPersone(int numeroPersone) {
		this.numeroPersone = numeroPersone;
	}

	public boolean getTolleranza() {
		return tolleranza;
	}

	public void setTolleranza(boolean tolleranza) {
		this.tolleranza = tolleranza;
	}

	public int getDurata() {
		return durata;
	}

	public void setDurata(int durata) {
		this.durata = durata;
	}

	public int getIdTavoloOccupato() {
		return idTavoloOccupato;
	}

	public void setIdTavoloOccupato(int idTavoloOccupato) {
		this.idTavoloOccupato = idTavoloOccupato;
	}

	/*
	 * Gli eventi hanno un ordinamento di tipo temporale
	 */
	@Override
	public int compareTo(Event o) {
		
		return this.time-o.getTime();
	}

	@Override
	public String toString() {
		return "Event ["+time + ", type=" + type + ", numeroPersone=" + numeroPersone + ", tolleranza="
				+ tolleranza + ", durata=" + durata + "]";
	}

	
	
	
}
