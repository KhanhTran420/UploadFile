package com.example.news.service;

import com.example.news.exception.ResourceNotFoundException;
import com.example.news.exception.UploadFailException;
import com.example.news.model.dto.FileDto;
import com.example.news.utils.MultipartInputStreamFileResource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UploadFileServiceImpl implements UploadFileService {

    @Value("https://media.global.com.vn/")
    private String fileURL;

    @Value("CV,ICON,AVATAR,IMAGE,OTHER")
    private String fileType;

    String bucketName = "khanh";

    @Override
    public FileDto uploadFile(String fileType, MultipartFile file) throws IOException {
        if (!checkFileType(fileType)){
            return new FileDto("Type file does nit exist");
        }
        String url = new StringBuilder(fileURL).append("upload?bucketName=").append(bucketName).append("&fileType=").append(fileType).toString();
        LinkedMultiValueMap<String,Object> body = new LinkedMultiValueMap<>();
        body.add("file", new MultipartInputStreamFileResource(file.getInputStream(),file.getOriginalFilename()));
        return callAPI(body,url,FileDto.class);
    }

    public<T> T callAPI(LinkedMultiValueMap<String, Object> body, String url, Class<T> obj) {
        T javaObject = null;

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.ALL_VALUE);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String,Object>> entity = new HttpEntity<>(body,headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url,entity,String.class);
        if (response.getStatusCode().value() != 200){
            throw new ResourceNotFoundException(String.format("Error call API uploadfile: %",response.getBody(), HttpStatus.UNPROCESSABLE_ENTITY));
        }

        System.out.println(String.format("response: %",response.getBody()));
        try {
            Gson gson = new GsonBuilder().create();
            javaObject = gson.fromJson(response.getBody(),obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return javaObject;
    }

    private boolean checkFileType(String file) {
        List<String> items = Arrays.stream(fileType.split("\\s*,\\s*"))
                .filter(file::equals)
                .collect(Collectors.toList());
        return items.size() != 0;
    }
}
