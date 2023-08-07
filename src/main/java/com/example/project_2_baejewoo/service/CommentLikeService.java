package com.example.project_2_baejewoo.service;

import com.example.project_2_baejewoo.dto.CommentDto;
import com.example.project_2_baejewoo.entity.ArticleEntity;
import com.example.project_2_baejewoo.entity.CommentEntity;
import com.example.project_2_baejewoo.entity.LikeArticleEntity;
import com.example.project_2_baejewoo.entity.UserEntity;
import com.example.project_2_baejewoo.repository.ArticleRepository;
import com.example.project_2_baejewoo.repository.CommentRepository;
import com.example.project_2_baejewoo.repository.LikeArticleRepository;
import com.example.project_2_baejewoo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final ArticleRepository articleRepository;
    private final LikeArticleRepository likeArticleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    // 3-1 댓글 구현하기
    public void comment(Long articleId, Authentication authentication, CommentDto dto){

        String username = authentication.getName();
        // 댓글 달려고 하는 피드가 있는지 확인
        Optional<ArticleEntity> articleEntity = articleRepository.findById(articleId);

        if (!articleEntity.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        ArticleEntity article = articleEntity.get();

        // 댓글 작성은 로그인 한 사람만 쓸 수 있다.

        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        UserEntity user = userEntity.get();
        if (!user.getUsername().equals(username)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setArticle(article);
        commentEntity.setUser(user);
        commentEntity.setContent(dto.getContent());
        commentRepository.save(commentEntity);

    }
    // 3-1-2 댓글 수정하기
    public void updateComment(Long articleId, Long commentId, Authentication authentication, CommentDto dto){

        String username = authentication.getName();

        // 수정하려고 하는 댓글이 있는지 확인
        Optional<CommentEntity> commentEntity = commentRepository.findById(commentId);
        if (commentEntity.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        CommentEntity comment = commentEntity.get();

        // 수정하려고 하는 댓글이 해당 피드에 달린 댓글인지 확인
        if (comment.getArticle().getId() != articleId){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 수정하려고 하는 댓글이 현재 사용자가 등록한 댓글인지 확인
        if (!comment.getUser().getUsername().equals(username)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        // 댓글 수정
        comment.setContent(dto.getContent());
        commentRepository.save(comment);
    }
    // 3-1-3 댓글 삭제하기 soft-delete
    public void deleteComment(Long articleId, Long commentId, Authentication authentication, LocalDateTime time){

        String username = authentication.getName();

        // 삭제하려고 하는 댓글이 있는지 확인
        Optional<CommentEntity> commentEntity = commentRepository.findById(commentId);
        if (commentEntity.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        CommentEntity comment = commentEntity.get();

        // 삭제하려고 하는 댓글이 해당 피드에 달린 댓글인지 확인
        if (comment.getArticle().getId() != articleId){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 삭제하려고 하는 댓글이 현재 사용자가 등록한 댓글인지 확인
        if (!comment.getUser().getUsername().equals(username)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // 댓글 삭제 soft_delete?
        comment.setDelete_at(time.toString());
        commentRepository.save(comment);
    }

    // 3-2 좋아요 구현하기
    public boolean like(Long articleId, Authentication authentication){

        String username = authentication.getName();
        // 좋아요 하려고 하는 피드가 있는지 확인
        Optional<ArticleEntity> articleEntity = articleRepository.findById(articleId);

        if (!articleEntity.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        ArticleEntity article = articleEntity.get();

        // 좋아요 하려고 하는 피드가 자신이 작성한 피드인지 확인
        if (article.getUser().getUsername().equals(username)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            // 자신이 작성한 피드에는 좋아요를 못 누른다.
        }

        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        UserEntity user = userEntity.get();
        Long userId = user.getId();

        Optional<LikeArticleEntity> likeArticleEntity = likeArticleRepository.findByArticleIdAndUser_Id(articleId, userId);
        // 해당 피드에 현재 사용자가 좋아요를 누른적이 없다면 좋아요 , true return
        if(likeArticleEntity.isEmpty()) {
            LikeArticleEntity likeArticle = new LikeArticleEntity();
            likeArticle.setArticle(article);
            likeArticle.setUser(user);
            likeArticleRepository.save(likeArticle);
            return true;
        }
        // 이미 눌렀으면 삭제후 false return
        else {
            likeArticleRepository.deleteByArticleIdAndUser_Id(articleId, userId);
            return false;
        }

    }
}
