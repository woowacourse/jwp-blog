package techcourse.myblog.web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.ArticleRepository;
import techcourse.myblog.domain.repository.CommentRepository;
import techcourse.myblog.domain.repository.UserRepository;

import java.util.Objects;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTests {
    private static final String JSESSIONID = "JSESSIONID";
    private static final String URI_ARTICLES = "/articles";
    private static final String SEAN_NAME = "sean";
    private static final String SEAN_EMAIL = "sean@gmail.com";
    private static final String POBI_NAME = "pobi";
    private static final String POBI_EMAIL = "pobi@gmail.com";
    private static final String DEFAULT_PASSWORD = "Woowahan123!";
    private static final String TITLE = "title";
    private static final String CONTENT = "contents";
    private static final String COVER_URL = "coverUrl";
    private static final String DEFAULT_URL = "/";
    private static final String LOGIN_UTL = "/login";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String UPDATED_COMMENT = "updated_comment";
    private static final String ARTICLE_PATTERN = ".*articles/";

    private static int SEAN_ARTICLE_ID;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    private int commentId = 0;

    @BeforeEach
    void 게시글_댓글_작성() {
        userRepository.save(new User(SEAN_NAME, SEAN_EMAIL, DEFAULT_PASSWORD));
        userRepository.save(new User(POBI_NAME, POBI_EMAIL, DEFAULT_PASSWORD));

        webTestClient.post().uri(URI_ARTICLES)
                .cookie(JSESSIONID, getResponseCookie(SEAN_EMAIL, DEFAULT_PASSWORD).getValue())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData(TITLE, TITLE)
                        .with(COVER_URL, COVER_URL)
                        .with(CONTENT, CONTENT))
                .exchange()
                .expectStatus().isFound()
                .expectBody().consumeWith(response -> {
            String path = Objects.requireNonNull(response.getResponseHeaders().getLocation()).getPath();
            int index = path.lastIndexOf(DEFAULT_URL);
            SEAN_ARTICLE_ID = Integer.parseInt(path.substring(index + 1));
        });

        commentId++;
        addComments();
    }

    @Test
    void 댓글_수정() {
        getStatus(POBI_EMAIL, UPDATED_COMMENT)
                .expectStatus().isFound()
                .expectHeader().valueMatches(HttpHeaders.LOCATION, ARTICLE_PATTERN + SEAN_ARTICLE_ID);
    }

    @Test
    void 다른_사람이_댓글_삭제() {
        String deletePath = "/articles/" + SEAN_ARTICLE_ID + "/comments/" + commentId;

        webTestClient.delete().uri(deletePath)
                .cookie(JSESSIONID, getResponseCookie(SEAN_EMAIL, DEFAULT_PASSWORD).getValue())
                .exchange().expectStatus()
                .is3xxRedirection();
    }

    @Test
    void 다른_사람이_댓글_수정() {
        getStatus(SEAN_EMAIL, UPDATED_COMMENT)
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 댓글_삭제() {
        String deletePath = "/articles/" + SEAN_ARTICLE_ID + "/comments/" + commentId;

        webTestClient.delete().uri(deletePath)
                .cookie(JSESSIONID, getResponseCookie(POBI_EMAIL, DEFAULT_PASSWORD).getValue())
                .exchange().expectStatus()
                .isFound()
                .expectHeader().valueMatches(HttpHeaders.LOCATION, ARTICLE_PATTERN + SEAN_ARTICLE_ID);

    }

    @AfterEach
    void setUp() {
        commentRepository.deleteAll();
        articleRepository.deleteAll();
        userRepository.deleteAll();
    }

    private ResponseCookie getResponseCookie(String email, String password) {
        return webTestClient.post().uri(LOGIN_UTL)
                .body(fromFormData(EMAIL, email)
                        .with(PASSWORD, password))
                .exchange()
                .expectStatus().is3xxRedirection()
                .returnResult(ResponseCookie.class).getResponseCookies().getFirst(JSESSIONID);
    }

    private void addComments() {
        webTestClient.post().uri("/articles/" + SEAN_ARTICLE_ID + "/comments")
                .cookie(JSESSIONID, getResponseCookie(POBI_EMAIL, DEFAULT_PASSWORD).getValue())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData(CONTENT, CONTENT)
                        .with("articleId", "" + SEAN_ARTICLE_ID))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches(HttpHeaders.LOCATION, ARTICLE_PATTERN + SEAN_ARTICLE_ID);
    }

    private WebTestClient.ResponseSpec getStatus(String pobiEmail, String comment) {
        return webTestClient.put().uri("/articles/" + SEAN_ARTICLE_ID + "/comments/" + commentId)
                .cookie(JSESSIONID, getResponseCookie(pobiEmail, DEFAULT_PASSWORD).getValue())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData(CONTENT, comment)
                        .with("articleId", "" + SEAN_ARTICLE_ID))
                .exchange();
    }
}
