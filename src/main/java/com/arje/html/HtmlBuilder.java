package com.arje.html;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.Iterator;

public interface HtmlBuilder {

    String getHtmlString();
    void includeStyling(String styling);
    void includePlanInfo(Sheet sheetWithPlanInfo);
    void includePlanData(Sheet sheetWithPlanData);
    void includeTrainings(Iterator<Sheet> sheets);
}
