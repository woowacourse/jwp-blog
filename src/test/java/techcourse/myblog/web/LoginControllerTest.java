package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTest {
    private static final String CUSTOM_USER_ID = "1";
    private static final String NAME = "유효한이름";
    private static final String EMAIL = "valid@email.com";
    private static final String PASSWORD = "ValidPassword!123";

    private BodyInserters.FormInserter<String> validUserData;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        validUserData = getBodyInserters(NAME, EMAIL, PASSWORD);
    }

    @Test
    public void 로그인_페이지_이동테스트() {
        webTestClient.get().uri("/login")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 로그인_테스트() {
        // given
        postSignup(validUserData);

        // when
        webTestClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(getBodyInserters(EMAIL, PASSWORD))
                .exchange()
                // then
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/;jsessionid=([0-9A-Z])+");
    }

    @Test
    public void 로그인_상태에서_로그인_페이지로_이동하는_경우_예외처리() {
        // given
        postSignup(validUserData);
        String cookie = getCookie();

        // when
        webTestClient.get().uri("/login")
                .header("Cookie", cookie)
                .exchange()
                // then
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/");
    }

    @Test
    public void 로그인_상태에서_로그아웃_테스트() {
        // given
        postSignup(validUserData);
        String cookie = getCookie();

        // when
        webTestClient.get().uri("/logout")
                .header("Cookie", cookie)
                .exchange()
                // then
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/");
    }

    @Test
    public void 로그아웃_상태에서_로그아웃하는_경우_예외처리() {
        // when
        webTestClient.get().uri("/logout")
                .exchange()
                // then
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/.*");
    }

    private String getCookie() {
        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(validUserData)
                .exchange()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");

    }

    private void postSignup(BodyInserters.FormInserter<String> userData) {
        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(userData)
                .exchange();
    }

    private BodyInserters.FormInserter<String> getBodyInserters(String email, String password) {
        return BodyInserters.fromFormData("email", email)
                .with("password", password);
    }

    private BodyInserters.FormInserter<String> getBodyInserters(String name, String email, String password) {
        return BodyInserters.fromFormData("userId", CUSTOM_USER_ID)
                .with("name", name)
                .with("email", email)
                .with("password", password);
    }
}
