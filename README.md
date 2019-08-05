## 나만의 블로그 서비스 (1주차)

### 기능 요구사항
- 게시글 생성
    - 게시글 작성 페이지 이동
    - 메인페이지(index.html)에서 게시글 생성 버튼을 누르기
    - GET /writing 으로 요청
    - 작성 페이지(article-edit.html)로 이동
- 게시글 작성
    - POST /articles 으로 요청
    - 게시글 생성 시 게시글은 ArticleRepository의 List<Article> articles에 저장한다.
    - 게시글 페이지(article.html)로 이동
- 게시글 목록 조회
    - 메인 페이지 이동
    - GET / 으로 요청으로 이동 시 메인 페이지에 게시글 목록이 노출
- 게시글 조회
    - 게시글 페이지 이동
    - 메인페이지(index.html)에서 게시글을 클릭 시 게시글 페이지(article.html)으로 이동
    - GET /articles/{articleId} 으로 요청

- 게시글 수정
    - 게시글 수정 페이지 이동
        - 게시글 페이지(article.html)에서 수정 버튼 누르기
        - GET /articles/{articleId}/edit 으로 요청
        - 게시글 수정 페이지(article-edit.html)로 이동
    - 게시글 수정
        - PUT /articles/{articleId} 으로 요청
        - 게시글 페이지(article.html)로 이동
- 게시글 삭제
    - 게시글 페이지(article.html)에서 삭제 버튼 누르기
    - DELETE /articles/{articleId} 으로 요청
    - 게시글 목록 조회 페이지(index.html)로 이동


### 제약조건
- HTML 중복제거
- 정적 파일 수정 시 재시작 하지 않고 변경사항 반영하기
- class 파일 수정 시 자동으로 재시작 하기

## 댓글 관련 기능 구현하기

- 댓글 작성 시 작성자와 게시글 정보가 같이 저장
- 댓글 생성/조회/수정/삭제조회 기능 구현
- 수정/삭제는 댓글 작성자만 가능