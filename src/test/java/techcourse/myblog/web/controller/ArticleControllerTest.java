package techcourse.myblog.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.web.controller.dto.ArticleDto;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AutoConfigureWebTestClient
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
            String url = response.getResponseHeaders().get("Location").get(0);
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
    void 게시글_수정() {
        String updateTitle = "updatedTitle";
        String updateCoverUrl = "updatedCoverUrl";
        String updateContents = "updatedContents";

        postArticle(response -> {
            long id = Long.parseLong(response.getResponseHeaders().get("Location").get(0).split("/")[4]);
            webTestClient.put().uri("/articles/" + id)
                    .body(fromFormData("title", updateTitle)
                            .with("coverUrl", updateCoverUrl)
                            .with("contents", updateContents))
                    .exchange()
                    .expectStatus().is3xxRedirection()
                    .expectBody()
                    .consumeWith(
                            response1 -> {
                                String url = response1.getResponseHeaders().get("Location").get(0);
                                webTestClient.get().uri(url)
                                        .exchange()
                                        .expectStatus().isOk()
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

    @Test
    void 게시글_삭제() {
        postArticle(response -> {
            long id = Long.parseLong(response.getResponseHeaders().get("Location").get(0).split("/")[4]);
            webTestClient.delete().uri("/articles/" + id)
                    .exchange()
                    .expectStatus().is3xxRedirection();
        }, articleDto);
    }
}
