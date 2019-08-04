package techcourse.myblog.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainControllerTests {
    private static final String JSESSIONID = "JSESSIONID";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String TEST_EMAIL = "sean@gmail.com";
    private static final String TEST_NAME = "sean";
    private static final String TEST_PASSWORD = "Woowahan123!";
    private static final String WRITING_URL = "/writing";
    private static final String LOGIN_URL = "/login";
    private static final String LOCATION = "Location";
    private static final String URL_PATTERN = ".*/login";
    private static final String DEFAULT_URL = "/";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @Test
    void index_페이지_조회() {
        webTestClient.get().uri(DEFAULT_URL)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 로그인_하지않고_게시글_작성_페이지_이동시_리다이렉트() {
        webTestClient.get().uri(WRITING_URL)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches(LOCATION, URL_PATTERN);
    }

    @Test
    void 로그인후_게시글_작성_페이지_이동시_성공() {
        userRepository.save(new User(TEST_NAME, TEST_EMAIL, TEST_PASSWORD));

        webTestClient.get().uri(WRITING_URL)
                .cookie(JSESSIONID, getResponseCookie().getValue())
                .exchange()
                .expectStatus().isOk();

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
