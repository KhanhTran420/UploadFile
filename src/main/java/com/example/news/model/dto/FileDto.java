package com.example.news.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDto {
    private String fileName;
    private String previewUrl;
    private String message;

    public FileDto(String message) {
        this.message = message;
    }

    public FileDto(String fileName, String previewUrl) {
        this.fileName = fileName;
        this.previewUrl = previewUrl;
    }

    public FileDto() {
    }
}
