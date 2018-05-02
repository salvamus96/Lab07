package it.polito.tdp.poweroutages.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class PowerOutage {

	private int id;
	private int nercId;
	private int customersAffected;
	private LocalDateTime dateBegan;
	private LocalDateTime dateFinished;
	private long duration;

	
	public PowerOutage(int id, int nercId, int customersAffected, LocalDateTime dateBegan, LocalDateTime dateFinished) {
		super();
		this.id = id;
		this.nercId = nercId;
		this.customersAffected = customersAffected;
		this.dateBegan = dateBegan;
		this.dateFinished = dateFinished;
		
		// UNTIL utile per calcolare la differenza tra date specificando anche l'unità di misura 
		this.duration = this.dateBegan.until(dateFinished, ChronoUnit.HOURS);
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNercId() {
		return nercId;
	}

	public void setNercId(int nercId) {
		this.nercId = nercId;
	}

	public int getCustomersAffected() {
		return customersAffected;
	}

	public void setCustomersAffected(int customersAffected) {
		this.customersAffected = customersAffected;
	}

	public LocalDateTime getDateBegan() {
		return dateBegan;
	}

	public void setDateBegan(LocalDateTime dateBegan) {
		this.dateBegan = dateBegan;
	}

	public LocalDateTime getDateFinished() {
		return dateFinished;
	}

	public void setDateFinished(LocalDateTime dateFinished) {
		this.dateFinished = dateFinished;
	}
	
	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.dateFinished.getYear() + " " + this.dateBegan + " " + this.dateFinished + " " + duration + " " + this.customersAffected);
		return builder.toString();
	}
	
	

}
