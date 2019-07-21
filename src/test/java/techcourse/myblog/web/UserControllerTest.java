package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void 회원가입_페이지_접근_테스트() {
        webTestClient.get().uri("/auth/signup")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 회원_등록_테스트() {
        회원_등록().expectStatus()
                .is3xxRedirection();
    }

    private WebTestClient.ResponseSpec 회원_등록() {
        return webTestClient.post().uri("/auth/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(
                        BodyInserters.fromFormData("name", "pkch")
                                .with("email", "pkch@woowa.com")
                                .with("password", "qwerqwer")
                ).exchange();
    }

    @Test
    void 로그인_페이지_접근_테스트() {
        webTestClient.get().uri("/auth/login")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 로그인_안된_경우_회원_목록_페이지_접근_테스트() {
        webTestClient.get().uri("/auth/users")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(res -> {
                    URI redirectUri = res.getResponseHeaders().getLocation();
                    assertThat(redirectUri.getPath()).isEqualTo("/auth/login");
                });
    }

    @Test
    void 로그인_정상_흐름_테스트() {
        회원_등록().expectStatus().is3xxRedirection();
        webTestClient.post().uri("/auth/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(
                        BodyInserters.fromFormData("email", "pkch@woowa.com")
                                .with("password", "qwerqwer")
                ).exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(redirectRes -> {
                    URI redirectUri = redirectRes.getResponseHeaders().getLocation();
                    assertThat(redirectUri.getPath()).isEqualTo(URI.create("/"));
                });
    }
}