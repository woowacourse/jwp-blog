package techcourse.myblog.interceptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.domain.User;
import techcourse.myblog.web.LoginUtil;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationInterceptorTest {
    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToWebHandler(exchange -> {
            String path = exchange.getRequest().getURI().getPath();
            if ("/users".equals(path) || "/users/mypage".equals(path) || "/users/mypage/edit".equals(path)) {
                return exchange.getSession()
                        .doOnNext(webSession ->
                                webSession.getAttributes().put(LoginUtil.USER_SESSION_KEY, new User("Martin", "martin@gmail.com", "Aa12345!")))
                        .then();
            }
            return null;
        }).build();
    }

    @Test
    void usersPage_AfterLogin() {
        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void myPage_AfterLogin() {
        webTestClient.get().uri("/users/mypage")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void editPage_AfterLogin() {
        webTestClient.get().uri("/users/mypage/edit")
                .exchange()
                .expectStatus()
                .isOk();
    }
}