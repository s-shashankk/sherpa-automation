package com.automation.hub.logic;

import com.automation.hub.model.ValidationEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValidationParser {

	/**
	 * @param values rows from readSheet("B7:T") (ValueRange)
	 * @param notes  notes from readNotesForRange("B7:B")
	 */
	public static List<ValidationEntry> parse(List<List<Object>> values, List<String> notes) {
		List<ValidationEntry> result = new ArrayList<>();

		for (int i = 0; i < values.size(); i++) {
			List<Object> row = values.get(i);

			String carrierCell = row.size() > 0 ? row.get(0).toString().trim() : "";
			String roCarrier = row.size() > 1 ? row.get(1).toString().trim() : "";
			String eLabel = row.size() > 9 ? row.get(9).toString().trim() : "default";
			String dpm = row.size() > 18 ? row.get(18).toString().trim() : "";

			if (eLabel.isBlank()) {
				eLabel = "default";
			}
			if (dpm.isBlank()) {
				continue; // nothing to map
			}

			// main row carrier text
			if (!carrierCell.isBlank()) {
				String[] cc = CarrierCountryExtractor.extract(carrierCell);
				result.add(new ValidationEntry(cc[0], cc[1], roCarrier, eLabel, dpm));
			}

			// entries from notes
			String noteText = (notes != null && i < notes.size()) ? notes.get(i) : "";

			if (noteText != null && !noteText.isBlank()) {

				final String finalRoCarrier = roCarrier;
				final String finalElabel = eLabel;
				final String finalDpm = dpm;

				String[] lines = noteText.split("\\r?\\n");
				Arrays.stream(lines).map(String::trim).filter(line -> !line.isBlank()).forEach(line -> {
					String[] cc = CarrierCountryExtractor.extract(line);
					result.add(new ValidationEntry(cc[0], cc[1], finalRoCarrier, finalElabel, finalDpm));
				});
			}

		}

		return result;
	}
}
