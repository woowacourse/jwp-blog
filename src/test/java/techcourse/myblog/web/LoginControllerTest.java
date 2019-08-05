package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LoginControllerTest extends AuthedWebTestClient {
    private static final List<String> LOGIN_KEYS = Arrays.asList("email", "password");
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void 로그인_성공_테스트() {
        webTestClient.post().uri("/login")
                .body(params(LOGIN_KEYS, "test@test.com", "A!1bcdefg"))
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader().valueMatches("Location", "^.+\\/.+$");
    }

    @Test
    void 이메일_없음_테스트() {
        webTestClient.post().uri("/login")
                .body(params(Arrays.asList("email", "password"), "xxx@gmail.com", "A!1bcdefg"))
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains("등록된 이메일이 없습니다.")).isTrue();
                });
    }

    @Test
    void 비밀번호_틀림_테스트() {
        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params(Arrays.asList("email", "password"), "test@test.com", "B!1bcdefg"))
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains("비밀번호가 일치하지 않습니다.")).isTrue();
                });
    }
}