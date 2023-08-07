package com.example.project_2_baejewoo.repository;

import com.example.project_2_baejewoo.entity.UserFollowsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFollowRepository extends JpaRepository<UserFollowsEntity, Long> {

}
