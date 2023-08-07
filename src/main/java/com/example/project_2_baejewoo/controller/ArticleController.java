package com.example.project_2_baejewoo.controller;

import com.example.project_2_baejewoo.dto.ArticleDto;
import com.example.project_2_baejewoo.dto.ArticleFeedsDto;
import com.example.project_2_baejewoo.dto.ArticleSingleDto;
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
    // 2-4 피드는 단독 조회가 가능하다.
    // 피드에 연관된 모든 정보가 포함되어야 한다. 모든 이미지의 각각의 url과 댓글 목록, 좋아요 숫자
    // 피드를 단독 조회할 시, 로그인이 된 상태여야 한다. <- 작성자를 의미?  or 인스타그램 사용자?

    @GetMapping("/single/{articleId}")
    public ArticleSingleDto single(@PathVariable("articleId")Long articleId, Authentication authentication){
        return articleService.singleSearch(articleId, authentication);
    }

    // 2-5 피드는 수정이 가능하다.
    // 피드에 등록된 이미지의 경우, 삭제 및 추가만 가능하다.
    // 피드의 이미지가 삭제될 경우 서버에서도 해당 이미지를 삭제하도록 한다. <- 이거 어떻게 함?

    // 2-5-1 이미지 삭제 or title/content 수정 or // 이미지 추가는 postmapping 으로 이미 구현해둠
    @PutMapping("/{articleId}")
    public ResponseDto update(@PathVariable("articleId")Long articleId, Authentication authentication,
                              @RequestBody ArticleDto dto)

    {
        articleService.updateFeed(articleId ,authentication, dto);
        ResponseDto response = new ResponseDto();
        response.setMessage("피드가 수정 되었습니다.");
        return response;
    }

    @PutMapping("/{articleId}/images/{imageId}")
    public ResponseDto deleteImage(@PathVariable("articleId")Long articleId, Authentication authentication,
                                   @PathVariable("imageId") Long imageId)
    {
        articleService.deleteImage(articleId ,authentication, imageId);
        ResponseDto response = new ResponseDto();
        response.setMessage("피드에 이미지가 삭제 되었습니다.");
        return response;
    }

    @DeleteMapping("/{articleId}")
    public ResponseDto delete(@PathVariable("articleId")Long id, Authentication authentication, LocalDateTime localDateTime)
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