# 우아한 테크 코스 레벨2

## 게시글 생성/조회 기능 구현하기
### 구현 기능 목록
* 게시글 생성
    1. 메인페이지에서 게시글 생성 버튼 누르기
    2. GET /writing 요청
    3. 작성페이지로 이동 (article-edit.html)

* 게시글 작성
    1. POST /articles 요청
    2. 게시글 생성 시 게시글은 ArticleRepository 에 저장
    3. 게시글 페이지로 이동 (article.html)
    
* 게시글 목록 조회
    1. 게시글 페이지 이동 (index.html -> article.html)
    2. GET /articles/{articleId} 요청
    
## 게시글 수정/삭제 구현하기
### 구현 기능 목록
* 게시글 수정 페이지 이동
    1. 게시글 페이지 수정 버튼 누르기
    2. GET / articles/{articleId}/edit 요청
    3. 게시글 수정페이지로 이동 (article-edit.html)

* 게시글 수정
    1. PUT /articles/{articleId} 요청
    2. 게시글 페이지로 이동 (article.html)
    
* 게시글 삭제
    1. 게시글 페이지에서 삭제 버튼 누르기
    2. DELETE /articles/{articleId} 요청
    3. 게시글 목록 조회 페이지로 이동 (index.html)

