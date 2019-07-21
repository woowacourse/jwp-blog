# myblog

## Done

전체 기능구현 완료

## Refactoring

1. ~~FlashMessage 적용~~
    현재 error 메세지를 전달할 때 RedirectAttributes를 사용하고 있다. 이때 URI에서 QueryString으로 에러 내용을 노출하고
    있기 때문에 이를 방지하고자 flash message 적용 예정
    
    [참고 : Spring Redirect시에 데이타 숨겨서 넘기는 방법](http://www.coolio.so/spring-redirect%EC%8B%9C%EC%97%90-%EB%8D%B0%EC%9D%B4%ED%83%80-%EC%88%A8%EA%B2%A8%EC%84%9C-%EB%84%98%EA%B8%B0%EB%8A%94-%EB%B0%A9%EB%B2%95/)
    
   **적용완료**
   
2. ~~Spring Validation~~
    많은 크루들이 `@Valid`를 사용하는 Spring Validation을 사용하고 있음.
    그 결과 컨트롤러에서도 클라이언트에서 전송된 값을 검증하는 로직이 있는 것이 좋다고 판단하여 이를 적용하고자함.
    
    [참고1 : Spring Validation 공통모듈 만들기](https://jojoldu.tistory.com/129)
    [참고2 : Spring boot 스프링 부트에서 request Validation 요청값 검증하기](https://velog.io/@junwoo4690/Spring-boot-%EC%8A%A4%ED%94%84%EB%A7%81-%EB%B6%80%ED%8A%B8%EC%97%90%EC%84%9C-request-Validation-%EC%9A%94%EC%B2%AD%EA%B0%92-%EA%B2%80%EC%A6%9D%ED%95%98%EA%B8%B0)
    
    **적용완료**
    
3. ~~Dto 사용 고려~~
    현재 Entity 클래스가 `DTO` + `도메인`의 역할을 하고 있다고 생각.
    하지만 이는 level1에서 배운 도메인 객체에 반하는 것이라고 판단하여 클라이언트에게서 받는 입력값은 Dto로 받도록 생각 중
    
    **적용완료**

4. ~~로그인 로직 처리~~
    cookie를 통한 자동 로그인이 되도록 구현 예정
    
    [참고 : 쿠키(Cookie)와 세션(Session) & 로그인 동작 방법](https://cjh5414.github.io/cookie-and-session/)
    
    JSESSIONID로 적용