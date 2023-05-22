package com.example.news.repository;

import com.example.news.model.LmsNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LmsNewsRepository extends JpaRepository<LmsNews,Long> {

}
