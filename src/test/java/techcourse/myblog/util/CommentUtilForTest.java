package techcourse.myblog.util;

import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.comment.CommentDataForTest;

public class CommentUtilForTest {

    public static String createComment(WebTestClient webTestClient, String uri, String cookie) {
        final String[] path = new String[1];

        WebTest.executePostTest(webTestClient, uri + "/comments", cookie,
                BodyInserters.fromFormData("contents", CommentDataForTest.COMMENT_CONTENTS))
                .expectBody()
                .consumeWith(response -> {
                    path[0] = response.getResponseHeaders().getLocation().getPath();
                });

        return path[0];
    }
}
