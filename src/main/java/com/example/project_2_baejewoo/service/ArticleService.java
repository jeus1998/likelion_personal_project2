package com.example.project_2_baejewoo.service;

import com.example.project_2_baejewoo.dto.ArticleDto;
import com.example.project_2_baejewoo.dto.ArticleFeedsDto;
import com.example.project_2_baejewoo.entity.ArticleEntity;
import com.example.project_2_baejewoo.entity.ArticleImagesEntity;
import com.example.project_2_baejewoo.entity.UserEntity;
import com.example.project_2_baejewoo.repository.ArticleImageRepository;
import com.example.project_2_baejewoo.repository.ArticleRepository;
import com.example.project_2_baejewoo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
        String profileDir = String.format("media/article/%d/", id);

        try {
            Files.createDirectories(Path.of(profileDir));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 2-2. 확장자를 포함한 이미지 이름 만들기
        String originalFilename = Image.getOriginalFilename();

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
        ImagesEntity.setArticle_image_url(String.format("/static/article/%d/%s", id, originalFilename));
        articleImageRepository.save(ImagesEntity);
    }

    // 2-3 작성한 사용자 기준으로 목록 형태의 조회가 가능하다.
    public Page<ArticleFeedsDto> searchFeeds (String username, Long page, Long limit){

        List<ArticleEntity> feedAllLists = articleRepository.findAll();

        List<ArticleEntity> feedFilteredLists = new ArrayList<>();

        for (ArticleEntity target : feedAllLists){

            if (target.getUser().getUsername().equals(username) && target.getDelete_at() == null){
                feedFilteredLists.add(target);
            }

        }
        if(feedFilteredLists.isEmpty()){
            log.info(username +"의 피드가 없습니다.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        // 피드에 넣은 이미지가 없으면 필요한 : 대표 이미지 url : 관리자의 프로필 이미지 url
        Optional<UserEntity> adminEntity = userRepository.findByUsername("admin");
        UserEntity admin = adminEntity.get();
        String url = admin.getProfile_image();


        // 요구사항에 맞게 데이터 저장
        List<ArticleFeedsDto> articleFeedsDto = new ArrayList<>();
        for (ArticleEntity target: feedFilteredLists ) {

            ArticleFeedsDto tarketDto  = new ArticleFeedsDto();
            tarketDto.setId(target.getId());
            tarketDto.setUsername(target.getUser().getUsername());
            tarketDto.setTitle(target.getTitle());
            tarketDto.setContent(target.getContent());

            Optional<ArticleImagesEntity> articleImages = articleImageRepository.findFirstByArticleIdOrderByIdAsc(target.getId());
            // 피드에 넣은 이미지가 없으면
            if(articleImages.isEmpty()){
                tarketDto.setRepresentImageUrl(url);
            }
            // 피드에 넣은 이미지가 있으면
            else {
                ArticleImagesEntity articleImagesEntity = articleImages.get();
                tarketDto.setRepresentImageUrl(articleImagesEntity.getArticle_image_url());
            }
            articleFeedsDto.add(tarketDto);
        }

        int totalItems = articleFeedsDto.size();
        int start = Math.toIntExact(page * limit);
        int end = Math.min((start + Math.toIntExact(limit)), totalItems);
        List<ArticleFeedsDto> pagedFeeds = articleFeedsDto.subList(start, end);

        return new PageImpl<>(pagedFeeds, Pageable.unpaged(), totalItems);

    }
    // update content or title or 이미지 삭제
    public void updateFeed(Long articleId, Authentication authentication, ArticleDto dto){
        String username = authentication.getName();

        Optional<ArticleEntity> articleEntity = articleRepository.findById(articleId);

         if (!articleEntity.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
         }

        ArticleEntity article = articleEntity.get();
        if (!article.getUser().getUsername().equals(username)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        // NullPointerException 방지용 if 문들
        // title 수정

        log.info(dto.getContent());
        if(!dto.getTitle().isEmpty()){
            article.setTitle(dto.getTitle());
            log.info("title 수정");
        }

        // content 수정
        if(!dto.getContent().isEmpty()){
            article.setContent(dto.getContent());
            log.info("cotent 수정");
        }
        // 수정된 내용 저장
        articleRepository.save(article);


        }

    public void deleteImage(Long articleId, Authentication authentication, Long  imageId){
        String username = authentication.getName();

        Optional<ArticleEntity> articleEntity = articleRepository.findById(articleId);
        if (!articleEntity.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        ArticleEntity article = articleEntity.get();
        if (!article.getUser().getUsername().equals(username)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        // 이미지 삭제
        if(imageId!=null) {
            Optional<ArticleImagesEntity> articleImages = articleImageRepository.findById(imageId);
            if (articleImages.isEmpty()) {
                log.info("해당 이미지가 없습니다!");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            ArticleImagesEntity articleImage = articleImages.get();

            // 지울려고하는 imageId가 해당 피드가 아니라면 잘못된 요청
            if (articleId != articleImage.getArticle().getId()) {
                log.info("지울려고 하는 이미지가 해당 피드의 이미지가 아닙니다");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            String[] split = articleImage.getArticle_image_url().split("/");
            String name = split[split.length-1];
            String imagePath = "media/article/" + articleId + "/" + name;

            // 실제 서버에서 이미지 삭제
            try {
                Files.delete(Path.of(imagePath));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // DB에서 삭제
            articleImageRepository.deleteById(imageId);
        }
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
