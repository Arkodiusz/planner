package com.arje;

import com.arje.gui.GUI;
import com.arje.html.HtmlBuilder;
import com.arje.pdf.PdfWriter;
import com.arje.training.Training;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import static com.arje.utils.FileUtils.getPathWithDifferentExtension;
import static com.arje.utils.FileUtils.getStringFromFile;
import static com.arje.utils.XlsUtils.getSheetsFromXls;

public class Main {

    public static final String $ = "$";
    public static final String $_STYLE = "$style";
    public static final String $_PLANS = "$plans";

    public static final String CSS = ".css";
    public static final String HTML = ".html";
    public static final String XLS = ".xls";
    public static final String XLSX = ".xlsx";

    public static void main(String[] args) {
        new GUI();
    }

    public static void generatePdfFromGivenFiles(String pathToTemplateHtml, String pathToXlsFile) throws Exception {
        HtmlBuilder htmlBuilder = new HtmlBuilder(getStringFromFile(pathToTemplateHtml));

        htmlBuilder.replace($_STYLE, getStringFromFile(getPathWithDifferentExtension(pathToTemplateHtml, CSS)));

        Iterator<Sheet> sheets = getSheetsFromXls(pathToXlsFile);

        replaceMarkersWithPlanInfo(htmlBuilder, sheets.next());

        htmlBuilder.replace($_PLANS, getTrainingsFromSheets(sheets));

        File tempHtmlFile = new File(getPathWithDifferentExtension(pathToXlsFile, HTML));
        org.apache.commons.io.FileUtils.writeStringToFile(tempHtmlFile, htmlBuilder.getHtmlString(), StandardCharsets.UTF_8);

        new PdfWriter().writePDF(tempHtmlFile.getAbsolutePath());

        if (!tempHtmlFile.delete()) {
            throw new RuntimeException("ERROR! couldn't delete " + tempHtmlFile.getAbsolutePath());
        }
    }

    private static void replaceMarkersWithPlanInfo(HtmlBuilder htmlBuilder, Sheet sheetWithPlanInfo) {
        for (Row row : sheetWithPlanInfo) {
            replaceMarkerWithText(htmlBuilder, row);
        }
    }

    private static void replaceMarkerWithText(HtmlBuilder htmlBuilder, Row row) {
        String marker = $ + row.getCell(0).toString();
        String text = row.getCell(1).toString();
        htmlBuilder.replace(marker, text);
    }

    private static String getTrainingsFromSheets(Iterator<Sheet> sheets) {
        StringBuilder trainingPlans = new StringBuilder();
        while (sheets.hasNext()) {
            Training training = new Training(sheets.next());
            trainingPlans.append(training.toHtml());
        }
        return trainingPlans.toString();
    }
}