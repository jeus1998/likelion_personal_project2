package com.example.project_2_baejewoo.service;

import com.example.project_2_baejewoo.dto.ArticleFeedsDto;
import com.example.project_2_baejewoo.dto.FriendRelationDto;
import com.example.project_2_baejewoo.dto.FriendRequestListDto;
import com.example.project_2_baejewoo.dto.UserInformationDto;
import com.example.project_2_baejewoo.entity.*;
import com.example.project_2_baejewoo.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class Day4Service {

    private final UserRepository userRepository;
    private final UserFollowRepository userFollowRepository;
    private final UserFriendRepository userFriendRepository;
    private final ArticleRepository articleRepository;
    private final ArticleImageRepository articleImageRepository;
    public UserInformationDto searchUser(Long userId){
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        UserEntity user = userEntity.get();
        UserInformationDto dto = new UserInformationDto();
        dto.setUsername(user.getUsername());
        dto.setProfile_url(user.getProfile_image());
        return dto;
    }

    public String followUser(Long userId, Authentication authentication){

        String username = authentication.getName();

        // 팔로우 하고 싶은 user가 존재 하는지 확인
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        UserEntity to_user = userEntity.get(); // 팔로우 받는 사람
        Long followerId = to_user.getId();

        // 현재 사용자
        Optional<UserEntity> userEntity2 = userRepository.findByUsername(username);
        if (userEntity2.isEmpty()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        UserEntity from_user = userEntity2.get(); // 팔로우 하는 사람
        Long followingId = from_user.getId();

        // 현재 사용자가 현재 사용자를 팔로우 즉 자기 스스로 팔로우를 하지 못하게 해야 한다.
        if (to_user.getUsername().equals(from_user.getUsername())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // 팔로우 정보가 DB에 중복해서 들어가서는 안된다. 이미 있는지 체크
        Optional<UserFollowsEntity> userFollowsCheck = userFollowRepository.findByUserFollowerIdAndUserFollowingId(followerId, followingId );
        if(userFollowsCheck.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        }

        UserFollowsEntity userFollowsEntity = new UserFollowsEntity();
        userFollowsEntity.setUserFollowing(from_user);
        userFollowsEntity.setUserFollower(to_user);
        userFollowRepository.save(userFollowsEntity);

        return to_user.getUsername();
    }

    public String unLockUser(Long userId, Authentication authentication){
        String username = authentication.getName();
        // 현재 사용자의 user_id 찾기 -> follower_id 찾기 위해서

        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        if (userEntity.isEmpty()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        UserEntity currentUser =userEntity.get();
        Long followerId = currentUser.getId();

        Optional<UserFollowsEntity> userFollowsEntity = userFollowRepository.findByUserFollowerIdAndUserFollowingId(followerId, userId);
        if(userFollowsEntity.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        userFollowRepository.deleteById(userFollowsEntity.get().getId());

        // 누구를 팔로우 끊었는지 보여주기 위해
        Optional<UserEntity> user = userRepository.findById(userId);
        String delete = user.get().getUsername();

        return delete;
    }
    // 친구 요청 메서드
    public String sendRequest(Long userId, Authentication authentication){
        String username = authentication.getName();

        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        if(userEntity.isEmpty()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        UserEntity fromUser = userEntity.get(); // 현재 사용자가 요청
        Long fromUserId = fromUser.getId();

        Optional<UserEntity> userEntity2 = userRepository.findById(userId);
        if(userEntity2.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        UserEntity toUser = userEntity2.get(); // 친구 요청 받는쪽
        String toUserName = toUser.getUsername();

        // 친구요청은 자기 자신한테 보낼수 없다.
        if (username.equals(toUserName)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 이미 자기 자신이 한 요청이 존재하는지 체크하기 or 이미 친구 관계인지
        Optional<UserFriendsEntity> userFriendsEntity = userFriendRepository.findByFromUserIdAndToUserId(fromUserId, userId);
        if(userFriendsEntity.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 이미 상대방쪽에서 요청을 했으면 요청 불가
        Optional<UserFriendsEntity> userFriendsEntity2 = userFriendRepository.findByFromUserIdAndToUserId(userId, fromUserId);
        if(userFriendsEntity2.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        UserFriendsEntity userFriends = new UserFriendsEntity();
        userFriends.setFromUser(fromUser);
        userFriends.setToUser(toUser);
        userFriends.setRequest("친구요청");
        userFriendRepository.save(userFriends);

        return toUserName;
    }
    // 친구 요청 목록 확인 메서드
    public FriendRequestListDto getRequest(Authentication authentication){
        String username = authentication.getName(); // toUser
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        if (userEntity.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Long toUserId = userEntity.get().getId();

        List<UserFriendsEntity> userFriendsEntities = userFriendRepository.findByToUserId(toUserId);
        if (userFriendsEntities.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        FriendRequestListDto friendRequestListDto = new FriendRequestListDto();
        for (UserFriendsEntity target : userFriendsEntities ) {
            // 이미 친구면 요청 목록에는 없어야한다.
            if(!target.getRequest().equals("친구"))
            friendRequestListDto.addFriendRequest(target.getId(), target.getFromUser().getUsername(), target.getRequest());
        }

        return friendRequestListDto;
    }
    // 수락 or 거절 -> 관계 형성
    public void decideRequest(Long relationId, Authentication authentication, FriendRelationDto dto){

        String username = authentication.getName();

        Optional<UserFriendsEntity> userFriendsEntity = userFriendRepository.findById(relationId);
        if (userFriendsEntity.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        UserFriendsEntity userFriends = userFriendsEntity.get();

        // 현재 사용자가 친구 요청을 받은 사람이 아니라면
        if (!userFriends.getToUser().getUsername().equals(username)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Long fromUserId = userFriends.getFromUser().getId();
        Long toUserId = userFriends.getToUser().getId();


        // 관계 설정용
        Optional<UserEntity> fromUserEntity = userRepository.findById(fromUserId);
        UserEntity fromUser = fromUserEntity.get();
        Optional<UserEntity> toUserEntity = userRepository.findById(toUserId);
        UserEntity toUser = toUserEntity.get();

        if (dto.getRequest().equals("수락")){
            userFriends.setRequest("친구");
            userFriendRepository.save(userFriends);
            UserFriendsEntity friendsEntity = new UserFriendsEntity();
            friendsEntity.setRequest("친구");
            friendsEntity.setFromUser(toUser); // 반대로 설정
            friendsEntity.setToUser(fromUser);
            userFriendRepository.save(friendsEntity);
        }
        else if (dto.getRequest().equals("거절")){
            userFriendRepository.deleteById(relationId);
        }

    }
    // 4-5
    public Page<ArticleFeedsDto> followerFeeds(Long page, Long limit, Authentication authentication) {
        String username = authentication.getName();
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        UserEntity user = userEntity.get();
        Long currentId = user.getId();

        List<Long> followerIds = new ArrayList<>();
        List<UserFollowsEntity> userFollowsEntities = userFollowRepository.findAll();
        for (UserFollowsEntity target : userFollowsEntities) {
            if (target.getUserFollowing().getId() == currentId) {
                followerIds.add(target.getUserFollower().getId());
            }
        }
        if (followerIds.size() == 0) {
            log.info("팔로우 한 사람이 없습니다.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        // 피드에 넣은 이미지가 없으면 필요한 : 대표 이미지 url : 관리자의 프로필 이미지 url
        Optional<UserEntity> adminEntity = userRepository.findByUsername("admin");
        UserEntity admin = adminEntity.get();
        String url = admin.getProfile_image();

        List<ArticleFeedsDto> allFollowerFeeds = new ArrayList<>();

        for (Long followerId : followerIds) {
            List<ArticleEntity> followerFeeds = articleRepository.findByUserIdAndDeleteAtIsNull(followerId);

            for (ArticleEntity target : followerFeeds) {
                ArticleFeedsDto targetDto = new ArticleFeedsDto();
                targetDto.setId(target.getId());
                targetDto.setUsername(target.getUser().getUsername());
                targetDto.setTitle(target.getTitle());
                targetDto.setContent(target.getContent());

                Optional<ArticleImagesEntity> articleImages = articleImageRepository.findFirstByArticleIdOrderByIdAsc(target.getId());

                // 피드에 넣은 이미지가 없으면
                if (articleImages.isEmpty()) {
                    targetDto.setRepresentImageUrl(url);
                } else {
                    ArticleImagesEntity articleImagesEntity = articleImages.get();
                    targetDto.setRepresentImageUrl(articleImagesEntity.getArticle_image_url());
                }
                allFollowerFeeds.add(targetDto);
            }
        }

        int totalItems = allFollowerFeeds.size();
        int start = Math.toIntExact(page * limit);
        int end = Math.min((start + Math.toIntExact(limit)), totalItems);
        List<ArticleFeedsDto> pagedFeeds = allFollowerFeeds.subList(start, end);

        return new PageImpl<>(pagedFeeds, Pageable.unpaged(), totalItems);
    }


    // 4-6
    public Page<ArticleFeedsDto> friendsFeeds(Long page, Long limit, Authentication authentication){

        String username = authentication.getName();
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        UserEntity user = userEntity.get();
        Long currentId = user.getId();

        List<Long> FriendsIds = new ArrayList<>();

        List<UserFriendsEntity> userFriendsEntities = userFriendRepository.findAll();
        for ( UserFriendsEntity target : userFriendsEntities ) {
            if (target.getRequest().equals("친구") && target.getFromUser().getId() == currentId){
                FriendsIds.add(target.getToUser().getId());
            }
        }
        // 친구가 없다면?
        if (FriendsIds.size()==0){
            log.info("친구가 없어요ㅠㅠ");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        // 피드에 넣은 이미지가 없으면 필요한 : 대표 이미지 url : 관리자의 프로필 이미지 url
        Optional<UserEntity> adminEntity = userRepository.findByUsername("admin");
        UserEntity admin = adminEntity.get();
        String url = admin.getProfile_image();


        List<ArticleFeedsDto> allFriendFeeds = new ArrayList<>();

        for (Long friendId : FriendsIds) {
            List<ArticleEntity> friendFeeds = articleRepository.findByUserIdAndDeleteAtIsNull(friendId);

            for (ArticleEntity target : friendFeeds){
                ArticleFeedsDto tarketDto = new ArticleFeedsDto();
                tarketDto.setId(target.getId());
                tarketDto.setUsername(target.getUser().getUsername());
                tarketDto.setTitle(target.getTitle());
                tarketDto.setContent(target.getContent());

                Optional<ArticleImagesEntity> articleImages = articleImageRepository.findFirstByArticleIdOrderByIdAsc(target.getId());
                // 피드에 넣은 이미지가 없으면
                if (articleImages.isEmpty()) {
                    tarketDto.setRepresentImageUrl(url);
                } else {
                    ArticleImagesEntity articleImagesEntity = articleImages.get();
                    tarketDto.setRepresentImageUrl(articleImagesEntity.getArticle_image_url());
                }
                allFriendFeeds.add(tarketDto);
            }
        }

        int totalItems = allFriendFeeds.size();
        int start = Math.toIntExact(page * limit);
        int end = Math.min((start + Math.toIntExact(limit)), totalItems);
        List<ArticleFeedsDto> pagedFeeds = allFriendFeeds.subList(start, end);

        return new PageImpl<>(pagedFeeds, Pageable.unpaged(), totalItems);
    }
}
