package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.repository.ArticleRepository;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest extends AuthedWebTestClient {
    private static final Logger log = LoggerFactory.getLogger(ArticleControllerTest.class);

    private String title = "제목";
    private String contents = "contents";
    private String coverUrl = "https://image-notepet.akamaized.net/resize/620x-/seimage/20190222%2F88df4645d7d2a4d2ed42628d30cd83d0.jpg";

    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        init();
    }

    @AfterEach
    void tearDown() {
        end();
    }

    @Test
    void index() {
        get("/").exchange().expectStatus().isOk();
    }

    @Test
    void articleForm() {
        get("/writing").exchange().expectStatus().isOk();
    }

    @Test
    void saveArticle() {
        addArticle()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".+\\/articles/.+");
    }

    private WebTestClient.ResponseSpec addArticle() {
        return post("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange();
    }

    private EntityExchangeResult<byte[]> getExchange() {
        return addArticle().expectBody().returnResult();
    }

    private long getArticleId() {
        return Long.parseLong(getExchange().getResponseHeaders().getLocation().getPath().split("/")[2]);
    }

    @Test
    void Article_get_by_id() {
        long articleId = getArticleId();
        get("/articles/" + articleId).exchange().expectStatus().isOk();
    }

//    @Test
//    @Transactional
//    void updateArticle() {
//        long articleId = getArticleId();
//        Article article = articleRepository.findById(articleId).orElseThrow(() -> new IllegalArgumentException("아티클 오류"));
//        User newUser = userRepository.findByEmail(Email.of(user.getEmail())).get();
//        article.setAuthor(newUser);
//
//        put("/articles/" + articleId)
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .body(BodyInserters
//                        .fromFormData("title", "updatedTitle")
//                        .with("coverUrl", "updatedCoverUrl")
//                        .with("contents", "updatedContents"))
//                .exchange()
//                .expectStatus().isOk();
//    }
//
//    @Test
//    @Transactional
//    void deleteArticle() {
//        User newUser = userRepository.findByEmail(Email.of(user.getEmail())).get();
//        Article article = new Article(title, contents, coverUrl);
//        long articleId = articleRepository.save(article).getId();
//        article.setAuthor(newUser);
//        articleRepository.save(article);
//        delete("/articles/" + articleId)
//                .exchange()
//                .expectStatus()
//                .is3xxRedirection();
//
//        assertThatThrownBy(() -> articleRepository.findById(articleId).orElseThrow(IllegalAccessError::new))
//                .isInstanceOf(IllegalAccessError.class);
//    }
}