package com.arje;

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

import static com.arje.Planner.PDF;

public class FileUtils {

    static Iterator<Sheet> getSheetsFromXls(String pathToXlsFile) {
        try(FileInputStream xlsFile = new FileInputStream(pathToXlsFile)) {
            Workbook workbook = new XSSFWorkbook(xlsFile);
            return workbook.iterator();
        } catch (IOException e) {
            throw new RuntimeException("ERROR! Couldn't load " + pathToXlsFile);
        }
    }

    static String getStringFromFile(String pathToFile) {
        try {
            File file = new File(pathToFile);
            return org.apache.commons.io.FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("ERROR! Couldn't load " + pathToFile);
        }
    }

    static void writePDF(String pathToSourceHtml) {
        File inputHTML = new File(pathToSourceHtml);

        Document document;
        try {
            document = Jsoup.parse(inputHTML, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        try (OutputStream outputStream = new FileOutputStream(getPathWithDifferentExtension(pathToSourceHtml, PDF))) {
            ITextRenderer renderer = new ITextRenderer();
            SharedContext sharedContext = renderer.getSharedContext();
            sharedContext.setMedia("screen");
//            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);
            sharedContext.setReplacedElementFactory(new MediaReplacedElementFactory(renderer.getSharedContext().getReplacedElementFactory()));
            renderer.setDocumentFromString(document.html());
            renderer.layout();
            renderer.createPDF(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static String getPathWithDifferentExtension(String sourcePath, String extension) {
        int lastIndexOfSlash = sourcePath.lastIndexOf("/");
        if (lastIndexOfSlash == -1) {
            lastIndexOfSlash = sourcePath.lastIndexOf("\\");
        }
        String basePath = sourcePath.substring(0, lastIndexOfSlash);
        String fileName = sourcePath.replace(basePath, "");
        String rawFileName = fileName.substring(0, fileName.indexOf("."));
        return basePath + "/" + rawFileName + extension;
    }
}
