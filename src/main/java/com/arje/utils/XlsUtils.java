package com.arje.utils;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class XlsUtils {

    public static Iterator<Sheet> getSheetsFromXls(String pathToXlsFile) {
        try (FileInputStream xlsFile = new FileInputStream(pathToXlsFile)) {
            Workbook workbook = new XSSFWorkbook(xlsFile);
            return workbook.iterator();
        } catch (IOException e) {
            throw new RuntimeException("ERROR! Couldn't load " + pathToXlsFile);
        }
    }
}
