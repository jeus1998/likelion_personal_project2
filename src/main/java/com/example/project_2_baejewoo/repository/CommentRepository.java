package com.example.project_2_baejewoo.repository;

import com.example.project_2_baejewoo.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
