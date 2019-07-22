package techcourse.myblog.interceptor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.users.UserDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginInterceptorTest {

    @Autowired
    private WebTestClient webTestClient;

    private void createSession() {
        webTestClient = WebTestClient.bindToWebHandler(exchange -> exchange.getSession()
                .doOnNext(webSession ->
                        webSession.getAttributes().put("user", new UserDto.Response(1L, "asd", "asd")))
                .then()).build();
    }


    @Test
    void 로그인_안하고_users_접근시_Redirection() {
        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader()
                .valueMatches("location", ".*/users/login");
    }

    @Test
    void 로그인후_users_접근() {
        createSession();

        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 로그인후_mypage_페이지_접근() {
        createSession();

        webTestClient.get().uri("/users/{id}", 1)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 로그인후_mypage_edit__접근() {
        createSession();

        webTestClient.get().uri("/users/{id}/edit", 1)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 로그인_안하고_mypage_edit_접근() {

        webTestClient.get().uri("/users/{id}/edit", 1)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader()
                .valueMatches("location", ".*/users/login");
    }
}