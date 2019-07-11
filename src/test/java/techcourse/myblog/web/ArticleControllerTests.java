package techcourse.myblog.web;

import org.apache.commons.lang3.StringEscapeUtils;
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

import static org.assertj.core.api.Java6Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void articleForm() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void create_article() {
        String title = "목적의식 있는 연습을 통한 효과적인 학습";
        String coverUrl = "https://t1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/5tdm/image/7OdaODfUPkDqDYIQKXk_ET3pfKo.jpeg";
        String contents = "나는 우아한형제들에서 우아한테크코스 교육 과정을 진행하고 있다. 우테코를 설계하면서 고민스러웠던 부분 중의 하나는 '선발 과정을 어떻게 하면 의미 있는 시간으로 만들 것인가?'였다.";

        webTestClient.post()
                .uri("/write")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.get()
                            .uri(response.getRequestHeaders().getLocation())
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .consumeWith(response2 -> {
                                String body = new String(response2.getResponseBody());
                                assertThat(body.contains(title)).isTrue();
                                assertThat(body.contains(coverUrl)).isTrue();
                                assertThat(body.contains(StringEscapeUtils.escapeJava(contents))).isTrue();
                            });
                });
    }

    @Test
    void create_article_en() {
        String title = "titleTest";
        String coverUrl = "urlTest";
        String contents = "contentsTest";
        webTestClient.post()
                .uri("/write")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.get()
                            .uri(response.getRequestHeaders().getLocation())
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .consumeWith(response2 -> {
                                String body = new String(response2.getResponseBody());
                                assertThat(body.contains(title)).isTrue();
                                assertThat(body.contains(coverUrl)).isTrue();
                                assertThat(body.contains(contents)).isTrue();
                            });
                });
    }

    @Test
    void 게시글_페이지_정상_조회() {
        String title = "titleTest";
        String coverUrl = "urlTest";
        String contents = "contentsTest";
        articleRepository.insert(new Article(title, coverUrl, contents));

        webTestClient.get()
                .uri("/articles/" + (articleRepository.size() - 1))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(title)).isTrue();
                    assertThat(body.contains(coverUrl)).isTrue();
                    assertThat(body.contains(contents)).isTrue();
                });
    }

    @Test
    void 존재하지_않는_게시글_조회_에러() {
        webTestClient.get()
                .uri("/articles/" + (articleRepository.size()))
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void 게시글_수정페이지_이동() {
        String title = "titleTest";
        String coverUrl = "urlTest";
        String contents = "contentsTest";
        articleRepository.insert(new Article(title, coverUrl, contents));

        webTestClient.get()
                .uri("/articles/0/edit")
                .exchange()
                .expectStatus().isOk()
        ;
    }

    @Test
    void 게시글_수정() {
        String newTitle = "newTile";
        String title = "title";
        String coverUrl = "coverUrl";
        String contents = "contents";
        articleRepository.insert(new Article(title, coverUrl, contents));

        webTestClient.put()
                .uri("/articles/0")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", newTitle)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(newTitle)).isTrue();
                    assertThat(body.contains(coverUrl)).isTrue();
                    assertThat(body.contains(contents)).isTrue();
                });
    }
}
