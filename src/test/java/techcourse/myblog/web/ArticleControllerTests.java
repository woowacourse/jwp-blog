package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.net.URI;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.web.ControllerTestUtil.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private static final Long DEFAULT_ARTICLE_ID = 999L;
    private static final String DEFAULT_ARTICLE_TITLE = "some article";
    private static final String DEFAULT_ARTICLE_CONTENT = "some content";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void 메인화면() {
        webTestClient.get().uri("/")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void 게시글_생성_페이지_이동() {
        // Given
        String sid = postLoginSync(webTestClient, DEFAULT_USER_EMAIL, DEFAULT_USER_PASSWORD)
            .getResponseCookies().getFirst(KEY_JSESSIONID).getValue();

        // When & Then
        webTestClient.get().uri("/writing")
            .cookie(KEY_JSESSIONID, sid)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void 게시글_생성() {
        // Given
        String title = "article publish test";
        String coverUrl = "https://images.pexels.com/photos/731217/pexels-photo-731217.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940";
        String contents = "## some good title";

        String sid = postLoginSync(webTestClient, DEFAULT_USER_EMAIL, DEFAULT_USER_PASSWORD)
            .getResponseCookies().getFirst(KEY_JSESSIONID).getValue();

        // When
        URI articleLocation = postArticleSync(webTestClient, title, coverUrl, contents, sid)
            .getResponseHeaders().getLocation();

        // Then
        assertThat(new String(getSync(webTestClient, articleLocation.toASCIIString(), sid)
            .getResponseBody()))
            .contains(title)
            .contains(contents);
    }

    @Test
    void 게시글_조회() {
        webTestClient.get().uri("/articles/" + DEFAULT_ARTICLE_ID)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .consumeWith(response -> {
                String body = new String(Objects.requireNonNull(response.getResponseBody()));
                assertThat(body).contains(DEFAULT_ARTICLE_TITLE);
                assertThat(body).contains(DEFAULT_ARTICLE_CONTENT);
            });
    }

    @Test
    void 게시글_작성자가_게시글_수정() {
        // Given
        String titleToChange = "changed title";
        String coverUrlToChange = "";
        String contentsToChange = "# changed article";

        String sid = postLoginSync(webTestClient, DEFAULT_USER_EMAIL, DEFAULT_USER_PASSWORD)
            .getResponseCookies().getFirst(KEY_JSESSIONID).getValue();

        // When
        URI articleLocation = postArticleSync(webTestClient,
            "article update test",
            "https://images.pexels.com/photos/731217/pexels-photo-731217.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",
            "## awesome title", sid)
            .getResponseHeaders().getLocation();

        webTestClient.put().uri(articleLocation)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .cookie(KEY_JSESSIONID, sid)
            .body(BodyInserters
                .fromFormData("title", titleToChange)
                .with("coverUrl", coverUrlToChange)
                .with("contents", contentsToChange))
            .exchange()
            .expectBody()
            .returnResult();

        // Then
        webTestClient.get().uri(articleLocation)
            .exchange()
            .expectBody()
            .consumeWith(response -> {
                String body = new String(Objects.requireNonNull(response.getResponseBody()));
                assertThat(body).contains(titleToChange);
                assertThat(body).contains(coverUrlToChange);
                assertThat(body).contains(contentsToChange);
            });
    }

    @Test
    void 게시글_작성자가_게시글_삭제() {
        String sid = postLoginSync(webTestClient, DEFAULT_USER_EMAIL, DEFAULT_USER_PASSWORD)
            .getResponseCookies().getFirst(KEY_JSESSIONID).getValue();

        URI articleLocation = postArticleSync(webTestClient, "article to delete", "", "# This will be deleted", sid)
            .getResponseHeaders().getLocation();

        webTestClient.delete().uri(articleLocation)
            .cookie(KEY_JSESSIONID, sid)
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectHeader().valueMatches("location", ".*/");

        webTestClient.get().uri(articleLocation)
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    void 게시글_작성자가_게시글_수정_페이지_이동() {
        String sid = postLoginSync(webTestClient, DEFAULT_USER_EMAIL, DEFAULT_USER_PASSWORD)
            .getResponseCookies().getFirst(KEY_JSESSIONID).getValue();

        webTestClient.get().uri("/articles/" + DEFAULT_ARTICLE_ID + "/edit")
            .cookie(KEY_JSESSIONID, sid)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void 다른_사용자가_게시글_수정() {
        String sid = postLoginSync(webTestClient, "paul123@example.com", "p@ssW0rd")
            .getResponseCookies().getFirst(KEY_JSESSIONID).getValue();

        webTestClient.put().uri("/articles/" + DEFAULT_ARTICLE_ID)
            .cookie(KEY_JSESSIONID, sid)
            .body(BodyInserters
                .fromFormData("title", "newTitle")
                .with("coverUrl", "")
                .with("contents", "newContents"))
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectHeader().valueMatches("location", ".*/articles/\\d*")
            .expectBody();
    }

    @Test
    void 다른_사용자가_게시글_삭제() {
        String sid = postLoginSync(webTestClient, "paul123@example.com", "p@ssW0rd")
            .getResponseCookies().getFirst(KEY_JSESSIONID).getValue();

        webTestClient.delete().uri("/articles/" + DEFAULT_ARTICLE_ID)
            .cookie(KEY_JSESSIONID, sid)
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectHeader().valueMatches("location", ".*/");
    }

    @Test
    void 다른_사용자가_게시글_수정_페이지_이동() {
        String sid = postLoginSync(webTestClient, "paul123@example.com", "p@ssW0rd")
            .getResponseCookies().getFirst(KEY_JSESSIONID).getValue();

        webTestClient.get().uri("/articles/" + DEFAULT_ARTICLE_ID + "/edit")
            .cookie(KEY_JSESSIONID, sid)
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectHeader().valueMatches("location", ".*/articles/\\d*");
    }
}
