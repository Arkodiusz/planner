package com.arje.pdf;

import com.itextpdf.text.pdf.BaseFont;
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

    public static void convertHtmlToPdf(File sourceHtml) {
        try (OutputStream outputStream = new FileOutputStream(getPathWithDifferentExtension(sourceHtml.getAbsolutePath(), PDF))) {
            ITextRenderer renderer = new ITextRenderer();
            Document document = Jsoup.parse(sourceHtml, "UTF-8");
            document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
            int pageCount = calculatePageCount(renderer, document);
            renderer.getSharedContext().setInteractive(false);
            renderer.setDocumentFromString(getHtmlWithFooterPlacedAtBottomOfLastPdfPage(document, pageCount));
            renderer.getFontResolver().addFont("template/assets/fonts/Barlow-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.getFontResolver().addFont("template/assets/fonts/Barlow-Medium.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.getFontResolver().addFont("template/assets/fonts/Barlow-SemiBold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.getFontResolver().addFont("template/assets/fonts/Barlow-Black.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
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
