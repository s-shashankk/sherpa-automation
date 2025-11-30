package com.automation.hub.logic;

public class CarrierCountryExtractor {
	public static String[] extract(String raw) {
		if (raw == null || raw.isBlank()) {
			return new String[] { "", "" };
		}

		// Remove anything in brackets: (...)
		raw = raw.replaceAll("\\(.*?\\)", "").trim();

		String[] words = raw.split("\\s+");
		if (words.length == 1) {
			return new String[] { words[0], "" };
		}

		String carrier;
		String country;

		switch (words.length) {
		case 2:
			// WOM Chile
			carrier = words[0];
			country = words[1];
			break;

		case 3:
			// Entel Peru North → Entel | Peru North
			carrier = words[0];
			country = words[1] + " " + words[2];
			break;

		case 4:
			// Everything Everywhere United Kingdom
			// Vodafone Group United Kingdom
			carrier = words[0] + " " + words[1];
			country = words[2] + " " + words[3];
			break;

		default:
			// 5 or more: first 2 words carrier, rest country
			carrier = words[0] + " " + words[1];
			country = raw.substring(carrier.length()).trim();
			break;
		}

		return new String[] { carrier.trim(), country.trim() };
	}
}
