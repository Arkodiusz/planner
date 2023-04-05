package com.arje.pdf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.arje.utils.FileUtils.getPathWithDifferentExtension;

public class PdfWriter {

    private static final String PDF = ".pdf";
    private static final int PAGE_HEIGHT_PX = 1056;

    private String escapeHTML(String s){
        StringBuilder sb = new StringBuilder();
        int n = s.length();
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            switch (c) {
                case 'ą': sb.append("&#261;"); break;
                case 'ć': sb.append("&#263;"); break;
                case 'ę': sb.append("&#281;"); break;
                case 'ł': sb.append("&#322;"); break;
                case 'ń': sb.append("&#324;"); break;
                case 'ó': sb.append("&#243;"); break;
                case 'ś': sb.append("&#347;"); break;
                case 'ź': sb.append("&#378;"); break;
                case 'ż': sb.append("&#380;"); break;
                case 'Ą': sb.append("&#260;"); break;
                case 'Ć': sb.append("&#262;"); break;
                case 'Ę': sb.append("&#280;"); break;
                case 'Ł': sb.append("&#321;"); break;
                case 'Ń': sb.append("&#323;"); break;
                case 'Ó': sb.append("&#211;"); break;
                case 'Ś': sb.append("&#346;"); break;
                case 'Ź': sb.append("&#377;"); break;
                case 'Ż': sb.append("&#379;"); break;

                default: sb.append(c); break;
            }
        }
        return sb.toString();
    }

    public static void convertHtmlToPdf(File sourceHtml) {
        try (OutputStream outputStream = new FileOutputStream(getPathWithDifferentExtension(sourceHtml.getAbsolutePath(), PDF))) {
            ITextRenderer renderer = new ITextRenderer();
            Document document = Jsoup.parse(sourceHtml, "UTF-8");
            document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
            int pageCount = calculatePageCount(renderer, document);
            renderer.getSharedContext().setInteractive(false);
            renderer.setDocumentFromString(getHtmlWithFooterPlacedAtBottomOfLastPdfPage(document, pageCount));
            renderer.getFontResolver().addFont("template/assets/fonts/Barlow-Regular.ttf", true);
            renderer.getFontResolver().addFont("template/assets/fonts/Barlow-Bold.ttf", true);
            renderer.getFontResolver().addFont("template/assets/fonts/Barlow-Black.ttf", true);
            renderer.layout();
            renderer.createPDF(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int calculatePageCount(ITextRenderer renderer, Document document) {
        renderer.setDocumentFromString(document.html());
        renderer.layout();
        return renderer.getRootBox().getLayer().getPages().size();
    }

    private static String getHtmlWithFooterPlacedAtBottomOfLastPdfPage(Document document, int pageCount) {
        return document.html().replace("$pixels", "-" + ((pageCount - 1) * PAGE_HEIGHT_PX) + "px");
    }
}
