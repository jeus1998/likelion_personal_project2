package com.example.project_2_baejewoo.service;

import com.example.project_2_baejewoo.entity.UserEntity;
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
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public void updateProfile(MultipartFile Image, Authentication authentication){

        String username = authentication.getName();

        // 이미 로그인을 한 상태여서 레포지토리에 없을 수가 없다.
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        UserEntity user = optionalUser.get();

        // 현재 사용자와 업데이트 하고 싶은 사용자가 맞나 다시 체크
        if (!username.equals(user.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        else {
            Long userId = user.getId();
            // 2-1. 폴더만 만드는 과정 -> profile/1/
            String profileDir = String.format("profile/%d/", userId);
            try {
                Files.createDirectories(Path.of(profileDir));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            // 2-2. 확장자를 포함한 이미지 이름 만들기
            String originalFilename = Image.getOriginalFilename();
            String[] fileNameSplit = originalFilename.split("\\.");
            String extension = fileNameSplit[fileNameSplit.length - 1];
            String profileFilename = username + "." + extension;

            // baejeu.jpg
            // 2-3. 폴더와 파일 경로를 포함한 이름 만들기

            String profilePath = profileDir + profileFilename;
            // profile/1/baejeu.jpg

            // 3. MultipartFile 을 저장하기
            try {
                Image.transferTo(Path.of(profilePath));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            // 4. UserEntity 업데이트 (정적 프로필 이미지를 회수할 수 있는 URL)
            user.setProfile_image(String.format("/static/%s", profileFilename));
            userRepository.save(user);
        }
    }
}
