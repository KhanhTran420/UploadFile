package com.example.news.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class LmsNewsLabel extends BaseEntity {

    @Column(name = "label")
    private String label;

    public LmsNewsLabel(){}

    public LmsNewsLabel(String label){
        this.label=label;
    }
}
