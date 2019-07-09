# 나만의 블로그 만들기

## 게시글 관련 기능 구현 요구사항

- `/`: 메인 페이지(`index.html`)
    - [x] 게시글 생성 버튼을 누르면 `/writing`으로 GET 요청을 보낸다.
    - [ ] 작성된 게시글 목록이 노출된다.
    - [ ] 게시글을 클릭하면 게시글 페이지(`/articles/{articleId}`)로 이동한다.

- `/writing`: 게시글 작성 페이지(`article-edit.html`)
    - [ ] 저장 버튼을 누르면 `/articles`로 POST 요청을 보낸다.
        - [x] 요청이 처리되면 `article.html`로 리다이렉트한다.
        - [x] X 버튼을 누르면 메인 페이지로 이동한다.
        - [ ] request받은 게시글을 ArticleRespository에 저장한다.

- `/articles/{articleId}`: 게시글 페이지(`article.html`)
