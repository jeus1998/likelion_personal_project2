package com.example.project_2_baejewoo.repository;

import com.example.project_2_baejewoo.entity.ArticleImagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleImageRepository extends JpaRepository<ArticleImagesEntity, Long> {

    // @Query("SELECT a FROM ArticleImagesEntity a WHERE a.article.id = :articleId ORDER BY a.id ASC")
    Optional<ArticleImagesEntity> findFirstByArticleIdOrderByIdAsc(@Param("articleId") Long articleId);
}
