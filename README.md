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

1. 회원등록/ 조회 기능 구현

    ~~1. h2.db -> mysql~~
    2. 회원등록 / 조회 기능 구현
        1. 회원 등록
            - login.html -> 회원가입페이지(signup.html)에서 POST /users로 요청
            - DB의 User 테이블에 user를 저장
            - 회원 생성 완료 시 로그인 페이지(login.html)로 이동
             - 비밀 번호 확인 기능
             - 회원 가입 규칙을 어겼을 때 페이지에서 errorMessage 발생
                ~~- 부트스트랩의 Alert() 이용~~
                - 프론트에서 유효성 체크 필요
            - 회원 가입 규칙
                ~~1. 중복 email가입 불가능~~ (백엔드)
                ~~2. 이름은 2~10자 제한 (숫자, 특수문자 불가능)~~ (프론트엔드)
                3. 비밀번호는 8자 이상의 소문자, 대문자, 숫자, 특수문자 조합 (프론트엔드 미완성?)
        2. 조회
            ~~- GET /users 로 요청해서 회원목록페이지(user-list.html) 이동~~
            ~~- DB에서 회원정보 가져와 출력~~ 
    ~~3. 실행 쿼리 보기 설정~~
    4. 로그인
        1. 성공했을 때 상단 오른쪽에 회원정보, 이름 띄우기
        2. 로그인 실패
            1. 이메일이 없을 때
            2. 비밀번호가 틀렸을 때
    4. 로그아웃
    
    고려사항 
        1. 같은 아이디가 아닐때 수정페이지 못들어가도록
        2. 로그인 했을 때, (회원가입, 로그인 페이지 못가도록)

    TODO.
    
        1. DTO를 내부 static 클래스를 만들어 사용해볼 것
        2. ModelMapper
        
      