package com.example.project_2_baejewoo.service;

import com.example.project_2_baejewoo.dto.UserInformationDto;
import com.example.project_2_baejewoo.entity.UserEntity;
import com.example.project_2_baejewoo.entity.UserFollowsEntity;
import com.example.project_2_baejewoo.repository.UserFollowRepository;
import com.example.project_2_baejewoo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class Day4Service {

    private final UserRepository userRepository;
    private final UserFollowRepository userFollowRepository;
    public UserInformationDto searchUser(Long userId){
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        UserEntity user = userEntity.get();
        UserInformationDto dto = new UserInformationDto();
        dto.setUsername(user.getUsername());
        dto.setProfile_url(user.getProfile_image());
        return dto;
    }

    public String followUser(Long userId, Authentication authentication){

        String username = authentication.getName();

        // 팔로우 하고 싶은 user가 존재 하는지 확인
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        UserEntity to_user = userEntity.get(); // 팔로우 받는 사람

        // 현재 사용자
        Optional<UserEntity> userEntity2 = userRepository.findByUsername(username);
        if (userEntity2.isEmpty()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        UserEntity from_user = userEntity2.get(); // 팔로우 하는 사람

        // 현재 사용자가 현재 사용자를 팔로우 즉 자기 스스로 팔로우를 하지 못하게 해야 한다.
        if (to_user.getUsername().equals(from_user.getUsername())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        UserFollowsEntity userFollowsEntity = new UserFollowsEntity();
        userFollowsEntity.setUser_following(from_user);
        userFollowsEntity.setUser_follower(to_user);
        userFollowRepository.save(userFollowsEntity);

        return to_user.getUsername();
    }
}
