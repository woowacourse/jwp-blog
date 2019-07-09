package techcourse.myblog.web;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;

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
        Map<String,String> requestMap = new HashMap<>();
        String title = "hello";
        String backgroundURL = "http://asdf.com";
        String content = "helloContent";

        requestMap.put("title", title);
        requestMap.put("backgroundURL", backgroundURL);
        requestMap.put("content",content);
        webTestClient.post()
            .uri("/articles")
            .body(Mono.just(requestMap), Map.class)
            .exchange()
            .expectStatus().is3xxRedirection();

    }
}
