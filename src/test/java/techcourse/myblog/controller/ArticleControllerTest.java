package techcourse.myblog.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.ArticleRepository;
import techcourse.myblog.domain.repository.UserRepository;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;
import static techcourse.myblog.TestUtil.getCookie;
import static techcourse.myblog.TestUtil.signUp;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArticleControllerTest {
    private static final String USER_NAME_1 = "test1";
    private static final String EMAIL_1 = "test1@test.com";
    private static final String EMAIL_2 = "test2@test.com";
    private static final String PASSWORD_1 = "123456";
    private static final String TITLE_1 = "title";
    private static final String CONTENTS = "contents";
    private static final String COVER_URL = "COVER_URL";
    private static final String TITLE_2 = "title2";
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final WebTestClient webTestClient;
    private Article article;
    private String cookie;
    private String anotherCookie;


    @Autowired
    public ArticleControllerTest(WebTestClient webTestClient, ArticleRepository articleRepository,
                                 UserRepository userRepository) {
        this.webTestClient = webTestClient;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @BeforeAll
    void 회원가입_두_번_그리고_쿠키_발급() {
        signUp(webTestClient, EMAIL_1, USER_NAME_1, PASSWORD_1);
        signUp(webTestClient, EMAIL_2, USER_NAME_1, PASSWORD_1);
        cookie = getCookie(webTestClient, EMAIL_1, PASSWORD_1);
        anotherCookie = getCookie(webTestClient, EMAIL_2, PASSWORD_1);
    }

    @BeforeEach
    void setup() {
        User user = userRepository.findByEmail(EMAIL_1).get();
        article = new Article(TITLE_1, CONTENTS, COVER_URL, user);
        articleRepository.save(article);
    }

    @AfterEach
    void tearDown() {
        articleRepository.deleteAll();
    }

    @AfterAll
    void over() {
        articleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("게시글 저자인 유저가 로그인 한 경우")
    class with_login_authorized_user {
        @Test
        void 게시글_조회() {
            List<Article> foundArticles = articleRepository.findAll();
            Article foundArticle = foundArticles.get(0);
            webTestClient.get().uri("articles/" + foundArticle.getId())
                    .header("cookie", cookie)
                    .exchange()
                    .expectStatus().isOk();
            assertTrue(getArticleBody(foundArticle.getId()).contains(CONTENTS));
        }

        @Test
        void 게시글_생성() {
            webTestClient.post().uri("/articles")
                    .header("cookie", cookie)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(fromFormData("title", TITLE_2)
                            .with("contents", CONTENTS)
                            .with("coverUrl", COVER_URL))
                    .exchange()
                    .expectStatus()
                    .is3xxRedirection();
        }

        @Test
        void 게시글_수정() {
            List<Article> foundArticles = articleRepository.findAll();
            Article foundArticle = foundArticles.get(0);

            webTestClient.put().uri("/articles/" + foundArticle.getId())
                    .header("cookie", cookie)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(fromFormData("title", TITLE_2)
                            .with("contents", CONTENTS)
                            .with("coverUrl", COVER_URL))
                    .exchange();
            assertTrue(getArticleBody(foundArticle.getId()).contains(TITLE_2));

        }

        @Test
        void 게시글_삭제() {
            List<Article> foundArticles = articleRepository.findAll();
            Article foundArticle = foundArticles.get(0);

            webTestClient.delete().uri("/articles/" + foundArticle.getId())
                    .header("cookie", cookie)
                    .exchange();
            assertThat(articleRepository.findById(foundArticle.getId()).isPresent()).isFalse();
        }


    }

    @Nested
    @DisplayName("게시글 저자가 아닌 유저가 로그인되어 있는 경우")
    class non_author_login {
        @Test
        void 게시글_조회() {
            List<Article> foundArticles = articleRepository.findAll();
            Article foundArticle = foundArticles.get(0);

            webTestClient.get().uri("articles/" + foundArticle.getId())
                    .header("cookie", anotherCookie)
                    .exchange()
                    .expectStatus().isOk();
        }


        @Test
        void 게시글_수정() {
            List<Article> foundArticles = articleRepository.findAll();
            Article foundArticle = foundArticles.get(0);
            String uri = "/articles/" + foundArticle.getId();

            webTestClient.put().uri(uri)
                    .header("cookie", anotherCookie,
                            "referer", uri)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(fromFormData("title", TITLE_2)
                            .with("contents", CONTENTS)
                            .with("coverUrl", COVER_URL))
                    .exchange();

            foundArticle = articleRepository.findById(foundArticle.getId()).get();
            assertThat(foundArticle.getTitle()).isEqualTo(TITLE_1);
        }

        @Test
        void 게시글_삭제() {
            List<Article> foundArticles = articleRepository.findAll();
            Article foundArticle = foundArticles.get(0);
            String uri = "/articles/" + foundArticle.getId();

            webTestClient.delete().uri(uri)
                    .header("referer", uri)
                    .header("cookie", anotherCookie)
                    .exchange()
                    .expectStatus().isFound()
                    .expectBody()
                    .consumeWith((response) -> {
                        String location = response.getUrl().getPath();
                        assertThat(location).isEqualTo(uri);
                    });

            assertThat(articleRepository.findById(foundArticle.getId()).isPresent()).isTrue();
        }
    }

    @Nested
    @DisplayName("로그인하지 않은 경우")
    class without_login {

        @Test
        void 게시글_조회() {
            List<Article> foundArticles = articleRepository.findAll();
            Article foundArticle = foundArticles.get(0);

            webTestClient.get().uri("articles/" + foundArticle.getId())
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        void 게시글_생성() {
            String uri = "/articles";

            webTestClient.post().uri(uri)
                    .header("referer", uri)
                    .exchange()
                    .expectStatus().isFound()
                    .expectBody()
                    .consumeWith((response) -> {
                        String location = response.getUrl().getPath();
                        assertThat(location).isEqualTo(uri);
                    });

            assertThat(articleRepository.findAll().size()).isEqualTo(1);
        }

        @Test
        void 게시글_수정() {
            List<Article> foundArticles = articleRepository.findAll();
            Article foundArticle = foundArticles.get(0);
            String uri = "/articles/" + foundArticle.getId();

            webTestClient.put().uri(uri)
                    .header("referer", uri)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(fromFormData("title", TITLE_2)
                            .with("contents", CONTENTS)
                            .with("coverUrl", COVER_URL))
                    .exchange();

            foundArticle = articleRepository.findById(foundArticle.getId()).get();
            assertThat(foundArticle.getTitle()).isEqualTo(TITLE_1);
        }

        @Test
        void 게시글_삭제() {
            List<Article> foundArticles = articleRepository.findAll();
            Article foundArticle = foundArticles.get(0);
            String uri = "/articles/" + foundArticle.getId();

            webTestClient.delete().uri(uri)
                    .header("referer", uri)
                    .exchange()
                    .expectStatus().isFound()
                    .expectBody()
                    .consumeWith((response) -> {
                        String location = response.getUrl().getPath();
                        assertThat(location).isEqualTo(uri);
                    });

            assertThat(articleRepository.findById(foundArticle.getId()).isPresent()).isTrue();
        }
    }

    private String getArticleBody(long articleId){
        EntityExchangeResult<byte[]>  result = webTestClient.get().uri("articles/" + articleId)
                .header("cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .returnResult();
        return new String(Objects.requireNonNull(result.getResponseBody()));
    }

}