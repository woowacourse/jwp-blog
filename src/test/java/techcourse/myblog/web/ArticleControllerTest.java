package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.Article.Article;
import techcourse.myblog.repository.ArticleRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTestClient extends AuthedWebTestClient {
    private static final Logger log = LoggerFactory.getLogger(ArticleControllerTestClient.class);

    private String title = "제목";
    private String contents = "contents";
    private String coverUrl = "https://image-notepet.akamaized.net/resize/620x-/seimage/20190222%2F88df4645d7d2a4d2ed42628d30cd83d0.jpg";

    @Autowired
    private ArticleRepository articleRepository;


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
        post("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".+\\/articles/.+");
    }

    @Test
    void Article_get_by_id() {
        long articleId = articleRepository.save(new Article(title, contents, coverUrl)).getId();
        get("/articles/" + articleId).exchange().expectStatus().isOk();
    }

    @Test
    void updateArticle() {
        long articleId = articleRepository.save(new Article(title, contents, coverUrl)).getId();
        put("/articles/" + articleId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
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
        delete("/articles/" + articleId)
                .exchange()
                .expectStatus()
                .is3xxRedirection();

        assertThatThrownBy(() -> articleRepository.findById(articleId).orElseThrow(IllegalAccessError::new))
                .isInstanceOf(IllegalAccessError.class);
    }
}