package techcourse.myblog.interceptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.users.UserDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginInterceptorTest {
    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToWebHandler(exchange -> {
            String path = exchange.getRequest().getURI().getPath();
            if ("/users".equals(path) || "/users/1".equals(path) || "/users/1/edit".equals(path)) {
                return exchange.getSession()
                        .doOnNext(webSession ->
                                webSession.getAttributes().put("user", new UserDto.Response(1L, "asd", "asd")))
                        .then();
            }
            return null;
        }).build();
    }

    @Test
    void 로그인후_userlist_페이지_접근() {
        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 로그인후_mypage_페이지_접근() {
        webTestClient.get().uri("/users/{id}", 1)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 로그인후_mypage_edit_페이지_접근() {
        webTestClient.get().uri("/users/{id}/edit", 1)
                .exchange()
                .expectStatus()
                .isOk();
    }
}