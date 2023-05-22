package com.example.news.model.type;

public enum NewsContentType {
    VIDEO("video"),
    IMAGE("anh");

    public final String type;

    NewsContentType(String type) {
        this.type = type;
    }
}
