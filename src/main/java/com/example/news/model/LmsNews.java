package com.example.news.model;

import com.example.news.model.type.NewsContentType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "lms_news")
public class LmsNews extends BaseEntity implements Serializable {

    @Column(name = "subject")
    private String subject;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(joinColumns = {
            @JoinColumn(nullable = false, updatable = false)},inverseJoinColumns = {
            @JoinColumn(nullable = false, updatable = false)})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<LmsNewsLabel> lmsNewsLabels = new HashSet<>();

    @Column(name = "text_content")
    private String textContent;

    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    private NewsContentType contentType;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "is_hot_news")
    private Boolean isHotNews;

    @Column(name = "is_pinned")
    private Boolean isPinned;

    @Column(name = "attachment_link")
    private String attachmentLink;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "course_link")
    private Integer courseLink;

    @Column(name = "eventLink")
    private Integer eventLink;
}
