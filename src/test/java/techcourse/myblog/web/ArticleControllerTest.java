package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

public class ArticleControllerTest extends ControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        jSessionId = login(webTestClient);
    }

    @Test
    void 게시글_생성_폼_열기() {
        webTestClient.get().uri("/articles")
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().isOk()
        ;
    }

    @Test
    void 게시글_수정_폼_열기() {
        String articleId = "9";

        webTestClient.get().uri("/articles/" + articleId)
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().isOk()
        ;
    }

    @Test
    void 게시글_작성() {
        webTestClient.post().uri("/articles")
                .cookie(JSESSIONID, jSessionId)
                .body(BodyInserters
                        .fromFormData("title", "test title")
                        .with("coverUrl", "test coverUrl")
                        .with("contents", "test contents"))
                .exchange()
                .expectStatus().is3xxRedirection()
        ;
    }

    @Test
    void 게시글_수정() {
        String articleId = "9";
        String title = "제목";
        String coverUrl = "https://i.pinimg.com/736x/78/28/39/7828390ef4efbe704e480440f3bd3875.jpg";
        String contents = "CONTENTS";

        webTestClient.put().uri("/articles/" + articleId)
                .cookie(JSESSIONID, jSessionId)
                .body(BodyInserters.fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectHeader().valueMatches("Location", ".*/articles/" + articleId)
        ;
    }

    @Test
    void 게시글_수정_권한없음() {
        String articleId = "9";
        String title = "제목";
        String coverUrl = "https://i.pinimg.com/736x/78/28/39/7828390ef4efbe704e480440f3bd3875.jpg";
        String contents = "CONTENTS";

        webTestClient.put().uri("/articles/" + articleId)
                .cookie(JSESSIONID, login(webTestClient, "woowa@woowa.com", "12Woowa@@"))
                .body(BodyInserters.fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectBody()
                .returnResult().getStatus().is4xxClientError()
        ;
    }

    @Test
    void 게시글_삭제() {
        String articleId = "9";
        webTestClient.delete().uri("/articles/" + articleId)
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
        ;
    }

    @Test
    void 게시글_삭제_권한없음() {
        String articleId = "9";
        webTestClient.delete().uri("/articles/" + articleId)
                .cookie(JSESSIONID, login(webTestClient, "woowa@woowa.com", "12Woowa@@"))
                .exchange()
                .expectBody()
                .returnResult().getStatus().is4xxClientError()
        ;
    }
}
