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

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {
    @Autowired
    private WebTestClient webTestClient;
    private String name;
    private String email;
    private String password;
    private String rePassword;

    @BeforeEach
    void setUp() {
        name = "done";
        email = "done@woowa.com";
        password = "123456789";
        rePassword = "123456789";
    }

    @Test
    void test1/*회원등록_확인*/() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password)
                        .with("rePassword", rePassword))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void test2/*회원조회_테스트*/() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password)
                        .with("rePassword", rePassword))
                .exchange()
                .expectStatus().is3xxRedirection();

        webTestClient.get()
                .uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains("done")).isTrue();
                    assertThat(body.contains("done@woowa.com")).isTrue();
                });
    }

    @Test
    void test3/*로그인_확인*/() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password)
                        .with("rePassword", rePassword))
                .exchange()
                .expectStatus().is3xxRedirection();

        webTestClient.post().uri("/users/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void test4/*로그인_실패_확인_이메일실패*/() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password)
                        .with("rePassword", rePassword))
                .exchange()
                .expectStatus().is3xxRedirection();

        webTestClient.post().uri("/users/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "JM@gmail.com")
                        .with("password", password))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/login");
    }

    @Test
    void test5/*로그인_실패_확인_비밀번호_불일치*/() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password)
                        .with("rePassword", rePassword))
                .exchange()
                .expectStatus().is3xxRedirection();

        webTestClient.post().uri("/users/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", "1234"))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/login");
    }

    @Test
    void test6/*로그아웃_확인*/() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password)
                        .with("rePassword", rePassword))
                .exchange()
                .expectStatus().is3xxRedirection();

        webTestClient.post().uri("/users/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .expectStatus().is3xxRedirection();

        webTestClient.get().uri("/logout")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/");
    }
}
