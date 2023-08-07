package com.example.project_2_baejewoo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ArticleSingleDto {

    private Long articleId; // 피드 번호

    private String title; // 제목

    private String content; // 내용

    private String username; // 피드 작성자

    private List<Map<String, String>> comments = new ArrayList<>(); // 댓글 목록

    private Long numLikes; // 좋아요 숫자

    private List<String> images_url = new ArrayList<>(); // 피드 이미지 url 목록


}
