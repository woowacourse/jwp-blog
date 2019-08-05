package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {
    private WebTestClient webTestClient;
    private UserRepository userRepository;

    @Autowired
    private UserControllerTests(WebTestClient webTestClient, UserRepository userRepository) {
        this.webTestClient = webTestClient;
        this.userRepository = userRepository;

    }

    @BeforeEach
    void setUp() {
        userRepository.save(new User("zino@naver.com", "zino", "zino123!"));
    }

    @Test
    void 회원가입_POST() {
        webTestClient.post()
                .uri("/users")
                .body(fromFormData("name", "name")
                        .with("email", "email")
                        .with("password", "password"))
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    void 로그인_테스트() {
        webTestClient.post()
                .uri("/login")
                .body(fromFormData("email", "zino@naver.com")
                        .with("password", "zino123!")
                        .with("name", "zino"))
                .exchange()
                .expectStatus()
                .isFound();
    }

    @Test
    void 로그아웃_테스트() {
        webTestClient.get()
                .uri("/logout")
                .header("Cookie", setCookie())
                .exchange()
                .expectStatus()
                .isFound();
    }

    @Test
    void myPage_조회_테스트() {
        webTestClient.get()
                .uri("/mypage")
                .header("Cookie", setCookie())
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void myPage_수정_페이지_테스트() {
        webTestClient.get()
                .uri("/mypage/edit")
                .header("Cookie", setCookie())
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void myPage_수정_테스트() {
        webTestClient.put()
                .uri("/mypage/edit")
                .body(fromFormData("email", "zino@naver.com")
                        .with("password", "zino123!@#")
                        .with("name", "zinozino"))
                .header("Cookie", setCookie())
                .exchange()
                .expectStatus()
                .isFound();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    private String setCookie() {
        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("email", "zino@naver.com")
                        .with("password", "zino123!"))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }
}
