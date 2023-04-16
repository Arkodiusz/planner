package com.arje.spreadsheet;

import com.arje.exception.InvalidExcelFileExtensionException;
import com.arje.helpers.SimpleRow;
import com.arje.helpers.SimpleSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtils {

    public static final String XLSX = ".xlsx";

    public static Iterator<SimpleSheet> getSheetsFromXls(MultipartFile multipartFile) throws IOException {
        validateXlsFileName(multipartFile);
        Workbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
        List<SimpleSheet> simpleSheets = new ArrayList<>();
        for (Sheet xlsxSheet : workbook) {
            SimpleSheet simpleSheet = new SimpleSheet();
            int currentRow = 0;
            Iterator<Row> rowIterator = xlsxSheet.iterator();
            while (rowIterator.hasNext()) {
                boolean isEmptyRow = false;
                Row xlsxRow = xlsxSheet.getRow(currentRow);
                SimpleRow simpleRow = new SimpleRow();
                if (xlsxRow != null) {
                    int currentCell = 0;
                    Iterator<Cell> cellIterator = xlsxRow.iterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = xlsxRow.getCell(currentCell);
                        if (cell != null) {
                            simpleRow.add(cell.toString());
                            cellIterator.next();
                        } else {
                            simpleRow.add("");
                        }
                        currentCell++;
                    }
                } else {
                    simpleRow.add("");
                    isEmptyRow = true;
                }
                simpleSheet.add(simpleRow);
                if (!isEmptyRow) {
                    rowIterator.next();
                }
                currentRow++;
            }
            simpleSheets.add(simpleSheet);
        }
        return simpleSheets.iterator();
    }

    private static void validateXlsFileName(MultipartFile multipartFile) throws InvalidExcelFileExtensionException {
        String filename = multipartFile.getOriginalFilename();
        if (filename == null || !filename.endsWith(XLSX)) {
            throw new InvalidExcelFileExtensionException();
        }
    }
}
