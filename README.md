# 나만의 블로그 만들기

### TODO LIST

(AritlcleRepository를 데이터베이스 이용하도록 변경)

- [x] ~~gradle에 jpa + h2 + mysql 종속성 추가~~
- [x] ~~properties에 mysql 종속성 추가~~
- [x] ~~mysql database 생성~~
- [x] ~~extends CrudRepository 로 변경~~
- [x] ~~Article 객체 @Entity로 변경~~
- [x] ~~insert 되는지 확인~~
- [x] ~~delete 확인~~
- [x] ~~select 확인~~
- [x] ~~테스트 코드 성공으로 바꾸기~~

(메인 페이지 로그인 vs 비로그인 상태)

- [x] ~~메인페이지가 비로그인 상태일 경우 GUEST - 로그인 - 회원가입 순으로 버튼 노출~~
- [x] ~~메인 페이지가 로그인 상태일 경우 이름 - 마이페이지 - 로그아웃 순으로 버튼 노출~~

(회원 등록)

- [x] ~~signup.html로 이동하는 버튼 생성 및 이동~~
- [x] ~~signup.html에서 POST /users 요청~~
- [x] ~~User entity~~
- [x] ~~User repository~~
- [x] ~~Account Controller 만들기~~

(회원 가입 규칙 - 웹 ( html5 form validation 활용))

- [x] ~~비밀번호 확인 기능 동작~~
- [x] ~~비밀번호 확인이 안되면 보내기 버튼 안되게~~
- [x] ~~실패시 화면에서 보여주기~~

(회원 가입 규칙 - 서버)

- [x] ~~같은 email로 중복가입 불가~~
- [x] ~~errors.rejectValue() 사용해서 해보기~~
- [x] ~~email 형식에 맞게~~
- [x] ~~이름 2~10자로 제한~~
- [x] ~~이름에 숫자 or 특수문자 있으면 안됨~~
- [x] ~~비밀번호 8자 이상 + 소문자+대문자+숫자+특수문자 조합~~
- [x] ~~회원 등록 실패시 errorMessage를 model에 담아 보내기~~
- [x] ~~Error에 field 추가~~

(회원 조회)
- [x] ~~버튼 추가~~
- [x] ~~GET /users 요청해 회원목록페이지(user-list.html) 이동~~
- [x] ~~DB에 있는 전체 회원 정보 노출~~

(로그인 성공 시)

- [x] ~~로그인 버튼 클릭시 login.html로 이동~~
- [x] ~~로그인 성공 시 메인(/) 화면 노출~~
- [x] ~~로그인 성공시 메인 우측 상단에 사용자 이름 띄우기~~
- [x] ~~다시 로그인/회원가입 화면에 접근할 경우 메인(/) 화면 노출~~
- [x] ~~Security gradle import~~
- [x] ~~security configure extends 만들기~~
- [x] ~~UserDetail implements 만들기~~
- [x] ~~UserDetailService 만들고~~

(로그인 실패시)

- [x] ~~로그인 실패시 errorMesage를 model에 담아 보내기~~
  - [x] ~~이메일이 없는 경우~~
  - [x] ~~비밀번호가 틀린 경우~~
- [x] ~~실패시 alerts으로 보내주기 (부트스트랩 alerts 사용)~~

(로그아웃시)

- [x] ~~메인화면(/) 노출~~
- [x] ~~메인 우측 상단에 사용자 이름 지우기~~

(회원 수정)

- [x] ~~마이페이지 클릭 시 mypage.html 로 이동 (/accounts/profile/{id})~~
- [x] ~~해당 id(userId)의 계정의 프로필이 보이게 변경~~
- [x] ~~본인 프로필계정이 아니면 수정 버튼 노출 안되게 변경~~
- [x] ~~수정버튼 클릭시 mypage-edit.html로 이동~~

- [x] ~~로그인 한 경우만 mypage-edit.html로 이동(GET /accounts/profile/edit)~~
- [x] 로그인 안한 상태면 로그인창으로 이동
- [x] ~~마이페이지(프로필)에서 본인일 경우에만 수정버튼이 나오게~~
- [ ] 로그인 하면 원래 가려던 페이지로 이동
- [x] ~~mypage-edit.html에서 PUT으로 수정 요청~~
- [x] ~~mypage-edit.html에 input태그 변경~~
- [x] ~~mypage-edit.html에 form태그 추가~~

(회원 탈퇴)

- [x] ~~MyPage > profile 하단 > 탈퇴 버튼을 추가~~
- [x] ~~DELETE로 탈퇴 요청~~

(추가)

- [x] 패키지 분리
- [x] 컨트롤러 분리
- [x] ~~AccountController 테스트 코드 중복 제거~~
- [x] ~~AccountController 테스트 코드 @BeforeEach 사용하지 말고 생성자에서 테스트 유저 추가~~
- [x] ArticleController 테스트 코드 중복 제거
- [x] ArticleController 테스트 코드 @BeforeEach 사용하지 말고 생성자에서 테스트 유저 추가
- [x] ~~컨트롤러 Exception Handler 와 404 에러 페이지 추가~~
- [x] url /articles를 prefix로 빼기 (/writing -> /articles/writing)
- [x] ~~html에서 form 중복 제거하기 (article-edit.html)~~
- [x] ~~html에서 form 중복 제거하기 (header.html)~~
- [x] 테스트할 때만 h2 사용하기  
- [ ] 자기가 쓴 글만 수정할 수 있도록 하기


=================================

- ~~게시글 작성 시 작성자가 같이 저장~~

댓글작성 기능 구현
- ~~커멘트 엔터티 생성~~
- ~~Add createdAt column into Comment entity~~
- ~~커맨트 레파지토리 구현~~
- ~~커맨트 서비스 구현~~
- ~~Comment Controller~~
- ~~Add addNewComment controller method in ArticleController~~
- ~~Show comments related to articles~~
- ~~Add modifyCommentById controller method in CommentController~~
- ~~Add deleteCommentById controller method in CommentController~~
- ~~게시글 삭제시 댓글도 삭제~~
- ~~aws ec2 인스턴스 설정 쉘 스크립트 작성~~
- ~~배포 쉘 스크립트 작성~~
- 댓글 수정 Restful 백엔드 작업
    - ~~RestCommentController 클래스 생성~~
    - RestCommentService 클래스 생성
    - ~~GET 메서드~~
    - ~~POST 메서드~~
    - ~~DELETE 메서드~~
    - ~~PUT 메서드~~
- 댓글 수정 프론트엔드 작업
    - ~~댓글 추가 버튼 누를 시 POST ajax 요청~~
    - ~~리스폰스가 정상적으로 왔을 시 현재 댓글들을 지우고 응답으로 온 댓글들로 대체~~
    - ~~댓글 삭제 버튼 누를 시 DELETE ajax 요청~~
    - ~~댓글 수정 버튼 누를 시 PUT ajax 요청~~
    - ~~article.html 이 로드될 시 GET ajax 요청~~
- ~~자기가 쓴 댓글만 수정/삭제 가능~~

리팩토링
- ~~컨트롤러 클래스를 추상화한 AbstractControllerTests 작성~~
    - ~~Setup 에서 로그인~~
    - ~~EntityExchangeResult 를 반환하는 GET 요청 메서드~~
    - ~~EntityExchangeResult 를 반환하는 POST 요청 메서드~~
    - ~~EntityExchangeResult 를 반환하는 PUT 요청 메서드~~
    - ~~EntityExchangeResult 를 반환하는 DELETE 요청 메서드~~
    - ~~annotation argument resolver 로 세션에서 사용자 가져오기~~
