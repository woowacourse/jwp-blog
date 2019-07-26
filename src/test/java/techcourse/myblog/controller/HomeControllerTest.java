package techcourse.myblog.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomeControllerTest {
    private static final String LOGIN_TEXT = "Login";

    @Autowired
    WebTestClient webTestClient;

    @Test
    void 로그인_하지_않았을때_초기_페이지() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = getResponseBody(response.getResponseBody());
                    assertThat(body.contains(LOGIN_TEXT)).isTrue();
                });
    }

    private String getResponseBody(byte[] responseBody) {
        return new String(responseBody, StandardCharsets.UTF_8);
    }

}
