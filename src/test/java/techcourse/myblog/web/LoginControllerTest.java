package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class LoginControllerTest {

    @Autowired
    WebTestClient webTestClient;

    void setSession() {
        webTestClient = WebTestClient.bindToWebHandler(exchange -> {
            String path = exchange.getRequest().getURI().getPath();
            if ("/users/mypage".equals(path) || "/users/mypage/edit".equals(path)) {
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
            if ("/users/mypage".equals(path) || "/users/mypage/edit".equals(path)) {
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
    void 로그아웃_페이지_접근() {
        webTestClient.get().uri("/logout")
                .exchange()
                .expectStatus()
                .isFound();
    }

    @Test
    void 로그인_없는_이메일() {
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", "Buddy")
                        .with("email", "buddy@buddy.com")
                        .with("password", "Aa12345!")
                        .with("confirmPassword", "Aa12345!")
                ).exchange();

        webTestClient.post().uri("/login/check")
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
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", "ssosso")
                        .with("email", "ssosso@gmail.com")
                        .with("password", "Aa12345!")
                        .with("confirmPassword", "Aa12345!")
                ).exchange();

        webTestClient.post().uri("/login/check")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "ssosso@gmail.com")
                        .with("password", "exception-password"))
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void 로그인_전_마이페이지_접근() {
        webTestClient.get().uri("/users/mypage").exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void 로그인_후_마이페이지_접근() {
        setSession();
        webTestClient.get().uri("/users/mypage").exchange()
                .expectStatus().isOk();
        removeSession();
    }

    @Test
    void 로그인_전_회원수정페이지_접근() {
        webTestClient.get().uri("/users/mypage/edit").exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void 로그인_후_회원수정페이지_접근() {
        setSession();
        webTestClient.get().uri("/users/mypage/edit").exchange()
                .expectStatus().isOk();
        removeSession();
    }

}
