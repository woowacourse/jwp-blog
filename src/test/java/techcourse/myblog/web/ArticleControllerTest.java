package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {
    private static final String CUSTOM_ARTICLE_ID = "1";
    private static final String TITLE = "Hi, 코니";
    private static final String COVER_URL = "Hi, 코니";
    private static final String CONTENTS = "안녕안녕";

    private static int articleIdentifier = 0;

    private BodyInserters.FormInserter<String> articleData;
    private EntityExchangeResult<byte[]> addResponse;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        articleData = getBodyInserters(
                TITLE + articleIdentifier,
                COVER_URL + articleIdentifier,
                CONTENTS + articleIdentifier);
        addResponse = postArticle(articleData);
    }

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
        webTestClient.get()
                .uri(addResponse.getResponseHeaders().getLocation())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(res.getResponseBody());
                    assertThat(body.contains(TITLE + articleIdentifier)).isTrue();
                    assertThat(body.contains(COVER_URL + articleIdentifier)).isTrue();
                    assertThat(body.contains(CONTENTS + articleIdentifier)).isTrue();
                });
    }

    @Test
    public void 게시글_수정_페이지가_잘_출력되는지_테스트() {
        webTestClient.get()
                .uri(addResponse.getResponseHeaders().getLocation() + "/edit")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(res.getResponseBody());
                    assertThat(body.contains(TITLE + articleIdentifier)).isTrue();
                    assertThat(body.contains(COVER_URL + articleIdentifier)).isTrue();
                    assertThat(body.contains(CONTENTS + articleIdentifier)).isTrue();
                });
    }

    @Test
    public void 게시글_수정_테스트() {
        // given
        String updatedTitle = "new TITLE";
        String updatedCoverUrl = "new COVER_URL";
        String updatedContents = "new CONTENTS";

        // when
        /** 게시글 수정 **/
        EntityExchangeResult<byte[]> updateResponse = webTestClient.put()
                .uri(addResponse.getResponseHeaders().getLocation())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(getBodyInserters(updatedTitle, updatedCoverUrl, updatedContents))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .returnResult();
        /** 게시글 수정 후 리다이렉트 페이지 **/
        webTestClient.get()
                .uri(updateResponse.getResponseHeaders().getLocation())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    // then
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(updatedTitle)).isTrue();
                    assertThat(body.contains(updatedCoverUrl)).isTrue();
                    assertThat(body.contains(updatedContents)).isTrue();
                });
    }

    @Test
    public void 게시글_삭제_테스트() {
        // when
        /** 게시글 삭제 **/
        EntityExchangeResult<byte[]> deleteResponse = webTestClient.delete()
                .uri(addResponse.getResponseHeaders().getLocation())
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .returnResult();
        /** 게시글 삭제 후 리다이렉트 페이지 **/
        EntityExchangeResult<byte[]> result = webTestClient.get()
                .uri(deleteResponse.getResponseHeaders().getLocation())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .returnResult();

        // then
        String body = new String(result.getResponseBody());
        assertThat(body.contains(TITLE + articleIdentifier)).isFalse();
        assertThat(body.contains(CONTENTS + articleIdentifier)).isFalse();
        assertThat(body.contains(COVER_URL + articleIdentifier)).isFalse();
    }

    @AfterEach
    void tearDown() {
        articleIdentifier++;
    }

    private EntityExchangeResult<byte[]> postArticle(BodyInserters.FormInserter<String> articleData) {
        return webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(articleData)
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .returnResult()
                ;
    }

    private BodyInserters.FormInserter<String> getBodyInserters(String title, String coverUrl, String contents) {
        return BodyInserters.fromFormData("articleId", CUSTOM_ARTICLE_ID)
                .with("title", title)
                .with("coverUrl", coverUrl)
                .with("contents", contents);
    }
}
