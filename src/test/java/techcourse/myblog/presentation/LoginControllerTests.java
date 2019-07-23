package techcourse.myblog.presentation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.service.dto.UserRequestDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;
import static techcourse.myblog.presentation.LoginController.NOT_EXIST_EMAIL_ERROR;
import static techcourse.myblog.presentation.LoginController.NOT_EXIST_PASSWORD_ERROR;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    private final UserRequestDto userRequestDto = new UserRequestDto();

    @BeforeEach
    void setUp() {
        userRequestDto.setName("sloth");
        userRequestDto.setEmail("sooreal@gmail.com");
        userRequestDto.setPassword("Woowahan123!");

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", userRequestDto.getName())
                        .with("email", userRequestDto.getEmail())
                        .with("password", userRequestDto.getPassword()))
                .exchange();
    }

    @Test
    void 로그인_테스트() {
        postRequestWithLoginBody("/login", userRequestDto.getEmail(), userRequestDto.getPassword())
                .is3xxRedirection()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");
    }

    @Test
    void 이메일_입력_오류_로그인_실패_테스트() {
        postRequestWithLoginBody("/login", "WrongEmail", userRequestDto.getPassword())
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String responseBody = new String(response.getResponseBody());
                    assertThat(responseBody.contains(NOT_EXIST_EMAIL_ERROR)).isTrue();
                });
    }

    @Test
    void 비밀번호_입력_오류_로그인_실패_테스트() {
        postRequestWithLoginBody("/login", userRequestDto.getEmail(), "WrongPassword")
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String responseBody = new String(response.getResponseBody());
                    assertThat(responseBody.contains(NOT_EXIST_PASSWORD_ERROR)).isTrue();
                });
    }

    @Test
    void 로그아웃_테스트() {
        webTestClient.get().uri("/logout")
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }

    @Test
    StatusAssertions postRequestWithLoginBody(String uri, String email, String password) {
        return webTestClient.post().uri(uri)
                .body(fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .expectStatus();
    }
}
