package com.example.news.repository;

import com.example.news.model.Product;
import com.example.news.model.type.NewsContentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsProductByName(String name);

    Page<Product> getListProduct(String keyword, List<NewsContentType> contentType, Pageable pageable);
}
