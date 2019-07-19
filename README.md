# 게시글 생성
- 게시글 작성 페이지 이동
    - 메인페이지(index.html)에서 게시글 생성 버튼을 누르기
    - GET /new 으로 요청
    - 작성 페이지(article-edit.html)로 이동
- 게시글 작성
    - POST /articles 으로 요청
    - 게시글 생성 시 게시글은 ArticleRepository의 List<Article> articles에 저장한다.
    - 게시글 페이지(article.html)로 이동

# 게시글 목록 조회
- 메인 페이지 이동
    - GET / 으로 요청으로 이동 시 메인 페이지에 게시글 목록이 노출
    
# 게시글 조회
- 게시글 페이지 이동
    - 메인페이지(index.html)에서 게시글을 클릭 시 게시글 페이지(article.html)으로 이동
    - GET /articles/{articleId} 으로 요청
    
# 게시글 수정
- 게시글 수정 페이지 이동
    - 게시글 페이지(article.html)에서 수정 버튼 누르기
    - GET /articles/{articleId}/edit 으로 요청
    - 게시글 수정 페이지(article-edit.html)로 이동
- 게시글 수정
    - PUT /articles/{articleId} 으로 요청
    - 게시글 페이지(article.html)로 이동
    
# 게시글 삭제
- 게시글 페이지(article.html)에서 삭제 버튼 누르기
    - DELETE /articles/{articleId} 으로 요청
    - 게시글 목록 조회 페이지(index.html)로 이동
    
# 2주차
   
## 회원조회
- 사용자는 get 요청으로 /users 를 요청하여 회원목록 페이지로 이동
- DB 에서 모든 회원의 정보를 출력
 
## 회원등록
- 사용자는 로그인 페이지에서 post 요청으로 /users 를 요청하여 회원가입 페이지로 이동
- 사용자가 입력한 회원정보를 DB에 저장
- 로그인 페이지로 다시 이동
```bash
- 회원가입 규칙
    - 동일한 email로 중복가입을 할 수 없다.
    - 이름은 2~10자로 제한하며 숫자나 특수문자가 포함될 수 없다.
    - 비밀번호는 8자 이상의 소문자, 대문자, 숫자, 특수문자의 조합이다.
    - 비밀번호 확인 기능이 동작해야 한다.
```
    
## 로그인 기능
- 로그인 페이지에서 ID와 Password 체크 후에
    - 성공시 : 메인화면
    - 실패시 : 상황에 맞는 실패 메시지를 띄움
- 로그인 한 유저가 로그인/회원가입 화면에 접근할 경우 메인화면을 띄운다.
## 로그아웃 기능
- 로그아웃 버튼을 누르면 로그아웃을 처리하고 메인페이지로 돌아간다.

## 회원수정
- 회원수정 시 본인 여부를 판단하여 다른사람이 수정을 시도하는 경우 제한한다.
- 사용자는 로그인이 된 상태에서 자기 것만 수정 가능 하다.
- put 요청으로 처리된다.

## 회원탈퇴
- Mypage -> profile 하단 -> 탈퇴버튼을 추가한다.
- 사용자는 로그인이 된 상태에서 자기 것만 탈퇴 가능 하다.
- delete 요청으로 처리된다.