package com.arje.helpers;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SimpleRow {
    private final List<String> cells = new ArrayList<>();

    public String getCell(int id) {
        return cells.get(id);
    }

    public void add(String cell) {
        cells.add(cell);
    }
}
