package it.polito.tdp.poweroutages.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.poweroutages.db.PowerOutageDAO;

public class Model {

	private PowerOutageDAO podao;
	private List <Nerc> nercs;
	
	private OutageIdMap outageMap;
	private NercIdMap nercMap;
	
	private List <PowerOutage> soluzione;
	private int maxCustomers;
	
	// dati settati dalla funzione analizza, sono delle COSTANTI imposte dall'utente
	private int maxHours;
	private int maxYears;
	
	public Model() {
		
		podao = new PowerOutageDAO();
		
		outageMap = new OutageIdMap ();
		nercMap = new NercIdMap ();
	
// popolazione dei nerc presenti nel database		
		nercs = podao.getNercList(nercMap);
		
// ogni oggetto NERC ha una lista di outages su cui occorre effettuare l'analisi
		for (Nerc n : nercs)
			podao.getOutagesFromNerc(n, outageMap);
		
	}
	
	public List<Nerc> getNercList() {
		return podao.getNercList(nercMap);
	}

	
	public String analizza(Nerc nerc, int maxYears, int maxHours) {

		this.maxCustomers = Integer.MIN_VALUE;
		this.soluzione = new ArrayList <> ();
	
		this.maxYears = maxYears;
		this.maxHours = maxHours;
			
		List <PowerOutage> parziale = new ArrayList <> ();
		
// lista che fa riferimento al nerc passato come parametro: per questioni di efficienza
// conviene accedervi dall'Identity Map		
		List <PowerOutage> outages = nercMap.get(nerc).getOutages();

		recursive (parziale, outages);
		
		String s = "";
		if (soluzione != null) {
			s += "Tot people affected: " + this.totCustomers(soluzione) + "\n";
			s += "Tot hours of outage: " + this.sumHours(soluzione) + "\n";
			for (PowerOutage p : soluzione)
				s += p.toString() + "\n";
		
			return s;
		}
		
		return "Soluzione non trovata";
		
	}


	private void recursive(List<PowerOutage> parziale, List<PowerOutage> outages) {

// condizione di terminazione: NON OCCORRE POICHE' HO UN FILTRO CHE FUNGE DA TERMINAZIONE,
// occorre verificare ogni volta se ho trovato la soluzione migliore o meno
		int customers = this.totCustomers(parziale);
		if (customers > this.maxCustomers) {
			this.maxCustomers = customers;
			soluzione = new ArrayList <> (parziale);
		}
			
			
		for (PowerOutage p : outages) {
			
// aggiungo solo outages non presenti nella parziale e più recenti rispetto al precedente:
// parziale sarà così ordinata per data dalla più vecchi alla più recente
	
			if (!parziale.contains(p) && this.addRecent(parziale, p)) {
					parziale.add(p);
				
					// filtro
					if (this.controllaParziale(parziale)) 
						recursive (parziale, outages);
					
					// backtracking
					parziale.remove(parziale.size()-1);
				}
			}
	}
	
	private boolean addRecent(List<PowerOutage> parziale, PowerOutage p) {
// questa condizione di if è indispendabile per evitare NullPointerException quando parziale è vuota
		if (!parziale.isEmpty()) {
			LocalDateTime precedent = parziale.get(parziale.size() - 1).getDateBegan();
			if(p.getDateFinished().isAfter(precedent))
				return true;
		}
		else 
			// se è vuota deve pur inziare ad a provare ad aggiungere un elemento
			return true;

		return false;
	}

	
	private boolean controllaParziale(List<PowerOutage> parziale) {
	
		if (this.sumHours(parziale) > maxHours)
			return false;
		
		// parziale è ordinata da old a recent
		LocalDateTime old = parziale.get(0).getDateBegan();		
		LocalDateTime recent = parziale.get(parziale.size()-1).getDateFinished();
		
		if (old.until(recent, ChronoUnit.YEARS) > maxYears)
			return false;
		
		return true;
	}

	
	private int sumHours(List<PowerOutage> parziale) {
		int sumHours = 0;	
		for (PowerOutage p : parziale)
			sumHours += p.getDuration();
		return sumHours;
	}

	private int totCustomers(List<PowerOutage> parziale) {
		int sum = 0;
		for (PowerOutage p : parziale)
			sum += p.getCustomersAffected();
		return sum;
	}

	
	
	

}
