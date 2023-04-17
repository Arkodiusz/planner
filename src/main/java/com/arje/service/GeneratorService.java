package com.arje.service;

import com.arje.spreadsheet.GoogleSheetsUtils;
import com.arje.exception.InvalidGeneratedPdfException;
import com.arje.helpers.SimpleSheet;
import com.arje.html.ThymeleafHtmlBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Iterator;

import static com.arje.spreadsheet.ExcelUtils.getSheetsFromXls;
import static com.arje.spreadsheet.GoogleSheetsUtils.*;

@Service
@AllArgsConstructor
public class GeneratorService {

    private final PdfWriterService pdfWriter;

    public byte[] generatePdfFromXlsxFile(MultipartFile xlsxFile) throws IOException {
        Iterator<SimpleSheet> trainingData = getSheetsFromXls(xlsxFile);
        return getGeneratedPdfWithTrainingData(trainingData);
    }

    public byte[] generatePdfFromGoogleSpreadsheetUrl(String spreadsheetUrl) throws IOException, GeneralSecurityException {
        Iterator<SimpleSheet> trainingData = getSheetsFromGoogleSpreadsheet(spreadsheetUrl);
        return getGeneratedPdfWithTrainingData(trainingData);
    }

    private byte[] getGeneratedPdfWithTrainingData(Iterator<SimpleSheet> sheets) {
        String processedHtmlAsString = new ThymeleafHtmlBuilder().getHtmlString(sheets);
        byte[] generatedPdf = pdfWriter.convertHtmlToPdf(processedHtmlAsString);
        validateGeneratedFile(generatedPdf);
        return generatedPdf;
    }

    private static void validateGeneratedFile(byte[] generatedPdf) throws InvalidGeneratedPdfException {
        if (generatedPdf == null || generatedPdf.length == 0) {
            throw new InvalidGeneratedPdfException();
        }
    }
}
