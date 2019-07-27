package techcourse.myblog.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.controller.dto.UserDto;
import techcourse.myblog.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginControllerTest {
    private static final String USER_NAME = "test";
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "passWord!1";
    private static final String WRONG_EMAIL = "test2@test.com";
    private static final String WRODN__PASSWORD = "passWord!2";

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("로그인 폼을 보여준다.")
    void loginForm() {
        webTestClient.get().uri("/login")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("로그인을 성공하면 메인페이지를 보여준다.")
    void successLogin() {
        Utils.createUser(webTestClient, new UserDto(USER_NAME, EMAIL, PASSWORD));

        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("email", EMAIL)
                        .with("password", PASSWORD))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    URI location = response.getResponseHeaders().getLocation();
                    webTestClient.get().uri(location)
                            .exchange()
                            .expectBody()
                            .consumeWith(redirectResponse -> {
                                String body = Utils.getResponseBody(redirectResponse.getResponseBody());
                                assertThat(body.contains(USER_NAME)).isTrue();
                            });
                });
    }

    @Test
    @DisplayName("email이 없는 경우 로그인에 실패한다.")
    void failLoginWhenWrongEmailInput() {

    }

    @Test
    @DisplayName("비밀번호가 틀린 경우 로그인에 실패한다.")
    void failLoginWhenWrongPassword() {

    }

    @AfterEach
    void tearDown() {

    }
}