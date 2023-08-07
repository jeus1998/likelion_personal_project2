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
        Long followerId = to_user.getId();

        // 현재 사용자
        Optional<UserEntity> userEntity2 = userRepository.findByUsername(username);
        if (userEntity2.isEmpty()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        UserEntity from_user = userEntity2.get(); // 팔로우 하는 사람
        Long followingId = from_user.getId();

        // 현재 사용자가 현재 사용자를 팔로우 즉 자기 스스로 팔로우를 하지 못하게 해야 한다.
        if (to_user.getUsername().equals(from_user.getUsername())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // 팔로우 정보가 DB에 중복해서 들어가서는 안된다. 이미 있는지 체크
        Optional<UserFollowsEntity> userFollowsCheck = userFollowRepository.findByUserFollowerIdAndUserFollowingId(followerId, followingId );
        if(userFollowsCheck.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        }

        UserFollowsEntity userFollowsEntity = new UserFollowsEntity();
        userFollowsEntity.setUserFollowing(from_user);
        userFollowsEntity.setUserFollower(to_user);
        userFollowRepository.save(userFollowsEntity);

        return to_user.getUsername();
    }

    public String unLockUser(Long userId, Authentication authentication){
        String username = authentication.getName();
        // 현재 사용자의 user_id 찾기 -> follower_id 찾기 위해서

        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        if (userEntity.isEmpty()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        UserEntity currentUser =userEntity.get();
        Long followerId = currentUser.getId();

        Optional<UserFollowsEntity> userFollowsEntity = userFollowRepository.findByUserFollowerIdAndUserFollowingId(followerId, userId);
        if(userFollowsEntity.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        userFollowRepository.deleteById(userFollowsEntity.get().getId());

        // 누구를 팔로우 끊었는지 보여주기 위해
        Optional<UserEntity> user = userRepository.findById(userId);
        String delete = user.get().getUsername();

        return delete;
    }
}
