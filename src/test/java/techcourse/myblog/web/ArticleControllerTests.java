package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
        @Autowired
        private WebTestClient webTestClient;

        @Autowired
        private ArticleRepository articleRepository;

        @BeforeEach
        void setUp() {
                articleRepository.deleteAll();
        }

        @Test
        void indexTest() {
                webTestClient.get().uri("/")
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        void writeArticleTest() {
                webTestClient.get().uri("/writing")
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        void confirmWriteTest() {
                String title = "제목title";
                String coverUrl = "링크link";
                String contents = "내용content";

                webTestClient.post().uri("/articles")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("title", title).with("coverUrl", coverUrl).with("contents", contents))
                    .exchange()
                    .expectStatus().isFound().expectHeader().valueMatches("location", "(.)*(/articles/)" + articleRepository.getLastArticleId());
        }

        @Test
        void viewArticleTest() {
                articleRepository.write(new Article());
                webTestClient.get().uri("/articles/" + articleRepository.getLastArticleId())
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        void editArticleTest() {
                articleRepository.write(new Article());
                webTestClient.get().uri("/articles/" + articleRepository.getLastArticleId() + "/edit")
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        void confirmEditTest() {
                String title = "제목title";
                String coverUrl = "링크link";
                String contents = "내용content";

                Article article = new Article(title, coverUrl, contents);
                articleRepository.write(article);
                webTestClient.put().uri("/articles/" + articleRepository.getLastArticleId())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("title", title).with("coverUrl", coverUrl).with("contents", contents))
                    .exchange()
                    .expectStatus().isFound().expectHeader().valueMatches("location", "(.)*(/articles/)" + articleRepository.getLastArticleId());
        }

        @Test
        void deleteArticleTest() {
                articleRepository.write(new Article());
                webTestClient.delete().uri("/articles/" + articleRepository.getLastArticleId())
                    .exchange()
                    .expectStatus().is3xxRedirection();
                webTestClient.get().uri("/articles/" + articleRepository.getLastArticleId())
                    .exchange()
                    .expectStatus().is5xxServerError();
        }
}