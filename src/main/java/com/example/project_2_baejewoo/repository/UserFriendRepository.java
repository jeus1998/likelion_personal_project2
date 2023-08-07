package com.example.project_2_baejewoo.repository;

import com.example.project_2_baejewoo.entity.UserFriendsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFriendRepository extends JpaRepository<UserFriendsEntity, Long> {
}
