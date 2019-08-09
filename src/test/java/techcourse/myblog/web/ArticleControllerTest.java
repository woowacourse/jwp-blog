package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.service.ArticleGenericService;

import java.util.Arrays;

@Transactional
public class ArticleControllerTest extends AuthedWebTestClient {
    private static final Logger log = LoggerFactory.getLogger(ArticleControllerTest.class);

    private String title = "제목";
    private String contents = "contents";
    private String coverUrl = "https://image-notepet.akamaized.net/resize/620x-/seimage/20190222%2F88df4645d7d2a4d2ed42628d30cd83d0.jpg";

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleGenericService articleGenericService;

    @Test
    void index() {
        get("/").exchange().expectStatus().isOk();
    }

    @Test
    void articleForm() {
        get("/writing").exchange().expectStatus().isOk();
    }

    @Test
    @Transactional
    void saveArticle() {
        post("/articles")
                .body(params(Arrays.asList("title", "contents", "coverUrl"), title, contents, coverUrl))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".+\\/articles/.+");
    }

    @Test
    void Article_get_by_id() {
        get("/articles/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void updateArticle() {
        put("/articles/1")
                .body(params(Arrays.asList("title", "contents", "coverUrl"), "updated", "updated", "updated"))
                .exchange()
                .expectStatus().isOk();

        put("/articles/1")
                .body(params(Arrays.asList("title", "contents", "coverUrl"), "title", "contents", "coverUrl"))
                .exchange();
    }
}