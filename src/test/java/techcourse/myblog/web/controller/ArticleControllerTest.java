package techcourse.myblog.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.web.controller.dto.ArticleDto;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private ArticleDto articleDto;

    @BeforeEach
    void setUp() {
        articleDto = new ArticleDto("제목",
                "https://i.pinimg.com/736x/78/28/39/7828390ef4efbe704e480440f3bd3875.jpg",
                "CONTENTS");
    }

    @Test
    void 메인화면_조회() {
        testGetMethod("/");
    }

    private WebTestClient.ResponseSpec testGetMethod(String uri) {
        return webTestClient.get().uri(uri)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 글쓰기_폼_열기() {
        testGetMethod("/articles");
    }

    @Test
    void 게시물_생성() {
        postArticle(response -> {
            String url = getRedirectUrl(response);
            testGetMethod(url)
                    .expectBody()
                    .consumeWith(
                            redirectResponse -> {
                                String body = new String(redirectResponse.getResponseBody());
                                assertThat(body.contains(articleDto.getTitle())).isTrue();
                                assertThat(body.contains(articleDto.getCoverUrl())).isTrue();
                                assertThat(body.contains(articleDto.getContents())).isTrue();
                            }
                    );
        }, articleDto);

    }

    private WebTestClient.BodyContentSpec postArticle(Consumer<EntityExchangeResult<byte[]>> consumer, ArticleDto articleDto) {
        return webTestClient.post().uri("/articles")
                .body(fromFormData("title", articleDto.getTitle())
                        .with("coverUrl", articleDto.getCoverUrl())
                        .with("contents", articleDto.getContents()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(consumer);
    }

    @Test
    void updateArticle() {
        String updateTitle = "updatedTitle";
        String updateCoverUrl = "updatedCoverUrl";
        String updateContents = "updatedContents";

        postArticle(postResponse -> {
            long id = extractArticleId(postResponse);
            webTestClient.put().uri("/articles/" + id)
                    .body(fromFormData("title", updateTitle)
                            .with("coverUrl", updateCoverUrl)
                            .with("contents", updateContents))
                    .exchange()
                    .expectStatus().is3xxRedirection()
                    .expectBody()
                    .consumeWith(
                            putResponse -> {
                                String url = getRedirectUrl(putResponse);
                                testGetMethod(url)
                                        .expectBody()
                                        .consumeWith(redirectResponse -> {
                                            String body = new String(redirectResponse.getResponseBody());
                                            assertThat(body.contains(updateTitle)).isTrue();
                                            assertThat(body.contains(updateCoverUrl)).isTrue();
                                            assertThat(body.contains(updateContents)).isTrue();
                                        });
                            }
                    );
        }, articleDto);
    }

    private long extractArticleId(EntityExchangeResult<byte[]> postResponse) {
        return Long.parseLong(getRedirectUrl(postResponse).split("/")[4]);
    }

    private String getRedirectUrl(EntityExchangeResult<byte[]> response) {
        return response.getResponseHeaders().get("Location").get(0);
    }

    @Test
    void removeArticle() {
        postArticle(response -> {
            long id = Long.parseLong(response.getResponseHeaders().get("Location").get(0).split("/")[4]);
            webTestClient.delete().uri("/articles/" + id)
                    .exchange()
                    .expectStatus().is3xxRedirection();
        }, articleDto);
    }
}
