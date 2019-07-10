package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void index() {
        webTestClient.get().uri("/")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void articleForm() {
        webTestClient.get().uri("/writing")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void createArticlePost() {
        Map<String, String> requestMap = new HashMap<>();
        String title = "hello";
        String backgroundURL = "http://asdf.com";
        String content = "helloContent";

        requestMap.put("title", title);
        requestMap.put("backgroundURL", backgroundURL);
        requestMap.put("content", content);
        webTestClient.post()
            .uri("/articles")
            .body(Mono.just(requestMap), Map.class)
            .exchange()
            .expectStatus().is3xxRedirection();
    }

    @Test
    void retrieveArticle() {
        Article newArticle = Article.of("title", "http://background.com", "가나다라마바사");
        articleRepository.addArticle(newArticle);

        webTestClient.get()
            .uri("/articles/" + newArticle.getId())
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void editArticle() {
        Article newArticle = Article.of("title", "http://background.com", "가나다라마바사");
        articleRepository.addArticle(newArticle);

        webTestClient.get()
            .uri("/articles/" + newArticle.getId() + "/edit")
            .exchange()
            .expectStatus().isOk();
    }


    @Test
    void editArticlePut() {
        Article newArticle = Article.of("my article", "http://image.com/", "origin contents");
        articleRepository.addArticle(newArticle);
        Map<String, String> requestMap = new HashMap<>();

        requestMap.put("title", "changed title");
        requestMap.put("backgroundURL", newArticle.getCoverUrl());
        requestMap.put("content", "changed contents");
        webTestClient.put()
            .uri("/articles/" + newArticle.getId() )
            .body(Mono.just(requestMap), Map.class)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void deleteArticle(){
        Article newArticle = Article.of("my article", "http://image.com/", "origin contents");
        articleRepository.addArticle(newArticle);

        webTestClient.delete()
            .uri("/articles/" + newArticle.getId() )
            .exchange()
            .expectStatus().isOk();
    }
}
