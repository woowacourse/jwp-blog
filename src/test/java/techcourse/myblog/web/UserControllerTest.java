package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.web.dto.UserRequestDto;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void duplicate_email_alert() {
        postUser(UserRequestDto.of("john", "abcde@example.com", "p@ssW0rd", "p@ssW0rd"),
            postResponse -> {
                postUser(UserRequestDto.of("kim", "abcde@example.com", "p@ssW0rd123", "p@ssW0rd123"),
                    anotherPostResponse -> {
                        String body = new String(anotherPostResponse.getResponseBody());
                        assertThat(body).contains("이미 등록된 이메일입니다.");
                    });
            });
    }

    private void postUser(UserRequestDto user, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.post()
            .uri("/users")
            .body(BodyInserters
                .fromFormData("name", user.getName())
                .with("email", user.getEmail())
                .with("password", user.getPassword())
                .with("passwordConfirm", user.getPasswordConfirm()))
            .exchange()
            .expectBody()
            .consumeWith(consumer);
    }

    @Test
    void user_list_view() {
        UserRequestDto userRequestDto = UserRequestDto.of("john", "test_user_list_view@example.com", "p@ssW0rd", "p@ssW0rd");
        postUser(userRequestDto, postResponse -> {
            webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(postResponse2 -> {
                assertThat(new String(postResponse2.getResponseBody()))
                    .contains(userRequestDto.getName())
                    .contains(userRequestDto.getEmail());
            });
        });
    }

    @Test
    void login_logout() {
        UserRequestDto user = UserRequestDto.of("john", "login_logout_test@example.com", "p@ssW0rd", "p@ssW0rd");
        postUser(user, postUserResponse -> {
            String sid = getSessionString(postUserResponse);

            postLogin(user, sid, loginResponse -> {
                getRedirection(loginResponse, loginRedirectResponse -> {
                    assertThat(new String(loginRedirectResponse.getResponseBody()))
                        .contains(user.getName());
                    webTestClient.get().uri("/logout" + sid)
                        .exchange()
                        .expectStatus().is3xxRedirection()
                        .expectBody()
                        .consumeWith(logoutResponse -> {
                            getRedirection(logoutResponse, logoutRedirectResponse -> {
                                assertThat(new String(logoutRedirectResponse.getResponseBody()))
                                    .doesNotContain(user.getName())
                                    .contains("Welcome Brown!");
                            });
                        });
                });
            });
        });
    }

    private void postLogin(UserRequestDto user, String sid, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.post()
            .uri("/login" + (sid != null ? sid : ""))
            .body(BodyInserters
                .fromFormData("email", user.getEmail())
                .with("password", user.getPassword()))
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectBody()
            .consumeWith(consumer);
    }

    private void getRedirection(EntityExchangeResult<byte[]> response, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.get()
            .uri(response.getResponseHeaders().getFirst("Location"))
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .consumeWith(consumer);
    }

    @Test
    void mypage() {
        UserRequestDto user = UserRequestDto.of("john", "mypage_test@example.com", "p@ssW0rd", "p@ssW0rd");
        postUser(user, postUserResponse -> {
            String sid = getSessionString(postUserResponse);

            postLogin(user, sid, loginResponse -> {
                getMypage(sid, mypageResponse -> {
                    assertThat(new String(mypageResponse.getResponseBody()))
                        .contains(user.getEmail())
                        .contains(user.getName());
                });
            });
        });
    }

    private void getMypage(String sid, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.get()
            .uri("/mypage" + (sid != null ? sid : ""))
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .consumeWith(consumer);
    }

    @Test
    void mypage_put() {
        UserRequestDto user = UserRequestDto.of("john", "user_put_test@example.com", "p@ssW0rd", "p@ssW0rd");
        postUser(user, postUserResponse -> {
            String sid = getSessionString(postUserResponse);

            postLogin(user, sid, loginResponse -> {
                webTestClient.put().uri("/users" + sid)
                    .body(BodyInserters.fromFormData("name", "park"))
                    .exchange()
                    .expectStatus().is3xxRedirection()
                    .expectBody()
                    .consumeWith(mypagePutResponse -> {
                        getMypage(sid, mypageResponse -> {
                            assertThat(new String(mypageResponse.getResponseBody()))
                                .contains("park")
                                .contains(user.getEmail());
                        });
                    });
            });
        });
    }

    /**
     * Creates string for holding session.
     *
     * @param postUserResponse The first response of the request in the test
     * @return ex. response -> ;jsessionid=ED8F611EA32E4D7FB5CE60A3C1E0F54A
     */
    private String getSessionString(EntityExchangeResult<byte[]> postUserResponse) {
        return ";jsessionid=" + postUserResponse.getResponseCookies().getFirst("JSESSIONID").getValue();
    }
}
