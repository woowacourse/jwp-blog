package techcourse.myblog.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArticleControllerTest {
    private static final String USER_NAME_1 = "test1";
    private static final String USER_NAME_2 = "test2";
    private static final String EMAIL_1 = "test1@test.com";
    private static final String EMAIL_2 = "test2@test.com";
    private static final String PASSWORD_1 = "123456";
    private static final String PASSWORD_2 = "12345";
    private static final String TITLE = "title";
    private static final String CONTENTS = "contents";
    private static final String COVER_URL = "COVER_URL";
    private static final String TITLE_2 = "title2";
    private final Article article = new Article("title", "contents", "coverUrl");
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private String cookie;
    private String anotherCookie;
    private WebTestClient webTestClient;

    @Autowired
    public ArticleControllerTest(WebTestClient webTestClient, ArticleRepository articleRepository,
                                 UserRepository userRepository) {
        this.webTestClient = webTestClient;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @BeforeAll
    void 회원가입_두_번_그리고_로그인() {
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

    }

    @BeforeEach
    void setup() {
        User user = userRepository.findByEmail(EMAIL_1).get();
        article.setAuthor(user);
        articleRepository.save(article);
    }

    @Test
    void 로그인했을_때_게시글_생성() {
        webTestClient.post().uri("/articles/write")
                .header("cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("title", TITLE_2)
                        .with("contents", CONTENTS)
                        .with("coverUrl", COVER_URL))
                .exchange();

        assertThat(articleRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    void 로그인했을_때_게시글_수정() {
        List<Article> foundArticles = articleRepository.findAll();
        Article foundArticle = foundArticles.get(0);

        webTestClient.put().uri("/articles/" + foundArticle.getId())
                .header("cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("title", TITLE_2)
                        .with("contents", CONTENTS)
                        .with("coverUrl", COVER_URL))
                .exchange();

        foundArticle = articleRepository.findById(foundArticle.getId()).get();
        assertThat(foundArticle.getTitle()).isEqualTo(TITLE_2);
    }

    @Test
    void 로그인했을_때_게시글_삭제() {
        List<Article> foundArticles = articleRepository.findAll();
        Article foundArticle = foundArticles.get(0);

        webTestClient.delete().uri("/articles/" + foundArticle.getId())
                .header("cookie", cookie)
                .exchange();

        assertThat(articleRepository.findById(foundArticle.getId()).isPresent()).isFalse();
    }

    @Test
    void 삭제권한_없는_사용자가_게시글_삭제() {
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

    @AfterEach
    void tearDown() {
        articleRepository.delete(article);
    }

    @AfterAll
    void over() {
        articleRepository.deleteAll();
        userRepository.deleteAll();
    }

}