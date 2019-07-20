나만의 블로그 만들기

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

- [x] ~~user drop 메뉴에 전체 회원 목록 버튼 추가~~
- [x] ~~버튼 클릭시 user-list.html로 이동~~

- [x] ~~GET /accounts/users 요청해 회원목록페이지(user-list.html) 이동~~
- [x] ~~user-list.html에서 DB에 있는 전체 회원 정보 노출~~
- [x] ~~user-list.html에서 목록 하나 클릭 시 GET /accounts/profile/{id}~~

(로그인 성공 시)

- [x] ~~로그인 버튼 클릭시 login.html로 이동~~
- [x] ~~로그인 성공 시 메인(/) 화면 노출~~
- [x] ~~로그인 성공시 메인 우측 상단에 사용자 이름 띄우기~~
- [x] ~~다시 로그인/회원가입 화면에 접근할 경우 메인(/) 화면 노출~~
- [x] ~~Security gradle import~~
- [x] ~~security configure extends 만들기~~
- [x] ~~UserDetail implements 만들기~~
- [x] ~~UserDetailService 만들고~~
- [x] ~~intercepter webConfig 만들기~~
- [x] ~~HandlerIntercepterAdapter 만들기~~
- [x] ~~/accounts/profile/edit 에 접근시 로그인 여부 확인. 비 로그인일 시 /login으로 보냄~~
- [x] ~~/accounts/logout 에 접근시 로그인 여부 확인. 비로그인 상태일 경우 /로 보냄~~
- [x] ~~/accounts/signup, /accounts/login 에 접근시 로그인 여부 확인. 로그인 상태일 경우 /로 보냄~~

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

- [x] ~~회원정보 수정시 본인 여부 판단해 본인일 경우만 mypage-edit.html로 이동(GET /accounts/profile/edit)~~
- [x] ~~본인이 아닌 경우엔 /login으로 리다이렉트~~
- [x] ~~마이페이지(프로필)에서 본인일 경우에만 수정버튼이 나오게~~
- [x] ~~mypage-edit.html에서 PUT으로 수정 요청~~
- [x] ~~mypage-edit.html에 input태그 변경~~
- [x] ~~mypage-edit.html에 form태그 추가~~

(회원 탈퇴)

- [x] ~~MyPage > profile 하단 > 탈퇴 버튼을 추가~~
- [x] ~~버튼 클릭시 DELETE로 탈퇴 요청~~
- [x] ~~탈퇴버튼 클릭시 ????.html 로 이동~~
- [x] ~~DELETE로 탈퇴 요청~~

(추가)

- [ ] readme 생성 (todolist와 분리)

- [ ] url /articles를 prefix로 빼기 (/writing -> /articles/writing)
- [x] ~~html에서 form 중복 제거하기 (article-edit.html)~~
- [x] ~~html에서 form 중복 제거하기 (header.html)~~
- [ ] article entity lombok @Data 대신 바꾸기
- [ ] 테스트할 땐 db가 h2로 못가나? 
- [ ] 자기가 쓴 글만 수정할 수 있도록 하기.
- [ ] 로그인 필수 페이지 접근 시 로그인 시도 후 원래 가려던 페이지로 가기
- [ ] ~~/accounts/users를 post /accounts/signup으로 변경~~
- [ ] 이미지....이미지......
- [ ] login.html / signup.html 에서 logo부분 중복 제거 (header.html 에 포함된 부분)
- [ ] AccountController가 너무 많이 가지고있는듯...?  무슨 기준으로 나누지 테스트도 연관되어있어서
- [x] ~~drop menu 에서 로그인/비로그인 상관 없이 기본인 거 중복 제거~~
- [ ] 로그인 페이지에서 회원가입 버튼 랜딩 url /accounts/signup으로 변경
- [ ] DB 저장시 비밀번호 암호화