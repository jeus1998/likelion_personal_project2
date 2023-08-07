package com.example.project_2_baejewoo.repository;

import com.example.project_2_baejewoo.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
    List<ArticleEntity> findByUserIdAndDeleteAtIsNull(Long userId);
}
