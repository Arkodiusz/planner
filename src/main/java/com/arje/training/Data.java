package com.arje.training;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.LinkedHashMap;
import java.util.Map;

public class Data {

    Map<String, String> data = new LinkedHashMap<>();

    public Data(Sheet sheet) {
        for (Row row : sheet) {
            data.put(row.getCell(0).toString(), row.getCell(1).toString());
        }
    }

    public String toHtml() {
        StringBuilder html = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            html.append("<b>");
            html.append(entry.getKey());
            html.append(": ");
            html.append("</b>");
            html.append(entry.getValue());
            html.append("<br>");
        }
        return html.toString();
    }
}
