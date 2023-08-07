package com.example.project_2_baejewoo.repository;

import com.example.project_2_baejewoo.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findByArticleId(@Param("articleId")Long articleId);
}
