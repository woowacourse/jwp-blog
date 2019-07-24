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

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private static final String SESSION_ID_KEY = "JSESSIONID";

    @Autowired
    private WebTestClient webTestClient;

    private static final UserRequestDto DEFAULT_USER =
        UserRequestDto.of("John", "john@example.com", "p@ssW0rd", "p@ssW0rd");


    @Test
    void duplicate_email_alert() {
        // Given
        UserRequestDto duplicateUser = UserRequestDto.of("john", DEFAULT_USER.getEmail(), "p@ssW0rd23", "p@ssW0rd23");

        // When
        EntityExchangeResult<byte[]> response = postUserSync(duplicateUser);

        // Then
        assertThat(new String(response.getResponseBody()))
            .contains(UserRequestDto.EMAIL_UNIQUE_MESSAGE);
    }

    private EntityExchangeResult<byte[]> postUserSync(UserRequestDto user) {
        return webTestClient.post()
            .uri("/users")
            .body(BodyInserters
                .fromFormData("name", user.getName())
                .with("email", user.getEmail())
                .with("password", user.getPassword())
                .with("passwordConfirm", user.getPasswordConfirm()))
            .exchange()
            .expectBody()
            .returnResult();
    }

    @Test
    void user_list_view() {
        // When
        EntityExchangeResult<byte[]> response = simpleGetRequestSync("/users", null);

        // Then
        assertThat(new String(response.getResponseBody()))
            .contains(DEFAULT_USER.getName())
            .contains(DEFAULT_USER.getEmail());
    }

    @Test
    void login_logout() {
        // Given
        String sid = getSessionId(postLoginSync(DEFAULT_USER, null));

        // When
        simpleGetRequestSync("/logout", sid);

        // Then
        EntityExchangeResult<byte[]> indexResponse = simpleGetRequestSync("/", sid);
        assertThat(new String(indexResponse.getResponseBody()))
            .doesNotContain(DEFAULT_USER.getName())
            .contains("Welcome Brown!");
    }

    private EntityExchangeResult<byte[]> postLoginSync(UserRequestDto user, String sid) {
        WebTestClient.RequestBodySpec req = webTestClient.post().uri("/login");
        if (sid != null) {
            req = req.cookie(SESSION_ID_KEY, sid);
        }
        return req.body(BodyInserters
            .fromFormData("email", user.getEmail())
            .with("password", user.getPassword()))
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectBody()
            .returnResult();
    }

    private EntityExchangeResult<byte[]> simpleGetRequestSync(String uri, String sid) {
        WebTestClient.RequestHeadersSpec req = webTestClient.get().uri(uri);
        if (sid != null) {
            req = req.cookie(SESSION_ID_KEY, sid);
        }
        return req
            .exchange()
            .expectBody()
            .returnResult();
    }

    @Test
    void mypage() {
        // Given
        String sid = getSessionId(postLoginSync(DEFAULT_USER, null));

        // When
        EntityExchangeResult<byte[]> response = simpleGetRequestSync("/mypage", sid);

        // Then
        assertThat(new String(response.getResponseBody()))
            .contains(DEFAULT_USER.getEmail())
            .contains(DEFAULT_USER.getName());
    }

    @Test
    void mypage_put() {
        // Given
        String sid = getSessionId(postLoginSync(DEFAULT_USER, null));

        // When
        webTestClient.put().uri("/users")
            .cookie(SESSION_ID_KEY, sid)
            .body(BodyInserters.fromFormData("name", "park"))
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectBody()
            .returnResult();

        // Then
        EntityExchangeResult<byte[]> mypageResponse = simpleGetRequestSync("/mypage", sid);
        assertThat(new String(mypageResponse.getResponseBody()))
            .contains("park")
            .contains(DEFAULT_USER.getEmail());
    }

    private String getSessionId(EntityExchangeResult<byte[]> response) {
        return response.getResponseCookies().getFirst(SESSION_ID_KEY).getValue();
    }
}
