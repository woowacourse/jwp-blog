package techcourse.myblog.util;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.article.ArticleDataForTest;

public class ArticleUtilForTest {
    public static String createArticle(WebTestClient webTestClient, String cookie) {
        final String[] path = new String[1];

        webTestClient.post()
                .uri("/articles")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", ArticleDataForTest.ARTICLE_TITLE)
                        .with("coverUrl", ArticleDataForTest.ARTICLE_COVER_URL)
                        .with("contents", ArticleDataForTest.ARTICLE_CONTENTS))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response -> {
                    path[0] = response.getResponseHeaders().getLocation().getPath();
                });

        return path[0];
    }
}
