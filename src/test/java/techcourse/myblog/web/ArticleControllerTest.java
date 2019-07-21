package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {
    private static final String CUSTOM_ARTICLE_ID = "1";
    private static final String TITLE = "Hi, 코니";
    private static final String COVER_URL = "Hi, 코니";
    private static final String CONTENTS = "안녕안녕";

    private BodyInserters.FormInserter<String> articleData = getBodyInserters(TITLE, COVER_URL, CONTENTS);

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void 인덱스_페이지_테스트() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 게시글_작성_페이지_테스트() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 게시글_추가_테스트() {
        postArticle(articleData,
                response -> webTestClient.get()
                        .uri(response.getResponseHeaders().getLocation())
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody()
                        .consumeWith(res -> {
                            String body = new String(res.getResponseBody());
                            assertThat(body.contains(TITLE)).isTrue();
                            assertThat(body.contains(COVER_URL)).isTrue();
                            assertThat(body.contains(CONTENTS)).isTrue();
                        })
        );
    }

    @Test
    public void 게시글_수정_페이지가_잘_출력되는지_테스트() {
        postArticle(articleData,
                response -> webTestClient.get()
                        .uri(response.getResponseHeaders().getLocation() + "/edit")
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody()
                        .consumeWith(res -> {
                            String body = new String(res.getResponseBody());
                            assertThat(body.contains(TITLE)).isTrue();
                            assertThat(body.contains(COVER_URL)).isTrue();
                            assertThat(body.contains(CONTENTS)).isTrue();
                        })
        );
    }

    @Test
    public void 게시글_수정_테스트() {
        String updatedTitle = "new TITLE";
        String updatedCoverUrl = "new COVER_URL";
        String updatedContents = "new CONTENTS";

        postArticle(articleData,
                response -> webTestClient.put()
                        .uri(response.getResponseHeaders().getLocation())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .body(getBodyInserters(updatedTitle, updatedCoverUrl, updatedContents))
                        .exchange()
                        .expectStatus().isFound()
                        .expectBody()
                        .consumeWith(res -> {
                            webTestClient.get()
                                    .uri(res.getResponseHeaders().getLocation())
                                    .exchange()
                                    .expectStatus()
                                    .isOk()
                                    .expectBody()
                                    .consumeWith(r -> {
                                        String body = new String(r.getResponseBody());
                                        assertThat(body.contains(updatedTitle)).isTrue();
                                        assertThat(body.contains(updatedCoverUrl)).isTrue();
                                        assertThat(body.contains(updatedContents)).isTrue();
                                    });
                        })
        );
    }

    @Test
    public void 게시글_삭제_테스트() {
        postArticle(articleData,
                response -> webTestClient.delete()
                        .uri(response.getResponseHeaders().getLocation())
                        .exchange()
                        .expectStatus()
                        .isFound()
                        .expectBody()
                        .consumeWith(
                                res -> webTestClient.get()
                                        .uri(res.getResponseHeaders().getLocation())
                                        .exchange()
                                        .expectStatus()
                                        .isOk()
                                        .expectBody()
                                        .consumeWith(r -> {
                                            String body = new String(r.getResponseBody());
                                            assertThat(body.contains(TITLE)).isFalse();
                                            assertThat(body.contains(CONTENTS)).isFalse();
                                            assertThat(body.contains(COVER_URL)).isFalse();
                                        })
                        )
        );
    }

    private void postArticle(BodyInserters.FormInserter<String> articleData, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(articleData)
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(consumer);
    }

    private BodyInserters.FormInserter<String> getBodyInserters(String title, String coverUrl, String contents) {
        return BodyInserters.fromFormData("articleId", CUSTOM_ARTICLE_ID)
                .with("title", title)
                .with("coverUrl", coverUrl)
                .with("contents", contents);
    }
}
