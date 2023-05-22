package com.example.news.utils;

import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;

public class MultipartInputStreamFileResource extends InputStreamResource {
    private final String fileName;

    public MultipartInputStreamFileResource(InputStream inputStream, String fileName){
        super(inputStream);
        this.fileName = fileName;
    }

}
