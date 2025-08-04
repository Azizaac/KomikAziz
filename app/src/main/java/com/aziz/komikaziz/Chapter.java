package com.aziz.komikaziz;

import java.io.Serializable;

public class Chapter implements Serializable {
    private String title;
    private String endpoint;

    public Chapter(String title, String endpoint) {
        this.title = title;
        this.endpoint = endpoint;
    }

    public String getTitle() {
        return title;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
