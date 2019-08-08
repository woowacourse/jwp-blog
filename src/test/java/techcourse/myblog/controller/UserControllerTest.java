package techcourse.myblog.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.dto.UserRequest;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerTest extends AbstractControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void 회원_가입_페이지를_확인한다() {
        webTestClient.get().uri("/users/new")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 로그인_페이지를_확인한다() {
        webTestClient.get().uri("/login")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 회원_가입이_잘_되는지_확인한다() {
        UserRequest userDto = new UserRequest("pobi", "pobi@pobi.com", "@Password12", "@Password12");

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", userDto.getName())
                        .with("email", userDto.getEmail())
                        .with("password", userDto.getPassword())
                        .with("passwordConfirm", userDto.getPassword()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/login.*");
    }

    @Test
    public void 로그인_전에_회원_목록_페이지_접근을_시도하면_로그인_페이지로_리다이렉트한다() {
        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/login.*");
    }

    @Test
    public void 로그인_후에_회원_목록_페이지에_접근한다() {
        String jSessionId = extractJSessionId(login(user1));
        webTestClient.get().uri("/users")
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(Objects.requireNonNull(res.getResponseBody()));
                    assertThat(body.contains(user1.getName())).isTrue();
                    assertThat(body.contains(user1.getEmail())).isTrue();
                });
    }

    @Test
    public void 로그인_전에_마이페이지_접근을_시도하면_로그인_페이지로_리다이렉트한다() {
        webTestClient.get().uri("/mypage")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/login.*");
    }

    @Test
    public void 로그인_후에_마이페이지에_접근한다() {
        String jSessionId = extractJSessionId(login(user1));
        webTestClient.get().uri("/mypage")
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(Objects.requireNonNull(res.getResponseBody()));
                    assertThat(body.contains(user1.getName())).isTrue();
                    assertThat(body.contains(user1.getEmail())).isTrue();
                });
    }
}