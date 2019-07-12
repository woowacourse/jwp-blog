package techcourse.myblog.web;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AutoConfigureWebTestClient
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
        webTestClient.get().uri("/articles/new")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void articleAddTest() {
        String title = "목적의식 있는 연습을 통한 효과적인 학습";
        String coverUrl = "https://scontent-icn1-1.xx.fbcdn.net/v/t1.0-9/1514627_858869820895635_1119508450771753991_n.jpg?_nc_cat=110&_nc_ht=scontent-icn1-1.xx&oh=a32aa56b750b212aee221da7e9711bb1&oe=5D8781A4";
        String contents = "나는 우아한형제들에서 우아한테크코스(이하 우테코) 교육 과정을 진행하고 있다. 우테코를 설계하면서 고민스러웠던 부분 중의 하나는 ‘선발 과정을 어떻게 하면 의미 있는 시간으로 만들 것인가?’였다.";

        webTestClient.post().uri("/write")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    String url = response.getResponseHeaders().get("Location").get(0);
                    webTestClient.get().uri(url)
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .consumeWith(redirectResponse -> {
                                try {
                                    String body = new String(redirectResponse.getResponseBody(), "UTF-8");
                                    assertThat(body.contains(title)).isTrue();
                                    assertThat(body.contains(StringEscapeUtils.escapeJava(contents))).isTrue();
                                    assertThat(body.contains(coverUrl)).isTrue();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            });
                });
    }

    @Test
    void articleUpdate() {
        String title = "목적의식 있는 연습을 통한 효과적인 학습";
        String coverUrl = "https://scontent-icn1-1.xx.fbcdn.net/v/t1.0-9/1514627_858869820895635_1119508450771753991_n.jpg?_nc_cat=110&_nc_ht=scontent-icn1-1.xx&oh=a32aa56b750b212aee221da7e9711bb1&oe=5D8781A4";
        String contents = "나는 우아한형제들에서 우아한테크코스(이하 우테코) 교육 과정을 진행하고 있다. 우테코를 설계하면서 고민스러웠던 부분 중의 하나는 ‘선발 과정을 어떻게 하면 의미 있는 시간으로 만들 것인가?’였다.";

        Article testArticle = new Article();
        testArticle.setTitle("title");
        testArticle.setCoverUrl("coverUrl");
        testArticle.setContents("contents");
        articleRepository.add(testArticle);

        webTestClient.put().uri("/articles/0")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    String url = response.getResponseHeaders().get("Location").get(0);
                    webTestClient.get().uri(url)
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .consumeWith(redirectResponse -> {
                                try {
                                    String body = new String(redirectResponse.getResponseBody(), "UTF-8");
                                    assertThat(body.contains(title)).isTrue();
                                    assertThat(body.contains(StringEscapeUtils.escapeJava(contents))).isTrue();
                                    assertThat(body.contains(coverUrl)).isTrue();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            });
                });
    }

    @Test
    void articleDelete() {
        Article testArticle = new Article();
        testArticle.setTitle("title");
        testArticle.setCoverUrl("coverUrl");
        testArticle.setContents("contents");
        articleRepository.add(testArticle);

        webTestClient.delete().uri("/articles/0")
                .exchange()
                .expectStatus().is3xxRedirection();

        assertThat(articleRepository.size()).isEqualTo(0);
    }
}
