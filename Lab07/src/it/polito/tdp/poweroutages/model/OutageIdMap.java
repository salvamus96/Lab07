package it.polito.tdp.poweroutages.model;

import java.util.HashMap;
import java.util.Map;

public class OutageIdMap {

	private Map<Integer, PowerOutage> map;
	
	public OutageIdMap() {
		
		map = new HashMap<>();
	}
	
	public PowerOutage get(int id) {
		return map.get(id);
	}
	
	public PowerOutage get(PowerOutage powerOutage) {
		PowerOutage old = map.get(powerOutage.getId());
		if (old == null) {
			// nella mappa non c'è questo corso!
			map.put(powerOutage.getId(), powerOutage);
			return powerOutage;
		}
		return old;
	}
	
	public void put(int id, PowerOutage powerOutage) {
		map.put(id, powerOutage);
	}
}
