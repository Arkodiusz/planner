package com.arje.html;

import com.arje.data.Data;
import com.arje.data.Training;
import com.arje.exception.CssProcessingException;
import com.arje.exception.HtmlProcessingException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ThymeleafHtmlBuilder {

    public static final String PATH_TO_TEMPLATE_HTML = "pdf_document/pdf_template.html";
    public static final String PATH_TO_TEMPLATE_CSS = "pdf_document/pdf_template.css";

    public static final String STYLE_VARIABLE = "style";
    public static final String DATA_VARIABLE = "data";
    public static final String TRAININGS_VARIABLE = "trainings";

    private final Context context = new Context();
    private final TemplateEngine templateEngine = new TemplateEngine();
    private final ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();

    public String getHtmlString(Iterator<Sheet> sheets) {
        try {
            includeStyling(getCssFileAsString());
            includePlanInfo(sheets.next());
            includePlanData(sheets.next());
            includeTrainings(sheets);

            resolver.setTemplateMode(TemplateMode.HTML);
            resolver.setCharacterEncoding("UTF-8");
            templateEngine.setTemplateResolver(resolver);
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

    private String getCssFileAsString() throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:" + PATH_TO_TEMPLATE_CSS);
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
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
