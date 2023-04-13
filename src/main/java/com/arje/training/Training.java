package com.arje.training;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Getter
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
            exercises.add(new Exercise(rows.next(), columnCount));
        }
    }
}
