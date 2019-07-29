package techcourse.myblog.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerTest {
    public static final String TITLE = "title";
    public static final String CONTENTS = "contents";
    public static final String COVER_URL = "cover_url";
    private static final String USER_NAME_1 = "name";
    private static final String EMAIL_1 = "test@test.com";
    private static final String PASSWORD_1 = "1234";
    private static final Logger log = LoggerFactory.getLogger(CommentControllerTest.class);

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    private String cookie;

//    @Autowired
//    public CommentControllerTest(WebTestClient webTestClient) {
//        this.webTestClient = webTestClient;
//    }

    @BeforeEach
    void setUp() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("userName", USER_NAME_1)
                        .with("email", EMAIL_1)
                        .with("password", PASSWORD_1))
                .exchange();

        log.info("DDDDDDDDD >> " + userRepository.findByEmail(EMAIL_1).orElseThrow(() -> new IllegalArgumentException("not found")).toString());

        cookie = webTestClient.post().uri("login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("email", EMAIL_1)
                        .with("password", PASSWORD_1))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");

        webTestClient.post().uri("/articles/write")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("title", TITLE)
                        .with("contents", CONTENTS)
                        .with("coverUrl", COVER_URL))
                .exchange();

        log.info("DDDDDDDDD >> " + articleRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("not found")).toString());

    }

    @Test
    void 댓글_작성_성공했는지_확인() {
        webTestClient.post().uri("/articles/1/comments").header("cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("contents", "댓글 내용"))
                .exchange().expectStatus().isFound()
                .expectBody()
                .consumeWith((response -> {
                    String url = response.getResponseHeaders().get("Location").get(0);
                    webTestClient.get().uri(url)
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .consumeWith(redirectResponse -> {
                                String body = new String(redirectResponse.getResponseBody());
                                assertThat(body.contains("댓글 내용")).isTrue();
                            });
                }));
    }

    @Test
    void 댓글_삭제_성공_확인() {
        webTestClient.post().uri("/articles/1/comments")
                .header("cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("contents", "댓글 내용"))
                .exchange();

        webTestClient.delete().uri("/articles/1/comments/1")
                .header("cookie", cookie)
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith((response -> {
                    String url = response.getResponseHeaders().get("Location").get(0);
                    webTestClient.get().uri(url)
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .consumeWith(redirectResponse -> {
                                String body = new String(redirectResponse.getResponseBody());
                                assertThat(body.contains("댓글 내용")).isFalse();
                            });
                }));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        articleRepository.deleteAll();

    }
}