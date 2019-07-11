package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.Article;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void writeArticle() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void create_article() {
        webTestClient.post().uri("/articles")
                .exchange()
                .expectStatus().isOk();
    }

//    @Test
//    void submit_article() {
//        String title = "sss";
//        String coverUrl = "https://www.google.com/url?sa=i&source=images&cd=&ved=2ahUKEwj73uqZ1qnjAhVlEqYKHbrCBMoQjRx6BAgBEAU&url=https%3A%2F%2Farbordayblog.org%2Flandscapedesign%2F12-fast-growing-shade-trees%2F&psig=AOvVaw0oBz9z_VowOrACEVShDLF2&ust=1562824799010531";
//        String contents = "contents";
//
//
//        webTestClient.post()
//                .uri("/articles")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .body(BodyInserters
//                        .fromFormData("title", title)
//                        .with("coverUrl", coverUrl)
//                        .with("contents", contents))
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .consumeWith(response -> {
//                    webTestClient.get().uri(Objects.requireNonNull(response.getResponseHeaders().get("Location")).get(0))
//                            .exchange()
//                            .expectStatus().isOk()
//                            .expectBody()
//                            .consumeWith(res -> {
//                                String body = new String(Objects.requireNonNull(res.getResponseBody()));
//                                assertThat(body.contains(title)).isTrue();
//                                assertThat(body.contains(coverUrl)).isTrue();
//                                // This assertion can fail when length of contents over 100 because of excerpt.
////                        assertThat(body.contains(contents)).isTrue();
//                            });
//                });
//    }


}
