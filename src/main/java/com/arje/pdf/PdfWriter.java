package com.arje.pdf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.arje.utils.FileUtils.getPathWithDifferentExtension;

public class PdfWriter {

    private static final String PDF = ".pdf";

    public void writePDF(String pathToSourceHtml) {
        File inputHTML = new File(pathToSourceHtml);

        Document document;
        try {
            document = Jsoup.parse(inputHTML, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        try (OutputStream outputStream = new FileOutputStream(getPathWithDifferentExtension(pathToSourceHtml, PDF))) {
            ITextRenderer renderer = new ITextRenderer();
            SharedContext sharedContext = renderer.getSharedContext();
            sharedContext.setMedia("screen");
//            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);
            sharedContext.setReplacedElementFactory(new MediaReplacedElementFactory(renderer.getSharedContext().getReplacedElementFactory()));
            renderer.setDocumentFromString(document.html());
            renderer.layout();
            renderer.createPDF(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
