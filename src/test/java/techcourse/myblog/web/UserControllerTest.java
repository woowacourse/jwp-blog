package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;


@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @Autowired
    UserRepository userRepository;

    @Test
    void show_sign_up() {
        webTestClient.get().uri("/users/signup").exchange().expectStatus().isOk();
    }

    @Test
    void create_user() {
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", "Martin")
                        .with("email", "martin@gmail.com")
                        .with("password", "Aa12345!")
                        .with("confirmPassword", "Aa12345!")
                ).exchange()
                .expectStatus().isFound();
    }

    @Test
    void show_login() {
        webTestClient.get().uri("/login").exchange().expectStatus().isOk();
    }

    @Test
    void users_withLogin() {
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", "Martin")
                        .with("email", "martin@gmail.com")
                        .with("password", "Aa12345!")
                        .with("confirmPassword", "Aa12345!")
                ).exchange()
                .expectStatus()
                .isFound();

        loginSession();
        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void users_withoutLogin() {
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", "Martin")
                        .with("email", "martin@gmail.com")
                        .with("password", "Aa12345!")
                        .with("confirmPassword", "Aa12345!")
                ).exchange()
                .expectStatus()
                .isFound();

        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus()
                .isFound();
    }

    @Test
    void check_same_email() {
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", "Martin")
                        .with("email", "martin@gmail.com")
                        .with("password", "Aa12345!")
                        .with("confirmPassword", "Aa12345!")
                ).exchange()
                .expectStatus().isFound();

        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", "Martin")
                        .with("email", "martin@gmail.com")
                        .with("password", "Aa12345!")
                        .with("confirmPassword", "Aa12345!")
                ).exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(res -> {
            String body = new String(res.getResponseBody());
            assertThat(body.contains("중복된 이메일 입니다.")).isTrue();
        });
    }

    @Test
    void check_valid_name() {
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", "M")
                        .with("email", "martin@gmail.com")
                        .with("password", "Aa12345!")
                        .with("confirmPassword", "Aa12345!")
                ).exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(res -> {
            String body = new String(res.getResponseBody());
            assertThat(body.contains("형식에 맞는 이름이 아닙니다.")).isTrue();
        });
    }

    @Test
    void check_valid_password() {
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", "Martin")
                        .with("email", "martin@gmail.com")
                        .with("password", "A")
                        .with("confirmPassword", "A")
                ).exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(res -> {
            String body = new String(res.getResponseBody());
            assertThat(body.contains("형식에 맞는 비밀번호가 아닙니다.")).isTrue();
        });
    }

    @Test
    void check_valid_confirm_password() {
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", "Martin")
                        .with("email", "martin@gmail.com")
                        .with("password", "Aa12345!")
                        .with("confirmPassword", "Ss12345!")
                ).exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(res -> {
            String body = new String(res.getResponseBody());
            assertThat(body.contains("비밀번호가 일치하지 않습니다.")).isTrue();
        });
    }

    @Test
    void showMyPage_withLogin() {
        loginSession();
        webTestClient.get().uri("/users/mypage")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void showMyPage_withoutLogin() {
        webTestClient.get().uri("/users/mypage")
                .exchange()
                .expectStatus()
                .isFound();
    }

    @Test
    void showUserEdit_withLogin() {
        loginSession();
        webTestClient.get().uri("/users/mypage/edit")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void showUserEdit_withoutLogin() {
        webTestClient.get().uri("/users/mypage/edit")
                .exchange()
                .expectStatus()
                .isFound();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    void loginSession() {
        webTestClient = WebTestClient.bindToWebHandler(exchange -> {
            String path = exchange.getRequest().getURI().getPath();
            if ("/users".equals(path) || "/users/mypage".equals(path) || "/users/mypage/edit".equals(path)) {
                return exchange.getSession()
                        .doOnNext(webSession ->
                                webSession.getAttributes().put("user", new User("Martin", "martin@gmail.com", "Aa12345!")))
                        .then();
            }
            return null;
        }).build();
    }
}
