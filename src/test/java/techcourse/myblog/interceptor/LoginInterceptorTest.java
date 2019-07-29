package techcourse.myblog.interceptor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class LoginInterceptorTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void login_없이_mypage_접근() {
        webTestClient.get().uri("/mypage")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/login");
    }

    @Test
    void login_없이_mypage_하위_경로_접근() {
        webTestClient.get().uri("/mypage/edit")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/login");
    }

    @Test
    void login_없이_articles_하위_경로_접근() {
        webTestClient.get().uri("/articles/1/edit")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/login");
    }
}