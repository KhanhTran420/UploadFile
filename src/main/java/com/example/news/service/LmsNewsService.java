package com.example.news.service;

import com.example.news.exception.UploadFailException;
import com.example.news.model.LmsNews;
import com.example.news.model.dto.NewsDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface LmsNewsService {
    LmsNews createNews(NewsDto newsDto, MultipartFile attachment, MultipartFile thumbnail) throws IOException, UploadFailException;

    List<LmsNews> getAllNews();
}
