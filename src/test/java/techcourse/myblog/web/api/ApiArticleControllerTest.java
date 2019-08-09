package techcourse.myblog.web.api;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.web.AuthedWebTestClient;

class ApiArticleControllerTest extends AuthedWebTestClient {

    @Test
    void read() {
        get("/api/articles/1")
                .exchange()
                .expectBody()
                .jsonPath("title").isNotEmpty();
    }

    @Test
    void create() {
        ArticleDto articleDto = new ArticleDto("api", "ajax", "coverUrl");
        postJson("/api/articles")
                .body(Mono.just(articleDto), ArticleDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("title").isNotEmpty()
                .jsonPath("contents").isNotEmpty()
                .jsonPath("coverUrl").isNotEmpty();
    }

    @Test
    void update() {
        ArticleDto articleDto = new ArticleDto("edited", "edited", "edited");
        putJson("/api/articles/1")
                .body(Mono.just(articleDto), ArticleDto.class)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody()
                .jsonPath("title").isNotEmpty()
                .jsonPath("contents").isNotEmpty()
                .jsonPath("coverUrl").isNotEmpty();
    }
}