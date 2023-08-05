package com.example.project_2_baejewoo.controller;

import com.example.project_2_baejewoo.dto.ArticleDto;
import com.example.project_2_baejewoo.dto.ResponseDto;
import com.example.project_2_baejewoo.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;
    @PostMapping
    public ResponseDto create(@RequestBody ArticleDto dto, Authentication authentication){

        articleService.writeFeed(dto, authentication);

        ResponseDto response = new ResponseDto();
        response.setMessage("피드 등록이 완료 되었습니다.");
        return response;
    }

    // 피드에 이미지 넣기
    @PostMapping(value ="/{articleId}",
               consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto putImage(@PathVariable("articleId") Long id,
                                @RequestParam("image") MultipartFile Image,
                                Authentication authentication)
    {
        articleService.putImage(id, Image, authentication);
        ResponseDto response = new ResponseDto();
        response.setMessage("피드에 이미지가 등록되었습니다.");
        return response;
    }

    @DeleteMapping("/{articleId}")
    public ResponseDto create(@PathVariable("articleId")Long id, Authentication authentication, LocalDateTime localDateTime)
    {
        LocalDateTime time  = localDateTime.now();
        articleService.softDeleteFeed(id, authentication, time);
        ResponseDto response = new ResponseDto();
        response.setMessage("피드가 삭제 되었습니다.");
        return response;
    }
}
