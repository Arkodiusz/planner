package com.arje.training;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Training {

    private final String name;
    private final String comments;
    private final List<String> headers = new ArrayList<>();
    private final List<List<String>> exercises = new ArrayList<>();
    private int columnCount;

    public Training(Sheet sheet) {
        Iterator<Row> rows = sheet.iterator();

        Row nameRow = sheet.getRow(0);
        if (nameRow != null) {
            this.name = nameRow.getCell(0).toString();
            rows.next();
        } else {
            this.name = "TRAINING";
        }

        Row commentsRow = sheet.getRow(1);
        if (commentsRow != null) {
            this.comments = commentsRow.getCell(0).toString();
            rows.next();
        } else {
            this.comments = "";
        }

        Row headerRow = rows.next();
        for (Cell header : headerRow) {
            columnCount++;
            headers.add(header.toString());
        }

        while (rows.hasNext()) {
            exercises.add(getExerciseDetailsFromRow(rows.next()));
        }
    }

    private List<String> getExerciseDetailsFromRow(Row row) {
        List<String> cells = new ArrayList<>();
        int i = 0;
        for (int column = 0; column < columnCount; column++) {
            Cell cell = row.getCell(column);
            if (cell != null) {
                cells.add(cell.toString().replace(".0", ""));
            } else {
                cells.add("");
            }
            i++;
        }
        while (i < columnCount) {
            cells.add("");
            i++;
        }
        return cells;
    }

    public String getName() {
        return name;
    }

    public String getComments() {
        return comments;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public List<List<String>> getExercises() {
        return exercises;
    }

    public int getColumnCount() {
        return columnCount;
    }
}
