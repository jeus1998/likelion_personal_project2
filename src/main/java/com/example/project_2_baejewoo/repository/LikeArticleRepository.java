package com.example.project_2_baejewoo.repository;

import com.example.project_2_baejewoo.entity.LikeArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface LikeArticleRepository extends JpaRepository<LikeArticleEntity, Long> {

    Optional<LikeArticleEntity> findByArticleIdAndUser_Id(Long articleId, Long userId);

    List<LikeArticleEntity> findByArticleId(@Param("articleId")Long articleId);

    void deleteByArticleIdAndUser_Id(Long articleId, Long userId);

}