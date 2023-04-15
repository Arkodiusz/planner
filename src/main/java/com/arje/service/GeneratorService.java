package com.arje.service;

import com.arje.exception.InvalidExcelFileExtensionException;
import com.arje.exception.InvalidGeneratedPdfException;
import com.arje.html.ThymeleafHtmlBuilder;
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

    public byte[] generatePdf(MultipartFile multipartFile) throws IOException {
        validateXlsFileName(multipartFile);
        Iterator<Sheet> sheets = getSheetsFromXls(multipartFile);
        String processedHtmlAsString = new ThymeleafHtmlBuilder().getHtmlString(sheets);
        byte[] generatedPdf = pdfWriter.convertHtmlToPdf(processedHtmlAsString);
        validateGeneratedFile(generatedPdf);
        return generatedPdf;
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

    private static void validateGeneratedFile(byte[] generatedPdf) throws InvalidGeneratedPdfException {
        if (generatedPdf == null || generatedPdf.length == 0) {
            throw new InvalidGeneratedPdfException();
        }
    }
}
