package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTest {
    private static final String CUSTOM_USER_ID = "1";
    private static final String NAME = "유효한이름";
    private static final String EMAIL = "valid@email.com";
    private static final String PASSWORD = "ValidPassword!123";

    private BodyInserters.FormInserter<String> validUserData = getBodyInserters(NAME, EMAIL, PASSWORD);

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        postSignup(validUserData);
    }

    @Test
    public void 로그인_페이지_테스트() {
        webTestClient.get().uri("/login")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 로그인_테스트() {
        webTestClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(getBodyInserters(EMAIL, PASSWORD))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(res -> {
                    /** 인덱스 페이지 이동 **/
                    webTestClient.get()
                            .uri(res.getRequestHeaders().getLocation())
                            .exchange()
                            .expectStatus()
                            .isOk();
                });
    }

    @Test
    public void 로그아웃_테스트() {
        webTestClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(getBodyInserters(EMAIL, PASSWORD))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(r -> {
                    /** 로그아웃 **/
                    webTestClient.get()
                            .uri("/logout")
                            .exchange()
                            .expectStatus().isFound();
                });
    }

    private void postSignup(BodyInserters.FormInserter<String> userData) {
        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(userData)
                .exchange();
    }

    private BodyInserters.FormInserter<String> getBodyInserters(String email, String password) {
        return BodyInserters.fromFormData("email", email)
                .with("password", password);
    }

    private BodyInserters.FormInserter<String> getBodyInserters(String name, String email, String password) {
        return BodyInserters.fromFormData("userId", CUSTOM_USER_ID)
                .with("name", name)
                .with("email", email)
                .with("password", password);
    }
}
