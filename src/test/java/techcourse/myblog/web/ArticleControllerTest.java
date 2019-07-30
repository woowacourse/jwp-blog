package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.article.Article;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.user.User;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest extends AbstractControllerTest {
    private String title = "title";
    private String coverUrl = "coverUrl";
    private String contents = "contents";
    private User user1 = new User("heejoo", "heejoo@gmail.com", "Aa12345!");
    private User user2 = new User("cony", "cony@gmail.com", "Aa12345!");
    private Article article = new Article(title, coverUrl, contents, user1);

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void 로그인_전_Article_생성_페이지_접근() {
        webTestClient.get().uri("/articles/new").exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/login.*");
    }

    @Test
    void 로그인_후_Article_생성_페이지_접근() {
        String jSessionId = extractJSessionId(login(user1));
        webTestClient.get().uri("/articles/new")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 로그인_전_Article_생성() {
        ArticleDto articleDto = new ArticleDto(title, coverUrl, contents);
        getResponse(webTestClient.post()
                .uri("/articles/new"), articleDto, null)
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/login.*");
    }

    @Test
    void 로그인_후_Article_생성() {
        String jSessionId = extractJSessionId(login(user1));
        ArticleDto articleDto = new ArticleDto(title, coverUrl, contents);

        WebTestClient.RequestBodySpec requestBodySpec = webTestClient.post().uri("/articles/new")
                .cookie("JSESSIONID", jSessionId);

        WebTestClient.ResponseSpec responseSpec = getResponse(requestBodySpec, articleDto, jSessionId)
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/articles.*");

        checkBody(responseSpec, articleDto);
    }

    @Test
    void 없는_Article() {
        webTestClient.get().uri("/articles/10").exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void 로그인_전_수정_페이지_접근() {
        webTestClient.get().uri("articles/" + article.getArticleId() + "/edit")
                .exchange().expectStatus().is3xxRedirection();
    }

    @Test
    void 로그인_후_수정_페이지_접근() {
        String jSessionId = extractJSessionId(login(user1));
        webTestClient.get().uri("articles/1/edit")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 로그인_전_Article_수정() {
        ArticleDto articleDto = new ArticleDto("update title", "update coverUrl", "update contents");

        getResponse(webTestClient.put().uri("/articles/" + article.getArticleId()), articleDto, null)
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 로그인_후_Article_수정() {
        ArticleDto articleDto = new ArticleDto("update title", "update coverUrl", "update contents");
        String jSessionId = extractJSessionId(login(user1));

        WebTestClient.ResponseSpec responseSpec = getResponse(webTestClient.put().uri("/articles/1"), articleDto, jSessionId);

        responseSpec.expectBody()
                .consumeWith(response -> {
                    webTestClient.get().uri(Objects.requireNonNull(response.getResponseHeaders().get("Location")).get(0))
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(Objects.requireNonNull(res.getResponseBody()));
                                assertThat(body.contains(articleDto.getTitle())).isTrue();
                                assertThat(body.contains(articleDto.getCoverUrl())).isTrue();
                                assertThat(body.contains(articleDto.getContents())).isTrue();
                            });
                });
    }

    @Test
    void 로그인_전_Article_삭제() {
        webTestClient.delete().uri("/articles/2")
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/login.*");
    }

    @Test
    void 로그인_후_Article_삭제() {
        String jSessionId = extractJSessionId(login(user1));
        webTestClient.delete().uri("/articles/2")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/.*");
    }

    @Test
    void 로그인_전_댓글_생성() {
        webTestClient.post().uri("/articles/1/comment/new")
                .body(BodyInserters
                        .fromFormData("contents", "댓글입니다."))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 로그인_후_댓글_생성() {
        String jSessionId = extractJSessionId(login(user1));
        webTestClient.post().uri("/articles/1/comment/new")
                .cookie("JSESSIONID", jSessionId)
                .body(BodyInserters
                        .fromFormData("contents", "댓글입니다."))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/articles.*");
    }

    @Test
    void 로그인_후_댓글_수정() {
        String jSessionId = extractJSessionId(login(user1));
        String updateComment = "수정된 댓글입니다.";
        WebTestClient.ResponseSpec responseSpec = webTestClient.put().uri("/articles/1/comment/1")
                .cookie("JSESSIONID", jSessionId)
                .body(BodyInserters
                        .fromFormData("contents", updateComment))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/articles.*");

        responseSpec.expectBody()
                .consumeWith(response -> {
                    webTestClient.get().uri(Objects.requireNonNull(response.getResponseHeaders().get("Location")).get(0))
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(Objects.requireNonNull(res.getResponseBody()));
                                assertThat(body.contains(updateComment)).isTrue();
                            });
                });


    }

    @Test
    void 로그인_후_댓글_삭제() {
        String jSessionId = extractJSessionId(login(user1));
        webTestClient.delete().uri("/articles/1/comment/2")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/articles.*");
    }

    @Test
    void 자신이_쓰지_않은_댓글_수정_시도() {
        String jSessionId = extractJSessionId(login(user2));
        String updateComment = "수정된 댓글입니다.";

        webTestClient.put().uri("/articles/1/comment/1")
                .cookie("JSESSIONID", jSessionId)
                .body(BodyInserters
                        .fromFormData("contents", updateComment))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 자신이_쓰지_않은_댓글_삭제_시도() {
        String jSessionId = extractJSessionId(login(user2));
        webTestClient.delete().uri("/articles/1/comment/1")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    private WebTestClient.ResponseSpec getResponse(WebTestClient.RequestBodySpec requestBodySpec, ArticleDto articleDto, String jSessionId) {
        return requestBodySpec
                .cookie("JSESSIONID", jSessionId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", articleDto.getTitle())
                        .with("coverUrl", articleDto.getCoverUrl())
                        .with("contents", articleDto.getContents()))
                .exchange();
    }

    private void checkBody(WebTestClient.ResponseSpec responseSpec, ArticleDto articleDto) {
        responseSpec.expectBody()
                .consumeWith(response -> {
                    webTestClient.get().uri(Objects.requireNonNull(response.getResponseHeaders().get("Location")).get(0))
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(Objects.requireNonNull(res.getResponseBody()));
                                assertThat(body.contains(articleDto.getTitle())).isTrue();
                                assertThat(body.contains(articleDto.getCoverUrl())).isTrue();
                                assertThat(body.contains(articleDto.getContents())).isTrue();
                            });
                });
    }
}
