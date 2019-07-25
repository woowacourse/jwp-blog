package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTests {
    @Autowired
    private WebTestClient webTestClient;
    private String name;
    private String email;
    private String password;

    @BeforeEach
    void setUp() {
        name = "done";
        email = "done@woowa.com";
        password = "12345678";

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password))
                .exchange();
    }

    @AfterEach
    void tearDown() {
        webTestClient.delete().uri("/mypage")
                .exchange();
    }

    @Test
    void 로그인_화면_이동_확인() {
        webTestClient.get().uri("/login")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 로그인_확인() {
        webTestClient.post().uri("/users/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*localhost:[0-9]+/.*");
    }

    @Test
    void 로그인_실패_확인_이메일실패() {
        webTestClient.post().uri("/users/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "done@gmail.com")
                        .with("password", password))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void 로그인_실패_확인_비밀번호_불일치() {
        webTestClient.post().uri("/users/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", "wrongPassword"))
                .exchange()
                .expectStatus().is4xxClientError();
    }
}
