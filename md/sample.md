# 추가학습 블로그 샘플파일

## 의존성 주입 방법 3가지
- Field Injection

- Setter Injection

- Constructor Injection


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


### 궁금한 점
- html에서 thymeleaf 쓸 때, thymeleaf들의 위치는 어디가 맞는건지?
 태그 바로 다음? 아니면 마지막?
 
 ex) <div th:text="~~~" id="~" ...> , <div id="~" ~~~" th:text="~"> 