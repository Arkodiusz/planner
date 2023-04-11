package com.arje.training;

import java.util.List;

public class Exercise {

    private final String name;
    private final String link;
    private final List<String> details;

    public Exercise(String name, String link, List<String> details) {
        this.name = name;
        this.link = link;
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public List<String> getDetails() {
        return details;
    }
}
