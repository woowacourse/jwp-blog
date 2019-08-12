#TODO    
- ajax 사용
    - 댓글
        - [ ] 수정
        - [ ] 삭제
        - [x] 생성
    - 로그인
        - [ ] 실패
    - 가입
        - [ ] 중복된 이메일



- 회원탈퇴한 유저의 게시글에 접근하면 에러가 난다.






#done
- user logic -> user, login, logout, mypage logic으로 분리한다
    - controller 분리
    - url 수정에 따른 html 파일 속 path 변경
    - test case 분리
- 테스트 케이스 이름들을 더 구체적인 이름으로 수정한다
- th:unless 제거한다.
- getResponseBody를 삭제한다
-  게시글 생성 시 사용자 정보 같이 저장하기
-  댓글 관련 기능 구현
    -  댓글 작성 시 작성자와 게시글 정보가 같이 저장됨
    -  댓글 생성 기능 구현
    -  댓글 조회 기능 구현
    -  댓글 수정 기능 구현
    -  댓글 삭제 기능 구현
        -  수정은 댓글 작성자만 가능
        -  삭제는 댓글 작성자만 가능

- articleController, userController 메서드 이름 convention 통일
- uri convention 통일(refactor articleController)
- comment controller test 작성
- auto reloading
- commentController와 articleController에서 user 검증하는 로직 중복 제거
    - argument resolver를 사용한다(컨트롤러에서 user를 직접 매개변수로 받음)
    - article과 comment에게 author를 확인하는 책임을 준다
- apache:commons dependency를 제거한다.
- lombok dependency를 제거한다.    
- 댓글 수정 시 새로운 에디터 창 띄우는 기능을 프런트에서 구현한다

