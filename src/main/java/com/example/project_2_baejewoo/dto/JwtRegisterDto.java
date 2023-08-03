package com.example.project_2_baejewoo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class JwtRegisterDto {
    // 필수 정보
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String passwordCheck;

    // 필수 x
    private String address; // 주소
    private String email; // 이메일
    private String phone; // 전화번호
}
