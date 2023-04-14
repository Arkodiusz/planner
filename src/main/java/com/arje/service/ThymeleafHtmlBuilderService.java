package com.arje.service;

import com.arje.data.Data;
import com.arje.data.Training;
import com.arje.exception.CssProcessingException;
import com.arje.exception.HtmlProcessingException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.io.FileUtils.readFileToString;

@Service
public class ThymeleafHtmlBuilderService {

    public static final String PATH_TO_TEMPLATE_HTML = "src/main/resources/templates/template.html";
    public static final String PATH_TO_TEMPLATE_CSS = "src/main/resources/templates/template.css";

    public static final String STYLE_VARIABLE = "style";
    public static final String DATA_VARIABLE = "data";
    public static final String TRAININGS_VARIABLE = "trainings";

    private final Context context = new Context();;
    private final TemplateEngine templateEngine = new TemplateEngine();;

    public ThymeleafHtmlBuilderService() {
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        this.templateEngine.setTemplateResolver(resolver);
    }

    public String getHtmlString(Iterator<Sheet> sheets) {
        try {
            includeStyling(readFileToString(new File(PATH_TO_TEMPLATE_CSS), StandardCharsets.UTF_8));
            includePlanInfo(sheets.next());
            includePlanData(sheets.next());
            includeTrainings(sheets);
            return templateEngine.process(PATH_TO_TEMPLATE_HTML, context);
        } catch (RuntimeException e) {
            String message = e.getMessage();
            if (message == null || message.isEmpty()) {
                message = "Error during processing HTML template. Check formatting in excel file";
            }
            throw new HtmlProcessingException(message);
        } catch (IOException e) {
            throw new CssProcessingException(e.getMessage());
        }
    }

    private void includeStyling(String styling) {
        context.setVariable(STYLE_VARIABLE, "\n" + styling + "\n");
    }

    private void includePlanInfo(Sheet sheetWithPlanInfo) {
        for (Row row : sheetWithPlanInfo) {
            String marker = row.getCell(0).toString();
            String text = row.getCell(1).toString();
            context.setVariable(marker, text);
        }
    }

    private void includePlanData(Sheet sheetWithPlanData) {
        Data data = new Data(sheetWithPlanData);
        context.setVariable(DATA_VARIABLE, data.getData());
    }

    private void includeTrainings(Iterator<Sheet> sheets) {
        List<Training> trainings = new ArrayList<>();
        while (sheets.hasNext()) {
            Training training = new Training(sheets.next());
            trainings.add(training);
        }
        context.setVariable(TRAININGS_VARIABLE, trainings);
    }
}
