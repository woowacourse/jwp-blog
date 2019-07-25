package techcourse.myblog.web;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.AbstractTest;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class LoginControllerTest extends AbstractTest {
    private String cookie;

    @BeforeEach
    void setUp() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "email@gmail.com")
                        .with("password", "password1234!")
                        .with("name", "name"))
                .exchange();

        cookie = webTestClient.post().uri("/login")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters
                .fromFormData("email", "email@gmail.com")
                .with("password", "password1234!"))
            .exchange()
            .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }

    @Test
    void 로그인_페이지_이동_테스트() {
        webTestClient.get().uri("/login")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 로그인_후_로그인_페이지_이동_테스트() {
        webTestClient.get().uri("/login")
            .header("Cookie", cookie)
            .exchange()
            .expectStatus().isFound().expectHeader().valueMatches("location", "(.)*/");
    }

    @Test
    void 로그인_요청_성공_테스트() {
        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "email@gmail.com")
                        .with("password", "password1234!"))
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    void 로그인_후_로그인_요청_테스트() {
        webTestClient.post().uri("/login")
            .header("Cookie", cookie)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters
                .fromFormData("email", "email@gmail.com")
                .with("password", "password1234!"))
            .exchange()
            .expectStatus().isFound().expectHeader().valueMatches("location", "(.)*/");
    }

    @Test
    void 로그인_요청_존재하지_않는_이메일_테스트() {
        String noneEmail = "none@gamil.com";

        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", noneEmail)
                        .with("password", "password1234!"))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void 로그인_요청_패스워드_불일치_테스트() {
        String wrongPassword = "wrongPassword";

        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "email@gmail.com")
                        .with("password", wrongPassword))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void 로그아웃_성공_테스트() {
        webTestClient.get().uri("/logout")
            .header("Cookie", cookie)
            .exchange()
            .expectStatus().isFound();
    }

    @Test
    void 비로그인_로그아웃_테스트() {
        webTestClient.get().uri("/logout")
            .exchange()
            .expectStatus().isFound().expectHeader().valueMatches("location", "(.)*/");
    }
}
