package com.example.project_2_baejewoo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "LikeArticle")
public class LikeArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_article_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private ArticleEntity article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private UserEntity user;

}
