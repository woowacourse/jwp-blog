package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.User;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private static final long USER_ID = 0L;
    private static final String NAME = "코니";
    private static final String PASSWORD = "@Password12";

    private User user;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void 회원가입_페이지_테스트() {
        webTestClient.get().uri("/signup")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 로그인_페이지_테스트() {
        webTestClient.get().uri("/login")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 회원가입_테스트() {
        user = new User(USER_ID, NAME, "cony1@cony.com", PASSWORD);

        enrollUser(user, response -> {
            webTestClient.get()
                    .uri(response.getResponseHeaders().getLocation())
                    .exchange()
                    .expectStatus()
                    .isOk();
        });
    }

    @Test
    public void 로그인_테스트() {
        user = new User(USER_ID, NAME, "cony2@cony.com", PASSWORD);

        enrollUser(user, response -> {
            webTestClient.post()
                    .uri("/login")
                    .body(BodyInserters
                            .fromFormData("email", user.getEmail())
                            .with("password", user.getPassword()))
                    .exchange()
                    .expectStatus().is3xxRedirection();
        });
    }

    @Test
    public void 로그아웃_테스트() {
        user = new User(USER_ID, NAME, "cony3@cony.com", PASSWORD);

        enrollUser(user, response -> {
            webTestClient.post()
                    .uri("/login")
                    .body(BodyInserters
                            .fromFormData("email", user.getEmail())
                            .with("password", user.getPassword()))
                    .exchange()
                    .expectStatus().is3xxRedirection()
                    .expectBody()
                    .consumeWith(res -> {
                        webTestClient.get()
                                .uri("/logout")
                                .exchange()
                                .expectStatus().is3xxRedirection();
                    });
        });
    }

    @Test
    public void 회원조회_페이지_테스트() {
        user = new User(USER_ID, NAME, "cony4@cony.com", PASSWORD);

        enrollUser(user, response -> {
            webTestClient.get()
                    .uri("/users")
                    .header("Cookie", getCookie(user))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .consumeWith(res -> {
                        String body = new String(res.getResponseBody());
                        assertThat(body.contains(user.getName())).isTrue();
                        assertThat(body.contains(user.getEmail())).isTrue();
                    });
        });
    }

    @Test
    public void 마이페이지_테스트() {
        user = new User(USER_ID, NAME, "cony5@cony.com", PASSWORD);

        enrollUser(user, response -> {
            webTestClient.get()
                    .uri("/mypage")
                    .header("Cookie", getCookie(user))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .consumeWith(res -> {
                        String body = new String(res.getResponseBody());
                        assertThat(body.contains(user.getName())).isTrue();
                        assertThat(body.contains(user.getEmail())).isTrue();
                    });
        });
    }

    @Test
    public void 회원정보_수정_테스트() {
        user = new User(USER_ID, NAME, "cony6@cony.com", PASSWORD);

        enrollUser(user, response -> {
            webTestClient.put()
                    .uri("/mypage/edit")
                    .header("Cookie", getCookie(user))
                    .body(BodyInserters
                            .fromFormData("name", "새로운코니")
                            .with("email", user.getEmail())
                            .with("password", "@Password123"))
                    .exchange()
                    .expectStatus().is3xxRedirection();
        });
    }

    private void enrollUser(User user, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.post()
                .uri("/users")
                .body(BodyInserters
                        .fromFormData("name", user.getName())
                        .with("email", user.getEmail())
                        .with("password", user.getPassword()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(consumer);
    }

    private String getCookie(User user) {
        return webTestClient.post().uri("/login")
                .body(BodyInserters
                        .fromFormData("email", user.getEmail())
                        .with("password", user.getPassword()))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }
}