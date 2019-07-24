#TODO
3. th:unless 제거한다.
4. 현 상황: getResponseBody 메서드 선언이 여러 곳에 있다. 



#done
1. user logic -> user, login, logout, mypage logic으로 분리한다
    (1) controller 분리
    (2) url 수정에 따른 html 파일 속 path 변경
    (3) test case 분리
2. 테스트 케이스 이름들을 더 구체적인 이름으로 수정한다

4. getResponseBody를 삭제한다