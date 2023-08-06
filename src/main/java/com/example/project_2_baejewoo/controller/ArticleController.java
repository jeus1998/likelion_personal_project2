package com.example.project_2_baejewoo.controller;

import com.example.project_2_baejewoo.dto.ArticleDto;
import com.example.project_2_baejewoo.dto.ArticleFeedsDto;
import com.example.project_2_baejewoo.dto.ResponseDto;
import com.example.project_2_baejewoo.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    public ResponseDto create(@RequestBody ArticleDto dto, Authentication authentication)
    {
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
    // 2-3 작성한 사용자 기준으로 목록 형태의 조회가 가능하다.
    // EX) username = user 에 대한 피드 목록 조회 article 에서 user가 작성한 피드 목록, 제목, 각각의 피드의 대표 이미지에 관한 정보
    @GetMapping("/{username}")
    public Page<ArticleFeedsDto> searchFeeds (@PathVariable("username")String username,
                                              @RequestParam(value = "page", defaultValue = "0") Long page,
                                              @RequestParam(value = "limit", defaultValue = "3") Long limit)
    {

        return articleService.searchFeeds(username, page, limit);

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

// 한 사용자는 여러개의 피드를 작성하고 각 피드에는 여러개의 이미지 삽입이 가능하다.
// 목록 형태의 조회란 대상 사용자의 정보를 제공하면 피드 목록이 조회가 된다 => 작성자 아이디, 제목, 대표 이미지
// 대표 이미지란 피드에 등록된 첫번째 이미지를 의미한다. created_at 오름차순? or pk 순서
// 각 피드에 등록된 이미지가 없다면 지정된 기본 이미지를 보여준다. 기본 이미지라..