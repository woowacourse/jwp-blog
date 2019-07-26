

# 1주차 미션

# 요구사항

- 게시글 생성/조회/목록조회 기능 구현

------

## 게시글 생성

- 게시글 작성 페이지 이동
  - 메인페이지(index.html)에서 게시글 생성 버튼을 누르기
  - `GET /writing` 으로 요청
  - 작성 페이지(article-edit.html)로 이동
- 게시글 작성
  - `POST /articles` 으로 요청
  - 게시글 생성 시 게시글은 ArticleRepository의 `List<Article> articles`에 저장한다.
  - 게시글 페이지(article.html)로 이동

------

## 게시글 목록 조회

- 메인 페이지 이동
  - `GET /` 으로 요청으로 이동 시 메인 페이지에 게시글 목록이 노출

------

## 게시글 조회

- 게시글 페이지 이동
  - 메인페이지(index.html)에서 게시글을 클릭 시 게시글 페이지(article.html)으로 이동
  - `GET /articles/{articleId}` 으로 요청

------

## 제약조건

- HTML 중복제거
- 정적 파일 수정 시 재시작 하지 않고 변경사항 반영하기
- class 파일 수정 시 자동으로 재시작 하기

------

# 힌트

------

## HTML 중복제거

- 같은 형태로 표현되는 head 태그 부분 공통부분으로 만들어 중복된 코드를 제거한다
  - https://elfinlas.github.io/2018/02/16/thymeleaf-layout-dialect_exam/
  - https://blog.hanumoka.net/2018/08/07/spring-20180807-spring-thymeleaf-layout-dialect2-head/
- Header처럼 모든 페이지에서 노출되는 DOM Element의 중복된 코드를 제거한다.

------

## SpringBoot Devtools

> [Spring Boot Document - Developer Tools](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#using-boot-devtools)
> [참고 블로그](https://haviyj.tistory.com/11)

- Enable hot swapping
- Switches template engines to disable caching
- Enables LiveReload to refresh browser automatically
- Other reasonable defaults based on development instead of production

------

## 절대경로와 상대경로

- 현재 제공되는 HTML 파일에서는 상대경로로 외부 리소스(js, css 등)에 접근하고 있다.
- Spring Boot를 거치지 않고 정적 파일로 페이지를 열면 정상 동작하지만 Spring Boot를 통해 요청을 받으면 정상적으로 노출 되지 않을 수 있다.



# 게시글 수정/삭제기능 구현하기



# 요구사항

------

## 게시글 수정

- 게시글 수정 페이지 이동
  - 게시글 페이지(article.html)에서 수정 버튼 누르기
  - `GET /articles/{articleId}/edit` 으로 요청
  - 게시글 수정 페이지(article-edit.html)로 이동
- 게시글 수정
  - `PUT /articles/{articleId}` 으로 요청
  - 게시글 페이지(article.html)로 이동

------

## 게시글 삭제

- 게시글 페이지(article.html)에서 삭제 버튼 누르기
  - `DELETE /articles/{articleId}` 으로 요청
  - 게시글 목록 조회 페이지(index.html)로 이동

------

# 힌트

------

## 게시글 작성 페이지 공유

- 게시글 생성과 수정이 같은 html 페이지를 사용하므로, 각 상황에 따라 thymeleaf 문법을 활용하기

------

## PUT/DELETE Method

- Form은 기본적으로 GET과 POST 메서드밖에 지원하지 않는다. form 태그에서 PUT/DELETE를 요청하는 방법을 검색해서 해결한다. (힌트: _method)

#2주차 미션
- 회원 등록/조회기능 구현하기
- 회원 로그인 기능 구현하기
- 회원정보 수정/탈퇴 기능 구현하기

#3주차 미션
- 댓글 작성 시 작성자와 게시글 정보가 같이 저장
- 댓글 생성/조회/수정/삭제조회 기능 구현
    - 수정/삭제는 댓글 작성자만 가능