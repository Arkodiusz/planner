package com.arje.data;

import com.arje.helpers.SimpleRow;
import com.arje.helpers.SimpleSheet;
import lombok.Getter;

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

    public Training(SimpleSheet sheet) {
        Iterator<SimpleRow> rows = sheet.getRows().iterator();

        this.trainingName = rows.next().getCell(0);
        this.comments = rows.next().getCell(0);

        SimpleRow headerRow = rows.next();
        for (String header : headerRow.getCells()) {
            columnCount++;
            headers.add(header);
        }

        while (rows.hasNext()) {
            exercises.add(new Exercise(rows.next(), columnCount));
        }
    }
}
