package com.arje;

import com.arje.gui.GUI;
import com.arje.html.HtmlBuilder;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import static com.arje.pdf.PdfWriter.convertHtmlToPdf;
import static com.arje.utils.FileUtils.getPathWithDifferentExtension;
import static com.arje.utils.FileUtils.getStringFromFile;
import static com.arje.utils.XlsUtils.getSheetsFromXls;
import static org.apache.commons.io.FileUtils.writeStringToFile;

public class Main {

    private static final String PATH_TO_TEMPLATE_HTML = "template/template.html";
    private static final String PATH_TO_TEMPLATE_CSS = "template/template.css";

    public static final String HTML = ".html";
    public static final String XLS = ".xls";
    public static final String XLSX = ".xlsx";

    public static void main(String[] args) {
        new GUI();
    }

    public static void generatePdfFromGivenFiles(String pathToXlsFile) throws Exception {
        validateXlsFilePath(pathToXlsFile);

        Iterator<Sheet> sheets = getSheetsFromXls(pathToXlsFile);

        HtmlBuilder htmlBuilder = new HtmlBuilder(getStringFromFile(PATH_TO_TEMPLATE_HTML));
        htmlBuilder.includeStyling(getStringFromFile(PATH_TO_TEMPLATE_CSS));
        htmlBuilder.includePlanInfo(sheets.next());
        htmlBuilder.includePlanData(sheets.next());
        htmlBuilder.includeTrainingPlans(sheets);

        File tempHtmlFile = getTemporaryHtmlFile(pathToXlsFile);
        writeStringToFile(tempHtmlFile, htmlBuilder.getHtmlString(), StandardCharsets.UTF_8);

        convertHtmlToPdf(tempHtmlFile);

        deleteTemporaryHtml(tempHtmlFile);
    }

    private static File getTemporaryHtmlFile(String pathToXlsFile) {
        return new File(getPathWithDifferentExtension(pathToXlsFile, HTML));
    }

    private static void validateXlsFilePath(String pathToXlsFile) {
        if (!(pathToXlsFile.endsWith(XLS) || pathToXlsFile.endsWith(XLSX))) {
            throw new RuntimeException("Selected plan is not " + XLS + " or " + XLSX);
        }
    }

    private static void deleteTemporaryHtml(File tempHtmlFile) {
        if (!tempHtmlFile.delete()) {
            throw new RuntimeException("ERROR! couldn't delete " + tempHtmlFile.getAbsolutePath());
        }
    }
}