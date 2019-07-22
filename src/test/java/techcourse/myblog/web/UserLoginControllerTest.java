package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application_test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserLoginControllerTest {
    private static final String name = "미스터코";
    private static final String email = "test@test.com";
    private static final String password = "123123123";

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToWebHandler(exchange -> exchange.getSession()
                .doOnNext(webSession ->
                        webSession.getAttributes().put("userId", 1L))
                .then()).build();
    }

    @Test
    public void 회원조회페이지_이동() {
        webTestClient.get()
                .uri("/users")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 유저페이지() {
        webTestClient.get()
                .uri("/users/1/mypage")
                .exchange().expectStatus().isOk();
    }

}
