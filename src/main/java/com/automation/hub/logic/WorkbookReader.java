package com.automation.hub.logic;

import java.util.ArrayList;
import java.util.List;

public class WorkbookReader {
	public static List<String> buildWorkbookKeys(List<List<Object>> rows) {

		List<String> keys = new ArrayList<>();

		for (int i = 0; i < rows.size(); i++) {
			List<Object> row = rows.get(i);

			if (row.size() < 50) { // AF and AX indexes exist only if row is long enough
				keys.add("");
				continue;
			}

			String carrier = row.size() > 1 ? row.get(1).toString().trim() : ""; // Column B
			String country = row.size() > 2 ? row.get(2).toString().trim() : ""; // Column C
			String roCarrier = row.size() > 3 ? row.get(3).toString().trim() : ""; // Column D
			String eLabel = row.size() > 49 ? row.get(49).toString().trim() : ""; // AX column
			// AX is index 49 (0-based)

			if (eLabel.isBlank()) {
				eLabel = "default";
			}

			String key = carrier + "|" + country + "|" + roCarrier + "|" + eLabel;
			keys.add(key);
		}

		return keys;
	}
}
