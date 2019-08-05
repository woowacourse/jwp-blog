package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.UserRepository;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class CommentControllerTest extends AuthedWebTestClient {
    private long articleId;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        setArticleId();
    }

    @AfterEach
    void tearDown() {
        delete("/articles/" + articleId).exchange();
    }

    @Test
    @Transactional
    void 코멘트_생성_테스트() {
        addComment().expectStatus().is3xxRedirection();
        checkBody("comment", true);
    }

    @Test
    void 코멘트_수정_테스트() {
        addComment().expectStatus().is3xxRedirection();
        String result = new String(get("/articles/" + articleId).exchange().expectBody().returnResult().getResponseBody());
        Pattern pattern = Pattern.compile("/articles/\\d+/comments/\\d+");
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
        Pattern pattern = Pattern.compile("/articles/\\d+/comments/\\d+");
        Matcher matcher = pattern.matcher(result);
        matcher.find();
        String url = matcher.group(0);
        delete(url)
                .exchange()
                .expectStatus().is3xxRedirection();
        checkBody("starkim", false);
    }

    private WebTestClient.ResponseSpec addComment() {
        return post("/articles/" + articleId + "/comments")
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