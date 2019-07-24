package techcourse.myblog.interceptor.login;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginInterceptorTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void 로그인_없이_접근불가능() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".+/login");
    }

    @Test
    void 로그인_없이_접근가능() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }
}