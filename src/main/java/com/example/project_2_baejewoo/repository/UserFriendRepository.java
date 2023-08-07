package com.example.project_2_baejewoo.repository;

import com.example.project_2_baejewoo.entity.UserFollowsEntity;
import com.example.project_2_baejewoo.entity.UserFriendsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFriendRepository extends JpaRepository<UserFriendsEntity, Long> {
    Optional<UserFriendsEntity> findByFromUserIdAndToUserId(Long fromId, Long toId);
    List<UserFriendsEntity> findByToUserId(Long toId);
}
