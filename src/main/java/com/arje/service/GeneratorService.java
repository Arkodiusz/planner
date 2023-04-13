package com.arje.service;

import com.arje.exception.InvalidExcelFileExtensionException;
import com.arje.html.HtmlBuilder;
import com.arje.html.ThymeleafHtmlBuilder;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import static com.arje.pdf.PdfWriter.convertHtmlToPdf;
import static org.apache.commons.io.FileUtils.readFileToString;

@Service
public class GeneratorService {

    public static final String XLS = ".xls";
    public static final String XLSX = ".xlsx";

    public static final String PATH_TO_TEMPLATE_HTML = "src/main/resources/templates/template.html";
    public static final String PATH_TO_TEMPLATE_CSS = "src/main/resources/templates/template.css";

    public byte[] generatePdf(String pathToXlsFile) throws IOException {

        validateXlsFilePath(pathToXlsFile);

        Iterator<Sheet> sheets = getSheetsFromXls(pathToXlsFile);

        HtmlBuilder htmlBuilder = new ThymeleafHtmlBuilder(PATH_TO_TEMPLATE_HTML);
        htmlBuilder.includeStyling(readFileToString(new File(PATH_TO_TEMPLATE_CSS), StandardCharsets.UTF_8));
        htmlBuilder.includePlanInfo(sheets.next());
        htmlBuilder.includePlanData(sheets.next());
        htmlBuilder.includeTrainings(sheets);

        return convertHtmlToPdf(htmlBuilder.getHtmlString());
    }

    private void validateXlsFilePath(String pathToXlsFile) throws InvalidExcelFileExtensionException {
        if (!(pathToXlsFile.endsWith(XLS) || pathToXlsFile.endsWith(XLSX))) {
            throw new InvalidExcelFileExtensionException();
        }
    }

    private Iterator<Sheet> getSheetsFromXls(String pathToXlsFile) throws IOException {
        Workbook workbook = new XSSFWorkbook(new FileInputStream(pathToXlsFile));
        return workbook.iterator();
    }
}
