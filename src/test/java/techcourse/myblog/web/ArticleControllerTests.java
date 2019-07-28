package techcourse.myblog.web;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.article.ArticleDto;

import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application_test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private static String title = "TEST";
    private static String coverUrl = "https://img.com";
    private static String contents = "testtest";
    private static String categoryId = "1";

    @Autowired
    private WebTestClient webTestClient;

    private static long articleId;

    @BeforeEach
    void setUp() {
        webTestClient.post()
                .uri("/articles/new")
                .body(createFormData(ArticleDto.class, title, coverUrl, contents, categoryId))
                .exchange()
                .expectHeader()
                .value("location", s -> {
                    articleId = Long.parseLong(s.split("/")[4]);
                });
    }

    @Test
    public void showArticleById() {
        webTestClient.get().uri("/articles/" + articleId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void updateArticleById() {
        webTestClient.get().uri("/articles/" + articleId + "/edit")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(title)).isTrue();
                    assertThat(body.contains(coverUrl)).isTrue();
                    assertThat(body.contains(contents)).isTrue();
                    assertThat(body.contains(categoryId)).isTrue();
                });
    }

    @Test
    public void showWritingPage() {
        webTestClient.get().uri("/articles/new")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void addDeleteArticle() {
        AtomicLong addId = new AtomicLong();
        webTestClient.post()
                .uri("/articles/new")
                .body(createFormData(ArticleDto.class, title, coverUrl, contents, categoryId))
                .exchange()
                .expectHeader()
                .value("location", s -> {
                    addId.set(Long.parseLong(s.split("/")[4]));
                });

        webTestClient.delete()
                .uri("/articles/" + addId.get())
                .exchange()
                .expectStatus().isFound();
    }

    @AfterEach
    void tearDown() {
        webTestClient.delete()
                .uri("/articles/" + articleId)
                .exchange()
                .expectStatus().isFound();
    }

    private <T> BodyInserters.FormInserter<String> createFormData(Class<T> classType, String... parameters) {
        BodyInserters.FormInserter<String> body = BodyInserters.fromFormData(Strings.EMPTY, Strings.EMPTY);

        for (int i = 1; i < classType.getDeclaredFields().length; i++) {
            System.out.println(classType.getDeclaredFields()[i].getName());
            body.with(classType.getDeclaredFields()[i].getName(), parameters[i - 1]);
        }

        return body;
    }
}
