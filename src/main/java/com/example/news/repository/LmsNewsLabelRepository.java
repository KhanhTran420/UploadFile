package com.example.news.repository;

import com.example.news.model.LmsNewsLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LmsNewsLabelRepository extends JpaRepository<LmsNewsLabel,Long> {
    Boolean existsByLabel(String label);

    LmsNewsLabel findByLabel(String label);
}
