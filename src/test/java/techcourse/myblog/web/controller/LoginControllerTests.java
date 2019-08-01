package techcourse.myblog.web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseCookie;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTests {
    private static final String JSESSIONID = "JSESSIONID";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String TEST_EMAIL = "sean@gmail.com";
    private static final String TEST_NAME = "sean";
    private static final String TEST_PASSWORD = "Woowahan123!";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_URL = "/logout";
    private static final String LOCATION = "Location";
    private static final String URL_PATTERN = ".*/.*";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(new User(TEST_NAME, TEST_EMAIL, TEST_PASSWORD));
    }

    @Test
    void 로그인_페이지_이동() {
        webTestClient.get().uri(LOGIN_URL)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 로그인_성공() {
        webTestClient.post().uri(LOGIN_URL)
                .body(fromFormData(EMAIL, TEST_EMAIL)
                        .with(PASSWORD, TEST_PASSWORD))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches(LOCATION, URL_PATTERN);
    }

    @Test
    void 로그아웃() {
        webTestClient.get().uri(LOGOUT_URL)
                .cookie(JSESSIONID, getResponseCookie().getValue())
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches(LOCATION, URL_PATTERN);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    private ResponseCookie getResponseCookie() {
        return webTestClient.post().uri(LOGIN_URL)
                .body(fromFormData(EMAIL, TEST_EMAIL)
                        .with(PASSWORD, TEST_PASSWORD))
                .exchange()
                .expectStatus().is3xxRedirection()
                .returnResult(ResponseCookie.class).getResponseCookies().getFirst(JSESSIONID);
    }
}
