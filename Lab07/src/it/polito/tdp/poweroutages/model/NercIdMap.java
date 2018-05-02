package it.polito.tdp.poweroutages.model;

import java.util.HashMap;
import java.util.Map;

public class NercIdMap {

	private Map<Integer, Nerc> map;
	
	public NercIdMap() {
		
		map = new HashMap<>();
	}
	
	public Nerc get(int id) {
		return map.get(id);
	}
	
	public Nerc get(Nerc nerc) {
		Nerc old = map.get(nerc.getId());
		if (old == null) {
			// nella mappa non c'� questo corso!
			map.put(nerc.getId(), nerc);
			return nerc;
		}
		return old;
	}
	
	public void put(int id, Nerc nerc) {
		map.put(id, nerc);
	}
}
