# Spring Boot / 게시글 관련 기능 구현

* 게시글 생성
    * 게시글 작성 페이지 이동 - get /writing
    * 게시글 작성 - post /articles
* 게시글 목록 조회 - get /
* 게시글 조회 - get /articles/{articleId}
* 게시글 수정
    * 게시글 수정 페이지 이동 (article-edit.html) - GET /articles/{articleId}/edit
    * 게시글 수정 - PUT /articles/{articleId}
    * 게시글 페이지 이동 (article.html)
* 게시글 삭제 - DELETE /articles/{articleId} 
    * 삭제 후 게시글 조회 페이지로 이동
    
# Spring Boot / 

* 기능 구현 목록

    1. 회원 등록
        1. 유효성 검사
            1. 이름, 이메일, 비밀번호
                1. 프론트엔드
                2. 백엔드
    2. 회원 목록 조회 (GET /users)
    3. 회원 로그인, 로그아웃
    4. 회원 정보 수정 
        1. 이름만 수정 가능
    5. 회원 탈퇴
    
1. 유의 사항
    1. h2.db -> Mysql로 이전은 아직 미완성
    2. sample data로 ('sample@mail.com', 123qweasd), ('sample2@mail.com'. '123qweasd') 회원 정보 추가
    3. 유저 목록 조회는 로그아웃 상태에서도 볼 수 있음 (GET /users)        
      