package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginInterceptorConfigTest {

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