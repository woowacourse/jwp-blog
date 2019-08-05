## TODOLIST

실습 - 자신이 작성한 글만 수정 가능

- [x] ~~방향 관계 결정 - 양방향? 단반향? 결정 : 단방향, 주인은 article~~
- [x] ~~연관관계 매핑 설정(Article)~~
- [x] ~~비 로그인 유저가 /articles/{articleId}/* 로 접근하면 안되게 구현~~
- [x] ~~비 로그인 유저가 GET /articles/{articleId} 는 접근가능하게 구현~~
- [x] ~~비 로그인 유저가 GET /articles/{articleId} 접근 가능한 테스트~~
- [x] ~~비 로그인 유저가 GET /articles/{articleId}/edit으로 접근하면 "/login"로 보내는 테스트~~
- [x] ~~비 로그인 유저가 PUT /articles/{articleId}으로 접근하면 "/login"로 보내는 테스트~~
- [x] ~~비 로그인 유저가 DELETE /articles/{articleId}으로 접근하면 "/login"로 보내는 테스트~~
- [x] ~~비 로그인 유저가 쓰기버튼 접근하면 "/login"으로 보내는 구현~~
- [x] ~~비 로그인 유저가 쓰기버튼 접근하면 "/login"으로 보내는 테스트~~
- [x] ~~홈에서 비 로그인유저한텐 게시글 작성 버튼 노출 안되게 구현~~
- [x] ~~articleDto 생성~~
- [x] ~~articleDto를 사용하도록 controller 변경~~
- [x] ~~기존 article 테스트 모두 돌아가게 변경 (로그인 한 후 실행하도록 변경)~~
- [x] ~~비 로그인 유저가 GET /articles/{articleId} 는 접근했을 때 수정 버튼 안보이는지 테스트~~
- [x] ~~로그인 유저가 자기 글이 아닌 GET /articles/{articleId} 에 접근했을 때 수정 버튼 안보이는지 테스트~~
- [x] ~~자기 글이 아닌 GET /articles/{articleId} 에 접근했을 때 수정 버튼 보이는지 테스트~~
- [x] ~~자기글이 아닌 GET /articles/{}/edit에 접근시 /로 리다이렉트~~
- [x] ~~자기글이 아닌 DELETE /articles/{}/edit에 접근시 /로 리다이렉트~~
- [x] ~~article.html에서 thymleaf 세션 유저 번호랑 아티클 유저 번호랑 일치할 때만 수정/삭제 버튼 보이게 변경~~



추가 

- [ ] ArticleDto  0상수로 변경하기 
- [ ] ArticleController에 /articles를 상수로 변경 후 테스트에서 사용
- [ ] Article showArticleByIdPageTest() 전체 테스트 돌렸을때 돌아가게 변경
- [ ] showArticleEditingPage() 전체 테스트로 돌렸을 때 돌아가게 변경
- [ ] ArticleControllerTests MockMVC로 변경

