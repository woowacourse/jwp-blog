package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.service.ArticleGenericService;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        long articleId = setArticleId();
        get("/articles/" + articleId).exchange().expectStatus().isOk();
    }

    @Test
    void updateArticle() {
        long articleId = setArticleId();
        put("/articles/" + articleId)
                .body(params(Arrays.asList("title", "contents", "coverUrl"), "updated", "updated", "updated"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void deleteArticle() {
        long articleId = setArticleId();
        delete("/articles/" + articleId)
                .exchange()
                .expectStatus()
                .is3xxRedirection();

        assertThatThrownBy(() -> articleRepository.findById(articleId).orElseThrow(IllegalAccessError::new))
                .isInstanceOf(IllegalAccessError.class);
    }

    private long setArticleId() {
        EntityExchangeResult<byte[]> result = post("/articles")
                .body(params(Arrays.asList("title", "contents", "coverUrl"), "title", "contents", ""))
                .exchange().expectBody().returnResult();
        return Long.parseLong(result.getResponseHeaders().getLocation().getPath().split("/")[2]);
    }
}