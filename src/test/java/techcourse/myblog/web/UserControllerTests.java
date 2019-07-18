package techcourse.myblog.web;

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
public class UserControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "email@gmail.com")
                        .with("password", "password1234!")
                        .with("name", "name"))
                .exchange();
    }

    @Test
    void 회원가입_페이지_이동_테스트() {
        webTestClient.get().uri("/signup")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 로그인_페이지_이동_테스트() {
        webTestClient.get().uri("/login")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 회원가입_요청_성공_테스트() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "newEmail@gmail.com")
                        .with("password", "newPassword1234!")
                        .with("name", "newName"))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", "(.)*(/login)(.)*");
    }

    @Test
    void 회원가입_요청_이메일_중복_실패_테스트() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "email@gmail.com")
                        .with("password", "password1234!")
                        .with("name", "name"))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", "(.)*(/signup)(.)*");
    }

    @Test
    void 회원가입_요청_이름_형식_실패_테스트() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "email@gmail.com")
                        .with("password", "password")
                        .with("name", "a"))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", "(.)*(/signup)(.)*");
    }

    @Test
    void 회원가입_요청_패스워드_실패_테스트() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "email@gmail.com")
                        .with("password", "password")
                        .with("name", "name"))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", "(.)*(/signup)(.)*");
    }

    @Test
    void 회원가입_요청_이메일_형식_실패_테스트() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "email")
                        .with("password", "password1234!")
                        .with("name", "name"))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", "(.)*(/signup)(.)*");
    }

    @Test
    void 회원_정보_전체_조회_테스트() {
        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk();
    }
}
