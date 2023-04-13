package com.arje.service;

import com.arje.html.HtmlBuilder;
import com.arje.html.ThymeleafHtmlBuilder;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import java.util.Iterator;

import static com.arje.pdf.PdfWriter.convertHtmlToPdf;
import static com.arje.utils.FileUtils.getStringFromFile;
import static com.arje.utils.XlsUtils.getSheetsFromXls;

@Service
public class GeneratorService {

    private static final String XLS = ".xls";
    private static final String XLSX = ".xlsx";

    public static final String PATH_TO_TEMPLATE_HTML = "src/main/resources/templates/template.html";
    public static final String PATH_TO_TEMPLATE_CSS = "src/main/resources/templates/template.css";

    public byte[] generatePdf(String pathToXlsFile) {

        validateXlsFilePath(pathToXlsFile);

        Iterator<Sheet> sheets = getSheetsFromXls(pathToXlsFile);

        HtmlBuilder htmlBuilder = new ThymeleafHtmlBuilder(PATH_TO_TEMPLATE_HTML);
        htmlBuilder.includeStyling(getStringFromFile(PATH_TO_TEMPLATE_CSS));
        htmlBuilder.includePlanInfo(sheets.next());
        htmlBuilder.includePlanData(sheets.next());
        htmlBuilder.includeTrainings(sheets);

        return convertHtmlToPdf(htmlBuilder.getHtmlString());
    }

    private void validateXlsFilePath(String pathToXlsFile) {
        if (!(pathToXlsFile.endsWith(XLS) || pathToXlsFile.endsWith(XLSX))) {
            throw new RuntimeException("Selected plan is not " + XLS + " or " + XLSX);
        }
    }
}
