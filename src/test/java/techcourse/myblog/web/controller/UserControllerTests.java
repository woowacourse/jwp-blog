package techcourse.myblog.web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.domain.repository.UserRepository;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests extends AuthedWebTestClient {
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
        get(USERS_URL, TEST_EMAIL, TEST_PASSWORD)
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(Objects.requireNonNull(response.getResponseBody()));
                    checkUserInfo(body);
                });
    }

    @Test
    void mypage_이동() {
        get(MY_PAGES_URL + userId, TEST_EMAIL, TEST_PASSWORD).isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(Objects.requireNonNull(response.getResponseBody()));
                    checkUserInfo(body);
                });
    }

    @Test
    void mypage_edit_이동() {
        get(UPDATE_URL + userId, TEST_EMAIL, TEST_PASSWORD)
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(Objects.requireNonNull(response.getResponseBody()));
                    checkUserInfo(body);
                });
    }

    @Test
    void mypage_업데이트() {
        webTestClient.put().uri(UPDATE_URL + userId)
                .cookie(JSESSIONID, getResponseCookie(TEST_EMAIL, TEST_PASSWORD).getValue())
                .body(fromFormData(NAME, TEST_NAME))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches(LOCATION, UPDATE_PATTERN + userId);
    }

    @AfterEach
    void 계정_삭제() {
        delete(DELETE_URL + userId, TEST_EMAIL, TEST_PASSWORD).is3xxRedirection()
                .expectHeader().valueMatches(LOCATION, DELETE_PATTERN);
    }

    private void checkUserInfo(String body) {
        assertThat(body.contains(TEST_NAME)).isTrue();
        assertThat(body.contains(TEST_EMAIL)).isTrue();
    }
}
