# 게시글 생성/조회기능 구현하기

## 게시글 생성
* 게시글 작성 페이지 이동
    * 메인페이지(index.html)에서 게시글 생성 버튼을 누르기
    * GET /writing 으로 요청
    * 작성 페이지(article-edit.html)로 이동
* 게시글 작성
    * POST /articles 으로 요청
    * 게시글 생성 시 게시글은 ArticleRepository의 List<Article> articles에 저장한다.
    * 게시글 페이지(article.html)로 이동

## 게시글 목록 조회
* 메인 페이지 이동
    * GET / 으로 요청으로 이동 시 메인 페이지에 게시글 목록이 노출

## 게시글 조회
* 게시글 페이지 이동
    * 메인페이지(index.html)에서 게시글을 클릭 시 게시글 페이지(article.html)으로 이동
    * GET /articles/{articleId} 으로 요청
    

## 회원등록
* 회원가입페이지(signup.html)에서 POST /users 로 요청
* Spring Data JPA를 이용하여 DB에 user 정보를 저장
* 생성 후 로그인 화면으로 이동
* 회원가입 시 아래의 회원가입 규칙을 지켜야 하고, 위반 시 사용자에게 알려준다.

## 회원가입 규칙
* 동일한 email로 중복가입을 할 수 없다. (TODO)
* 이름은 2~10자로 제한하며 숫자나 특수문자가 포함될 수 없다.
* 비밀번호는 8자 이상의 소문자, 대문자, 숫자, 특수문자의 조합이다.
* 비밀번호 확인 기능이 동작해야 한다.

## 회원조회
* GET /users 로 요청하여 회원목록페이지(user-list.html) 이동
* DB에 저장된 회원 정보 노출

---
# Ajax를 사용하도록 기능 변경
- [x] 댓글 기능
- [x] MyPage put 요청
- [ ] Login
- [ ] 헤더의 로그인 정보
- [ ] SignUp

# Script를 이용한 자동 배포
- [ ] 배포 성공

# API docs
- [ ] API docs 생성