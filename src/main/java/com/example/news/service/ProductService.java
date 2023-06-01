package com.example.news.service;

import com.example.news.exception.CustomException;
import com.example.news.exception.ResourceNotFoundException;
import com.example.news.model.Product;
import com.example.news.model.dto.ProductDto;
import com.example.news.model.type.NewsContentType;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    Product createProduct(MultipartFile imageProduct, ProductDto productDto);

    List<Product> getAllProduct();

    Product updateProduct(MultipartFile file, ProductDto productDto);

    ProductDto findById(Long id);

    void deleteProduct(Long id) throws ResourceNotFoundException;

    Page<ProductDto> getListProducts(String keyword, List<NewsContentType> contentType, Integer page, Integer size);
}
