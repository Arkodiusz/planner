package com.arje.service;

import com.itextpdf.text.pdf.BaseFont;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PdfWriterService {

    private static final int PAGE_HEIGHT_PX = 1056;
    private static final String FONTS_DIRECTORY = "src/main/resources/templates/assets/fonts/";

    public byte[] convertHtmlToPdf(String sourceHtml) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            Document document = Jsoup.parse(sourceHtml);
            document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
            int pageCount = calculatePageCount(renderer, document);
            renderer.getSharedContext().setInteractive(false);
            renderer.setDocumentFromString(getHtmlWithFooterPlacedAtBottomOfLastPdfPage(document, pageCount));
            renderer.getFontResolver().addFont(FONTS_DIRECTORY + "Barlow-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.getFontResolver().addFont(FONTS_DIRECTORY + "Barlow-Medium.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.getFontResolver().addFont(FONTS_DIRECTORY + "Barlow-SemiBold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.getFontResolver().addFont(FONTS_DIRECTORY + "Barlow-Black.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int calculatePageCount(ITextRenderer renderer, Document document) {
        renderer.setDocumentFromString(document.html());
        renderer.layout();
        return renderer.getRootBox().getLayer().getPages().size();
    }

    private String getHtmlWithFooterPlacedAtBottomOfLastPdfPage(Document document, int pageCount) {
        return document.html().replace("$pixels", "-" + ((pageCount - 1) * PAGE_HEIGHT_PX) + "px");
    }
}
