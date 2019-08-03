package techcourse.myblog.util;

import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.article.ArticleDataForTest;

public class ArticleUtilForTest {

    public static String createArticle(WebTestClient webTestClient, String cookie) {
        final String[] path = new String[1];

        WebTest.executePostTest(webTestClient, "/articles", cookie, ArticleDataForTest.ARTICLE_BODY)
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response -> {
                    path[0] = response.getResponseHeaders().getLocation().getPath();
                });

        return path[0];
    }
}
