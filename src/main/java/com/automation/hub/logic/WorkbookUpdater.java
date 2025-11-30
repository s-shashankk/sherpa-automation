package com.automation.hub.logic;

import com.google.api.services.sheets.v4.model.ValueRange;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class WorkbookUpdater {
	public static ValueRange generateDpmUpdates(List<String> workbookKeys, Map<String, String> dpmMap) {

        List<List<Object>> output = new ArrayList<>();

        for (String key : workbookKeys) {
            String dpm = dpmMap.getOrDefault(key, "");
            List<Object> row = new ArrayList<>();
            row.add(dpm);
            output.add(row);
        }

        return new ValueRange().setValues(output);
    }
}
