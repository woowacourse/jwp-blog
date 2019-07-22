package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.dto.LoginDto;
import techcourse.myblog.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginControllerTest extends ControllerTest {
    private UserDto userDto;
    private LoginDto loginDto;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        loginDto = new LoginDto();

        userDto.setName("test");
        userDto.setEmail("test@test.com");
        userDto.setPassword("PassW0rd@");
        userDto.setPasswordConfirm("PassW0rd@");

        loginDto.setEmail("test@test.com");
        loginDto.setPassword("PassW0rd@");
    }

    @Test
    @DisplayName("로그인에 성공한다.")
    void loginTest() {
        postUser(webTestClient, userDto, postUserResponse -> {
            String sessionId = getSessionId(postUserResponse);
            postLogin(webTestClient, loginDto, sessionId, postLoginResponse -> {
                getIndexView(webTestClient, sessionId, index ->
                        assertThat(new String(index.getResponseBody())).contains("test"));
            });
        });
    }

    @Test
    @DisplayName("로그인 후 로그아웃을 하면 메인페이지로 이동한다.")
    void logoutTest() {
        userDto.setName("logout");
        userDto.setEmail("logout@test.com");
        loginDto.setEmail("logout@test.com");

        postUser(webTestClient, userDto, postUserResponse -> {
            String sessionId = getSessionId(postUserResponse);
            postLogin(webTestClient, loginDto, sessionId, postLoginResponse -> {
                webTestClient.get()
                        .uri("/logout" + sessionId)
                        .exchange()
                        .expectStatus().is3xxRedirection()
                        .expectBody()
                        .consumeWith(logout ->
                                getIndexView(webTestClient, sessionId, index ->
                                        assertThat(new String(index.getResponseBody())).doesNotContain("logout")));
            });
        });
    }
}