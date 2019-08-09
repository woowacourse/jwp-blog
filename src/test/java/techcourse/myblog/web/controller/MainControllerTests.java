package techcourse.myblog.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainControllerTests extends AuthedWebTestClient {
    private static final String TEST_EMAIL = "sean@gmail.com";
    private static final String TEST_NAME = "sean";
    private static final String TEST_PASSWORD = "Woowahan123!";
    private static final String WRITING_URL = "/writing";
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
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches(LOCATION, URL_PATTERN);
    }

    @Test
    void 로그인후_게시글_작성_페이지_이동시_성공() {
        userRepository.save(new User(TEST_NAME, TEST_EMAIL, TEST_PASSWORD));

        get(WRITING_URL, TEST_EMAIL, TEST_PASSWORD).isOk();

        userRepository.deleteAll();
    }
}
