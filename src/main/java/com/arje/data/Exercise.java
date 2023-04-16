package com.arje.data;

import com.arje.helpers.SimpleRow;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Getter
public class Exercise {

    private final String name;
    private final String link;
    private final List<String> details = new ArrayList<>();

    public Exercise(SimpleRow row, int columnCount) {
        int filledCells = 1;

        Iterator<String> iterator = row.getCells().iterator();

        String[] split = iterator.next().split("@");
        if (split.length == 2) {
            this.name = split[0];
            this.link = split[1];
        } else {
            this.name = split[0];
            this.link = "";
        }

        while (iterator.hasNext()) {
            details.add(iterator.next());
            filledCells++;
        }

        while (filledCells < columnCount) {
            details.add("");
            filledCells++;
        }
    }
}
