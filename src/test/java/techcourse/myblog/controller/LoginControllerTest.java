package techcourse.myblog.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTest {
    private static final String LOGIN_FAIL_PAGE = "/login";
    private static final String USER_NAME_1 = "test1";
    private static final String EMAIL_1 = "test1@test.com";
    private static final String EMAIL_2 = "test2@test.com";
    private static final String PASSWORD_1 = "123456789";
    private static final String PASSWORD_2 = "12345";

    private WebTestClient webTestClient;
    private String cookie;

    @Autowired
    public LoginControllerTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @BeforeEach
    void setUp() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("userName", USER_NAME_1)
                        .with("email", EMAIL_1)
                        .with("password", PASSWORD_1))
                .exchange();


        cookie = webTestClient.post().uri("login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("email", EMAIL_1)
                        .with("password", PASSWORD_1))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }

    @Test
    void 로그인되어_있을_때_메인_페이지에서_유저_이름이_뜨는지_확인() {
        webTestClient.get().uri("/").header("cookie", cookie)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(redirectResponse -> {
                    String body = new String(redirectResponse.getResponseBody());
                    assertTrue(body.contains(USER_NAME_1));
                });
    }

    @Test
    void 존재하지_않는_아이디로_로그인할_때_로그인_페이지로_리다이렉트하는지_확인() {
        webTestClient.post().uri("/login")
                .body(fromFormData("email", EMAIL_2)
                        .with("password", PASSWORD_1))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    String url = response.getResponseHeaders().get("Location").get(0);
                    assertTrue(url.contains(LOGIN_FAIL_PAGE));

                });

    }

    @Test
    void 로그인_패스워드_불일치할_때_로그인_페이지로_리다이렉트하는지_테스트() {
        webTestClient.post().uri("/login")
                .body(fromFormData("email", EMAIL_1)
                        .with("password", PASSWORD_2))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    String url = response.getResponseHeaders().get("Location").get(0);
                    assertThat(url.contains(LOGIN_FAIL_PAGE)).isTrue();
                });
    }

    @AfterEach
    void tearDown() {
        webTestClient.delete().uri("/users").header("Cookie", cookie)
                .exchange();
    }
}
