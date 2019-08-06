## 게시글 관련 기능 TODO

### 게시글 작성
- [X] 게시글 작성페이지 이동 : ``GET /writing`` 요청 시 200 OK
- [X] 게시글 작성 : ``POST /articles``로 요청시 작성한 게시글 페이지로 이동

### 게시글 생성
- [X] 제목, 배경 이미지 url, 내용이 모두 입력되었을 때 -> 정상 처리
- [X] 제목, 내용이 입력되지 않았을 때 -> 에러 발생
- [X] 배경 이미지 url이 입력되지 않았을 때 -> 정상 처리 (기본 값을 설정)

### 게시글 조회
- [X] 존재하는 게시글 id에 접근 했을 때 -> 정상 처리
- [X] 존재하지 않는 게시글 id에 접근 했을 때 -> 에러 발생

### 게시글 수정
- [X] 게시글 수정 페이지 이동 : ``GET /articles/{articleId}/edit``으로 요청 시 200 OK 
        <br>(게시글 수정 페이지(article-edit.html)로 이동)
- [x] 게시글 수정 : ``PUT /articles/{articleId}``로 요청시 작성한 게시글 페이지로 이동
- [X] 존재하는 게시글 수정 (update) -> 정상 처리
- [X] 존재하지 않는 게시글 수정 요청 -> 에러 발생

### 게시글 삭제
- [X] 게시글 페이지(article.html)에서 삭제 버튼 클릭 시 ``DELETE /articles/{articleId}`` 으로 요청
- [X] 게시글 삭제 후 게시글 목록 조회 페이지(index.html)로 이동
- [X] 존재하는 게시글 삭제 (delete) 요청 -> 정상 처리
- [X] 존재하지 않는 게시글 삭제 요청 -> 에러 발생

### 제약조건
- [X] HTML 중복 제거
- [X] 정적 파일 수정 시 재시작하지 않고 변경사항 반영하기
- [X] class 파일 수정 시 자동으로 재시작하기

### 기타
- [X] 커스텀 익셉션 만들기

### 피드백
- [X] Article 클래스에 update구현
- [X] 테스트 코드 중복 제거
- [X] 필드 인젝션 -> 생성자 인젝션
- [X] PathVariable 자료형 통일 (Integer/int)

## 회원 관련 기능

### 회원 등록
- [X] 회원가입페이지(signup.html) 에서 ``POST /users`` 로 요청하면
    DB에 user정보 저장
- [X] 저장 후 로그인 화면으로 redirect
- [X] 실패 시 사용자에게 알려준다
- [X] 회원가입 관련 html 수정 (signup.html)

### 회원 가입 규칙
- [X] 동일한 email 중복 가입 시도 -> 에러
- [X] 이름은 2~10자, 숫자나 특수문자가 포함된 경우 -> 에러
- [X] 비밀번호는 8자 이상, 소문자, 대문자, 숫자, 특수문자의 조합이 아닌 경우 -> 에러
- [ ] 비밀번호 확인 기능이 동작

### 회원 조회
- [X] ``GET /users``로 요청하여 회원 목록 페이지(user-list.html) 이동
- [X] 회원 목록 페이지에서 DB에 저장된 회원 정보 확인
- [X] user-list.html 수정

### 기타
- [X] MySql로 DB 변경
- [X] 실행 쿼리 보기 설정하기
- [X] update CrudRepository의 save 대안 찾아보기
- [ ] 테스트 코드 독립성 - (테스트 메서드마다 DB 롤백할 수 있는 방법 찾아보기)

### 로그인
- [X] 로그인 성공 시 메인 화면(index.html)으로 redirect
- [X] 로그인 성공 시 메인화면 우측 상단에 사용자 이름을 띄운다.
- [X] 해당 이메일이 없는 경우 -> 에러
- [X] 비밀번호 불일치 경우 -> 에러
- [X] 로그아웃 요청 시 메인 화면(index.html)으로 redirect
- [X] 로그인 한 유저가 로그인/회원가입 화면에 접근할 경우 메인 화면(index.html)으로 redirect
- [X] 로그인 한 유저가 로그인/회원가입 화면에 접근할 경우 메인 화면(index.html)으로 redirect 테스트!
- [X] 로그인하지 않은 유저가 로그인/회원가입 화면에 접근할 경우 200 OK

### 회원 수정
- [X] ``GET /mypage`` 로 요청시 회원 정보 페이지로 이동 (200 OK)
- [X] ``GET /mypage/edit`` 로 요청시 회원정보 수정페이지로 이동 (200 OK)
- [X] ``POST /mypage``로 요청시 회원 정보 수정하고 ``GET /mypage`` 로 redirect

### 회원 탈퇴
- [X] MyPage > profile 하단 > 탈퇴 버튼 추가
- [X] 탈퇴버튼 클릭시 ``DELETE /users`` 로 요청 ~ 탈퇴 처리 후 ``GET /`` 로 redirect

### 게시글 수정
- [X] 작성자만 게시글 수정 가능하도록 하기
- [X] 게시글 생성 시 사용자 정보 같이 저장하기
- [X] 객체 연관관계 수정하기

### 댓글 등록 및 수정
- [X] 댓글 생성 기능 구현 ``POST /comments/{article_id}``
- [X] 댓글 조회 기능 구현 - CommentService
- [X] 댓글 수정 기능 구현 ``PUT /comments/{comment_id}``
- [X] 댓글 삭제 기능 구현 ``DELETE /comments/{article_id}/{comment_id}``
- [X] 수정/삭제는 댓글 작성자만 가능

### 비동기 통신&API / 댓글 추가 기능 구현
- 댓글 기능을 비동기 방식으로 구현하기
    - [X] 댓글 조회 비동기 방식으로 구현
    - [X] 댓글 추가 비동기 방식으로 구현
    - [X] 댓글 수정 비동기 방식으로 구현
    - [X] 댓글 삭제 비동기 방식으로 구현
- [ ] Script를 활용하여 자동 배포하기
- [ ] RollingFileAppender를 이용하여 배포한 서버의 로그를 저장하기
- [X] application.properties 를 application.yml 로 변경하기
- [X] Test Code 를 RestAssured 로 작성하기