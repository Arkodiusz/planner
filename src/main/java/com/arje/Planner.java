package com.arje;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import static com.arje.FileUtils.*;

public class Planner {

    public static final String $ = "$";
    public static final String $_STYLE = "$style";
    public static final String $_PLANS = "$plans";

    public static final String CSS = ".css";
    public static final String HTML = ".html";
    public static final String XLS = ".xls";
    public static final String XLSX = ".xlsx";
    public static final String PDF = ".pdf";

    public void generatePdfFromGivenFiles(String pathToTemplateHtml, String pathToXlsFile ) throws Exception {
        HtmlBuilder htmlBuilder = new HtmlBuilder(getStringFromFile(pathToTemplateHtml));

        htmlBuilder.replace($_STYLE, getStringFromFile(getPathWithDifferentExtension(pathToTemplateHtml, CSS)));

        Iterator<Sheet> sheets = getSheetsFromXls(pathToXlsFile);

        // replace markers with info included in first sheet
        Sheet sheetWithPlanInfo = sheets.next();
        for (Row row : sheetWithPlanInfo) {
            replaceMarkerWithText(htmlBuilder, row);
        }

        htmlBuilder.replace($_PLANS, getPlansFromSheets(sheets));


        File tempHtmlFile = new File(getPathWithDifferentExtension(pathToXlsFile, HTML));
        org.apache.commons.io.FileUtils.writeStringToFile(tempHtmlFile, htmlBuilder.getHtmlString(), StandardCharsets.UTF_8);


        writePDF(tempHtmlFile.getAbsolutePath());

//        if (!tempHtmlFile.delete()) {
//            throw new RuntimeException("ERROR! couldn't delete " + tempHtmlFile.getAbsolutePath());
//        }
    }

    private void replaceMarkerWithText(HtmlBuilder htmlBuilder, Row row) {
        String marker = $ + row.getCell(0).toString();
        String text = row.getCell(1).toString();
        htmlBuilder.replace(marker, text);
    }

    private String getPlansFromSheets(Iterator<Sheet> sheets) {
        StringBuilder trainingPlans = new StringBuilder();

        while (sheets.hasNext()) {
            Sheet sheet = sheets.next();

            int columnCount = 0;

            StringBuilder plan = new StringBuilder("<div class=single_plan>");
            plan.append("<h3>");
            plan.append(sheet.getSheetName());
            plan.append("</h3>");

            Iterator<Row> rows = sheet.iterator();

            // get comments
            String comments = "";
            Row commentsRow = sheet.getRow(0);
            if (commentsRow != null) {
                comments = commentsRow.getCell(0).toString();
                rows.next();
            }

            Row headerRow = rows.next();
            plan.append("<table>");

            plan.append("<tr>");
            for (Cell header : headerRow) {
                columnCount++;
                plan.append("<th>");
                plan.append(header.toString());
                plan.append("</th>");
            }
            plan.append("</tr>");

            //exercises
            while (rows.hasNext()) {
                Row row = rows.next();

                int i = 0;

                plan.append("<tr>");

                for (int column = 0; column < columnCount; column++) {
                    Cell cell = row.getCell(column);


                    plan.append("<td>");

                    if (cell != null) {
                        plan.append(cell.toString().replace(".0", ""));
                    }

                    plan.append("</td>");
                    i++;
                }

                while (i < columnCount) {
                    plan.append("<td></td>");
                    i++;
                }

                plan.append("</tr>");
            }

            //comments
            if (!comments.isBlank()) {
                plan.append("<tr>");
                plan.append("<td colspan=\"");
                plan.append(columnCount);
                plan.append("\">");
                plan.append(comments);
                plan.append("</td>");
                plan.append("</tr>");
            }


            plan.append("</table>");
            plan.append("</div>");

            trainingPlans.append(plan);
        }

        return trainingPlans.toString();
    }
}
