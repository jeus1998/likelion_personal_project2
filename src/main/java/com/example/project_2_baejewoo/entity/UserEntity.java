package com.example.project_2_baejewoo.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// Spring에 저장하고 싶은 사용자 정보

@Entity
@Table(name = "users")
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 필수항목
    @Column(nullable = false, unique = true)
    private String username; // 아이디
    @Column(nullable = false)
    private String password; // 비밀번호

    // 부수적으로 전화번호, 이메일, 주소 정보를 기입할 수 있다.
    private String address; // 주소
    private String email; // 이메일
    private String phone; // 전화번호

    // 로그인 한 상태에서, 자신을 대표하는 사진, 프로필 사진을 업로드 할 수 있다.
    private String profile_image;

    // 어떤 필드가 반대쪽 필드를 매핑 하는지를 설정
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)  // 유저 삭제시 전부 삭제
    List<ArticleEntity> articles = new ArrayList<>(); // 게시글

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<LikeArticleEntity> likesArticles = new ArrayList<>(); // 게시글 좋아요

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<CommentEntity> comments = new ArrayList<>(); // 게시글 댓글

    @OneToMany(mappedBy = "userFollower", cascade = CascadeType.ALL) // 해당 유저를 누가 팔로우 하는지
    List<UserFollowsEntity> followers = new ArrayList<>();

    @OneToMany(mappedBy = "userFollowing", cascade = CascadeType.ALL) //  해당 유저가 누구를 팔로우 하는지
    List<UserFollowsEntity> followings = new ArrayList<>();

    @OneToMany(mappedBy = "fromUser", cascade = CascadeType.ALL) // 한 유저가 여러명한테 친구 요청을 보내는게 가능하다.
    List<UserFriendsEntity> sendFriendRequests = new ArrayList<>();

    @OneToMany(mappedBy = "toUser", cascade = CascadeType.ALL)
    List<UserFriendsEntity> receivedFriendRequests = new ArrayList<>(); // 한 유저는 여러명한테 친구 요청을 받는게 가능하다.
}