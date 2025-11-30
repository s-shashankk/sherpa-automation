package com.automation.hub.logic;

import com.automation.hub.model.ValidationEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class MappingEngine {
	 public static Map<String, String> buildDpmMap(List<ValidationEntry> entries) {
	        Map<String, String> map = new HashMap<>();

	        for (ValidationEntry entry : entries) {

	            String key = entry.getCarrier()
	                    + "|" + entry.getCountry()
	                    + "|" + entry.getRoCarrier()
	                    + "|" + entry.getELabel();

	            // if duplicates exist, last one wins (you said mostly it won’t happen)
	            map.put(key, entry.getDpm());
	        }

	        return map;
	    }
}
