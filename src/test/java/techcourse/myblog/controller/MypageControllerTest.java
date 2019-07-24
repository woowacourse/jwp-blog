package techcourse.myblog.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MypageControllerTest {
    private static final String USER_NAME_1 = "test1";
    private static final String USER_NAME_2 = "test2";
    private static final String EMAIL_1 = "test1@test.com";
    private static final String PASSWORD_1 = "1234567";
    private static final String PASSWORD_2 = "12345";

    private WebTestClient webTestClient;
    private String cookie;

    @Autowired
    public MypageControllerTest(WebTestClient webTestClient) {
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
    void 수정했을_때_response가_수정한_내용을_헤더에_포함시키는지_확인() {
        webTestClient.post().uri("/mypage")
                .body(fromFormData("userName", USER_NAME_2)
                        .with("email", EMAIL_1)
                        .with("password", PASSWORD_2))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    String url = response.getResponseHeaders().get("Location").get(0);
                    webTestClient.get().uri(url)
                            .exchange()
                            .expectBody()
                            .consumeWith(redirectResponse -> {

                                String body = new String(redirectResponse.getResponseBody());
                                assertThat(body.contains(USER_NAME_2)).isTrue();
                            });
                });
    }

    @AfterEach
    void tearDown() {
        webTestClient.delete().uri("/users").header("Cookie", cookie)
                .exchange();
    }

}