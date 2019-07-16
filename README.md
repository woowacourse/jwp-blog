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

(회원 등록)

- [ ] signup.html로 이동하는 버튼 생성 및 이동
- [ ] signup.html에서 POST /users 요청

(회원 가입 규칙 - 웹 ( html5 form validation 활용))

- [ ] 이름 2~10자로 제한
- [ ] 이름에 숫자 or 특수문자 있으면 안됨
- [ ] 비밀번호 8자 이상 + 소문자+대문자+숫자+특수문자 조합
- [ ] 비밀번호 확인 기능 동작

(회원 가입 규칙 - 서버)

- [ ] 같은 email로 중복가입 불가
- [ ] 이름 2~10자로 제한
- [ ] 이름에 숫자 or 특수문자 있으면 안됨
- [ ] 비밀번호 8자 이상 + 소문자+대문자+숫자+특수문자 조합
- [ ] 비밀번호 확인 기능 동작
- [ ] 회원 등록 실패시 errorMessage를 model에 담아 보내기
- [ ] 실패시 alerts으로 보여주기 (부트스트랩 alerts 사용)

(회원 조회)

- [ ] GET /users 요청해 회원목록페이지(user-list.html) 이동
- [ ] DB에 있는 전체 회원 정보 노출

(로그인 성공 시)

- [ ] 로그인 버튼 클릭시 login.html로 이동
- [ ] 로그인 성공 시 메인(/) 화면 노출
- [ ] 로그인 성공시 메인 우측 상단에 사용자 이름 띄우기
- [ ] 다시 로그인/회원가입 화면에 접근할 경우 메인(/) 화면 노출

(로그인 실패시)

- [ ] 로그인 실패시 errorMesage를 model에 담아 보내기
  - [ ] 이메일이 없는 경우
  - [ ] 비밀번호가 틀린 경우
- [ ] 실패시 alerts으로 보내주기 (부트스트랩 alerts 사용)

(로그아웃시)

- [ ] 메인화면(/) 노출
- [ ] 메인 우측 상단에 사용자 이름 지우기

(회원 수정)

- [ ] 회원정보 수정시 본인 여부 판단해 본인일 경우만 signup.html로 이동
- [ ] 회원정보 수정시 본인이 아니면 alert 띄우기
- [ ] sighup.html에서 PUT으로 수정 요청

(회원 탈퇴)

- [ ] MyPage > profile 하단 > 탈퇴 버튼을 추가
- [ ] 탈퇴버튼 클릭시 ????.html 로 이동
- [ ] DELETE로 탈퇴 요청