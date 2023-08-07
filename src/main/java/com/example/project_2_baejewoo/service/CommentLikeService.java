package com.example.project_2_baejewoo.service;

import com.example.project_2_baejewoo.entity.ArticleEntity;
import com.example.project_2_baejewoo.entity.LikeArticleEntity;
import com.example.project_2_baejewoo.entity.UserEntity;
import com.example.project_2_baejewoo.repository.ArticleRepository;
import com.example.project_2_baejewoo.repository.LikeArticleRepository;
import com.example.project_2_baejewoo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final ArticleRepository articleRepository;
    private final LikeArticleRepository likeArticleRepository;
    private final UserRepository userRepository;

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
