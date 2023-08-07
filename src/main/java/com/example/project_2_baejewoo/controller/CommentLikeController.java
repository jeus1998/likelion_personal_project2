package com.example.project_2_baejewoo.controller;

import com.example.project_2_baejewoo.dto.CommentDto;
import com.example.project_2_baejewoo.dto.ResponseDto;
import com.example.project_2_baejewoo.service.CommentLikeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/article/{articleId}")
public class CommentLikeController {

    private final CommentLikeService service;

    // 3-1 댓글 구현하기 댓글이란? 피드에 대하여 의견을 제시하는 비교적 적은 문구를 의미한다.
    // 댓글 작성은 로그인 한 사람만 쓸 수 있다.
    // 댓글에는 작성자 아이디, 댓글 내용이 포함된다.
    @PostMapping("/comments")
    public ResponseDto comment(@PathVariable("articleId")Long articleId, Authentication authentication, @RequestBody CommentDto dto){
        service.comment(articleId, authentication, dto);
        ResponseDto response = new ResponseDto();
        response.setMessage("댓글을 달았습니다.");
        return response;
    }
    // 3-1-2 댓글 수정하기
    @PutMapping("/comments/{commentId}")
    public ResponseDto updateComment(@PathVariable("articleId")Long articleId, @PathVariable("commentId")Long commentId, Authentication authentication, @RequestBody CommentDto dto){
        service.updateComment(articleId, commentId, authentication, dto);
        ResponseDto response = new ResponseDto();
        response.setMessage("댓글을 수정 하였습니다.");
        return response;
    }

    // 3-1-3 댓글 삭제하기
    @DeleteMapping("/comments/{commentId}")
    public ResponseDto deleteComment(@PathVariable("articleId")Long articleId,
                                     @PathVariable("commentId")Long commentId,
                                     Authentication authentication,
                                     LocalDateTime localDateTime)
    {
        LocalDateTime time  = localDateTime.now();
        service.deleteComment(articleId, commentId, authentication, time);
        ResponseDto response = new ResponseDto();
        response.setMessage("댓글을 삭제 하였습니다.");
        return response;
    }

   // 3-2 좋아요 구현하기 다른 사용자의 피드는 좋아요를 할 수 있다.
   // 자신의 피드의 좋아요는 할 수 없다.
   // 좋아요 요청을 보낼 때 이미 좋아요 한 상태라면 좋아요는 취소된다.
   @Transactional
   @PostMapping("/like")
    public ResponseDto like(@PathVariable("articleId")Long articleId, Authentication authentication){

        Boolean check = service.like(articleId, authentication);
        ResponseDto response = new ResponseDto();
        if(check){
            response.setMessage("좋아요를 눌렀습니다.");
        }
        else{
            response.setMessage("좋아요를 취소하였습니다.");
        }
        return response;
    }
}
