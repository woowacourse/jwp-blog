package techcourse.myblog.articles.comments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.BaseControllerTests;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerTests extends BaseControllerTests {
    @Autowired
    WebTestClient webTestClient;

    private String articleId;

    private String jSessionId;

    @BeforeEach
    void setUp() {

        final String userPassword = "P@ssw0rd";
        final String userEmail = "emailArticle@gamil.com";

        addUser("name", userEmail, userPassword);
        jSessionId = getJSessionId(userEmail, userPassword);

        articleId = webTestClient.post().uri("/articles")
                .cookie(JSESSIONID, jSessionId)
                .body(fromFormData("title", "title")
                        .with("coverUrl", "coverUrl")
                        .with("contents", "contents"))
                .exchange()
                .returnResult(String.class)
                .getResponseHeaders().get("Location").get(0).split(".*/articles/")[1];


        String commentContents = "contents";

        webTestClient.post().uri("/articles/{id}/comments", articleId)
                .cookie(JSESSIONID, jSessionId)
                .body(fromFormData("contents", commentContents))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/articles/" + articleId);
    }

    @Test
    void update() {
        //TODO Comment.id 리턴 받아서 넣어주기, 수정된 거 확인하기

        webTestClient.put().uri("/articles/{articleId}/comments/{id}", articleId, 4)
                .cookie(JSESSIONID, jSessionId)
                .body(fromFormData("contents", "modifiedContents"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/articles/" + articleId);
    }

    @Test
    void 로그인_안하고_update_로그인창으로_이동() {

        webTestClient.put().uri("/articles/{articleId}/comments/{id}", articleId, 1)
                .body(fromFormData("contents", "modifiedContents"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader()
                .valueMatches("location", ".*/users/login");
    }

    @Test
    void 다른_사용자가_update_시도_Forbidden() {

        webTestClient.put().uri("/articles/{articleId}/comments/{id}", articleId, 1)
                .cookie(JSESSIONID, getJSessionId())
                .body(fromFormData("contents", "modifiedContents"))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void delete() {

        webTestClient.delete().uri("/articles/{articleId}/comments/{id}", articleId, 5)
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/articles/" + articleId);
    }
}