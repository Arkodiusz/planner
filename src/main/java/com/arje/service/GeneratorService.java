package com.arje.service;

import com.arje.exception.InvalidExcelFileExtensionException;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Iterator;

@Service
@AllArgsConstructor
public class GeneratorService {

    public static final String XLSX = ".xlsx";

    private final PdfWriterService pdfWriter;
    private final ThymeleafHtmlBuilderService htmlBuilder;

    public byte[] generatePdf(MultipartFile multipartFile) throws IOException {
        validateXlsFileName(multipartFile);
        Iterator<Sheet> sheets = getSheetsFromXls(multipartFile);
        String processedHtmlAsString = htmlBuilder.getHtmlString(sheets);
        return pdfWriter.convertHtmlToPdf(processedHtmlAsString);
    }

    private void validateXlsFileName(MultipartFile multipartFile) throws InvalidExcelFileExtensionException {
        String filename = multipartFile.getOriginalFilename();
        if (filename == null || !filename.endsWith(XLSX)) {
            throw new InvalidExcelFileExtensionException();
        }
    }

    private Iterator<Sheet> getSheetsFromXls(MultipartFile multipartFile) throws IOException {
        Workbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
        return workbook.iterator();
    }
}
