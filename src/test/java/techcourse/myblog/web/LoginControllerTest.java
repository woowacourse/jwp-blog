package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.repository.UserRepository;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LoginControllerTest extends AuthedWebTestClient {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(new User("andole", "A!1bcdefg", "andole@gmail.com"));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }


    @Test
    void 로그인_성공_테스트() {
        webTestClient.post().uri("/login")
                .body(BodyInserters.fromFormData("email", "andole@gmail.com")
                        .with("password", "A!1bcdefg"))
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
                .body(params(Arrays.asList("email", "password"), "andole@gmail.com", "B!1bcdefg"))
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains("비밀번호가 일치하지 않습니다.")).isTrue();
                });
    }
}