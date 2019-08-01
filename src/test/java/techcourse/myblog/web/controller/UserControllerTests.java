package techcourse.myblog.web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.domain.repository.UserRepository;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    private static final String JSESSIONID = "JSESSIONID";
    private static final String EMAIL = "email";
    private static final String NAME = "name";
    private static final String PASSWORD = "password";
    private static final String TEST_EMAIL = "sean@gmail.com";
    private static final String TEST_NAME = "sean";
    private static final String TEST_PASSWORD = "Woowahan123!";
    private static final String LOCATION = "Location";

    private long userId;

    @BeforeEach
    private void 회원가입_성공() {
        webTestClient.post().uri("/users")
                .body(fromFormData(NAME, TEST_NAME)
                        .with(EMAIL, TEST_EMAIL)
                        .with(PASSWORD, TEST_PASSWORD))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches(HttpHeaders.LOCATION, ".*/login.*");

        userId = userRepository.findUserByEmail(TEST_EMAIL).getId();
    }

    @Test
    void 회원가입_페이지_이동() {
        webTestClient.get().uri("/signup")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void userList_페이지_이동() {
        getExchange("users").isOk()
                .expectBody().consumeWith(response -> {
            String body = new String(Objects.requireNonNull(response.getResponseBody()));
            checkUserInfo(body);
        });
    }

    @Test
    void mypage_이동() {
        getExchange("/mypages/" + userId).isOk()
                .expectBody().consumeWith(response -> {
            String body = new String(Objects.requireNonNull(response.getResponseBody()));
            checkUserInfo(body);
        });
    }

    @Test
    void mypage_edit_이동() {
        getExchange("/users/update/" + userId).isOk()
                .expectBody().consumeWith(response -> {
            String body = new String(Objects.requireNonNull(response.getResponseBody()));
            checkUserInfo(body);
        });
    }

    @Test
    void mypage_업데이트() {
        webTestClient.put().uri("/users/update/" + userId)
                .cookie(JSESSIONID, getResponseCookie().getValue())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData(NAME, TEST_NAME))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches(LOCATION, ".*/mypage/" + userId);
    }

    @AfterEach
    void 계정_삭제() {
        webTestClient.delete().uri("/users/delete/" + userId)
                .cookie(JSESSIONID, getResponseCookie().getValue())
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches(LOCATION, ".*/.*");
    }

    private ResponseCookie getResponseCookie() {
        return webTestClient.post().uri("/login")
                .body(fromFormData(EMAIL, TEST_EMAIL)
                        .with(PASSWORD, TEST_PASSWORD))
                .exchange()
                .expectStatus().is3xxRedirection()
                .returnResult(ResponseCookie.class).getResponseCookies().getFirst(JSESSIONID);
    }

    private StatusAssertions getExchange(String users) {
        return webTestClient.get().uri(users)
                .cookie(JSESSIONID, getResponseCookie().getValue())
                .exchange()
                .expectStatus();
    }

    private void checkUserInfo(String body) {
        assertThat(body.contains(TEST_NAME)).isTrue();
        assertThat(body.contains(TEST_EMAIL)).isTrue();
    }
}
