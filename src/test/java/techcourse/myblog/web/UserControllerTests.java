package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.web.ControllerTestUtil.KEY_JSESSIONID;
import static techcourse.myblog.web.ControllerTestUtil.postLoginSync;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {
    private static final String DEFAULT_USER_NAME = "john";
    private static final String DEFAULT_USER_EMAIL = "john123@example.com";
    private static final String DEFAULT_USER_PASSWORD = "p@ssW0rd";
    @Autowired
    private WebTestClient webTestClient;
    private String name;
    private String email;
    private String password;
    private String jSessionId;

    @BeforeEach
    void setUp() {
        name = "john";
        email = "john123@example.com";
        password = "p@ssW0rd";
        jSessionId = getJSessionId(email, password);
    }

    private String getJSessionId(String email, String password) {
        EntityExchangeResult<byte[]> loginResult = webTestClient.post().uri("/users/login")
            .body(BodyInserters
                .fromFormData("email", email)
                .with("password", password))
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectHeader().valueMatches("Location", ".*/.*")
            .expectBody()
            .returnResult();

        return extractJSessionId(loginResult);
    }

    private String extractJSessionId(EntityExchangeResult<byte[]> loginResult) {
        String[] cookies = loginResult.getResponseHeaders().get("Set-Cookie").stream()
            .filter(it -> it.contains("JSESSIONID"))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("JSESSIONID가 없습니다."))
            .split(";");
        return Stream.of(cookies)
            .filter(it -> it.contains("JSESSIONID"))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("JSESSIONID가 없습니다."))
            .split("=")[1];
    }

    @Test
    void 회원가입_화면_이동_확인() {
        webTestClient.get().uri("/signup")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void 회원목록_조회_확인() {
        webTestClient.get().uri("/users")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .consumeWith(response -> {
                String body = new String(Objects.requireNonNull(response.getResponseBody()));
                assertThat(body).contains(name);
                assertThat(body).contains(email);
            });
    }

    @Test
    void 회원목록_조회_확인2() {
        webTestClient.get().uri("/users")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .consumeWith(response -> {
                String body = new String(Objects.requireNonNull(response.getResponseBody()));
                assertThat(body).contains(DEFAULT_USER_NAME);
                assertThat(body).contains(DEFAULT_USER_EMAIL);
            });
    }

    @Test
    void 회원등록_확인() {
        webTestClient.post().uri("/users")
            .body(BodyInserters
                .fromFormData("name", "james")
                .with("email", "james123@gmail.com")
                .with("password", "p@ssW0rd"))
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectHeader().valueMatches("Location", ".*/.*")
            .expectBody()
            .returnResult();

        // Delete
        String sid = postLoginSync(webTestClient, "james123@gamil.com", "p@ssW0rd")
            .getResponseCookies().getFirst(KEY_JSESSIONID).getValue();
        webTestClient.delete().uri("/mypage")
            .cookie(KEY_JSESSIONID, sid)
            .exchange()
            .expectStatus().is3xxRedirection();
    }

    @Test
    void 로그아웃_확인() {
        String newJSessionId = getJSessionId(email, password);

        webTestClient.get().uri("/logout")
            .cookie("JSESSIONID", newJSessionId)
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectHeader().valueMatches("Location", ".*/");

        webTestClient.delete().uri("/mypage")
            .cookie("JSESSIONID", newJSessionId)
            .exchange();
    }

    @Test
    void 마이페이지_화면_이동_확인() {
        webTestClient.get().uri("/mypage")
            .cookie("JSESSIONID", jSessionId)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .consumeWith(response -> {
                String body = new String(Objects.requireNonNull(response.getResponseBody()));
                assertThat(body).contains(name);
                assertThat(body).contains(email);
            });
    }

    @Test
    void 회원탈퇴_확인() {
        webTestClient.post().uri("/users")
            .body(BodyInserters
                .fromFormData("name", "done")
                .with("email", "user_exit_test@gmail.com")
                .with("password", "p@ssW0rd"))
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectHeader().valueMatches("Location", ".*/.*")
            .expectBody()
            .returnResult();

        String sid = postLoginSync(webTestClient, "user_exit_test@gmail.com", "p@ssW0rd")
            .getResponseCookies().getFirst(KEY_JSESSIONID).getValue();

        webTestClient.delete().uri("/users")
            .cookie(KEY_JSESSIONID, sid)
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectHeader().valueMatches("Location", ".*/");
    }

    @Test
    void 마이페이지_수정화면_이동_확인() {
        webTestClient.get().uri("/mypage/mypage-edit")
            .cookie("JSESSIONID", jSessionId)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .consumeWith(response -> {
                String body = new String(Objects.requireNonNull(response.getResponseBody()));
                assertThat(body).contains(name);
                assertThat(body).contains(email);
            });
    }

    @Test
    void 마이페이지_수정_확인() {
        webTestClient.put().uri("/mypage/mypage-edit")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .cookie("JSESSIONID", jSessionId)
            .body(BodyInserters
                .fromFormData("name", "dowon"))
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectHeader().valueMatches("Location", ".*/mypage");
    }
}
