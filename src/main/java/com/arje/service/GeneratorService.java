package com.arje.service;

import com.arje.exception.HtmlProcessingException;
import com.arje.exception.InvalidExcelFileExtensionException;
import com.arje.html.HtmlBuilder;
import com.arje.html.ThymeleafHtmlBuilder;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    public byte[] generatePdf(MultipartFile multipartFile) throws IOException {

        validateXlsFileName(multipartFile);

        Iterator<Sheet> sheets = getSheetsFromXls(multipartFile);

        String processedHtmlAsString = getProcessedHtmlAsString(sheets);

        return convertHtmlToPdf(processedHtmlAsString);
    }

    private String getProcessedHtmlAsString(Iterator<Sheet> sheets) throws IOException {
        try {
            HtmlBuilder htmlBuilder = new ThymeleafHtmlBuilder(PATH_TO_TEMPLATE_HTML);
            htmlBuilder.includeStyling(readFileToString(new File(PATH_TO_TEMPLATE_CSS), StandardCharsets.UTF_8));
            htmlBuilder.includePlanInfo(sheets.next());
            htmlBuilder.includePlanData(sheets.next());
            htmlBuilder.includeTrainings(sheets);
            return htmlBuilder.getHtmlString();
        } catch (RuntimeException e) {
            String message = e.getMessage();
            if (message == null || message.isEmpty()) {
                message = "Error during processing HTML template. Check formatting in excel file";
            }
            throw new HtmlProcessingException(message);
        }

    }

    private void validateXlsFileName(MultipartFile multipartFile) throws InvalidExcelFileExtensionException {
        String filename = multipartFile.getOriginalFilename();
        if (filename == null || !(filename.endsWith(XLS) || filename.endsWith(XLSX))) {
            throw new InvalidExcelFileExtensionException();
        }
    }

    private Iterator<Sheet> getSheetsFromXls(MultipartFile multipartFile) throws IOException {
        Workbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
        return workbook.iterator();
    }
}
