package com.example.project_2_baejewoo.controller;

import com.example.project_2_baejewoo.dto.ResponseDto;
import com.example.project_2_baejewoo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    //  1일차 요구사항: 로그인 한 상태에서, 자신을 대표하는 사진 프로필 사진을 업로드 할 수 있다.

    @PutMapping(value = "/profile",
                consumes = MediaType.MULTIPART_FORM_DATA_VALUE
                                  )
    public ResponseDto userProfile( Authentication authentication,
                                    @RequestParam("image") MultipartFile Image
    ){
        service.updateProfile(Image, authentication);

        ResponseDto response  = new ResponseDto();
        String username = authentication.getName();

        response.setMessage(username + "의 프로필이 업데이트 되었습니다.");

        return response;

    }
}
