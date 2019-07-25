package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ArticleControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    private String location;
    private String jSessionId;

    @BeforeEach
    void setUp() {
        LoginTestConfig.signUp(webTestClient);
        jSessionId = LoginTestConfig.getJSessionId(webTestClient);

        location = postArticle()
                .returnResult(String.class)
                .getResponseHeaders()
                .get("Location").get(0);
    }

    private WebTestClient.ResponseSpec postArticle() {
        return webTestClient.post().uri("/articles")
                .body(BodyInserters
                        .fromFormData("title", "제목")
                        .with("coverUrl", "주소")
                        .with("contents", "내용"))
                .cookie("JSESSIONID", jSessionId)
                .exchange();
    }

    @Test
    void writeArticleForm() {
        webTestClient.get().uri("/articles/writing")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void saveArticle() {
        webTestClient.post().uri("/articles")
                .body(BodyInserters
                        .fromFormData("title", "제목")
                        .with("coverUrl", "주소")
                        .with("contents", "내용"))
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/articles/.*");
    }

    @Test
    void fetchArticle() {
        webTestClient.get().uri(location)
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void editArticle() {
        webTestClient.get().uri(location + "/edit")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void saveEditedArticle() {
        webTestClient.put().uri(location)
                .body(BodyInserters
                        .fromFormData("title", "수정된_제목")
                        .with("coverUrl", "수정된_주소")
                        .with("contents", "수정된_내용"))
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    void deleteArticle() {
        webTestClient.post().uri("/articles")
                .body(BodyInserters
                        .fromFormData("title", "제목")
                        .with("coverUrl", "주소")
                        .with("contents", "내용"))
                .cookie("JSESSIONID", jSessionId)
                .exchange();

        webTestClient.delete().uri("/articles/2")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isFound();
    }

    @AfterEach
    void tearDown() {
        webTestClient.delete().uri(location)
                .cookie("JSESSIONID", jSessionId)
                .exchange();

        LoginTestConfig.deleteUser(webTestClient);
    }
}
