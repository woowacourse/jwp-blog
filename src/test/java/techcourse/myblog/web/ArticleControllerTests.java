package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.UserRepository;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private static final Logger log = LoggerFactory.getLogger(ArticleControllerTests.class);

    private String title = "제목";
    private String contents = "contents";
    private String coverUrl = "https://image-notepet.akamaized.net/resize/620x-/seimage/20190222%2F88df4645d7d2a4d2ed42628d30cd83d0.jpg";

    private String cookie;

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        log.debug("Before Cookie: {} ", cookie);
        userRepository.save(new User("andole", "A!1bcdefg", "andole@gmail.com"));
        cookie = webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("email", "andole@gmail.com")
                        .with("password", "A!1bcdefg"))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");

        log.debug("After Cookie : {}", cookie);
    }

    private WebTestClient.RequestBodySpec cookedPostRequest(String uri) {
        return webTestClient
                .post()
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    private WebTestClient.RequestHeadersSpec cookedGetRequest(String uri) {
        return webTestClient
                .get()
                .uri(uri)
                .header("Cookie", cookie);
    }

    private WebTestClient.RequestBodySpec cookedPutRequest(String uri) {
        return webTestClient.put()
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    private WebTestClient.RequestHeadersSpec cookedDeleteRequest(String uri) {
        return webTestClient
                .delete()
                .uri(uri)
                .header("Cookie", cookie);
    }


    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void articleForm() {
        cookedGetRequest("/writing")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void saveArticle() {
        cookedPostRequest("/articles")
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection();

        Iterator<Article> articles = articleRepository.findAll().iterator();
        Article article = articles.next();
        assertThat(article.getTitle()).isEqualTo(title);
    }

    @Test
    void findById() {
        long articleId = articleRepository.save(new Article(title, contents, coverUrl)).getId();

        cookedGetRequest("/articles/" + articleId)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void updateArticle() {
        long articleId = articleRepository.save(new Article(title, contents, coverUrl)).getId();
        cookedPutRequest("/articles/" + articleId)
                .body(BodyInserters
                        .fromFormData("title", "updatedTitle")
                        .with("coverUrl", "updatedCoverUrl")
                        .with("contents", "updatedContents"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void deleteArticle() {
        long articleId = articleRepository.save(new Article(title, contents, coverUrl)).getId();
        cookedDeleteRequest("/articles/" + articleId)
                .exchange()
                .expectStatus()
                .is3xxRedirection();

        assertThatThrownBy(() -> articleRepository.findById(articleId).orElseThrow(IllegalAccessError::new))
                .isInstanceOf(IllegalAccessError.class);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}