package com.example.news.model.dto;

import com.example.news.model.Category;
import com.example.news.model.type.NewsContentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private int currentQuantity;
    private double costPrice;
    private double salePrice;
    private String image;
    private Category category;
    private boolean activated;
    private boolean deleted;
    private NewsContentType newContentType;
}
