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

    public String toHtml() {
            StringBuilder html = new StringBuilder();

            html.append("<div class=single_plan>");

            html.append("<h3>");
            html.append(this.name);
            html.append("</h3>");

            html.append("<table class=training_table>");
            
            html.append("<tr class=\"header_row\">");
            int header_column = 0;
            for (String header : headers) {
                html.append(header_column == 0 ? "<th class=\"header_name\">" : "<th class=\"header_detail\">");
                html.append(header);
                html.append("</th>");
                header_column++;
            }
            html.append("</tr>");

        for (List<String> exercise : exercises) {
            html.append("<tr class=\"exercise_row\">");
            int exercise_column = 0;
            for (String detail : exercise) {
                if (exercise_column == 0) {
                    html.append("<td class=\"exercise_name\">");
                    String[] split = detail.split("@");
                    if (split.length == 1){
                        html.append(detail);
                    } else if (split.length == 2) {
                        html.append("<a href=\"");
                        html.append(split[1]);
                        html.append("\">");
                        html.append(split[0]);
                        html.append("</a>");
                    } else {
                        throw new RuntimeException("ERROR! Exercise name/link format error in training " + this.name);
                    }
                } else {
                    html.append("<td class=\"exercise_detail\">");
                    html.append(detail);
                }
                html.append("</td>");
                exercise_column++;
            }
            html.append("</tr>");
        }

        if (!comments.isBlank()) {
            html.append("<tr>");
            html.append("<td class=\"comment_row\" colspan=\"");
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
