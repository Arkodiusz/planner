package com.arje.data;

import com.arje.helpers.SimpleRow;
import com.arje.helpers.SimpleSheet;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class Data {

    Map<String, String> data = new LinkedHashMap<>();

    public Data(SimpleSheet sheet) {
        for (SimpleRow row : sheet.getRows()) {
            data.put(row.getCell(0), row.getCell(1).replace(".0", ""));
        }
    }
}
