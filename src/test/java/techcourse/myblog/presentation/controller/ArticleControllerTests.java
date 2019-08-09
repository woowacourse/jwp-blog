package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

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
    void updateForm_test() {
        String sessionId = logInAndGetSessionId(EMAIL, PASSWORD);
        Long articleId = createArticle(TITLE, COVER_URL, CONTENTS, sessionId);
        webTestClient.get().uri("/articles/" + articleId + "/edit")
                .header("Cookie", sessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void updateForm_작성자가_아닐때_에러_test() {
        String sessionId = logInAndGetSessionId(EMAIL, PASSWORD);
        Long articleId = createArticle(TITLE, COVER_URL, CONTENTS, sessionId);
        registerUser(NAME + "a", EMAIL + "a", PASSWORD + "a");
        sessionId = logInAndGetSessionId(EMAIL + "a", PASSWORD + "a");
        webTestClient.get().uri("/articles/" + articleId + "/edit")
                .header("Cookie", sessionId)
                .exchange()
                .expectHeader().valueMatches("location", "(http://localhost:" + portNo + "/articles/" + articleId + ")(.*)")
                .expectStatus().isFound();
        deleteUser(sessionId);
    }

    @Test
    void updateForm_비로그인시_에러_test() {
        String sessionId = logInAndGetSessionId(EMAIL, PASSWORD);
        Long articleId = createArticle(TITLE, COVER_URL, CONTENTS, sessionId);
        webTestClient.get().uri("/articles/" + articleId + "/edit")
                .exchange()
                .expectHeader().valueMatches("location", "(http://localhost:" + portNo + "/login)")
                .expectStatus().isFound();
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
        webTestClient.get().uri("/articles/" + articleId)
                .header("Cookie", sessionId)
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body).contains(TITLE + "123");
            assertThat(body).contains(COVER_URL + "123");
            assertThat(body).contains(CONTENTS + "123");
        });
    }

    @Test
    void update_작성자가_아닐때_수정_에러_test() {
        String sessionId = logInAndGetSessionId(EMAIL, PASSWORD);
        Long articleId = createArticle(TITLE, COVER_URL, CONTENTS, sessionId);
        registerUser(NAME + "a", EMAIL + "a", PASSWORD + "a");
        sessionId = logInAndGetSessionId(EMAIL + "a", PASSWORD + "a");
        webTestClient.put().uri("/articles/" + articleId)
                .header("Cookie", sessionId)
                .body(BodyInserters.fromFormData("title", TITLE + "123")
                        .with("coverUrl", COVER_URL + "123")
                        .with("contents", CONTENTS + "123"))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", "(http://localhost:" + portNo + "/articles/" + articleId + ")(.*)");
        webTestClient.get().uri("/articles/" + articleId)
                .header("Cookie", sessionId)
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body).doesNotContain(TITLE + "123");
            assertThat(body).doesNotContain(COVER_URL + "123");
            assertThat(body).doesNotContain(CONTENTS + "123");
        });
        deleteUser(sessionId);
    }

    @Test
    void update_비로그인시_수정_에러_test() {
        String sessionId = logInAndGetSessionId(EMAIL, PASSWORD);
        Long articleId = createArticle(TITLE, COVER_URL, CONTENTS, sessionId);
        webTestClient.put().uri("/articles/" + articleId)
                .body(BodyInserters.fromFormData("title", TITLE + "123")
                        .with("coverUrl", COVER_URL + "123")
                        .with("contents", CONTENTS + "123"))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", "(http://localhost:" + portNo + "/articles/" + articleId + ")(.*)");
        webTestClient.get().uri("/articles/" + articleId)
                .header("Cookie", sessionId)
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body).doesNotContain(TITLE + "123");
            assertThat(body).doesNotContain(COVER_URL + "123");
            assertThat(body).doesNotContain(CONTENTS + "123");
        });
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

    @Test
    void delete_작성자가_아닐때_삭제_에러_test() {
        String sessionId = logInAndGetSessionId(EMAIL, PASSWORD);
        Long articleId = createArticle(TITLE, COVER_URL, CONTENTS, sessionId);
        registerUser(NAME + "a", EMAIL + "a", PASSWORD + "a");
        sessionId = logInAndGetSessionId(EMAIL + "a", PASSWORD + "a");
        webTestClient.delete().uri("/articles/" + articleId)
                .header("Cookie", sessionId)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", "(http://localhost:" + portNo + "/articles/" + articleId + ")(.*)");
        webTestClient.get().uri("/articles/" + articleId)
                .exchange()
                .expectStatus().isOk();
        deleteUser(sessionId);
    }

    @Test
    void delete_비로그인시_삭제_에러_test() {
        String sessionId = logInAndGetSessionId(EMAIL, PASSWORD);
        Long articleId = createArticle(TITLE, COVER_URL, CONTENTS, sessionId);
        webTestClient.delete().uri("/articles/" + articleId)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", "(http://localhost:" + portNo + "/articles/" + articleId + ")(.*)");
        webTestClient.get().uri("/articles/" + articleId)
                .exchange()
                .expectStatus().isOk();
    }

    @AfterEach
    void tearDown() {
        String sessionId = logInAndGetSessionId(EMAIL, PASSWORD);
        deleteUser(sessionId);
    }
}
