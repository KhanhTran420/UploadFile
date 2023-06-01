package com.example.news.controller;

import com.example.news.exception.CustomException;
import com.example.news.exception.ResourceNotFoundException;
import com.example.news.model.BaseResponse;
import com.example.news.model.PaginationResponseModel;
import com.example.news.model.Product;
import com.example.news.model.dto.ProductDto;
import com.example.news.model.type.NewsContentType;
import com.example.news.service.ProductService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Gson gson = new Gson();


    @GetMapping
    public ResponseEntity<List<Product>> findAllProduct(){
        List<Product> products = productService.getAllProduct();
        return new  ResponseEntity<>(products,HttpStatus.OK);
    }


    @PostMapping("/create")
    public ResponseEntity createProduct(@RequestPart String request,@RequestPart MultipartFile file)  {
        ProductDto productDto = gson.fromJson(request,ProductDto.class);
        log.debug("create products: {}", productDto.getName());
//        Product product = productService.createProduct(file,productDto);
        return ResponseEntity.ok(productService.createProduct(file,productDto));
    }

    @PutMapping("/update")
    public ResponseEntity updateProduct(@RequestPart String request,@RequestPart MultipartFile file){
        ProductDto productDto = gson.fromJson(request, ProductDto.class);
        log.debug("create products: {}", productDto.getId());
        return ResponseEntity.ok(productService.updateProduct(file,productDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity getProductById(@PathVariable("id") Long id){
        log.debug("get Product: {}", id);
        return ResponseEntity.ok(productService.findById(id));
    }

    @DeleteMapping
    public ResponseEntity removeProduct(@RequestParam Long id) throws ResourceNotFoundException {
        log.debug("delete Product: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.ok("delete product: " + id);
    }

    @GetMapping("/list")
    public ResponseEntity getListProduct(@RequestParam(value = "keyword", required = false) String keyword,
                                         @RequestParam(value = "contentType", required = false) List<NewsContentType> contentType,
                                         @RequestParam(defaultValue = "1", value = "pageNo") Integer page,
                                         @RequestParam(defaultValue = "25", value = "pageSize") Integer size){
        log.debug("get list product");
        Page<ProductDto> productDtoPage = productService.getListProducts(keyword,contentType,page,size);
        PaginationResponseModel<ProductDto> res = new PaginationResponseModel<>();
        if (productDtoPage.hasContent()){
            long totalRecord = productDtoPage.getTotalElements();
            res = new PaginationResponseModel<>(productDtoPage.toList(),totalRecord,page,size);
        }
        return new ResponseEntity<>(new BaseResponse("get list success", res), HttpStatus.OK);
    }
}
