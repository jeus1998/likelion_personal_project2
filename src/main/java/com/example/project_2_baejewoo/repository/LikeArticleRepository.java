package com.example.project_2_baejewoo.repository;

import com.example.project_2_baejewoo.entity.LikeArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface LikeArticleRepository extends JpaRepository<LikeArticleEntity, Long> {

    Optional<LikeArticleEntity> findByArticleIdAndUser_Id(Long articleId, Long userId);

    void deleteByArticleIdAndUser_Id(Long articleId, Long userId);

}