package techcourse.myblog.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.util.MultiValueMap;
import techcourse.myblog.MyblogApplicationTests;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
public class LoginControllerTest extends MyblogApplicationTests {

    @Test
    @DisplayName("로그인 페이지 접근")
    void showLoginPage() {
        getRequestExpectStatus(HttpMethod.GET, "/login")
                .isOk();
    }

    @Test
    @DisplayName("로그인 잘되는 지 테스트")
    void Login() {
        MultiValueMap<String, String> map = getCustomUserDtoMap(USER_NAME, USER_EMAIL, USER_PASSWORD, USER_ID);
        Consumer<EntityExchangeResult<byte[]>> entityExchangeResultConsumer = (response) -> {
            String redirectUrl = response.getResponseHeaders().getLocation().toString();
            assertThat(redirectUrl.contains("/")).isTrue();
        };
        getResponseSpec(HttpMethod.POST, "/login", map).exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(entityExchangeResultConsumer);
    }

    @Test
    @DisplayName("로그인 상태 일 때 로그인 페이지로 못가게 하기")
    void donot_excess_loginPage() {

        String cookie = getLoginCookie(USER_EMAIL, USER_PASSWORD);
        getRequestWithCookieExpectStatus(HttpMethod.GET, "/login", cookie)
                .isFound();
        Consumer<EntityExchangeResult<byte[]>> entityExchangeResultConsumer = (response -> {
            assertThat(response.getResponseHeaders().getLocation().toString().contains("login")).isFalse();
        });
        getRequestWithCookieExpectStatus(HttpMethod.GET, "/login", cookie)
                .isFound()
                .expectBody()
                .consumeWith(entityExchangeResultConsumer);
    }

    @Test
    @DisplayName("아이디 불일치로 로그인 실패")
    void login_fail_mismatch_email() {

        MultiValueMap<String, String> map = getCustomUserDtoMap(USER_NAME, "kangmin789@naver.com", USER_PASSWORD, 1);
        Consumer<EntityExchangeResult<byte[]>> entityExchangeResultConsumer = (response) -> {
            String body = response.getResponseBody().toString();
            assertThat(body.contains(LOGIN_ERROR_MESSAGE));
        };
        getResponseSpec(HttpMethod.POST, "/login", map);
        getRequestExpectStatus(HttpMethod.POST, "/login").isOk()
                .expectBody()
                .consumeWith(entityExchangeResultConsumer);
    }

    @Test
    @DisplayName("비밀번호 불일치로 로그인 실패")
    void login_fail_mismatch_password() {
        MultiValueMap<String, String> map = getCustomUserDtoMap(USER_NAME, USER_EMAIL, "asdASD12!@#", 1);
        Consumer<EntityExchangeResult<byte[]>> entityExchangeResultConsumer = (response) -> {
            String body = response.getResponseBody().toString();
            assertThat(body.contains(LOGIN_ERROR_MESSAGE));
        };
        getResponseSpec(HttpMethod.POST, "/login", map);
        getRequestExpectStatus(HttpMethod.POST, "/login").isOk()
                .expectBody()
                .consumeWith(entityExchangeResultConsumer);
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout_sucess() {
        String cookie = getLoginCookie(USER_EMAIL, USER_PASSWORD);
        Consumer<EntityExchangeResult<byte[]>> entityExchangeResultConsumer = (response) -> {
            String redirectUrl = response.getResponseHeaders().getLocation().getPath();
            assertThat(redirectUrl.contains("/")).isTrue();
        };

        getRequestWithCookieExpectStatus(HttpMethod.GET, "/logout", cookie)
                .isFound()
                .expectBody()
                .consumeWith(entityExchangeResultConsumer);
    }

    @Test
    @DisplayName("로그인 안했을 떄 로그아웃 실패")
    void 로그아웃_실패_로그인_안했을_경우() {
        Consumer<EntityExchangeResult<byte[]>> entityExchangeResultConsumer = (response) -> {
            String redirectUrl = response.getResponseHeaders().getLocation().getPath();
            assertThat(redirectUrl.contains("/login")).isTrue();
        };

        getRequestExpectStatus(HttpMethod.GET, "/logout")
                .isFound()
                .expectBody()
                .consumeWith(entityExchangeResultConsumer);
    }
}