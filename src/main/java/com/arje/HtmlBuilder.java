package com.arje;

public class HtmlBuilder {

    private String htmlString;

    public HtmlBuilder(String htmlString) {
        this.htmlString = htmlString;
    }

    public void replace(String marker, String text) {
        htmlString = htmlString.replace(marker, text);
    }

    public String getHtmlString() {
        return htmlString;
    }
}

