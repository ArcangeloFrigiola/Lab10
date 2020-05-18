package it.polito.tdp.bar.model;

import java.time.Duration;
import java.time.LocalTime;

public class Event implements Comparable<Event>{
	
	/*
	 * In questo scenario, posso avere due tipi di eventi:
	 *   1. Arriva un nuovo gruppo di clienti (ARRIVO_GRUPPO_CLIENTI)
	 *   2. Un gruppo va via, liberando un tavolo (TAVOLO_LIBERATO)
	 */
	
	public enum EventType{
		ARRIVO_GRUPPO_CLIENTI, TAVOLO_LIBERATO
	}
	
	//ATTRIBUTI DELLA CLASSE EVENTO
	private LocalTime time;
	private EventType type;
	private int numeroPersone;
	private boolean tolleranza;
	private Duration durata;
	private int idTavoloOccupato;
	
	
	/**
	 * @param time
	 * @param type
	 * @param numeroPersone
	 */
	public Event(LocalTime time, EventType type, int numeroPersone, boolean tolleranza, Duration durata, int idTavoloOccupato) {
		super();
		this.time = time;
		this.type = type;
		this.numeroPersone = numeroPersone;
		this.tolleranza = tolleranza;
		this.durata = durata;
		this.idTavoloOccupato = idTavoloOccupato;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
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

	public Duration getDurata() {
		return durata;
	}

	public void setDurata(Duration durata) {
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
	public int compareTo(Event other) {
		
		return this.time.compareTo(other.time);
	}

	@Override
	public String toString() {
		return "Event [time=" + time + ", type=" + type + ", numeroPersone=" + numeroPersone + ", tolleranza="
				+ tolleranza + ", durata=" + durata.toMinutes() + "]";
	}
	
	
}
