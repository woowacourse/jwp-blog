# 게시글 관련 기능 구현

## 1단계 기능목록

### 게시글 생성

- 게시글 작성 페이지 이동
  - 메인페이지(index.html)에서 게시글 생성 버튼을 누르기
  - `GET /writing` 으로 요청
  - 작성 페이지(article-edit.html)로 이동

- 게시글 작성
  - `POST /articles` 으로 요청
  - 게시글 생성 시 게시글은 ArticleRepository의 `List<Article> articles`에 저장한다.
  - 게시글 페이지(article.html)로 이동

### 게시글 목록 조회

- 메인 페이지 이동
  - `GET /` 으로 요청으로 이동 시 메인 페이지에 게시글 목록이 노출

### 게시글 조회

- 게시글 페이지 이동
  - 메인페이지(index.html)에서 게시글을 클릭 시 게시글 페이지(article.html)으로 이동
  - `GET /articles/{articleId}` 으로 요청

### 게시글 수정

- 게시글 수정 페이지 이동
  - 게시글 페이지(article.html)에서 수정 버튼 누르기
  - `GET /articles/{articleId}/edit` 으로 요청
  - 게시글 수정 페이지(article-edit.html)로 이동
- 게시글 수정
  - `PUT /articles/{articleId}` 으로 요청
  - 게시글 페이지(article.html)로 이동
  
### 게시글 삭제

- 게시글 페이지(article.html)에서 삭제 버튼 누르기
  - `DELETE /articles/{articleId}` 으로 요청
  - 게시글 목록 조회 페이지(index.html)로 이동

## 2단계 기능목록

### 기존에 구현한 데이터를 MySql로 옮기기
### 회원등록
- 회원가입페이지(signup.html)에서 POST /users 로 요청
- Spring Data JPA를 이용하여 DB에 user 정보를 저장
- 생성 후 로그인 화면으로 이동
- 회원가입 시 아래의 회원가입 규칙을 지켜야 하고, 위반 시 사용자에게 알려준다.

### 회원조회
- GET /users 로 요청하여 회원목록페이지(user-list.html) 이동
- DB에 저장된 회원 정보 노출

### 회원등록실패
- 회원등록 실패 시 errorMessage를 Model에 담아서 페이지에 노출하기!
- 프론트엔드에서 errorMessage 노출 시 부트스트랩의 Alerts을 이용할 것
- 프론트엔드에서도 유효성 체크할 수 있도록 할 것
    - HTML5에서 제공하는 form validation 기능을 최대한 활용할 것
    
### 실행 쿼리 보기 설정하기

### 로그인
- 로그인 성공 시 메인 화면을 띄우고 우측 상단에 사용자 이름을 띄운다.
- 로그인 실패 시 상황에 맞는 실패 메시지를 띄운다.
    - 이메일이 없는 경우
    - 비밀번호가 틀린 경우
        
### 로그아웃
- 로그아웃 시 메인 화면을 띄운다.

### 기타
- 로그인 한 유저가 로그인/회원가입 화면에 접근할 경우 메인 화면을 띄운다.
- 회원 수정 시 본인 여부를 판단하여 다른 사람이 수정을 시도하는 경우 제한한다.

### 회원 수정 / 탈퇴 기능 구현하기
- 본인의 회원정보만 수정할 수 있어야 하고 본인만 탈퇴할 수 있어야 한다.
- 로그인 후 본인 확인하는 과정이 필요하다.

### 회원 수정
- 회원수정페이지(signup.html)에서 PUT 메소드를 이용하여 수정 요청

### 회원 탈퇴
- MyPage > profile 하단 > 탈퇴 버튼을 추가
- DELETE 메소드를 이용하여 탈퇴 요청