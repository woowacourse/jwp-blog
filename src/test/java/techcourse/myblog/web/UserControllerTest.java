package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    private String email = "buddy@gmail.com";
    private String userName = "Buddy";
    private String password = "Aa12345!";

    @Autowired
    WebTestClient webTestClient;

    @Test
    void 회원가입_페이지() {
        webTestClient.get().uri("/users/signup").exchange().expectStatus().isOk();
    }

    @Test
    void 유저_생성() {
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", "Brown")
                        .with("email", "brown@gmail.com")
                        .with("password", password)
                        .with("confirmPassword", password)
                ).exchange()
                .expectStatus().isFound();
    }

    @Test
    void 중복_이메일_확인() {
        create_user(userName, "buddy@buddy.com", password);

        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", userName)
                        .with("email", "buddy@buddy.com")
                        .with("password", password)
                        .with("confirmPassword", password))
                .exchange().expectStatus().isBadRequest()
                .expectBody().consumeWith(res -> {
            String body = new String(res.getResponseBody());
            assertThat(body.contains("중복된 이메일 입니다.")).isTrue();
        });
    }

    @Test
    void 유저_이름_확인() {
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", "J")
                        .with("email", email)
                        .with("password", password)
                        .with("confirmPassword", password)
                ).exchange()
                .expectStatus().isBadRequest()
                .expectBody().consumeWith(res -> {
            String body = new String(res.getResponseBody());
            assertThat(body.contains("형식에 맞는 이름이 아닙니다.")).isTrue();
        });
    }

    @Test
    void 유저_패스워드_확인() {
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", userName)
                        .with("email", email)
                        .with("password", "A")
                        .with("confirmPassword", "A")
                ).exchange()
                .expectBody().consumeWith(res -> {
            String body = new String(res.getResponseBody());
            assertThat(body.contains("형식에 맞는 비밀번호가 아닙니다.")).isTrue();
        });
    }

    @Test
    void 유저_패스워드_컨펌패스워드_매치_확인() {
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", userName)
                        .with("email", email)
                        .with("password", password)
                        .with("confirmPassword", "Ss12345!")
                ).exchange()
                .expectStatus().isBadRequest()
                .expectBody().consumeWith(res -> {
            String body = new String(res.getResponseBody());
            assertThat(body.contains("비밀번호가 일치하지 않습니다.")).isTrue();
        });
    }

    @Test
    void 유점_리스트_확인() {
        create_user("Martin", "martin@gmail.com", password);

        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody().consumeWith(res -> {
            String body = new String(res.getResponseBody());
            assertThat(body.contains("Martin")).isTrue();
            assertThat(body.contains("martin@gmail.com")).isTrue();
        });
    }

    //TODO 세션문제
    @Test
    void 유저정보_수정_페이지() {
        webTestClient.get().uri("/users/mypage/edit")
                .exchange()
                .expectStatus()
                .isOk();
    }

    void create_user(String userName, String email, String password) {
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", userName)
                        .with("email", email)
                        .with("password", password)
                        .with("confirmPassword", password)
                ).exchange().expectStatus().isFound();
    }

}
