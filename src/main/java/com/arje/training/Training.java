package com.arje.training;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Training {

    private final String trainingName;
    private final String comments;
    private final List<String> headers = new ArrayList<>();
    private final List<Exercise> exercises = new ArrayList<>();
    private int columnCount;

    public Training(Sheet sheet) {
        Iterator<Row> rows = sheet.iterator();

        Row nameRow = sheet.getRow(0);
        if (nameRow != null) {
            this.trainingName = nameRow.getCell(0).toString();
            rows.next();
        } else {
            this.trainingName = "TRAINING";
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
            exercises.add(getExerciseFromRow(rows.next()));
        }
    }

    private Exercise getExerciseFromRow(Row row) {

        String name = "";
        String link = "";
        List<String> details = new ArrayList<>();

        int i = 0;
        for (int column = 0; column < columnCount; column++) {
            Cell cell = row.getCell(column);
            if (cell != null) {
                String cellValue = cell.toString();
                if (column == 0) {
                    String[] split = cellValue.split("@");
                    if (split.length == 1) {
                        name = split[0];
                    } else if (split.length == 2) {
                        name = split[0];
                        link = split[1];
                    } else {
                        throw new RuntimeException("ERROR! Exercise name/link format error in sheet" + row.getSheet().getSheetName() + ", row " + row.getRowNum());
                    }
                } else {
                    details.add(cellValue);
                }
            } else {
                details.add("");
            }
            i++;
        }
        while (i < columnCount) {
            details.add("");
            i++;
        }
        return new Exercise(name, link, details);
    }

    public String getTrainingName() {
        return trainingName;
    }

    public String getComments() {
        return comments;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public int getColumnCount() {
        return columnCount;
    }
}
