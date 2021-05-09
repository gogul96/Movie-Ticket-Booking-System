package com.gogul.tickets.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class SeatsCache {

	private static Map<Long,Map<Long, Map<String, Integer>>> seatLayouts = new HashMap<>();
	
	public Optional<Map<Long, Map<String, Integer>>> get(long screenId) {
		return Optional.ofNullable(seatLayouts.get(screenId));
	}

	public boolean exists(long screenId) {
		// TODO Auto-generated method stub
		return false;
	}

	public void set(long screenId, Map<Long, Map<String, Integer>> seatLayoutsMap) {
		seatLayouts.put(screenId, seatLayoutsMap);
	}

	public void remove(long screenId) {
		seatLayouts.remove(screenId);
	}

}
