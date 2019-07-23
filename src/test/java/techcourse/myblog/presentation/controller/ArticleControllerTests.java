package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.service.ArticleService;
import techcourse.myblog.domain.Article;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static techcourse.myblog.presentation.controller.ArticleController.ARTICLE_MAPPING_URL;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private static Long id = 0L;
    private Article article;
    private ArticleDto articleDto;
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        articleDto = ArticleDto.builder()
                .title("a")
                .coverUrl("b")
                .contents("c")
                .build();
        article = articleService.save(articleDto);
    }

    @Test
    void createForm_get_isOk() {
        webTestClient.get().uri(ARTICLE_MAPPING_URL + "/writing")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void save_post_is3xxRedirect() {
        commonRequest(POST, articleDto, "")
                .expectStatus()
                .is3xxRedirection();
        id++;
    }

    @Test
    void updateForm_get_isOk() {
        webTestClient.get().uri(ARTICLE_MAPPING_URL + "/" + article.getId() + "/edit")
                .exchange().expectStatus().isOk();
    }

    @Test
    void update_post_is3xxRedirect() {
        commonRequest(PUT, articleDto, "/" + article.getId())
                .expectStatus()
                .is3xxRedirection();
    }

    @Test
    void update_editedData_true() {
        ArticleDto editedArticleDto = ArticleDto.builder()
                .title("edit")
                .coverUrl("edit")
                .contents("edit")
                .build();

        commonRequest(PUT, editedArticleDto, "/" + article.getId())
                .expectStatus()
                .is3xxRedirection()
                .expectBody().consumeWith(redirectResponse -> {
            webTestClient.get()
                    .uri(redirectResponse.getResponseHeaders().get("location").get(0))
                    .exchange()
                    .expectBody().consumeWith(response -> {
                String body = new String(response.getResponseBody());
                assertThat(body.contains("edit")).isTrue();
                assertThat(body.contains("edit")).isTrue();
                assertThat(body.contains("edit")).isTrue();
            });
        });
    }

    @Test
    void delete_firstArticle_is3xxRedirect() {
        Article editedArticle = articleService.save(articleDto);
        webTestClient.delete().uri(ARTICLE_MAPPING_URL + "/" + editedArticle.getId())
                .exchange()
                .expectStatus()
                .is3xxRedirection();

    }

    private WebTestClient.ResponseSpec commonRequest(HttpMethod method, ArticleDto articleDto, String attachedUrl) {
        return webTestClient.method(method)
                .uri(ARTICLE_MAPPING_URL + attachedUrl)
                .body(BodyInserters
                        .fromFormData("title", articleDto.getTitle())
                        .with("coverUrl", articleDto.getCoverUrl())
                        .with("contents", articleDto.getContents()))
                .exchange();
    }
}