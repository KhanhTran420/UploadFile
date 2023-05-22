package com.example.news.controller;

import com.example.news.model.LmsNews;
import com.example.news.model.Product;
import com.example.news.model.dto.NewsDto;
import com.example.news.service.LmsNewsService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Log4j2
public class LmsNewsController {

    private final LmsNewsService lmsNewsService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Gson gson = new Gson();

    @PostMapping
    public ResponseEntity createNews(@RequestPart MultipartFile attachment,
                                     @RequestPart(required = false) MultipartFile thumbnail,
                                     @RequestPart String request) throws IOException{
        NewsDto newsDto = gson.fromJson(request,NewsDto.class);
        logger.debug("create news: {}", newsDto.getSubject());
        return ResponseEntity.ok(lmsNewsService.createNews(newsDto,attachment,thumbnail));
    }

    @GetMapping
    public ResponseEntity<List<LmsNews>> findAllNews(){
        List<LmsNews> lmsNews = lmsNewsService.getAllNews();
        return new  ResponseEntity<>(lmsNews, HttpStatus.OK);
    }
}
