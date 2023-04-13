package com.arje.training;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Exercise {

    private String name = "";
    private String link = "";
    private final List<String> details = new ArrayList<>();

    public Exercise(Row row, int columnCount) {
        int filledCells = 1;
        for (int column = 0; column < columnCount; column++) {
            if (column == 0 && row.getCell(0) != null) {
                String[] split = row.getCell(0).toString().split("@");
                if (split.length == 1) {
                    this.name = split[0];
                } else if (split.length == 2) {
                    this.name = split[0];
                    this.link = split[1];
                } else {
                    throw new RuntimeException("ERROR! Exercise name/link format error in sheet" + row.getSheet().getSheetName() + ", row " + row.getRowNum());
                }
            }
            if (column > 0) {
                if (row.getCell(column) != null) {
                    details.add(row.getCell(column).toString().replace(".0", ""));
                } else {
                    details.add("");
                }
                filledCells++;
            }
        }
        while (filledCells < columnCount) {
            details.add("");
            filledCells++;
        }
    }
}
