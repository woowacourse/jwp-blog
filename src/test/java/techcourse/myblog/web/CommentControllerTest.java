package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class CommentControllerTest extends AuthedWebTestClient {
    private long articleId;

    @BeforeEach
    void setUp() {
        init();
        setArticleId();
    }

    @AfterEach
    void tearDown() {
        end();
        delete("/articles/" + articleId).exchange();
    }

    @Test
    void 코멘트_생성_테스트() {
        addComment().expectStatus().is3xxRedirection();
        checkBody("comment", true);
    }

    @Test
    void 코멘트_수정_테스트() {
        addComment().expectStatus().is3xxRedirection();
        String result = new String(get("/articles/" + articleId).exchange().expectBody().returnResult().getResponseBody());
        Pattern pattern = Pattern.compile("/articles/\\d+/comment/\\d+");
        Matcher matcher = pattern.matcher(result);
        matcher.find();
        String url = matcher.group(0);
        put(url).body(BodyInserters.fromFormData("contents", "starkim"))
                .exchange()
                .expectStatus().is3xxRedirection();
        checkBody("starkim", true);
    }

    @Test
    void 코멘트_삭제_테스트() {
        addComment().expectStatus().is3xxRedirection();
        String result = new String(get("/articles/" + articleId).exchange().expectBody().returnResult().getResponseBody());
        Pattern pattern = Pattern.compile("/articles/\\d+/comment/\\d+");
        Matcher matcher = pattern.matcher(result);
        matcher.find();
        String url = matcher.group(0);
        delete(url)
                .exchange()
                .expectStatus().is3xxRedirection();
        checkBody("starkim", false);
    }

    private WebTestClient.ResponseSpec addComment() {
        return post("/articles/" + articleId + "/comment")
                .body(params(Arrays.asList("contents"), "comment"))
                .exchange();
    }

    private void setArticleId() {
        EntityExchangeResult<byte[]> result = post("/articles")
                .body(params(Arrays.asList("title", "contents", "coverUrl"), "title", "contents", "coverUrl"))
                .exchange().expectBody().returnResult();
        articleId = Long.parseLong(result.getResponseHeaders().getLocation().getPath().split("/")[2]);
    }

    private void checkBody(String comment, boolean exist) {
        get("/articles/" + articleId).exchange().expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(comment) == exist).isTrue();
        });
    }
}