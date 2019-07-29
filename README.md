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
    
# Spring Data JPA / 회원 관련 기능 구현

* 회원 가입 페이지 이동 - get /signup
* 회원 가입 규칙
    * 이메일 중복 확인
    * 이름 - 숫자, 특수문자 X
    * 비밀 번호 확인
* 회원 등록 - post /users
* 회원 조회 - get / users
 
* 로그인
    * 로그인 성공 - 메인화면 우측 상단 변경
    * 로그인 실패 - 실패 메시지
* 로그인 중이면 로그인, 회원 가입 화면 접근 -> 메인 화면
* 로그인 유저만 해당 유저 수정페이지 접근 가능  
* 로그 아웃 - 로그아웃 메인화면 