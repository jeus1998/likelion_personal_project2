#  멋쟁이사자 백엔드 스쿨 5기 📮멋사SNS📮 미션형[2] 프로젝트

```
요구사항은 전부 POSTMAN으로 ProjectTest가 가능합니다.
PostMan 위치 : src/main/resources
Project Test2.postman_collection.json
```
###  요구사항!

#### DAY 1 / 사용자 인증하기

```
1. 사용자 회원가입이 가능하다.
    - 회원가입에 필수로 필요한 정보는 아이디와 비밀번호 이다.
    - 부수적으로 이메일, 전화번호를 기입할 수 있다.

2. 아이디와 비밀번호를 통해 로그인이 가능하다.
    - 인증 방식은 JWT를 이용한 토큰 인증 방식을 택한다.

3. 로그인 한 상태에서, 자신을 대표하는 사진, 프로필 사진을 업로드 할 수 있다.
```

#### DAY 2 / 피드 구현하기

```
💡 피드란?
사용자가 나누고 싶은 이야기를 작성하는, 사용자 개인의 공간을 의미합니다.

1. 피드는 제목과 내용을 붙일 수 있다.
    - 피드에는 복수의 이미지를 넣을 수 있다.

2. 피드를 작성하고자 한다면 로그인 된 상태여야 한다.
    - 사용자가 피드를 작성하면, 특별한 설정 없이 자신이 작성한 피드로 등록된다.

3. 피드는 작성한 사용자 기준으로, 목록 형태의 조회가 가능하다.
    - 조회를 위해 대상 사용자의 정보가 제공되어야 한다.
    - 피드 목록 조회시, 작성자 아이디, 제목과 대표 이미지에 관한 정보가 포함되어야 한다.
    - 이때 대표 이미지란 피드에 등록된 첫번째 이미지를 의미한다.
    - 만약 피드에 등록된 이미지가 없다면, 지정된 기본 이미지를 보여준다.

4. 피드는 단독 조회가 가능하다.
    - 피드 단독 조회시, 피드에 연관된 모든 정보가 포함되어야 한다. 이는 등록된 모든 이미지를 확인할 수 있는 각각의 URL과, 댓글 목록, 좋아요의 숫자를 포함한다.
    - 피드를 단독 조회할 시, 로그인이 된 상태여야 한다.

5. 피드는 수정이 가능하다.
    - 피드에 등록된 이미지의 경우, 삭제 및 추가만 가능하다.
    - 피드의 이미지가 삭제될 경우 서버에서도 해당 이미지를 삭제하도록 한다

6. 피드는 삭제가 가능하다.
    - 피드가 삭제될때는 실제로 데이터베이스에서 삭제하는 것이 아닌, 삭제 되었다는 표시를 남기도록 한다.

```

#### DAY 3 / 댓글, 좋아요 구현하기

```
1)댓글 구현하기

💡 댓글이란?
댓글이란 피드에 대하여 의견을 제시하는 비교적 적은 문구를 의미한다.

1. 댓글 작성은 로그인 한 사람만 쓸 수 있다.
    - 댓글에는 작성자 아이디, 댓글 내용이 포함된다.

2. 자신이 작성한 댓글은 수정 및 삭제가 가능하다.
    - 댓글이 삭제될때는 실제로 데이터베이스에서 삭제하는 것이 아닌, 삭제 되었다는 표시를 남기도록 한다.

3. 댓글의 조회는 피드의 단독 조회와 함께 이뤄진다.

2) 좋아요 구현하기

1. 다른 사용자의 피드는 좋아요를 할 수 있다.
    - 자신의 피드의 좋아요는 할 수 없다(권한 없음).
    - 좋아요 요청을 보낼 때 이미 좋아요 한 상태라면, 좋아요는 취소된다.

```

#### DAY 4 / 사용자 정보 구현하기

```
1. 사용자의 정보는 조회가 가능하다.
    - 이때 조회되는 정보는 아이디와 프로필 사진이다.

2. 로그인 한 사용자는 다른 사용자를 팔로우 할 수 있다.
    - 팔로우는 일방적 관계이다. A 사용자가 B를 팔로우 하는 것이 B 사용자가 A를 팔로우 하는것을 의미하지 않는다.

3. 로그인 한 사용자는 팔로우 한 사용자의 팔로우를 해제할 수 있다.

4. 로그인 한 사용자는 다른 사용자와 친구 관계를 맺을 수 있다.
    - 친구 관계는 양방적 관계이다. A 사용자가 B와 친구라면, B 사용자와 A 도 친구이다.
    - A 사용자는 B 사용자에게 친구 요청을 보낸다.
    - B 사용자는 자신의 친구 요청 목록을 확인할 수 있다.
    - B 사용자는 친구 요청을 수락 혹은 거절할 수 있다.

5. 사용자의 팔로우한 모든 사용자의 피드 목록을 조회할 수 있다.
    - 이때 작성한 사용자와 무관하게 작성된 순서의 역순으로 조회한다.
    - 그 외 조회되는 데이터는 피드 목록 조회와 동일하다.

6. 사용자와 친구관계의 모든 사용자의 피드 목록을 조회할 수 있다.
    - 이때 작성한 사용자와 무관하게 작성된 순서의 역순으로 조회한다.
    - 그 외 조회되는 데이터는 피드 목록 조회와 동일하다.

```
#### SecurityConfig를 보고 요청을 보낼 때 Authorization Type -> Bearer Token에 로그인을 해서 받은 token을 넣어주세요 
```java
 .authorizeHttpRequests(authHttp -> authHttp
                        .requestMatchers("/article/{username}", "/static/**", "/{userId}")
                        .permitAll()
                        .requestMatchers(
                                "/user/register", "/user/login"
                                   // 회원가입         // 로그인
                        ).anonymous()
                        .anyRequest()
                        .authenticated()
                )
```
#### 프로젝트 주석을 보고 하시면 더 편합니다.

#### POSTMAN 사용법 
```
DB Table를 확인하면서 테스트 해주세요!

1. 회원가입  POST : http://localhost:1212/user/register

요청: Body/raw/JSON
{
    "username": "baejeu121212",
    "password": "jeu1212",
    "passwordCheck": "jeu1212",
    
    "address": "광주",
    "email": "baejeu@naver.com",
    "phone": "010-9289-1234"
}
응답:
{
    "message": "회원가입 성공"
}

2. 로그인 POST : http://localhost:1212/user/login

요청: Body/raw/JSON
{
    "username": "admin",
    "password": "asdf"
}

응답:
{
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY5MTUwNDQ0NSwiZXhwIjoxNjkxNTA4MDQ1fQ.k8m6kGvAJaKOG4X9Wekgwte8TbmVFVS-r5vX6d1eoT5xXEX_Sp5eEk_2VfJD0hhASSvIAyzWRgsWqjxfno9X-g"
}

3. 추가 기능 현재 사용자 이름 확인 GET : http://localhost:1212/user/check

요청: Body X

응답 : 현재사용자 이름 ex) admin

4. 현재 사용자 프로필 업데이트 PUT : http://localhost:1212/user/profile

요청: Body/from-data/ 
key : image value : 이미지파일 선택 

응답:
{
    "message": "admin의 프로필이 업데이트 되었습니다."
}

5. 피드 등록 POST : http://localhost:1212/article

요청: Body/raw/JSON
{
    "title": "첫번째 등록",
    "content": "안녕하세요!"
}

응답: 
{
    "message": "피드 등록이 완료 되었습니다."
}

6. 피드 삭제 DELETE : http://localhost:1212/article/{articleId} DB 상에서 존재하는 articleId로 요청해야 한다. 피드를 작성한 사용자만 가능

요청: Body X

응답:
{
    "message": "피드가 삭제 되었습니다."
}

7. 피드에 이미지 넣기 POST : http://localhost:1212/article/{articleId}  DB 상에서 존재하는 articleId로 요청해야 한다. 피드를 작성한 사용자만 가능

요청: Body/form-data
Key: image Value: 이미지파일 선택 

응답:
{
    "message": "피드에 이미지가 등록되었습니다."
}

8. 2-3 목록형태 조회 GET : http://localhost:1212/article/{username} 인증 필요 X 

요청: Body X

응답:
{
    "content": [
        {
            "id": 2,
            "username": "admin",
            "title": "첫번째 등록",
            "content": "안녕하세요!",
            "representImageUrl": "/static/article/2/노션용PPT.JPG"
        },
        {
            "id": 3,
            "username": "admin",
            "title": "첫번째 등록",
            "content": "안녕하세요!",
            "representImageUrl": "/static/profile/admin/admin.JPG"
        },
        {
            "id": 4,
            "username": "admin",
            "title": "첫번째 등록",
            "content": "안녕하세요!",
            "representImageUrl": "/static/profile/admin/admin.JPG"
        }
    ],
    "pageable": "INSTANCE",
    "last": false,
    "totalPages": 2,
    "totalElements": 4,
    "size": 3,
    "number": 0,
    "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
    },
    "first": true,
    "numberOfElements": 3,
    "empty": false
}

9. 2-4 피드 단독 조회 GET : http://localhost:1212/article/single/{articleId}

요청: Body X

응답: 
{
    "articleId": 2,
    "title": "첫번째 등록",
    "content": "안녕하세요!",
    "username": "admin",
    "comments": [],
    "numLikes": 0,
    "images_url": [
        "/static/article/2/노션용PPT.JPG"
    ]
}

10. 피드의 title, content 수정 PUT: http://localhost:1212/article/{articleId} 

요청: Body/raw/JSON
{
    "title": "수정된제목",
    "content": "수정된내용"
}

응답: 
{
    "message": "피드가 수정 되었습니다."
}

11. 피드의 이미지 삭제 PUT : http://localhost:1212/article/{articleId}/images/{imageId} 

요청 : X

응답 : 
{
    "message": "피드에 이미지가 삭제 되었습니다."
}

12. 피드에 좋아요 누르기 POST : http://localhost:1212/article/{articleId}/like 본인이 작성한 피드에는 좋아요 못 누른다.

요청 : X

응답 : 
{
    "message": "좋아요를 눌렀습니다."
}

13. 피드에 댓글 달기 POST : http://localhost:1212/article/{articleId}/comments

요청: Body/raw/JSON
{
    "content": "댓글"
}

응답: 
{
    "message": "댓글을 달았습니다."
}

14. 피드 댓글 수정하기 PUT : http://localhost:1212/article/{articleId}/comments/{commentsId}

요청: Body/raw/JSON
{
    "content": "댓글수정하기"
}

응답: 

{
    "message": "댓글을 수정 하였습니다."
}

15. 피드 댓글 삭제하기 DELETE : http://localhost:1212/article/{articleId}/comments/{commentsId

요청: X
응답:
{
    "message": "댓글을 삭제 하였습니다."
}

16. 4-1 사용자 정보 조회 GET: http://localhost:1212/{userId} 인증 필요 없다.

요청: X
응답:
{
    "username": "admin",
    "profile_url": "/static/profile/admin/admin.JPG"
}

17. 4-2 현재 사용자가 팔로우 POST: http://localhost:1212/follow/{userId} 팔로우는 자기 자신을 할 수 없다.
요청 : X
응답: 

{
    "message": "admin를 팔로우 하였습니다."
}

18. 4-3 현재 사용자를 팔로우 하는 사람 끊기 DELETE: http://localhost:1212/follow/{userId} 
요청 : X
응답:
{
    "message": "baejeu121212를 팔로우 해제 하였습니다."
}

19. 친구 요청하기 POST:  http://localhost:1212/friend/{userId} 

요청 : X
응답 :
{
    "message": "baejeu121212에게 친구 요청을 보냈습니다."
}

20. 현재 사용자에게 온 친구 요청 목록 확인 GET: http://localhost:1212/friend
요청 : X
응답 :

{
    "friendRequests": [
        {
            "id": 1,
            "username": "admin",
            "request": "친구요청"
        }
    ]
}

21. 친구 요청 수락 PUT: http://localhost:1212/friend/{relationId} relationId = ArticleFriendId
요청 : Body/raw/JSON
{
    "request": "수락"
}
응답 :
{
    "message": "친구요청을수락하였습니다."
}

22. 사용자의 모든 친구 피드 목록을 조회 할 수 있다.
요청 : 쿼리 Params로 읽고 싶은 페이지와 페이지 크기를 조정 가능하다. Key: page Value: 숫자 / Key: limit Value: 숫자
http://localhost:1212/friends/feeds
응답 : 
{
    "content": [
        {
            "id": 2,
            "username": "admin",
            "title": "수정된제목",
            "content": "수정된내용",
            "representImageUrl": "/static/profile/admin/admin.JPG"
        },
        {
            "id": 3,
            "username": "admin",
            "title": "첫번째 등록",
            "content": "안녕하세요!",
            "representImageUrl": "/static/profile/admin/admin.JPG"
        },
        {
            "id": 4,
            "username": "admin",
            "title": "첫번째 등록",
            "content": "안녕하세요!",
            "representImageUrl": "/static/profile/admin/admin.JPG"
        }
    ],
    "pageable": "INSTANCE",
    "last": false,
    "totalPages": 2,
    "totalElements": 4,
    "size": 3,
    "number": 0,
    "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
    },
    "first": true,
    "numberOfElements": 3,
    "empty": false
}

23. 사용자의 모든 팔로잉 피드 목록을 조회할 수 있다.
요청 : 쿼리 Params로 읽고 싶은 페이지와 페이지 크기를 조정 가능하다. Key: page Value: 숫자 / Key: limit Value: 숫자
http://localhost:1212/follower/feeds

```
