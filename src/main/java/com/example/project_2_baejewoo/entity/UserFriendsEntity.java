package com.example.project_2_baejewoo.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user_friends")
public class UserFriendsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String request; // "요청" / "수락" / "거절"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_id")
    private UserEntity fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_id")
    private UserEntity toUser;
}
