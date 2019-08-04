package techcourse.myblog.web;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract public class ControllerTest {
    @Autowired
    protected WebTestClient webTestClient;
    protected String articleId;
    protected String commentId;
    protected String cookie;

    protected void init() {
        webTestClient.post().uri("/users")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters
                .fromFormData("email", "email@gmail.com")
                .with("password", "password1234!")
                .with("name", "name"))
            .exchange();

        cookie = webTestClient.post().uri("/login")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters
                .fromFormData("email", "email@gmail.com")
                .with("password", "password1234!"))
            .exchange()
            .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");

        articleId = webTestClient.post().uri("/articles")
            .header("Cookie", cookie)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters
                .fromFormData("title", "jaemok")
                .with("coverUrl", "yuarel")
                .with("contents", "naeyong"))
            .exchange().expectStatus().isFound()
            .returnResult(String.class)
            .getResponseHeaders()
            .get("location").get(0)
            .split(".*/articles/")[1];

        commentId = webTestClient.post().uri("/comments")
            .header("Cookie", cookie)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters
                .fromFormData("contents", "datgeul").with("articleId", articleId))
            .exchange().expectStatus().isFound()
            .returnResult(String.class)
            .getResponseHeaders()
            .get("location").get(0)
            .split(".*/articles/")[1];
    }
}
