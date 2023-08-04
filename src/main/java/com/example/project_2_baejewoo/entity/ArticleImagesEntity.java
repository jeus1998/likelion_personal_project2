package com.example.project_2_baejewoo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "articleImage")
public class ArticleImagesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_image_id")
    private Long id;

    private String article_image_url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    ArticleEntity article;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    UserEntity user;




}
