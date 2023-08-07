package com.example.project_2_baejewoo.controller;
import com.example.project_2_baejewoo.dto.FriendRelationDto;
import com.example.project_2_baejewoo.dto.FriendRequestListDto;
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

    // 4-4 로그인 한 사용자는 다른 사용자와 친구 관계를 맺을 수 있다.
    // 친구 관계는 양방적 관계이다. A 사용자가 B와 친구라면 B 사용자도 A도 친구이다.
    // A 사용자는 B 사용자에게 친구 요청을 보낸다.
    // B 사용자는 자신의 친구 요청 목록을 확인할 수 있다.
    // B 사용자는 친구 요청을 수락 혹은 거절할 수 있다.

    @PostMapping("/friend/{userId}") // 친구 요청 보내는 메서드
    public ResponseDto sendRequest(@PathVariable("userId") Long userId, Authentication authentication){
        String user = service.sendRequest(userId, authentication);
        ResponseDto response = new ResponseDto();
        response.setMessage( user + "에게 친구 요청을 보냈습니다.");
        return response;
    }
    // 현재 사용자에게 온 친구 요청 목록 확인 메서드
    @GetMapping("/friend")
    public FriendRequestListDto getRequest(Authentication authentication){

        return service.getRequest(authentication);
    }

    // 수락 or 거절 조회한 id 즉 요청 id를 통해서 요청을 수락or 거절 결정
    @PutMapping("/friend/{relationId}")
    public ResponseDto decideRequest(@PathVariable("relationId")Long relationId, Authentication authentication,
                                     @RequestBody FriendRelationDto dto){

        service.decideRequest(relationId, authentication, dto);
        ResponseDto response = new ResponseDto();
        response.setMessage("친구요청을" + dto.getRequest()+ "하였습니다.");
        return response;
    }

    // 4-5 사용자의 팔로우 한 모든 사용자의 피드 목록을 조회할 수 있다.
    // 이때 작성한 사용자와 무관하게 작성된 순서의 역순으로 조회한다.

}
