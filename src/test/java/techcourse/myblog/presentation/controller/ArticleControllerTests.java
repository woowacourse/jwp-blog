package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests extends ControllerTests {
    private static final String EMAIL = "hard@gmail.com";
    private static final String NAME = "hard";
    private static final String PASSWORD = "qwerasdf";
    private static final String TITLE = "title";
    private static final String CONTENTS = "contents";
    private static final String COVER_URL = "coverUrl";

    WebTestClient webTestClient;

    @Autowired
    public ArticleControllerTests(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @BeforeEach
    void setUp() {
        registerUser(NAME, EMAIL, PASSWORD);
    }

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void writeForm_test() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void save_test() {
        webTestClient.post().uri("/articles")
                .header("Cookie", logInAndGetSessionId(EMAIL, PASSWORD))
                .body(BodyInserters.fromFormData("title", TITLE)
                        .with("coverUrl", COVER_URL)
                        .with("contents", CONTENTS))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", "http://localhost:" + portNo + "/articles/" + countArticle());
    }

    @Test
    void update_test() {
        String sessionId = logInAndGetSessionId(EMAIL, PASSWORD);
        Long articleId = createArticle(TITLE, COVER_URL, CONTENTS, sessionId);
        webTestClient.put().uri("/articles/" + articleId)
                .header("Cookie", sessionId)
                .body(BodyInserters.fromFormData("title", TITLE + "123")
                        .with("coverUrl", COVER_URL + "123")
                        .with("contents", CONTENTS + "123"))
                .exchange()
                .expectHeader().valueMatches("location", "(http://localhost:" + portNo + "/articles/" + articleId + ")(.*)")
                .expectStatus().isFound();
    }

    @Test
    void delete_test() {
        String sessionId = logInAndGetSessionId(EMAIL, PASSWORD);
        Long articleId = createArticle(TITLE, COVER_URL, CONTENTS, sessionId);
        webTestClient.delete().uri("/articles/" + articleId)
                .header("Cookie", sessionId)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", "http://localhost:" + portNo + "/");
    }

    @AfterEach
    void tearDown() {
        String sessionId = logInAndGetSessionId(EMAIL, PASSWORD);
        deleteUser(sessionId);
    }
}
