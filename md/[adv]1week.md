# [adv] 추가학습주제 1주차

## Auto Configuration 비활성화

#### Auto Configuration 
추가된 jar 의존성을 기준으로 자동으로 스프링을 설정한다.

#### component
스프링이 bean으로 만들어 관리 할 대상 - @Service, @Repository, @Controller 구체적인, 특수화 된 @Component

#### component scan
spring 에서 사용할 been을 스캔하는 과정 
* @SpringBootApplication를 선언한 main 아래 패키지의 컴포넌트를 스캔
* 하위 패키지가 아닌 경우 @ComponentScan을 사용하여 직접 추가 가능

```$xslt
@ComponentScan({"",""})
@SpringBootApplication
public class SpringApplication {
```

[참고](https://www.springboottutorial.com/spring-boot-and-component-scan)

#### @SpringBootApplication
많은 스프링 부트 개발에서 auto-configuration, component scan을 사용한다. 단일 @SpringBootApplication 사용하면
다음의 세가지 기능을 사용할 수 있다.
* @EnableAutoConfiguration
* @ComponentScan
* @Configuration

## Spring Bean/Component Annotation
**@bean** 개발자가 컨트롤이 불가능한 외부 라이브러리들을 Bean으로 등록하고 싶은 경우
* 인스턴스를 생성하는 메소드를 만들고 해당 메소드에 @Bean을 선언
* @Target - method

**@Component** 개발자가 직접 컨트롤이 가능한 Class들의 경우
* 관리하고 싶은 클래스에 @Component 선언
* @Target - type

각각의 target 선언 때문에 직접 만든 class에 @bean을 선언하면 컴파일 에러 발생

[참고](https://jojoldu.tistory.com/27)

## Spring Framework vs Spring Boot

#### Spring Framework가 해결하는 핵심 문제
* DI(Dependency Injection), IOC(Inversion of Control)

* Duplication/Plumbing Code
    * Spring JDBC
    * Spring MVC
    * Spring AOP

* 사용성이 높은 솔루션을 제공하는 프레임워크들을 통합
    * Hibernate for ORM
    * iBatis for Object Mapping
    * JUnit and Mockito for Unit Testing

#### Spring Boot가 해결하는 핵심 문제
스프링 기반 애플리케이션들은 프레임워크들과의 통합을 위해서 많은 환경설정을 포함한다. 
(Spring MVC 를 사용할 때, 컴포넌트 스캔, 디스패쳐 서블릿, 뷰 리졸버, 웹 jar을 설정해야 한다.)

>**Spring Boot**는 특정 jar를 애플리케이션에 추가할 때 일부 빈들을 자동으로 설정 해준다.(Auto Configuration)

웹 애플리케이션을 개발하고 싶다면 Spring Boot Start Web을 사용해서 간단히 web을 
만들기위한 jar과 설정을 추가 할 수 있다.


[참고](https://m.blog.naver.com/PostView.nhn?blogId=sthwin&logNo=221271008423&proxyReferer=https%3A%2F%2Fwww.google.com%2F)

## RequestMapping

#### RequestMapping의 동작
스프링의 DispatcherServlet은 설정 파일을 이용해서 스프링 컨테이너를 생성

스프링 컨테이너에 HandlerMapping, HandlerAdapter, 컨트롤러, ViewResolver 등의 bean이 있음

웹 브라우저의 요청을 처리할 핸들러 객체를 찾기 위해 HandlerMapping 을 사용

@Controller 어노테이션이 적용된 bean 객체를 찾아 요청 처리

[참고](https://tinkerbellbass.tistory.com/40)

#### Front Controller Pattern 

>웹 사이트에 대한 모든 요청을 처리하는 컨트롤러 => spring : DispatcherSevlet

요청에서 명령(ex: url?)을 추출하여 명령 클래스의 새 인스턴스(1?)를 동적으로 생성하고 실행

궁금?
 * 이미 만들어진 bean을 사용하는 것 아닌가?
 
[참고](https://www.baeldung.com/java-front-controller-pattern)