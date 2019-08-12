package techcourse.myblog.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.ArticleRepository;
import techcourse.myblog.domain.repository.CommentRepository;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.service.dto.CommentDto;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static techcourse.myblog.TestUtil.*;
import static techcourse.myblog.controller.CommentController.RESPONSE_FAIL;
import static techcourse.myblog.controller.CommentController.RESPONSE_SUCCESS;

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
    private static final String CONTENTS_2 = "contents2";
    private static final String COMMENT_CONTENTS_1 = "comment_contents";
    private static final String COMMENT_CONTENTS_2 = "comment_contents2";

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final WebTestClient webTestClient;
    private final CommentRepository commentReopsitory;

    private String cookie;
    private String anotherCookie;
    private Comment comment;
    private CommentDto commentDto;

    @Autowired
    public CommentControllerTest(WebTestClient webTestClient, ArticleRepository articleRepository,
                                 UserRepository userRepository, CommentRepository commentRepository) {
        this.webTestClient = webTestClient;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.commentReopsitory = commentRepository;
    }

    @BeforeAll
    void 테스트_계정_생성_테스트_게시물_생성() {
        signUp(webTestClient, EMAIL_1, USER_NAME_1, PASSWORD_1);
        signUp(webTestClient, EMAIL_2, USER_NAME_1, PASSWORD_1);
        cookie = getCookie(webTestClient, EMAIL_1, PASSWORD_1);
        anotherCookie = getCookie(webTestClient, EMAIL_2, PASSWORD_1);
        createArticle(webTestClient, cookie, TITLE_1, CONTENTS_1, COVER_URL);
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

    private WebTestClient.ResponseSpec saveComment(String commentContents, String uri, String cookie) {
        CommentDto commentDto = new CommentDto(commentContents);
        return webTestClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Cookie", cookie)
                .body(Mono.just(commentDto), CommentDto.class)
                .exchange();
    }

    private WebTestClient.ResponseSpec editComment(String commentContents, String uri, String cookie) {
        CommentDto commentDto = new CommentDto(commentContents);
        return webTestClient.put()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Cookie", cookie)
                .body(Mono.just(commentDto), CommentDto.class)
                .exchange();
    }

    private WebTestClient.ResponseSpec deleteComment(String uri, String cookie) {
        return webTestClient.delete()
                .uri(uri)
                .header("Cookie", cookie)
                .exchange();
    }

    @Nested
    @DisplayName("공통 경우")
    class common {
        @Test
        void 댓글_0개_가져오기() {
            commentReopsitory.deleteAll();
            List<Article> foundArticles = articleRepository.findAll();
            Article foundArticle = foundArticles.get(0);
            String uri = "/articles/" + foundArticle.getId() + "/comments";
            webTestClient.get()
                    .uri(uri)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody()
                    .jsonPath("$.message").isEqualTo(RESPONSE_SUCCESS)
                    .jsonPath("$.data.length()").isEqualTo(0);
        }

        @Test
        void 댓글_1개_가져오기() {
            commentReopsitory.deleteAll();
            List<Article> foundArticles = articleRepository.findAll();
            Article foundArticle = foundArticles.get(0);
            String uri = "/articles/" + foundArticle.getId() + "/comments";
            saveComment(COMMENT_CONTENTS_1, uri, cookie);
            webTestClient.get()
                    .uri(uri)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody()
                    .jsonPath("$.message").isEqualTo(RESPONSE_SUCCESS)
                    .jsonPath("$.data.length()").isEqualTo(1);
        }

        @Test
        void 댓글_2개_가져오기() {
            commentReopsitory.deleteAll();
            List<Article> foundArticles = articleRepository.findAll();
            Article foundArticle = foundArticles.get(0);
            String uri = "/articles/" + foundArticle.getId() + "/comments";
            saveComment(COMMENT_CONTENTS_1, uri, cookie);
            saveComment(COMMENT_CONTENTS_2, uri, cookie);
            webTestClient.get()
                    .uri(uri)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody()
                    .jsonPath("$.message").isEqualTo(RESPONSE_SUCCESS)
                    .jsonPath("$.data.length()").isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("로그인하지 않은 경우")
    class without_login {
        @Test
        void 댓글_생성() {
            List<Article> foundArticles = articleRepository.findAll();
            Article foundArticle = foundArticles.get(0);
            String uri = "/articles/" + foundArticle.getId() + "/comments";
            saveComment(COMMENT_CONTENTS_1, uri, null).expectStatus().isBadRequest();
        }

        @Test
        void 댓글_생성_ajax() {
            List<Article> foundArticles = articleRepository.findAll();
            Article foundArticle = foundArticles.get(0);
            String baseUri = "/articles/" + foundArticle.getId() + "/comments";
            saveComment(COMMENT_CONTENTS_1, baseUri, null)
                    .expectStatus()
                    .isBadRequest()
                    .expectBody()
                    .jsonPath("$.message").isEqualTo(RESPONSE_FAIL)
                    .jsonPath("$.data").isEqualTo(null);
        }

        @Test
        void 댓글_수정() {
            Article foundArticle = articleRepository.findAll().get(0);
            Comment foundComment = commentReopsitory.findAll().get(0);
            String uri = "/articles/" + foundArticle.getId() + "/comments/" + foundComment.getId();
            editComment(COMMENT_CONTENTS_2, uri, null).expectStatus().isBadRequest();
        }

        @Test
        void 댓글_수정_ajax() {
            List<Article> foundArticles = articleRepository.findAll();
            Comment foundComment = commentReopsitory.findAll().get(0);

            Article foundArticle = foundArticles.get(0);
            String baseUri = "/articles/" + foundArticle.getId() + "/comments/" + foundComment.getId();
            String contents = "comment";
            editComment(contents, baseUri, null)
                    .expectStatus()
                    .isBadRequest()
                    .expectBody()
                    .jsonPath("$.message").isEqualTo(RESPONSE_FAIL)
                    .jsonPath("$.data").isEqualTo(null);
        }

        @Test
        void 댓글_삭제() {
            Article foundArticle = articleRepository.findAll().get(0);
            Comment foundComment = commentReopsitory.findAll().get(0);
            String uri = "/articles/" + foundArticle.getId() + "/comments/" + foundComment.getId();
            deleteComment(uri, null)
                    .expectStatus()
                    .isBadRequest();
        }

        @Test
        void 댓글_삭제_ajax() {
            List<Article> foundArticles = articleRepository.findAll();
            Article foundArticle = foundArticles.get(0);
            Comment foundComment = commentReopsitory.findAll().get(0);
            String uri = "/articles/" + foundArticle.getId() + "/comments/" + foundComment.getId();
            deleteComment(uri, null)
                    .expectStatus()
                    .isBadRequest()
                    .expectBody()
                    .jsonPath("$.message").isEqualTo(RESPONSE_FAIL)
                    .jsonPath("$.data").isEqualTo(null);
        }
    }

    @Nested
    @DisplayName("댓글 저자인 유저가 로그인한 경우")
    class with_login_authorized_user {

        @Test
        void 댓글_생성() {
            List<Article> foundArticles = articleRepository.findAll();
            Article foundArticle = foundArticles.get(0);
            String baseUri = "/articles/" + foundArticle.getId() + "/comments";
            saveComment(COMMENT_CONTENTS_1, baseUri, cookie)
                    .expectStatus()
                    .isCreated()
                    .expectBody()
                    .jsonPath("$.message").isEqualTo(RESPONSE_SUCCESS)
                    .jsonPath("$.data.contents").isEqualTo(COMMENT_CONTENTS_1);
        }

        @Test
        void 댓글_수정() {
            List<Article> foundArticles = articleRepository.findAll();
            Comment foundComment = commentReopsitory.findAll().get(0);

            Article foundArticle = foundArticles.get(0);
            String baseUri = "/articles/" + foundArticle.getId() + "/comments/" + foundComment.getId();
            editComment(COMMENT_CONTENTS_2, baseUri, cookie)
                    .expectStatus()
                    .isOk()
                    .expectBody()
                    .jsonPath("$.message").isEqualTo(RESPONSE_SUCCESS);
        }

        @Test
        void 댓글_삭제() {
            List<Article> foundArticles = articleRepository.findAll();
            Article foundArticle = foundArticles.get(0);
            Comment foundComment = commentReopsitory.findAll().get(0);
            String uri = "/articles/" + foundArticle.getId() + "/comments/" + foundComment.getId();
            deleteComment(uri, cookie)
                    .expectStatus()
                    .isOk()
                    .expectBody()
                    .jsonPath("$.message").isEqualTo(RESPONSE_SUCCESS);
        }
    }

    @Nested
    @DisplayName("댓글 저자가 아닌 유저가 로그인되어 있는 경우")
    class non_author_login {
        @Test
        void 댓글_수정() {
            Article foundArticle = articleRepository.findAll().get(0);
            Comment foundComment = commentReopsitory.findAll().get(0);

            String uri = "/articles/" + foundArticle.getId() + "/comments/" + foundComment.getId();
            CommentDto commentDTO = new CommentDto(CONTENTS_2);

            webTestClient.put().uri(uri)
                    .header("cookie", anotherCookie)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(Mono.just(commentDTO), CommentDto.class)
                    .exchange().expectStatus().isBadRequest();
        }

        @Test
        void 댓글_삭제() {
            Article foundArticle = articleRepository.findAll().get(0);
            Comment foundComment = commentReopsitory.findAll().get(0);
            String uri = "/articles/" + foundArticle.getId() + "/comments/" + foundComment.getId();
            webTestClient.delete().uri(uri)
                    .header("cookie", anotherCookie)
                    .exchange().expectStatus().isBadRequest();
        }
    }
}