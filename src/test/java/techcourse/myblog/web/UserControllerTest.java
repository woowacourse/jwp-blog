package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.User;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private static final String JSESSIONID = "JSESSIONID";
    private static final String REGEX_SEMI_COLON = ";";
    private static final String REGEX_EQUAL = "=";

    private static final long USER_ID = 0L;
    private static final String NAME = "코니";
    private static final String PASSWORD = "@Password12";

    private User user;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void 회원_가입_페이지를_확인한다() {
        webTestClient.get().uri("/signup")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 로그인_페이지를_확인한다() {
        webTestClient.get().uri("/login")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 회원_가입이_잘_되는지_확인한다() {
        user = new User(USER_ID, NAME, "cony@cony.com", PASSWORD);

        webTestClient.post()
                .uri("/users/new")
                .body(BodyInserters
                        .fromFormData("name", user.getName())
                        .with("email", user.getEmail())
                        .with("password", user.getPassword())
                        .with("passwordConfirm", user.getPassword()))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    public void 로그인이_잘_되는지_확인한다() {
        user = new User(USER_ID, NAME, "bony@cony.com", PASSWORD);

        webTestClient.post()
                .uri("/users/new")
                .body(BodyInserters
                        .fromFormData("name", user.getName())
                        .with("email", user.getEmail())
                        .with("password", user.getPassword())
                        .with("passwordConfirm", user.getPassword()))
                .exchange()
                .expectStatus().is3xxRedirection();

        webTestClient.post()
                .uri("/login")
                .body(BodyInserters
                        .fromFormData("email", user.getEmail())
                        .with("password", user.getPassword()))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    public void 로그아웃이_잘_되는지_확인한다() {
        user = new User(USER_ID, NAME, "sony@cony.com", PASSWORD);
        String jSessionId = getJSessionId(user);

        webTestClient.get()
                .uri("/logout")
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    public void 회원_목록_페이지를_확인한다() {
        user = new User(USER_ID, NAME, "kony@cony.com", PASSWORD);
        String jSessionId = getJSessionId(user);

        webTestClient.get()
                .uri("/users")
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(user.getName())).isTrue();
                    assertThat(body.contains(user.getEmail())).isTrue();
                });
    }

    @Test
    public void 마이페이지를_확인한다() {
        user = new User(USER_ID, NAME, "pony@cony.com", PASSWORD);
        String jSessionId = getJSessionId(user);

        webTestClient.get()
                .uri("/mypage")
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(user.getName())).isTrue();
                    assertThat(body.contains(user.getEmail())).isTrue();
                });
    }

    @Test
    public void 회원정보_수정이_잘_되는지_확인한다() {
        user = new User(USER_ID, NAME, "dony@cony.com", PASSWORD);
        String jSessionId = getJSessionId(user);

        webTestClient.put()
                .uri("/mypage/edit")
                .cookie(JSESSIONID, jSessionId)
                .body(BodyInserters
                        .fromFormData("name", "new코니")
                        .with("email", user.getEmail())
                        .with("password", user.getPassword()))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    private String getJSessionId(User user) {
        EntityExchangeResult<byte[]> loginResult = getLoginResult(user);

        String[] cookies = loginResult.getResponseHeaders().get("Set-Cookie").stream()
                .filter(it -> it.contains(JSESSIONID))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(JSESSIONID + "가 없습니다."))
                .split(REGEX_SEMI_COLON);

        return Stream.of(cookies)
                .filter(it -> it.contains(JSESSIONID))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(JSESSIONID + "가 없습니다."))
                .split(REGEX_EQUAL)[1];
    }

    private EntityExchangeResult<byte[]> getLoginResult(User user) {
        webTestClient.post()
                .uri("/users/new")
                .body(BodyInserters
                        .fromFormData("name", user.getName())
                        .with("email", user.getEmail())
                        .with("password", user.getPassword())
                        .with("passwordConfirm", user.getPassword()))
                .exchange()
                .expectStatus().is3xxRedirection();

        return webTestClient.post()
                .uri("/login")
                .body(BodyInserters
                        .fromFormData("email", user.getEmail())
                        .with("password", user.getPassword()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .returnResult();
    }
}