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
	
	private int maxHours;
	private int maxYears;
	
	public Model() {
		
		podao = new PowerOutageDAO();
		
		outageMap = new OutageIdMap ();
		nercMap = new NercIdMap ();
	
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
			
			// aggiungo solo un outages più recenti e non uguali
			if (!parziale.contains(p)) {
				
				boolean posso_aggiungere = false;
				if (parziale.size() >= 1) {
					LocalDateTime precedent = parziale.get(parziale.size()-1).getDateBegan();
					if (p.getDateFinished().isAfter(precedent))
						posso_aggiungere = true;
				}
				else
					posso_aggiungere = true;
		
				if (posso_aggiungere) {
					parziale.add(p);
				
					// filtro
					if (this.controllaParziale(parziale)) {
						recursive (parziale, outages);
					}
				
					// backtracking
					parziale.remove(parziale.size()-1);
				}
			}
		}
	
	}

	private boolean controllaParziale(List<PowerOutage> parziale) {
	
		if (this.sumHours(parziale) > maxHours)
			return false;
		
		LocalDateTime old = parziale.get(0).getDateBegan();
		
//		for (PowerOutage p : parziale) {
//			if (p.getDateBegan().isBefore(oldYear))
//				oldYear = p.getDateBegan();
//		}
		
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
