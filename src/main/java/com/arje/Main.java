package com.arje;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class Main {

    public static final String $ = "$";
    public static final String $_STYLE = "$style";
    public static final String $_PLANS = "$plans";

    public static final String CSS = ".css";
    public static final String HTML = ".html";
    public static final String PDF = ".pdf";

    public static void main(String[] args) throws IOException {

        String pathToTemplateHtml = "C:/code/planner/src/main/resources/template.html";
        String pathToXlsFile = "C:/code/planner/plan1.xlsx";

        // read html template
        String htmlString = getStringFromFile(pathToTemplateHtml);

        // replace MARKER_STYLE with styling read from template.css placed in the same dir ad template html
        htmlString = htmlString.replace($_STYLE, getStringFromFile(getPathToNewFile(pathToTemplateHtml, CSS)));


        // open xls file
        FileInputStream xlsFile = new FileInputStream(pathToXlsFile);
        Workbook workbook = new XSSFWorkbook(xlsFile);
        Iterator<Sheet> sheets = workbook.iterator();

        // replace markers from sheet0
        Sheet sheet0 = sheets.next();
        for (Row row : sheet0) {
            String marker = $ + row.getCell(0).toString();
            String text = row.getCell(1).toString();
            htmlString = htmlString.replace(marker, text);
        }

        // fill document with training plans
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

                for (Cell cell : row) {
                    plan.append("<td>");
                    plan.append(cell.toString().replace(".0", ""));
                    plan.append("</td>");
                    i++;
                }

                while(i < columnCount) {
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

        htmlString = htmlString.replace($_PLANS, trainingPlans);




        String pathToGeneratedHtml = getPathToNewFile(pathToXlsFile, HTML);
        File tempHtmlFile = new File(pathToGeneratedHtml);
        FileUtils.writeStringToFile(tempHtmlFile, htmlString, StandardCharsets.UTF_8);





        writePDF(pathToGeneratedHtml);

        // remove temporary html file
//        tempHtmlFile.delete();

    }

    private static String getStringFromFile(String pathToFile) {
        String result = "";
        try {
            File file = new File(pathToFile);
            result = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static void writePDF(String pathToSourceHtml) {
        File inputHTML = new File(pathToSourceHtml);

        Document document;
        try {
            document = Jsoup.parse(inputHTML, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        try (OutputStream outputStream = new FileOutputStream(getPathToNewFile(pathToSourceHtml, PDF))) {
            ITextRenderer renderer = new ITextRenderer();
            SharedContext sharedContext = renderer.getSharedContext();
            sharedContext.setMedia("screen");
//            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);
            renderer.setDocumentFromString(document.html());
            renderer.layout();
            renderer.createPDF(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getPathToNewFile(String sourcePath, String extension) {
        String basePath = sourcePath.substring(0,sourcePath.lastIndexOf("/"));
        String fileName = sourcePath.replace(basePath, "");
        String rawFileName = fileName.substring(0, fileName.indexOf("."));
        return basePath + "/" + rawFileName + extension;
    }
}