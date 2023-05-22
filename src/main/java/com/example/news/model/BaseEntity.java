package com.example.news.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.sql.Timestamp;

@MappedSuperclass
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"version","last_updated","last_updated_by","deleted","last_updated"})
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Long id;

    @Column(name = "version", columnDefinition = "int default 1")
    public int version;

    @Column(name = "description")
    public String description;

    @Column(name = "created_date",columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",nullable = true,updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    @CreatedDate
    public Timestamp createdDate;

    @Column(name = "created_by")
    @CreatedBy
    public String createdBy;

    @Column(name = "last_updated",columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    @LastModifiedDate
    public Timestamp lastUpdated;

    @Column(name = "last_updated_by")
    @LastModifiedBy
    public String lastUpdatedBy;

    @Column(name = "deleted",columnDefinition = "boolean default false")
    public Boolean deleted = false;

    @Column(name = "status",columnDefinition = "boolean default true")
    public Boolean status = false;
}
