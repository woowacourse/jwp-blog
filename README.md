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