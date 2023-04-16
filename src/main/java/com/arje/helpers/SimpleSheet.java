package com.arje.helpers;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SimpleSheet {
    private final List<SimpleRow> rows = new ArrayList<>();

    public void add(SimpleRow row) {
        rows.add(row);
    }
}
