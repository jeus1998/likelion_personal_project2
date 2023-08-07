package com.example.project_2_baejewoo.controller;

import com.example.project_2_baejewoo.dto.ResponseDto;
import com.example.project_2_baejewoo.dto.UserInformationDto;
import com.example.project_2_baejewoo.service.Day4Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class Day4Controller {

    private final Day4Service service;

    // 4-1 사용자의 정보는 조회가 가능하다. 이때 조회되는 정보는 아이디와 프로필 사진이다.
    // -> 아무나 조회 가능하다,,?
    @GetMapping("/{userId}")
    public UserInformationDto searchUser(@PathVariable("userId") Long userId){
        return service.searchUser(userId);
    }

    // 4-2 로그인 한 사용자는 다른 사용자를 팔로우 할 수 있다.
    // 팔로우는 일방적 관계이다. A 사용자가 B를 팔로우 하는 것이 B 사용자가 A를 팔로우 하는것을 의미하지 않는다.

    @PostMapping("/follow/{userId}") // 현재 사용자가 팔로우 하고 싶은 userId
    public ResponseDto followUser(@PathVariable("userId") Long userId, Authentication authentication){
        String user = service.followUser(userId, authentication);
        ResponseDto response = new ResponseDto();
        response.setMessage( user + "를 팔로우 하였습니다.");
        return response;
    }

    // 4-3 로그인 한 사용자는 팔로우 한 사용자의 팔로우를 해제할 수 있다.
    @DeleteMapping("/follow/{userId}")
    public ResponseDto unLockUser(@PathVariable("userId") Long userId, Authentication authentication){
        String user = service.unLockUser(userId, authentication);
        ResponseDto response = new ResponseDto();
        response.setMessage( user + "를 팔로우 해제 하였습니다.");
        return response;
    }


}
