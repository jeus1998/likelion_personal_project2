package com.example.project_2_baejewoo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "comment")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_comment_id")
    private Long id;

    private String content;

    private String delete_at; // 댓글이 삭제될때는 실제로 데이터베이스에서 삭제하는 것이 아닌, 삭제 되었다는 표시를 남기도록 한다.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    ArticleEntity article;
}
