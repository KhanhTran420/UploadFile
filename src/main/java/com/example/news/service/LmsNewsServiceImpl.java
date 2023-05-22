package com.example.news.service;

import com.example.news.exception.CustomException;
import com.example.news.exception.UploadFailException;
import com.example.news.model.LmsNews;
import com.example.news.model.LmsNewsLabel;
import com.example.news.model.dto.NewsDto;
import com.example.news.model.type.NewsContentType;
import com.example.news.repository.LmsNewsLabelRepository;
import com.example.news.repository.LmsNewsRepository;
import io.micrometer.common.util.StringUtils;
import org.jsoup.Jsoup;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class LmsNewsServiceImpl implements LmsNewsService {

    @Autowired
    private LmsNewsRepository lmsNewsRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private LmsNewsLabelRepository lmsNewsLabelRepository;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private ModelMapper modelMapper;


    @Value("https://media.global.com.vn/")
    private String fileURL;
    private static final int MAXIMUM_SIZE_IMAGE = 15;
    private static final int LMS_NEWS_PINED_LIMIT = 10;
    private static final int LIMIT_CONTENT_NEWS = 5000;

    private static final int PARAM_CONVERT_BYTES_TO_MB = 1024*1024;
    private static final int MINIMUM_WIDTH_IMAGE = 1440;
    private static final int MINIMUM_HEIGHT_IMAGE = 580;

    private final List<String> listFileType = Arrays.asList("png","jpg","jpeg");
    private final List<String> listVideoType = Arrays.asList("mp4");

    @Override
    public LmsNews createNews(NewsDto newsDto, MultipartFile attachment, MultipartFile thumbnail) throws IOException, UploadFailException {
        LmsNews news = new LmsNews();
        BeanUtils.copyProperties(newsDto,news);

        for (String label : newsDto.getLmsNewsLabels()){
            if (Boolean.FALSE.equals(lmsNewsLabelRepository.existsByLabel(label))){
                LmsNewsLabel lmsNewsLabel = new LmsNewsLabel();
                lmsNewsLabel.setLabel(label);
                lmsNewsLabelRepository.save(lmsNewsLabel);
            }
            news.getLmsNewsLabels().add(lmsNewsLabelRepository.findByLabel(label));
        }

        validateLengthContent(newsDto);

        if (Objects.nonNull(attachment)){
            validateFile(attachment, newsDto.getContentType());
            //upload file
            news.setAttachmentLink(uploadFileService.uploadFile("OTHER",attachment).getPreviewUrl());
            // upload thumbnail
            if (Objects.nonNull(thumbnail)){
                news.setThumbnail(uploadFileService.uploadFile("OTHER", thumbnail).getPreviewUrl());
            }
        }
        return lmsNewsRepository.save(news);

    }

    @Override
    public List<LmsNews> getAllNews() {
        return lmsNewsRepository.findAll();
    }

    private void validateLengthContent(NewsDto newsDto) {
        if (Objects.isNull(newsDto.getTextContent())){
            throw new CustomException("mo ta noi dung khong the bo trong", HttpStatus.BAD_REQUEST);
        }
        if (removeHTMLTextInValue(newsDto.getTextContent()).length() > LIMIT_CONTENT_NEWS){
            throw new CustomException("noi dung khong duoc truyn qua 10.000 ki tu", HttpStatus.BAD_REQUEST);
        }
    }

    private String removeHTMLTextInValue(String value){
        if (StringUtils.isNotBlank(value)){
            value = Jsoup.parse(value).text();
        }
        return value;
    }

    private void validateFile(MultipartFile attachment, NewsContentType type) throws UploadFailException {
        String [] fileTypeArr = Objects.requireNonNull(attachment.getOriginalFilename())
                .split("\\.");
        String fileType = fileTypeArr[fileTypeArr.length - 1]
                .toLowerCase();

        if (type.equals(NewsContentType.VIDEO)){
            if (!listVideoType.contains(fileType)){
                throw new UploadFailException("error");
            }
            if ((attachment.getSize()/(float) PARAM_CONVERT_BYTES_TO_MB > 200)){
                throw new UploadFailException("error");
            }
            return;
        }
        try {
            BufferedImage bufferedImage = ImageIO.read(attachment.getInputStream());

            if (listVideoType.contains(fileType)){
                if ((attachment.getSize()/(float) PARAM_CONVERT_BYTES_TO_MB >200)){
                    throw new UploadFailException("error");
                }
                return;
            }
            if (bufferedImage.getWidth() < MINIMUM_WIDTH_IMAGE || bufferedImage.getHeight() < MINIMUM_HEIGHT_IMAGE){
                throw new UploadFailException("error");
            }
            if (attachment.getSize()/(float) PARAM_CONVERT_BYTES_TO_MB > MAXIMUM_SIZE_IMAGE || !listFileType.contains(fileType)){
                throw new UploadFailException("error");
            }
        } catch (IOException e) {
            throw new UploadFailException("ko the upload tep tin");
        }
    }
}
