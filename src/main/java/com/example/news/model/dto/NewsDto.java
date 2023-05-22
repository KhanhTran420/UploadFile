package com.example.news.model.dto;

import com.example.news.model.type.NewsContentType;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewsDto {
    private Long id;
    private String subject;
    private String textContent;
    private List<String> lmsNewsLabels = new ArrayList<>();
    private NewsContentType contentType;
    private Boolean status;
    private Boolean isHotNews;
    private Boolean isPinned;
    private Integer courseLink;
    private Integer eventLink;
    private Timestamp createdDate;
    private String attachmentLink;
    private String thumbnail;
    private String createdBy;
//    private List<Long> userIds;

}
