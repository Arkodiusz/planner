package com.arje.html;

import com.arje.training.Data;
import com.arje.training.Training;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ThymeleafHtmlBuilder implements HtmlBuilder {

    public static final String STYLE_VARIABLE = "style";
    public static final String DATA_VARIABLE = "data";
    public static final String TRAININGS_VARIABLE = "trainings";

    String pathToTemplateFile;
    FileTemplateResolver resolver;
    Context context;
    TemplateEngine templateEngine;

    public ThymeleafHtmlBuilder(String pathToTemplateFile) {
        this.pathToTemplateFile = pathToTemplateFile;
        this.resolver = new FileTemplateResolver();
        this.resolver.setTemplateMode(TemplateMode.HTML);
        this.resolver.setCharacterEncoding("UTF-8");
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(resolver);
        this.context = new Context();
    }

    public String getHtmlString() {
        return templateEngine.process(pathToTemplateFile, context);
    }

    public void includeStyling(String styling) {
        context.setVariable(STYLE_VARIABLE, "\n" + styling + "\n");
    }

    public void includePlanInfo(Sheet sheetWithPlanInfo) {
        for (Row row : sheetWithPlanInfo) {
            String marker = row.getCell(0).toString();
            String text = row.getCell(1).toString();
            context.setVariable(marker, text);
        }
    }

    public void includePlanData(Sheet sheetWithPlanData) {
        Data data = new Data(sheetWithPlanData);
        context.setVariable(DATA_VARIABLE, data.getData());
    }

    public void includeTrainings(Iterator<Sheet> sheets) {
        List<Training> trainings = new ArrayList<>();
        while (sheets.hasNext()) {
            Training training = new Training(sheets.next());
            trainings.add(training);
        }
        context.setVariable(TRAININGS_VARIABLE, trainings);
    }
}
