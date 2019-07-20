package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.dto.LoginDto;
import techcourse.myblog.dto.UserDto;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginControllerTest {
    private UserDto userDto;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setName("test");
        userDto.setEmail("test@test.com");
        userDto.setPassword("PassW0rd@");
        userDto.setPasswordConfirm("PassW0rd@");
    }

    @Test
    @DisplayName("로그인에 성공한다.")
    void loginTest() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(userDto.getEmail());
        loginDto.setPassword(userDto.getPassword());

        postUser(userDto, postUserResponse -> {
            String sessionId = getSessionId(postUserResponse);

            postLogin(loginDto, sessionId, postLoginResponse ->
                    getRedirect(sessionId, getLoginRedirect ->
                            assertThat(new String(getLoginRedirect.getResponseBody())).contains("test")));
        });
    }

    @Test
    @DisplayName("로그인 후 로그아웃을 하면 메인페이지로 이동한다.")
    void logoutTest() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@test.com");
        loginDto.setPassword("PassW0rd@");

        postUser(userDto, postUserResponse -> {
            String sessionId = getSessionId(postUserResponse);

            postLogin(loginDto, sessionId, postLoginResponse -> webTestClient.get()
                    .uri("/logout" + sessionId)
                    .exchange()
                    .expectStatus().is3xxRedirection()
                    .expectBody()
                    .consumeWith(logout ->
                            getRedirect(sessionId, index ->
                                    assertThat(new String(index.getResponseBody())).doesNotContain("test"))));
        });
    }

    private void postUser(UserDto userDto, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.post()
                .uri("/users")
                .body(BodyInserters.fromFormData("name", userDto.getName())
                        .with("email", userDto.getEmail())
                        .with("password", userDto.getPassword())
                        .with("passwordConfirm", userDto.getPasswordConfirm()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(consumer);
    }

    private void postLogin(LoginDto loginDto, String sessionId, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.post()
                .uri("/login" + sessionId)
                .body(BodyInserters
                        .fromFormData("email", loginDto.getEmail())
                        .with("password", loginDto.getPassword()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(consumer);
    }

    private void getRedirect(String sessionId, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.get()
                .uri("/" + sessionId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(consumer);
    }

    private static String getSessionId(EntityExchangeResult<byte[]> postUserResponse) {
        return ";jsessionid=" + postUserResponse.getResponseCookies().getFirst("JSESSIONID").getValue();
    }
}