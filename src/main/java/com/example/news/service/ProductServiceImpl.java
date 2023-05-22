package com.example.news.service;

import com.example.news.exception.ResourceNotFoundException;
import com.example.news.model.Product;
import com.example.news.model.dto.ProductDto;
import com.example.news.repository.ProductRepository;
import com.example.news.utils.ImageUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    private final ImageUpload imageUpload;

    @Override
    public Product createProduct(MultipartFile imageProduct, ProductDto productDto) {
        Product product = new Product();
        BeanUtils.copyProperties(productDto,product);
        try {
            if (imageProduct == null) {
                product.setImage(null);
            } else {
                imageUpload.uploadFile(imageProduct);
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
            if (Objects.nonNull(productDto.getName())){
                if (productRepository.existsProductByName(productDto.getName())){
                    throw new ResourceNotFoundException("tên sản phẩm đã tồn tại");
                }
                product.setName(productDto.getName());
            }
            product.setDescription(productDto.getDescription());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.setCostPrice(productDto.getCostPrice());
            product.setCategory(productDto.getCategory());
            product.set_deleted(false);
            product.set_activated(true);
            return productRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }
}
