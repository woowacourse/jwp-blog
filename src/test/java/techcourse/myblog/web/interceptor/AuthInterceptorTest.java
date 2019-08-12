package techcourse.myblog.web.interceptor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.User;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthInterceptorTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void 로그인후_userlist_페이지_접근() {
        login();
        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 로그인하지_않고_userlist_페이지_접근_Redirect() {
        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus()
                .isFound()
                .expectHeader()
                .valueMatches("location", ".*/login")
        ;
    }

    @Test
    void 로그인후_mypage_페이지_접근() {
        login();
        webTestClient.get().uri("/mypage")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 로그인하지_않고_mypage_페이지_접근_Redirect() {
        webTestClient.get().uri("/mypage")
                .exchange()
                .expectStatus()
                .isFound()
                .expectHeader()
                .valueMatches("location", ".*/login")
        ;
    }

    @Test
    void 로그인후_mypage_edit_페이지_접근() {
        login();
        webTestClient.get().uri("/mypage-edit")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 로그인하지_않고_mypage_edit_페이지_접근_Redirect() {
        webTestClient.get().uri("/mypage-edit")
                .exchange()
                .expectStatus()
                .isFound()
                .expectHeader()
                .valueMatches("location", ".*/login")
        ;
    }

    @Test
    void 로그인하지_않고_댓글_작성_Redirect() {
        webTestClient.post().uri("/articles/1/comments")
                .exchange()
                .expectStatus()
                .isFound()
                .expectHeader()
                .valueMatches("location", ".*/login");
    }

    @Test
    void 로그인_후_댓글_작성() {
        login();
        webTestClient.post().uri("/articles/1/comments")
                .body(BodyInserters.fromFormData("contents", "contents"))
                .exchange()
                .expectStatus()
                .isOk()
        ;
    }

    private void login() {
        webTestClient = WebTestClient.bindToWebHandler(exchange ->
                exchange.getSession()
                        .doOnNext(webSession -> webSession.getAttributes()
                                .put("user", new User("CODEMCD",
                                        "iloveCU@gmail.com", "PassWord!1")))
                        .then()).build();
    }
}
