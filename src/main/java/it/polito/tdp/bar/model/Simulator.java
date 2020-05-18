package it.polito.tdp.bar.model;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.PriorityQueue;

import it.polito.tdp.bar.model.Event.EventType;

public class Simulator {

	//Genero una coda degli eventi
	private PriorityQueue<Event> queue = new PriorityQueue<>();
	
	//1. PARAMETRI DI SIMULAZIONE (setter required)
	private final int numeroTotTavoli = 15;
	private final int numeroSimulazioniMax = 2000;
	
	//2. MODELLO DEL MONDO
	private int tavoliDisponibili;
	private int tavoliDaDieci = 2; //Max 2
	private int tavoliDaOtto = 4; //Max 4
	private int tavoliDaSei = 4; //Max 4
	private int tavoliDaQuattro = 5; //Max 5
	private int tableId;
	
	//3. VALORI DA CALCOLARE
	private int clienti;
	private int clientiSoddisfatti;
	private int clientiInsoddisfatti;
	
	public int getClienti() {
		return clienti;
	}
	public int getClientiSoddisfatti() {
		return clientiSoddisfatti;
	}
	public int getClientiInsoddisfatti() {
		return clientiInsoddisfatti;
	}
	
	public double arrotonda(double value, int numCifreDecimali) {
		double temp = Math.pow(10, numCifreDecimali);
		return Math.round(value * temp) / temp;
	}
	
	//Metodo che restituisce un tempo tra un cliente ed il prossimo compreso tra 1 e 10 minuti
	public Duration getIntervalloTraClienti() {
		
		double intervallo = Math.random(); //restituisce un valore compreso tra [0,1)
		intervallo = arrotonda(intervallo, 1);
		Duration tempo = Duration.of(0, ChronoUnit.MINUTES);
		
		if(intervallo==0.0) {
			tempo = Duration.of(10, ChronoUnit.MINUTES);
		}else {
			for(int i=1; i<10; i++) {
				double temp = (i*1.0)/10;
				if(intervallo==temp) {
					tempo = Duration.of(i, ChronoUnit.MINUTES);
				}
			}
		}
		System.out.println("Tempo: "+tempo.toMinutes()+"\nNumero random: "+intervallo+"\n");
		return tempo;
	}
	
	//Metodo che restituisce il numero di clienti (tra 1 e 10)
	public int getNumeroClientiRandom() {
		
		double numero = Math.random();
		numero = arrotonda(numero, 1);
		int numP=0;
		
		if(numero==0) {
			numP = 10;
		}else {
			for(int i=1; i<10; i++) {
				double temp = (i*1.0)/10.0;
				System.out.println("Numero in i: "+temp+"\n");
				if(numero==temp) {
					numP = i;
				}
			}
		}
		System.out.println("Numero clienti: "+numP+"\nNumero random: "+numero+"\n");
		return numP;
	}
	
	//Metodo che restituisce una tolleranza del cliente
	public boolean getTolleranza() {
		
		double num = Math.random();
		num = arrotonda(num, 1);
		if(num==0.0) {
			return false; //Cliente non tollerante (1 possibilità su 10)
		}
		return true; //Cliente tollerante (9 possibilità su 10)
	}
	
	//Metodo che restituisce una durarta della permanenza del cliente al tavolo, tra 60 e 120 minuti
	public Duration getPermanenzaAlTavolo() {
		
		double intervallo = Math.random(); //restituisce un valore compreso tra [0,1)
		intervallo = arrotonda(intervallo, 1);
		Duration tempo = Duration.of(60, ChronoUnit.MINUTES);
		
		for(int i=0; i<10; i++) {
			double temp = (i*1.0)/10;
			if(intervallo == temp) {
				tempo = tempo.plus(Duration.of((6+6*i), ChronoUnit.MINUTES));
			}
		}
		System.out.println("Permanenza: "+tempo.toMinutes()+"\n");
		return tempo;
		
	}
	
	public void run() {
		
		//Inizializzo le variabili del mondo e la coda degli eventi
		
		this.tavoliDisponibili = this.numeroTotTavoli; //ad inizio simulazione ho tutti i tavoli disponibili
		this.clienti = this.clientiInsoddisfatti = this.clientiSoddisfatti = 0;
		this.tavoliDaDieci = this.tavoliDaOtto = this.tavoliDaQuattro = this.tavoliDaSei = 0; //ogni sottotipo di tavolo è libero
		
		this.queue.clear();
		LocalTime minutoArrivoCliente = LocalTime.of(0, 00); //devo arrivare a 2000
		int numeroSimulazioni = 0;
		
		do {
			
			Event e = new Event(minutoArrivoCliente, EventType.ARRIVO_GRUPPO_CLIENTI, getNumeroClientiRandom(), getTolleranza(), getPermanenzaAlTavolo(), 0);
			this.queue.add(e);
			minutoArrivoCliente = minutoArrivoCliente.plus(getIntervalloTraClienti());
			numeroSimulazioni++;
			
		}while(numeroSimulazioni<this.numeroSimulazioniMax);
		
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			System.out.println(e);
			processEvent(e);
		}
	}
	
	private void processEvent(Event e) {
		
		switch(e.getType()) {
		
		case ARRIVO_GRUPPO_CLIENTI:
			
			int persone = e.getNumeroPersone();
			
			if(this.tavoliDisponibili>0) {
				if(persone<=4) {
					if(this.tavoliDaQuattro>0) { //Se ho un tavolo da 4 libero, le metto li
						
						this.clienti++;
						this.clientiSoddisfatti++;
						this.tavoliDaQuattro--;
						this.tavoliDisponibili--;
					    tableId = 4;
						
						Event nuovo = new Event(e.getTime().plus(e.getDurata()), EventType.TAVOLO_LIBERATO, persone, true, null, 4);
						this.queue.add(nuovo);
						
					}else { //Se non ho un tavolo da quattro libero, cerco altro posto
						
						if (persone == 4) { //Se le persone sono esattamente 4, possono metterle o in un tavolo da 6 o in uno da 8 per riempire minimo il 50% del tavolo
							if (this.tavoliDaSei > 0) {

								this.clienti++;
								this.clientiSoddisfatti++;
								this.tavoliDaSei--;
								this.tavoliDisponibili--;
								tableId = 6;

								Event nuovo = new Event(e.getTime().plus(e.getDurata()), EventType.TAVOLO_LIBERATO,
										persone, true, null, 6);
								this.queue.add(nuovo);
								
								
							}else if(this.tavoliDaOtto>0) {
								
								this.clienti++;
								this.clientiSoddisfatti++;
								this.tavoliDaOtto--;
								this.tavoliDisponibili--;
								tableId = 8;

								Event nuovo = new Event(e.getTime().plus(e.getDurata()), EventType.TAVOLO_LIBERATO,
										persone, true, null, 8);
								this.queue.add(nuovo);
								
								
							}else if(e.getTolleranza()) { //Se non ho nessun tavolo, provo a metterli al bancone (if tolleranza == TRUE)
								this.clienti++;
								this.clientiSoddisfatti++;
								
							}else {
								this.clienti++;
								this.clientiInsoddisfatti++;
								
							}
						}else if(persone==3) { //Se sono esattamente 3, posso inserirle in un tavolo da 6, se c'è
							
							if (this.tavoliDaSei > 0) {

								this.clienti++;
								this.clientiSoddisfatti++;
								this.tavoliDaSei--;
								this.tavoliDisponibili--;
								tableId = 6;

								Event nuovo = new Event(e.getTime().plus(e.getDurata()), EventType.TAVOLO_LIBERATO,
										persone, true, null, 6);
								this.queue.add(nuovo);
								
								
							}else if(e.getTolleranza()) { //Se non ho nessun tavolo, provo a metterli al bancone (if tolleranza == TRUE)
								this.clienti++;
								this.clientiSoddisfatti++;
								
							}else {
								this.clienti++;
								this.clientiInsoddisfatti++;
								
							}
						}else if(e.getTolleranza()) { //Se non ho nessun tavolo, provo a metterli al bancone (if tolleranza == TRUE)
							this.clienti++;
							this.clientiSoddisfatti++;
							
						}else {
							this.clienti++;
							this.clientiInsoddisfatti++;
							
						}
					}
					
				}else if(persone<=6 && persone>4) {
					if (this.tavoliDaSei > 0) { // Se ho un tavolo da 4 libero, le metto li

						this.clienti++;
						this.clientiSoddisfatti++;
						this.tavoliDaSei--;
						this.tavoliDisponibili--;
						tableId = 6;

						Event nuovo = new Event(e.getTime().plus(e.getDurata()), EventType.TAVOLO_LIBERATO,
								persone, true, null, 6);
						this.queue.add(nuovo);
						
					}else {
						if (this.tavoliDaOtto > 0) { // Se ho un tavolo da 4 libero, le metto li

							this.clienti++;
							this.clientiSoddisfatti++;
							this.tavoliDaOtto--;
							this.tavoliDisponibili--;
							tableId = 8;

							Event nuovo = new Event(e.getTime().plus(e.getDurata()), EventType.TAVOLO_LIBERATO,
									persone, true, null, 8);
							this.queue.add(nuovo);
							
						}else if (this.tavoliDaDieci > 0) { // Se ho un tavolo da 4 libero, le metto li

							this.clienti++;
							this.clientiSoddisfatti++;
							this.tavoliDaDieci--;
							this.tavoliDisponibili--;
							tableId = 10;

							Event nuovo = new Event(e.getTime().plus(e.getDurata()), EventType.TAVOLO_LIBERATO,
									persone, true, null, 10);
							this.queue.add(nuovo);
							
							
						}else if(e.getTolleranza()) { //Se non ho nessun tavolo, provo a metterli al bancone (if tolleranza == TRUE)
							this.clienti++;
							this.clientiSoddisfatti++;
							
						}else {
							this.clienti++;
							this.clientiInsoddisfatti++;
							
						}
					}
					
				}else if(persone <=8 && persone>6) {
					
					if (this.tavoliDaOtto > 0) { // Se ho un tavolo da 4 libero, le metto li

						this.clienti++;
						this.clientiSoddisfatti++;
						this.tavoliDaOtto--;
						this.tavoliDisponibili--;
						tableId = 8;

						Event nuovo = new Event(e.getTime().plus(e.getDurata()), EventType.TAVOLO_LIBERATO,
								persone, true, null, 8);
						this.queue.add(nuovo);
						
						
					}else if(this.tavoliDaDieci>0) {
						
						this.clienti++;
						this.clientiSoddisfatti++;
						this.tavoliDaDieci--;
						this.tavoliDisponibili--;
						tableId = 10;

						Event nuovo = new Event(e.getTime().plus(e.getDurata()), EventType.TAVOLO_LIBERATO,
								persone, true, null, 10);
						this.queue.add(nuovo);
						
					}else if(e.getTolleranza()) { //Se non ho nessun tavolo, provo a metterli al bancone (if tolleranza == TRUE)
						this.clienti++;
						this.clientiSoddisfatti++;
						
					}else {
						this.clienti++;
						this.clientiInsoddisfatti++;
						
					}
					
				}else {
					
					if (this.tavoliDaDieci > 0) {

						this.clienti++;
						this.clientiSoddisfatti++;
						this.tavoliDaDieci--;
						this.tavoliDisponibili--;
						tableId = 10;

						Event nuovo = new Event(e.getTime().plus(e.getDurata()), EventType.TAVOLO_LIBERATO,
								persone, true, null, 10);
						this.queue.add(nuovo);
						

					}else if(e.getTolleranza()) { //Se non ho nessun tavolo, provo a metterli al bancone (if tolleranza == TRUE)
						this.clienti++;
						this.clientiSoddisfatti++;
						
					}else {
						this.clienti++;
						this.clientiInsoddisfatti++;
						
					}

				}
			}else {
				this.clienti++;
				if(e.getTolleranza()) {
					this.clientiSoddisfatti++;
				}else {
					this.clientiInsoddisfatti++;
				}
			}
			
			break;
			
		case TAVOLO_LIBERATO:
			
			this.tavoliDisponibili++;
			if(e.getIdTavoloOccupato()==4) {
				this.tavoliDaQuattro++;
			}else if(e.getIdTavoloOccupato()==6){
				this.tavoliDaSei++;
			}else if(e.getIdTavoloOccupato()==8) {
				this.tavoliDaOtto++;
			}else if(e.getIdTavoloOccupato()==10) {
				this.tavoliDaDieci++;
			}
			break;
		}
		
	}

	
}
