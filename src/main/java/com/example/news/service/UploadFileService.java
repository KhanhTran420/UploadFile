package com.example.news.service;

import com.example.news.exception.UploadFailException;
import com.example.news.model.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadFileService {
    FileDto uploadFile(String fileType, MultipartFile file) throws IOException, UploadFailException;
}
