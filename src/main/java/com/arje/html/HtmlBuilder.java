package com.arje.html;

import com.arje.training.Training;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Iterator;

public class HtmlBuilder {

    private static final String $ = "$";
    private static final String $_STYLE = "$style";
    private static final String $_PLANS = "$plans";

    private String htmlString;

    public HtmlBuilder(String htmlString) {
        this.htmlString = htmlString;
    }


    public void includeStyling(String cssString) {
        replace($_STYLE, cssString);
    }

    public void includePlanInfo(Sheet sheetWithPlanInfo) {
        for (Row row : sheetWithPlanInfo) {
            replaceMarkerWithText(row);
        }
    }

    public void includeTrainingPlans(Iterator<Sheet> sheets) {
        StringBuilder trainingPlans = new StringBuilder();
        while (sheets.hasNext()) {
            Training training = new Training(sheets.next());
            trainingPlans.append(training.toHtml());
        }
        replace($_PLANS, trainingPlans.toString());
    }

    private void replaceMarkerWithText(Row row) {
        String marker = $ + row.getCell(0).toString();
        String text = row.getCell(1).toString();
        replace(marker, text);
    }

    private void replace(String marker, String text) {
        htmlString = htmlString.replace(marker, text);
    }

    public String getHtmlString() {
        return htmlString;
    }
}

