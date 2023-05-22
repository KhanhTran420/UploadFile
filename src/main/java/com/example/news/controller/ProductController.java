package com.example.news.controller;

import com.example.news.model.BaseResponse;
import com.example.news.model.Product;
import com.example.news.model.dto.ProductDto;
import com.example.news.service.ProductService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    private Gson gson = new Gson();


    @GetMapping
    public ResponseEntity<List<Product>> findAllProduct(){
        List<Product> products = productService.getAllProduct();
        return new  ResponseEntity<>(products,HttpStatus.OK);
    }


    @PostMapping("/create")
    public ResponseEntity createProduct(@RequestPart String request,@RequestPart MultipartFile file)  {
        ProductDto productDto = gson.fromJson(request,ProductDto.class);
//        Product product = productService.createProduct(file,productDto);
        return ResponseEntity.ok(productService.createProduct(file,productDto));
    }
}