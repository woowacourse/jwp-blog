package techcourse.myblog.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentControllerTest {

    private static final String USER_NAME_1 = "test1";
    private static final String EMAIL_1 = "test1@test.com";
    private static final String EMAIL_2 = "test2@test.com";
    private static final String PASSWORD_1 = "123456";
    private static final String TITLE_1 = "title";
    private static final String CONTENTS_1 = "contents";
    private static final String COVER_URL = "COVER_URL";
    private static final String TITLE_2 = "title2";
    private static final String CONTENTS_2 = "contents2";

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final WebTestClient webTestClient;
    private final CommentRepository commentReopsitory;

    private String cookie;
    private String anotherCookie;
    private Comment comment;

    @Autowired
    public CommentControllerTest(WebTestClient webTestClient, ArticleRepository articleRepository,
                                 UserRepository userRepository, CommentRepository commentRepository) {
        this.webTestClient = webTestClient;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.commentReopsitory = commentRepository;
    }

    @BeforeAll
    void 회원가입_두_번_게시글_생성_한_번_그리고_쿠키_발급() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("userName", USER_NAME_1)
                        .with("email", EMAIL_1)
                        .with("password", PASSWORD_1))
                .exchange();

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("userName", USER_NAME_1)
                        .with("email", EMAIL_2)
                        .with("password", PASSWORD_1))
                .exchange();

        cookie = webTestClient.post().uri("login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("email", EMAIL_1)
                        .with("password", PASSWORD_1))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");

        anotherCookie = webTestClient.post().uri("login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("email", EMAIL_2)
                        .with("password", PASSWORD_1))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");

        webTestClient.post().uri("/articles/write")
                .header("cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("title", TITLE_1)
                        .with("contents", CONTENTS_1)
                        .with("coverUrl", COVER_URL))
                .exchange();
    }

    @BeforeEach
    void setup() {
        User user = userRepository.findByEmail(EMAIL_1).get();
        Article article = articleRepository.findAll().get(0);
        comment = new Comment(CONTENTS_1, article, user);
        commentReopsitory.save(comment);
    }

    @AfterEach
    void tearDown() {
        commentReopsitory.deleteAll();
    }

    @AfterAll
    void over() {
        commentReopsitory.deleteAll();
        articleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("로그인하지 않은 경우")
    class without_login {
        @Test
        void 댓글_생성() {
            List<Article> foundArticles = articleRepository.findAll();
            Article foundArticle = foundArticles.get(0);
            String uri = "/articles/" + foundArticle.getId() + "/comments";

            webTestClient.post().uri(uri)
                    .header("referer", uri)
                    .exchange().expectStatus().isFound()
                    .expectBody()
                    .consumeWith((response) -> {
                        String location = response.getUrl().getPath();
                        assertThat(location).isEqualTo(uri);
                    });
            assertThat(commentReopsitory.findAll().size()).isEqualTo(1);
        }

        @Test
        void 댓글_수정() {
            Article foundArticle = articleRepository.findAll().get(0);
            Comment foundComment = commentReopsitory.findAll().get(0);

            String uri = "/articles/" + foundArticle.getId() + "/comments/" + foundComment.getId();

            webTestClient.put().uri(uri)
                    .header("referer", uri)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(fromFormData("contents", CONTENTS_2))
                    .exchange();

            foundComment = commentReopsitory.findById(foundComment.getId()).get();
            assertThat(foundComment.getContents()).isEqualTo(CONTENTS_1);
        }

        @Test
        void 댓글_삭제() {

        }

    }

    @Nested
    @DisplayName("댓글 저자인 유저가 로그인한 경우")
    class with_login_authorized_user {

        @Test
        void 댓글_생성() {
            List<Article> foundArticles = articleRepository.findAll();
            Article foundArticle = foundArticles.get(0);
            String uri = "/articles/" + foundArticle.getId() + "/comments";

            webTestClient.post().uri(uri)
                    .header("cookie", cookie)
                    .header("referer", uri)
                    .exchange().expectStatus().isFound()
                    .expectBody()
                    .consumeWith((response) -> {
                        String location = response.getUrl().getPath();
                        assertThat(location).isEqualTo(uri);
                    });
            assertThat(commentReopsitory.findAll().size()).isEqualTo(2);
        }

        @Test
        void 댓글_수정() {

        }

        @Test
        void 댓글_삭제() {

        }

    }

    @Nested
    @DisplayName("댓글 저자가 아닌 유저가 로그인되어 있는 경우")
    class non_author_login {

    }
}