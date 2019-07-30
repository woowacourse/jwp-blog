package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private static final String USERNAME = "user";
    private static final String EMAIL = "user@mail.net";
    private static final String PASSWORD = "aSdF12#$";
    private static final String JSESSIONID = "JSESSIONID";
    private static final String SET_COOKIE = "Set-Cookie";
    private static final String REGEX_SEMI_COLON = ";";
    private static final String REGEX_EQUAL = "=";

    private String jSessionId;

    @Autowired
    private WebTestClient webTestClient;

    EntityExchangeResult<byte[]> login(final String email, final String password) {
        return webTestClient
                .post().uri("/login")
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .expectBody()
                .returnResult();
    }

    EntityExchangeResult<byte[]> signUp(final String username, final String email, final String password) {
        return webTestClient
                .post().uri("/users")
                .body(BodyInserters
                        .fromFormData("username", username)
                        .with("email", email)
                        .with("password", password))
                .exchange()
                .expectBody()
                .returnResult();
    }

    String extractJSessionId(final EntityExchangeResult<byte[]> loginResult) {
        final String[] cookies = loginResult.getResponseHeaders().get(SET_COOKIE).stream()
                .filter(it -> it.contains(JSESSIONID))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(JSESSIONID + "가 없습니다."))
                .split(REGEX_SEMI_COLON);
        return Stream.of(cookies)
                .filter(it -> it.contains(JSESSIONID))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(JSESSIONID + "가 없습니다."))
                .split(REGEX_EQUAL)[1]
                .trim();
    }

    @Test
    void 로그인() {
        final EntityExchangeResult<byte[]> response = login(EMAIL, PASSWORD);
        HttpStatus status = response.getStatus();
        jSessionId = extractJSessionId(response);
        assertThat(status).isEqualTo(HttpStatus.FOUND);
        assertThat(jSessionId).isNotBlank();
    }

    @Test
    void 없는_이메일로_로그인_시도() {
        assertThat(login("asdf@a.com", PASSWORD).getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void 잘못된_패스워드로_로그인_시도() {
        assertThat(login(EMAIL, "qwerty").getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void 로그아웃() {
        final EntityExchangeResult<byte[]> loginResult = login(EMAIL, PASSWORD);
        final EntityExchangeResult<byte[]> logoutResult = webTestClient
                .get().uri("/logout")
                .exchange().expectBody().returnResult();
        assertThat(extractJSessionId(loginResult))
                .isNotEqualTo(extractJSessionId(logoutResult));
    }

    @Test
    void 회원가입_삼고초려_1() {
        // 너무 짧은 이름
        final EntityExchangeResult<byte[]> signupTry = signUp("a", "a@a.com", PASSWORD);
        assertThat(signupTry.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void 회원가입_삼고초려_2() {
        // 패스워드 규칙 위반
        final EntityExchangeResult<byte[]> signupTry = signUp("abc", "a@a.com", "1234");
        assertThat(signupTry.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void 회원가입_삼고초려_3() {
        // 중복 이메일
        final EntityExchangeResult<byte[]> signupTry = signUp(USERNAME, EMAIL, PASSWORD);
        assertThat(signupTry.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void 회원가입_삼고초려_드디어_가입_성공() {
        // 가입 성공
        final EntityExchangeResult<byte[]> signupTry = signUp(USERNAME, "a@a.com", PASSWORD);
        assertThat(signupTry.getStatus()).isEqualTo(HttpStatus.FOUND);
    }
}

