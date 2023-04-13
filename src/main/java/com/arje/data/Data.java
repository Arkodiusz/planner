package com.arje.data;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class Data {

    Map<String, String> data = new LinkedHashMap<>();

    public Data(Sheet sheet) {
        for (Row row : sheet) {
            if (isNotNullRow(row)) {
                data.put(row.getCell(0).toString(), row.getCell(1).toString().replace(".0", ""));
            }
        }
    }

    private static boolean isNotNullRow(Row row) {
        return row.getCell(0) != null && row.getCell(1) != null;
    }
}
