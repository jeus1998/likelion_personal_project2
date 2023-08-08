package com.example.project_2_baejewoo.repository;

import com.example.project_2_baejewoo.entity.UserFollowsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface UserFollowRepository extends JpaRepository<UserFollowsEntity, Long> {
    Optional<UserFollowsEntity> findByUserFollowerIdAndUserFollowingId(Long followerId, Long followingId);
}
