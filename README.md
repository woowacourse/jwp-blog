# 나만의 블로그 서비스

## 게시글 관련 기능

### 게시글 생성/조회 기능
1. 게시글 작성 페이지 이동
    - 메인페이지(index.html)에서 게시글 생성 버튼을 누르기
    - ```GET /writing``` 으로 요청
    - 작성 페이지(article-edit.html)로 이동
    
2. 게시글 작성
    - ```POST /articles``` 으로 요청
    - 게시글 생성 시 게시글은 ```ArticleRepository```의 ```List<Article> articles```에 저장한다.
    - 게시글 페이지(article.html)로 이동
    
3. 게시글 목록 조회
    - 메인페이지 이동 시 ```GET /``` 으로 요청하여 전체 게시글 목록이 노출
    
4. 게시글 조회
    - 메인페이지에서 게시글 클릭시 ```GET /articles/{articleId}``` 으로 요청하여 게시글 페이지로 이동

### 게시글 수정/삭제 기능
1. 게시글 수정 페이지 이동
    - 게시글 페이지(article.html)에서 수정 버튼 누르기
    - GET /articles/{articleId}/edit 으로 요청
    - 게시글 수정 페이지(article-edit.html)로 이동
 
 2. 게시글 수정
    - ```PUT/articles/{articleId}``` 으로 요청 시 게시글 페이지(article.html)로 이동
 
 3. 게시글 삭제
    - 게시글 페이지(article.html)에서 삭제 버튼 누르기
    - DELETE /articles/{articleId} 으로 요청
    - 게시글 목록 조회 페이지(index.html)로 이동
    
## 회원 관련 기능

### 회원 등록/조회기능 구현하기
1. 회원등록
    - 회원가입페이지(signup.html)에서 ``POST /users`` 로 요청
    - Spring Data JPA를 이용하여 DB에 user 정보를 저장
    - 생성 후 로그인 화면으로 이동
    - 회원가입 시 아래의 회원가입 규칙을 지켜야 하고, 위반 시 사용자에게 알려준다.
    
    > 회원가입 규칙
    > - 동일한 email로 중복가입을 할 수 없다.
    > - 이름은 2~10자로 제한하며 숫자나 특수문자가 포함될 수 없다.
    > - 비밀번호는 8자 이상의 소문자, 대문자, 숫자, 특수문자의 조합이다.
    > - 비밀번호 확인 기능이 동작해야 한다.
    
2. 회원조회
    - ``GET /users`` 로 요청하여 회원목록페이지(user-list.html) 이동
    - DB에 저장된 회원 정보 노출