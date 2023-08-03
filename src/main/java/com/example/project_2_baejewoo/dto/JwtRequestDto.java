package com.example.project_2_baejewoo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JwtRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
