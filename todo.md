#TODO
- [ ] 게시글 생성 시 사용자 정보 같이 저장하기
- [ ] 댓글 관련 기능 구현
    - [ ] 댓글 작성 시 작성자와 게시글 정보가 같이 저장됨
    - [ ] 댓글 생성 / 조회 /  수정 / 삭제 기능 구현
        - [ ] 수정/삭제는 댓글 작성자만 가능


(선택) 1. interceptor 구현     
(선택) 2. controllerAdvice 구현



#done
1. user logic -> user, login, logout, mypage logic으로 분리한다
    (1) controller 분리
    (2) url 수정에 따른 html 파일 속 path 변경
    (3) test case 분리
2. 테스트 케이스 이름들을 더 구체적인 이름으로 수정한다
3. th:unless 제거한다.
4. getResponseBody를 삭제한다