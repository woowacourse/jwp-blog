package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.user.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class LoginControllerTest {

    @Autowired
    WebTestClient webTestClient;

    void setSession() {
        webTestClient = WebTestClient.bindToWebHandler(exchange -> {
            String path = exchange.getRequest().getURI().getPath();
            if ("/users/mypage".equals(path) || "/users/mypage/edit".equals(path) || "/login".equals(path)) {
                return exchange.getSession()
                        .doOnNext(webSession ->
                                webSession.getAttributes().put("user", new User("buddy", "buddyCU@buddy.com", "Aa12345!")))
                        .then();
            }
            return null;
        }).build();
    }

    void removeSession() {
        webTestClient = WebTestClient.bindToWebHandler(exchange -> {
            String path = exchange.getRequest().getURI().getPath();
            if ("/users/mypage".equals(path) || "/users/mypage/edit".equals(path) || "/login".equals(path)) {
                return exchange.getSession()
                        .doOnNext(webSession ->
                                webSession.getAttributes().remove("user"))
                        .then();
            }
            return null;
        }).build();
    }


    @Test
    void 로그인_페이지_접근() {
        webTestClient.get().uri("/login").exchange()
                .expectStatus().isOk();
    }

    @Test
    void 로그인_페이지_접근2() {
        setSession();
        webTestClient.get().uri("/login").exchange()
                .expectStatus().isOk();
        removeSession();
    }

    @Test
    void 로그아웃_페이지_접근() {
        webTestClient.get().uri("/logout")
                .exchange()
                .expectStatus()
                .isFound();
    }

    @Test
    void 로그인_없는_이메일() {
        create_user("Buddy", "buddy@buddy.com", "Aa12345!");

        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "buddy@buddy.com")
                        .with("password", "exception-password"))
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void 로그인_패스워드_불일치() {
        create_user("ssosso", "ssosso@email.com", "Aa12345!");

        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "ssosso@gmail.com")
                        .with("password", "exception-password"))
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    private void create_user(String userName, String email, String password) {
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", userName)
                        .with("email", email)
                        .with("password", password)
                        .with("confirmPassword", password)
                ).exchange();
    }


}
