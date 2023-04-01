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
        this.name = sheet.getSheetName();

        Iterator<Row> rows = sheet.iterator();

        Row commentsRow = sheet.getRow(0);
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

    public String toHtml() {
            StringBuilder html = new StringBuilder();

            html.append("<div class=single_plan>");

            html.append("<h3>");
            html.append(this.name);
            html.append("</h3>");

            html.append("<table>");
            
            html.append("<tr>");
            for (String header : headers) {
                html.append("<th>");
                html.append(header);
                html.append("</th>");
            }
            html.append("</tr>");

        for (List<String> exercise : exercises) {
            html.append("<tr>");
            for (String detail : exercise) {
                html.append("<td>");
                html.append(detail);
                html.append("</td>");
            }
            html.append("</tr>");
        }

        if (!comments.isBlank()) {
            html.append("<tr>");
            html.append("<td colspan=\"");
            html.append(columnCount);
            html.append("\">");
            html.append(comments);
            html.append("</td>");
            html.append("</tr>");
        }

        html.append("</table>");
        html.append("</div>");
        
        return html.toString();
    }
}
