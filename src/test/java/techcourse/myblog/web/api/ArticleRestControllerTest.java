package techcourse.myblog.web.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.web.controller.BaseControllerTests;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleRestControllerTest extends BaseControllerTests {

    @Autowired
    WebTestClient webTestClient;

    private String jSessionId;

    @BeforeEach
    void setUp() {
        final String userPassword = "P@ssw0rd";
        final String userEmail = "emailArticle@gamil.com";

        addUser("name", userEmail, userPassword);
        jSessionId = getJSessionId(userEmail, userPassword);
    }

    @Test
    void 모든_사용자_작성글_count_확인() {

        webTestClient.get().uri("/api/articles/counts")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 특정_유저_작성글_count_확인() {
        webTestClient.get().uri("/api/articles/counts?author={author}", "asd@asd")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().isOk();
    }
}