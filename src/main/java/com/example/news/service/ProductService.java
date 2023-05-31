package com.example.news.service;

import com.example.news.model.Product;
import com.example.news.model.dto.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    Product createProduct(MultipartFile imageProduct, ProductDto productDto);

    List<Product> getAllProduct();

    Product updateProduct(MultipartFile file, ProductDto productDto);
}
