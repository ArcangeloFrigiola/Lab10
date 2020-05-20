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
	
	//Metodo che restituisce un tempo tra un cliente ed il prossimo compreso tra 1 e 10 minuti
	public int getIntervalloTraClienti() {
		
		double intervallo = Math.random(); //restituisce un valore compreso tra [0,1)
		intervallo = Math.round((intervallo*10)+1);
		
		//System.out.println("Tempo: "+intervallo);
		return (int)intervallo;
	}
	
	//Metodo che restituisce il numero di clienti (tra 1 e 10)
	public int getNumeroClientiRandom() {
		
		float numero = (float) Math.random();
		numero = Math.round(numero*10);
		
		if(numero==0) {
			return 1;
		}
		
		//System.out.println("Numero clienti: "+numero+"\n");
		return (int)numero;
	}
	
	//Metodo che restituisce una tolleranza del cliente
	public boolean getTolleranza() {
		
		double percentualeTolleranza = Math.random(); //Ad esempio 0.70, esiste una P[...] del 70% che il cliente sia tollernate
		double valoreTolleranza = Math.random(); //Ad esempio 0.30
		
		if(valoreTolleranza<=percentualeTolleranza) {//se il valore di tolleranza rientra tra 0-percentualeTolleranza, allora il cliente è tollerante
			return true;
		}
		return false;
	}
	
	//Metodo che restituisce una durarta della permanenza del cliente al tavolo, tra 60 e 120 minuti
	public int getPermanenzaAlTavolo() {
		
		float intervallo = (float) Math.random(); //restituisce un valore compreso tra [0,1)
		intervallo = Math.round((intervallo*10));
		
		int num = ((int) intervallo)*6 + 60;
		//System.out.println("Permanenza: "+num+"\n");
		return num;
		
	}
	
	public void run() {
		
		//Inizializzo le variabili del mondo e la coda degli eventi
		
		this.tavoliDisponibili = this.numeroTotTavoli; //ad inizio simulazione ho tutti i tavoli disponibili
		this.clienti = this.clientiInsoddisfatti = this.clientiSoddisfatti = 0;
		
		this.queue.clear();
		int istanteArrivoCliente = 0; //immagino che il bar possa girare 24h/24
		int numeroSimulazioni = 0;
		
		System.out.println("\nAll'istante "+istanteArrivoCliente+" ci sono "+tavoliDisponibili+" tavoli disponibili\n");
		do {
			
			//Mi creo una coda degli eventi
			Event e = new Event(istanteArrivoCliente, EventType.ARRIVO_GRUPPO_CLIENTI, getNumeroClientiRandom(), getTolleranza(), getPermanenzaAlTavolo(), 0);
			this.queue.add(e);
			istanteArrivoCliente += (getIntervalloTraClienti()); //aggiorno il tempo di arrivo del prossimo cliente
			numeroSimulazioni++;
			System.out.println(e);
			
		}while(numeroSimulazioni<this.numeroSimulazioniMax);
		
		while(!this.queue.isEmpty()) {
			//La coda è ordinata in base all'istante di arrivo
			Event e = this.queue.poll();
			System.out.println(e);
			processEvent(e);
		}
	}
	
	/*Event nuovo = new Event(e.getTime()+e.getDurata(), EventType.TAVOLO_LIBERATO, e.getNumeroPersone(), false, e.getDurata(), 4);
    this.queue.add(nuovo);
    this.clienti++;*/
	
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
						
						Event nuovo = new Event(e.getTime()+e.getDurata(), EventType.TAVOLO_LIBERATO, persone, true, e.getDurata(), tableId);
						this.queue.add(nuovo);
						System.out.println("I "+persone+" clienti si sono accomodati al tavolo da "+tableId);
						
					}else { //Se non ho un tavolo da quattro libero, cerco altro posto
						
						if (persone == 4) { //Se le persone sono esattamente 4, possono metterle o in un tavolo da 6 o in uno da 8 per riempire minimo il 50% del tavolo
							if (this.tavoliDaSei > 0) {

								this.clienti++;
								this.clientiSoddisfatti++;
								this.tavoliDaSei--;
								this.tavoliDisponibili--;
								tableId = 6;

								Event nuovo = new Event(e.getTime()+e.getDurata(), EventType.TAVOLO_LIBERATO,
										persone, true, e.getDurata(), tableId);
								this.queue.add(nuovo);
								System.out.println("I "+persone+" clienti si sono accomodati al tavolo da "+tableId);
								
								
							}else if(this.tavoliDaOtto>0) {
								
								this.clienti++;
								this.clientiSoddisfatti++;
								this.tavoliDaOtto--;
								this.tavoliDisponibili--;
								tableId = 8;

								Event nuovo = new Event(e.getTime()+e.getDurata(), EventType.TAVOLO_LIBERATO,
										persone, true, e.getDurata(), tableId);
								this.queue.add(nuovo);
								System.out.println("I "+persone+" clienti si sono accomodati al tavolo da "+tableId);
								
								
							}else if(e.getTolleranza()) { //Se non ho nessun tavolo, provo a metterli al bancone (if tolleranza == TRUE)
								this.clienti++;
								this.clientiSoddisfatti++;
								System.out.println("I "+persone+" clienti si sono accomodati al bancone");
								
							}else {
								this.clienti++;
								this.clientiInsoddisfatti++;
								System.out.println("I "+persone+" clienti sono rimasti insoddisfatti");
								
							}
						}else if(persone==3) { //Se sono esattamente 3, posso inserirle in un tavolo da 6, se c'è
							
							if (this.tavoliDaSei > 0) {

								this.clienti++;
								this.clientiSoddisfatti++;
								this.tavoliDaSei--;
								this.tavoliDisponibili--;
								tableId = 6;

								Event nuovo = new Event(e.getTime()+e.getDurata(), EventType.TAVOLO_LIBERATO,
										persone, true, e.getDurata(), tableId);
								this.queue.add(nuovo);
								System.out.println("I "+persone+" clienti si sono accomodati al tavolo da "+tableId);
								
								
							}else if(e.getTolleranza()) { //Se non ho nessun tavolo, provo a metterli al bancone (if tolleranza == TRUE)
								this.clienti++;
								this.clientiSoddisfatti++;
								System.out.println("I "+persone+" clienti si sono accomodati al bancone");
								
							}else {
								this.clienti++;
								this.clientiInsoddisfatti++;
								System.out.println("I "+persone+" clienti sono rimasti insoddisfatti");
								
							}
						}else if(e.getTolleranza()) { //Se non ho nessun tavolo, provo a metterli al bancone (if tolleranza == TRUE)
							this.clienti++;
							this.clientiSoddisfatti++;
							System.out.println("I "+persone+" clienti si sono accomodati al bancone");
							
						}else {
							this.clienti++;
							this.clientiInsoddisfatti++;
							System.out.println("I "+persone+" clienti sono rimasti insoddisfatti");
							
						}
					}
					
				}else if(persone<=6 && persone>4) {
					if (this.tavoliDaSei > 0) { // Se ho un tavolo da 4 libero, le metto li

						this.clienti++;
						this.clientiSoddisfatti++;
						this.tavoliDaSei--;
						this.tavoliDisponibili--;
						tableId = 6;

						Event nuovo = new Event(e.getTime()+e.getDurata(), EventType.TAVOLO_LIBERATO,
								persone, true, e.getDurata(), tableId);
						this.queue.add(nuovo);
						System.out.println("I "+persone+" clienti si sono accomodati al tavolo da "+tableId);
						
						
					}else {
						if (this.tavoliDaOtto > 0) { // Se ho un tavolo da 4 libero, le metto li

							this.clienti++;
							this.clientiSoddisfatti++;
							this.tavoliDaOtto--;
							this.tavoliDisponibili--;
							tableId = 8;

							Event nuovo = new Event(e.getTime()+e.getDurata(), EventType.TAVOLO_LIBERATO,
									persone, true, e.getDurata(), tableId);
							this.queue.add(nuovo);
							System.out.println("I "+persone+" clienti si sono accomodati al tavolo da "+tableId);
							
						}else if (this.tavoliDaDieci > 0) { // Se ho un tavolo da 4 libero, le metto li

							this.clienti++;
							this.clientiSoddisfatti++;
							this.tavoliDaDieci--;
							this.tavoliDisponibili--;
							tableId = 10;

							Event nuovo = new Event(e.getTime()+e.getDurata(), EventType.TAVOLO_LIBERATO,
									persone, true, e.getDurata(), tableId);
							this.queue.add(nuovo);
							System.out.println("I "+persone+" clienti si sono accomodati al tavolo da "+tableId);
							
							
						}else if(e.getTolleranza()) { //Se non ho nessun tavolo, provo a metterli al bancone (if tolleranza == TRUE)
							this.clienti++;
							this.clientiSoddisfatti++;
							System.out.println("I "+persone+" clienti si sono accomodati al bancone");
							
						}else {
							this.clienti++;
							this.clientiInsoddisfatti++;
							System.out.println("I "+persone+" clienti sono rimasti insoddisfatti");
							
						}
					}
					
				}else if(persone <=8 && persone>6) {
					
					if (this.tavoliDaOtto > 0) { // Se ho un tavolo da 4 libero, le metto li

						this.clienti++;
						this.clientiSoddisfatti++;
						this.tavoliDaOtto--;
						this.tavoliDisponibili--;
						tableId = 8;

						Event nuovo = new Event(e.getTime()+e.getDurata(), EventType.TAVOLO_LIBERATO,
								persone, true, e.getDurata(), tableId);
						this.queue.add(nuovo);
						System.out.println("I "+persone+" clienti si sono accomodati al tavolo da "+tableId);
						
						
					}else if(this.tavoliDaDieci>0) {
						
						this.clienti++;
						this.clientiSoddisfatti++;
						this.tavoliDaDieci--;
						this.tavoliDisponibili--;
						tableId = 10;

						Event nuovo = new Event(e.getTime()+e.getDurata(), EventType.TAVOLO_LIBERATO,
								persone, true, e.getDurata(), tableId);
						this.queue.add(nuovo);
						System.out.println("I "+persone+" clienti si sono accomodati al tavolo da "+tableId);
						
					}else if(e.getTolleranza()) { //Se non ho nessun tavolo, provo a metterli al bancone (if tolleranza == TRUE)
						this.clienti++;
						this.clientiSoddisfatti++;
						System.out.println("I "+persone+" clienti si sono accomodati al bancone");
						
					}else {
						this.clienti++;
						this.clientiInsoddisfatti++;
						System.out.println("I "+persone+" clienti sono rimasti insoddisfatti");
						
					}
					
				}else {
					
					if (this.tavoliDaDieci > 0) {

						this.clienti++;
						this.clientiSoddisfatti++;
						this.tavoliDaDieci--;
						this.tavoliDisponibili--;
						tableId = 10;

						Event nuovo = new Event(e.getTime()+e.getDurata(), EventType.TAVOLO_LIBERATO,
								persone, true, e.getDurata(), tableId);
						this.queue.add(nuovo);
						System.out.println("I "+persone+" clienti si sono accomodati al tavolo da "+tableId);
						

					}else if(e.getTolleranza()) { //Se non ho nessun tavolo, provo a metterli al bancone (if tolleranza == TRUE)
						this.clienti++;
						this.clientiSoddisfatti++;
						System.out.println("I "+persone+" clienti si sono accomodati al bancone");
						
					}else {
						this.clienti++;
						this.clientiInsoddisfatti++;
						System.out.println("I "+persone+" clienti sono rimasti insoddisfatti");
						
					}

				}
			}else {
				this.clienti++;
				if(e.getTolleranza()) {
					this.clientiSoddisfatti++;
					System.out.println("I "+persone+" clienti si sono accomodati al bancone");
				}else {
					this.clientiInsoddisfatti++;
					System.out.println("I "+persone+" clienti sono rimasti insoddisfatti");
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
			System.out.println("Un tavolo da "+e.getIdTavoloOccupato()+" si è liberato!");
			break;
		}
		
	}

	
}
