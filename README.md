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
    
# feedback TODO

1. Test
    - ~~@DisplayName~~
    - Article Test 작성
    - ArticleRepository 테스트 작성
    - ~~Before 삭제~~ 
    
2. Getter -> Message 처리
    - setter 변경
    - ~~article.getId -> 메시지~~
    - ~~Constructor Injection~~
    
3. 동기화 사용
    - latestId