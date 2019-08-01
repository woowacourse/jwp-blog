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
    private static final String JSESSIONID = "JSESSIONID";
    private static final String EMAIL = "email";
    private static final String NAME = "name";
    private static final String PASSWORD = "password";
    private static final String TEST_EMAIL = "sean@gmail.com";
    private static final String TEST_NAME = "sean";
    private static final String TEST_PASSWORD = "Woowahan123!";
    private static final String LOCATION = "Location";
    private static final String UPDATE_URL = "/users/update/";
    private static final String DELETE_URL = "/users/delete/";
    private static final String LOGIN_URL = "/login";
    private static final String USERS_URL = "/users";
    private static final String JOIN_URL = "/signup";
    private static final String MY_PAGES_URL = "/mypages/";
    private static final String UPDATE_PATTERN = ".*/mypages/";
    private static final String DELETE_PATTERN = ".*/.*";
    private static final String JOIN_PATTERN = ".*/login.*";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    private long userId;

    @BeforeEach
    private void 회원가입_성공() {
        webTestClient.post().uri(USERS_URL)
                .body(fromFormData(NAME, TEST_NAME)
                        .with(EMAIL, TEST_EMAIL)
                        .with(PASSWORD, TEST_PASSWORD))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches(HttpHeaders.LOCATION, JOIN_PATTERN);

        userId = userRepository.findUserByEmail(TEST_EMAIL).getId();
    }

    @Test
    void 회원가입_페이지_이동() {
        webTestClient.get().uri(JOIN_URL)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void userList_페이지_이동() {
        getExchange(USERS_URL).isOk()
                .expectBody().consumeWith(response -> {
            String body = new String(Objects.requireNonNull(response.getResponseBody()));
            checkUserInfo(body);
        });
    }

    @Test
    void mypage_이동() {
        getExchange(MY_PAGES_URL + userId).isOk()
                .expectBody().consumeWith(response -> {
            String body = new String(Objects.requireNonNull(response.getResponseBody()));
            checkUserInfo(body);
        });
    }

    @Test
    void mypage_edit_이동() {
        getExchange(UPDATE_URL + userId).isOk()
                .expectBody().consumeWith(response -> {
            String body = new String(Objects.requireNonNull(response.getResponseBody()));
            checkUserInfo(body);
        });
    }

    @Test
    void mypage_업데이트() {
        webTestClient.put().uri(UPDATE_URL + userId)
                .cookie(JSESSIONID, getResponseCookie().getValue())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData(NAME, TEST_NAME))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches(LOCATION, UPDATE_PATTERN + userId);
    }

    @AfterEach
    void 계정_삭제() {
        webTestClient.delete().uri(DELETE_URL + userId)
                .cookie(JSESSIONID, getResponseCookie().getValue())
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches(LOCATION, DELETE_PATTERN);
    }

    private ResponseCookie getResponseCookie() {
        return webTestClient.post().uri(LOGIN_URL)
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
