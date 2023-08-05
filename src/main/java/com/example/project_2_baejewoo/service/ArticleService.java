package com.example.project_2_baejewoo.service;

import com.example.project_2_baejewoo.dto.ArticleDto;
import com.example.project_2_baejewoo.entity.ArticleEntity;
import com.example.project_2_baejewoo.entity.ArticleImagesEntity;
import com.example.project_2_baejewoo.entity.UserEntity;
import com.example.project_2_baejewoo.repository.ArticleImageRepository;
import com.example.project_2_baejewoo.repository.ArticleRepository;
import com.example.project_2_baejewoo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleImageRepository articleImageRepository;

    public void writeFeed(ArticleDto dto, Authentication authentication){

        String username = authentication.getName();
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);

        if (!userEntity.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        UserEntity user = userEntity.get();
        ArticleEntity newFeed = new ArticleEntity();
        newFeed.setTitle(dto.getTitle());
        newFeed.setContent(dto.getContent());
        newFeed.setUser(user);
        articleRepository.save(newFeed);
    }
    // 피드에 이미지 넣기
    public void putImage(Long id, MultipartFile Image, Authentication authentication){

        // 이미지 넣을려고 하는 피드가 있는지 확인
        Optional<ArticleEntity> articleEntity = articleRepository.findById(id);

        if (!articleEntity.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        ArticleEntity article = articleEntity.get();

        // 현재 사용자가 작성한 피드인지 확인
        if (!article.getUser().getUsername().equals(authentication.getName())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // 2-1. 폴더만 만드는 과정 -> article/1/
        String profileDir = String.format("article/%d/", id);

        try {
            Files.createDirectories(Path.of(profileDir));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 2-2. 확장자를 포함한 이미지 이름 만들기
        String originalFilename = Image.getOriginalFilename();
       /* log.info(originalFilename);
        String[] fileNameSplit = originalFilename.split("\\.");
        String extension = fileNameSplit[fileNameSplit.length - 1];
        String original  = fileNameSplit[0];
        String profileFilename = original + "." + extension;
        log.info(profileFilename);*/

        // 2-3. 폴더와 파일 경로를 포함한 이름 만들기
        String profilePath = profileDir + originalFilename;

        // 3. MultipartFile 을 저장하기
        try {
            Image.transferTo(Path.of(profilePath));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 4. (정적 프로필 이미지를 회수할 수 있는 URL)
        ArticleImagesEntity ImagesEntity = new ArticleImagesEntity();
        ImagesEntity.setArticle(article);
        ImagesEntity.setArticle_image_url(String.format("/static/%s", originalFilename));
        articleImageRepository.save(ImagesEntity);
    }

    // delete
    public void softDeleteFeed(Long id, Authentication authentication, LocalDateTime time){
        String username = authentication.getName();

        Optional<ArticleEntity> articleEntity = articleRepository.findById(id);

        if (!articleEntity.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        ArticleEntity article = articleEntity.get();

        if (!article.getUser().getUsername().equals(username)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        article.setDelete_at(time.toString()); // softDelete : DB에서는 삭제 X

        articleRepository.save(article);

    }



}
