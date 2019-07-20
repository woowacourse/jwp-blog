package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void create_article() {
        String title = "title";
        String coverUrl = "coverUrl";
        String contents = "contents";

        createArticleWith(title, coverUrl, contents);
    }

    @Test
    void read_article() {
        String title = "blogTitle";
        String coverUrl = "blogCoverUrl";
        String contents = "blogContents";

        int articleId = createArticleWith(title, coverUrl, contents);


        ResponseSpec rs = webTestClient.get()
                .uri("/articles/" + articleId)
                .exchange()
                .expectStatus().isOk();

        assertResponse(rs, response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(title)).isTrue();
            assertThat(body.contains(coverUrl)).isTrue();
            assertThat(body.contains(contents)).isTrue();
        });
    }

    @Test
    void read_article_edit_page() {
        String title = "blogTitle";
        String coverUrl = "blogCoverUrl";
        String contents = "blogContents";

        int articleId = createArticleWith(title, coverUrl, contents);

        ResponseSpec rs = webTestClient.get()
                .uri("/articles/" + articleId + "/edit")
                .exchange()
                .expectStatus().isOk();

        assertResponse(rs, response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(title)).isTrue();
            assertThat(body.contains(coverUrl)).isTrue();
            assertThat(body.contains(contents)).isTrue();
        });
    }

    @Test
    void update_article() {
        String title = "blogTitle";
        String coverUrl = "blogCoverUrl";
        String contents = "blogContents";

        int articleId = createArticleWith(title, coverUrl, contents);

        ResponseSpec rs = webTestClient.put()
                .uri("/articles/" + articleId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().isOk();

        assertResponse(rs, response -> {
            // redirect 의 경우 이 안에서 다시 get 을 요청하는 코드를 보았습니다.
            // 또 다시 get 을 하지 않고 이런 식으로 테스트를 해도 괜찮을까요?
            //
            // 둘이 어떤 차이가 있는지 잘 이해가 안됩니다..
            String body = new String(response.getResponseBody());
            assertThat(body.contains(title)).isTrue();
            assertThat(body.contains(coverUrl)).isTrue();
            assertThat(body.contains(contents)).isTrue();
        });
    }

    @Test
    void delete_article() {
        String title = "title";
        String coverUrl = "coverUrl";
        String contents = "contents";

        int articleId = createArticleWith(title, coverUrl, contents);

        webTestClient.delete()
                .uri("/articles/" + articleId)
                .exchange()
                .expectStatus().isFound()
                .expectHeader()
                .valueMatches("location", "[^/]*//[^/]*/");
    }

    private int createArticleWith(String title, String coverUrl, String contents) {
        int[] id = new int[]{0};
        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectHeader()
                .value("location", (location -> {
                    System.out.println(location);

                    id[0] = findId(location);
                }));

        return id[0];
    }

    private int findId(String locationFinishedById) {
        List<String> chunks = Arrays.asList(locationFinishedById.split("/"));

        return Integer.parseInt(chunks.get(chunks.size() - 1));
    }

    private void assertResponse(ResponseSpec rs, Consumer<EntityExchangeResult<byte[]>> assertBody) {
        rs.expectBody()
                .consumeWith(assertBody);
    }
}
