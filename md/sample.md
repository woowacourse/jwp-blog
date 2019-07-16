# 추가학습 블로그 샘플파일

## 의존성 주입 방법 3가지
- Field Injection

- Setter Injection

- Constructor Injection(추천)


## Thymeleaf

### text
> `<h1 class="text-center cover-title" th:text="${article.title}"> 여기에 article.title이 출력 </h1>`


### if문과 if-else

서버측의 데이터에 대한 분기 조건이 필요한 경우에 사용

- if문을 사용할 때
> `<div th:if="${article.coverUrl==''}">` coverUrl이 빈 문자열이면 이 부분이 출력 `</div>`

- if-else를 사용할 때
> `<div th:if="${article.coverUrl==''}">` coverUrl이 빈 문자열이면 이 부분이 출력 `</div>`
> `<div th:unless="${article.coverUrl==''}">` coverUrl이 빈 문자열이 아니면 이 부분이 출력 `</div>`

- 3항 연산자도 사용 가능
> `<input th:value="${article==null? '': article.coverUrl}" type="text" name="coverUrl"
    class="text-center bg-transparent no-border font-size-16 width-30 mx-auto d-block text-light text-white border bottom pdd-top-20"
    placeholder="배경 url을 입력해주세요" autocomplete="off">`
    
    
### Spring IoC Container
Spring Container: 객체와 그 의존성들을 관리하는 요소(bean객체들이 들어가있는 통)

reflection 때문에 private으로 선언해도 주입 가능

### Component Scan
@ComponentScan은 basePacakge attribute를 지정하지 않을 경우 자신의 하위 패키지 전체를 scan한다.
(외부 빈이 필요한 경우에도 Component Scan을 쓴다)

### debug
문서를 보는 것도 좋지만 디버깅을 통해 알아보는 것도 좋다.

### 궁금한 점
- html에서 thymeleaf 쓸 때, thymeleaf들의 위치는 어디가 맞는건지?
 태그 바로 다음? 아니면 일반 속성들 먼저 적고 마지막에?
 ex) <div th:text="~~~" id="~" placeholder="~"...> or <div id="~" ~~~" placeholder="~" th:text="~"> 
 
 jun's answer : 보통 태그 다음에 바로 thymeleaf를 쓰고 다른 속성을 씀
 
- redirect vs forward 차이(찾아보기)


### 볼 것
https://ooz.co.kr/260
