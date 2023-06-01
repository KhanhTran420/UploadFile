package com.example.news.service;

import com.example.news.exception.ResourceNotFoundException;
import com.example.news.exception.UploadFailException;
import com.example.news.model.Product;
import com.example.news.model.dto.ProductDto;
import com.example.news.model.type.NewsContentType;
import com.example.news.repository.ProductRepository;
import com.example.news.utils.ImageUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    private final ImageUpload imageUpload;

    private static final int MAXIMUM_SIZE_IMAGE = 15;
    private static final int PARAM_CONVERT_BYTES_TO_MB = 1024*1024;
    private static final int MINIMUM_WIDTH_IMAGE = 1440;
    private static final int MINIMUM_HEIGHT_IMAGE = 580;

    private final List<String> listFileType = Arrays.asList("png","jpg","jpeg");
    private final List<String> listVideoType = Arrays.asList("mp4");

    @Override
    public Product createProduct(MultipartFile imageProduct, ProductDto productDto) {
        Product product = new Product();
        BeanUtils.copyProperties(productDto,product);
        try {
            if (imageProduct == null) {
                product.setImage(null);
            } else {
                validateFile(imageProduct, productDto.getNewContentType());
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

    @Override
    public Product updateProduct(MultipartFile imageProduct, ProductDto productDto) {
        Product product = productRepository.findById(productDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("new does not existed"));

        BeanUtils.copyProperties(productDto,product);

        try {
            if (imageProduct == null){
                product.setImage(product.getImage());
            }
            else {
                if (imageUpload.checkExist(imageProduct) == false){
                    imageUpload.uploadFile(imageProduct);
                }
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
            if (Objects.nonNull(productDto.getName())){
                if (!productDto.getName().equals(product.getName())){
                    if (productRepository.existsProductByName(productDto.getName())){
                        throw new ResourceNotFoundException("tên sản phẩm đã tồn tại");
                    }
                    product.setName(productDto.getName());
                }
            }
            product.setDescription(productDto.getDescription());
            product.setCategory(productDto.getCategory());
            product.setCostPrice(productDto.getCostPrice());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.set_activated(true);
            product.set_deleted(false);
            return productRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void validateFile(MultipartFile file, NewsContentType type) throws UploadFailException{
        String [] fileTypeArr = Objects.requireNonNull(file.getOriginalFilename())
                .split("\\.");
        String fileType = fileTypeArr[fileTypeArr.length - 1]
                .toLowerCase();

        if (type.equals(NewsContentType.VIDEO)){
            if (!listVideoType.contains(fileType)){
                throw new UploadFailException("Hệ thống chỉ hỗ trợ định dạng "+ listVideoType);
            }
            if ((file.getSize()/(float) PARAM_CONVERT_BYTES_TO_MB > 200)){
                throw new UploadFailException("Dung lượng video vượt quá 200MB");
            }
            return;
        }
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());

            if (listVideoType.contains(fileType)){
                if ((file.getSize()/(float) PARAM_CONVERT_BYTES_TO_MB > 200)){
                    throw new UploadFailException("Hệ thống chỉ hỗ trợ định dạng mp4, tối đa 200MB");
                }
                return;
            }

            if (bufferedImage.getWidth() < MINIMUM_WIDTH_IMAGE || bufferedImage.getHeight() < MINIMUM_HEIGHT_IMAGE) {
                throw new UploadFailException("Kích cỡ ảnh tối thiểu: 1440 x 580");
            }

            if (file.getSize() / (float) PARAM_CONVERT_BYTES_TO_MB > MAXIMUM_SIZE_IMAGE
                    || !listFileType.contains(fileType)) {
                throw new UploadFailException("Định dạng ảnh không hợp lệ (png,jpg,jpeg). " +
                        "Dung lượng ảnh tối đa là 15MB ");
            }
        } catch (IOException e) {
            throw new UploadFailException("Không thể upload tệp tin");
        }
    }
}
