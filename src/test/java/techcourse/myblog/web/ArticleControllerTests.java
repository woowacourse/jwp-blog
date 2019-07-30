package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private Article basicArticle;
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        basicArticle = new Article("title", "url", "contents");
        articleRepository.save(basicArticle);
    }

    @Test
    void show_Article_Test() {
        getStatusAssertionsInGetWay("/").isOk();
    }

    @Test
    void writing_Test() {
        getStatusAssertionsInGetWay("/writing").isOk();
    }

    @Test
    void add_Article_Test() {
        redirect3xxTest(getRequestBodySpec("post", "/articles"), basicArticle);
    }

    @Test
    void save_Article_Test() {
        getStatusAssertionsInGetWay("articles/" + basicArticle.getId()).isOk();
    }

    @Test
    void edit_Article_Test() {
        getStatusAssertionsInGetWay("articles/" + basicArticle.getId() + "/edit").isOk();
    }

    @Test
    void update_Article_Test() {
        Article updatedArticle = new Article("b", "b", "b");
        articleRepository.updateArticleById(updatedArticle, 1);

        redirect3xxTest(getRequestBodySpec("put", "/articles/1"), updatedArticle);
    }

    @Test
    void delete_Article_Test() {
        articleRepository.deleteAll();
        getStatusAssertionsInGetWay("articles/1").is5xxServerError();
    }

    @AfterEach
    void tearDown() {
        articleRepository.deleteAll();
    }

    //StatusAssertion을 get방식으로 가져오겠다는 뜻입니다.
    private StatusAssertions getStatusAssertionsInGetWay(String uri) {
        return webTestClient
                .get()
                .uri(uri)
                .exchange()
                .expectStatus();
    }

    private void redirect3xxTest(WebTestClient.RequestBodySpec requestBodySpec, Article requestArticle) {
        requestBodySpec.contentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8)
                .body(BodyInserters
                        .fromFormData("title", requestArticle.getTitle())
                        .with("coverUrl", requestArticle.getCoverUrl())
                        .with("contents", requestArticle.getContents()))
                .exchange()
                .expectStatus()
                .is3xxRedirection().expectBody().consumeWith(redirectResponse -> {
            webTestClient.get()
                    .uri(redirectResponse.getResponseHeaders().get("location").get(0))
                    .exchange()
                    .expectBody().consumeWith(response -> {
                String body = new String(response.getResponseBody());
                assertThat(body.contains(requestArticle.getTitle())).isTrue();
                assertThat(body.contains(requestArticle.getCoverUrl())).isTrue();
                assertThat(body.contains(requestArticle.getContents())).isTrue();
            });
        });

    }

    private WebTestClient.RequestBodySpec getRequestBodySpec(String requestMethod, String uri) {
        if (requestMethod.equals("put")) {
            return webTestClient.put().uri(uri);
        }
        return webTestClient.post().uri(uri);
    }
}
