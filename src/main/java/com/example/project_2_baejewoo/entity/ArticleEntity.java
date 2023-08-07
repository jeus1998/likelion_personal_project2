package com.example.project_2_baejewoo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Article")
@Data
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    private String title;

    private String content;

    private String deleteAt; // delete_at 이 null 이 아니면 피드를 못 보게한다 -> softdelete?

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // 외래키 맵핑
    UserEntity user;

    // 피드 삭제 -> 피드에 연관된 이미지, 댓글, 좋아요 db 다 삭제

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    List<LikeArticleEntity> articleLikes = new ArrayList<>(); // 게시글 좋아요

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    List<ArticleImagesEntity> articlesImages = new ArrayList<>(); // 게시글 이미지

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    List<CommentEntity> comments = new ArrayList<>(); // 게시글 댓글


}
