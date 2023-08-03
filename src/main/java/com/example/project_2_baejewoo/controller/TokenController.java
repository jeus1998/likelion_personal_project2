package com.example.project_2_baejewoo.controller;


import com.example.project_2_baejewoo.dto.ResponseDto;
import com.example.project_2_baejewoo.security.CustomUserDetails;
import com.example.project_2_baejewoo.dto.JwtRequestDto;
import com.example.project_2_baejewoo.dto.JwtRegisterDto;
import com.example.project_2_baejewoo.dto.JwtTokenDto;
import com.example.project_2_baejewoo.security.JpaUserDetailsManager;
import com.example.project_2_baejewoo.security.JwtTokenUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("token")
public class TokenController {

    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    private final JpaUserDetailsManager userDetailsManager;

    public TokenController(
            JpaUserDetailsManager userDetailsManager,
            UserDetailsManager manager,
            PasswordEncoder passwordEncoder,
            JwtTokenUtils jwtTokenUtils
    )
    {
        this.userDetailsManager = userDetailsManager;
        this.manager = manager;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtils = jwtTokenUtils;
    }
    // 로그인 , 토큰 발행
    @PostMapping("/login")
    public JwtTokenDto issueJwt(@Valid @RequestBody JwtRequestDto dto) {
        UserDetails userDetails
                = manager.loadUserByUsername(dto.getUsername());

        if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        JwtTokenDto response = new JwtTokenDto();
        response.setToken(jwtTokenUtils.generateToken(userDetails));
        return response;
    }
    // 회원가입 저번 프로젝트와 다르게 회원가입 실패시 클라이언트에게 알려주는 코드
    // 필수 정보를 안 넣으면 403 에러를 발생한다. ???
    @PostMapping("/register")
    public ResponseDto registerUser(@Valid @RequestBody JwtRegisterDto dto){

        ResponseDto message = new ResponseDto();

        if (dto.getPassword().equals(dto.getPasswordCheck())) {
            log.info("password match!");

            if (!userDetailsManager.userExists(dto.getUsername())){
                manager.createUser(CustomUserDetails.builder()
                        .username(dto.getUsername())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .phone(dto.getPhone())
                        .address(dto.getAddress())
                        .email(dto.getEmail())
                        .build());
                message.setMessage("회원가입 성공");
            }
            else {
                message.setMessage("회원가입 실패");
            }
        }
        else{
            message.setMessage("회원가입 실패");
        }
        return message;
    }

    // 현재 로그인한 사용자 정보를 확인하기 위한 요청
    @GetMapping("/check")
    public String checkUser(Authentication authentication){
        String check = authentication.getName();
        return check;
    }
}
