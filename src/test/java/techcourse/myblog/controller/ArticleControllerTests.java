package techcourse.myblog.controller;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.Article;
import techcourse.myblog.repository.ArticleRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private static final String title = "목적의식 있는 연습을 통한 효과적인 학습";
    private static final String coverUrl = "https://scontent-icn1-1.xx.fbcdn.net/v/t1.0-9/1514627_858869820895635_1119508450771753991_n.jpg?_nc_cat=110&_nc_ht=scontent-icn1-1.xx&oh=a32aa56b750b212aee221da7e9711bb1&oe=5D8781A4";
    private static final String contents = "나는 우아한형제들에서 우아한테크코스(이하 우테코) 교육 과정을 진행하고 있다. 우테코를 설계하면서 고민스러웠던 부분 중의 하나는 ‘선발 과정을 어떻게 하면 의미 있는 시간으로 만들 것인가?’였다.";

    private Article article = new Article(title, coverUrl, contents);

    private WebTestClient webTestClient;
    private ArticleRepository articleRepository;

    @Autowired
    public ArticleControllerTests(WebTestClient webTestClient, ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
        this.webTestClient = webTestClient;
    }

    @BeforeEach
    void setup() {
        articleRepository.save(article);
    }

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 로그인_안_했을때_articleForm() {
        responseSpec(GET, "/articles/new")
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    void articleAddTest() {
        MultiValueMap<String, String> parsedArticleDto = new LinkedMultiValueMap<>();
        parsedArticleDto.add("title", title);
        parsedArticleDto.add("coverUrl", coverUrl);
        parsedArticleDto.add("contents", contents);

        responseSpec(POST, "/articles/write", parsedArticleDto)
                .expectStatus()
                .is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    String url = response.getResponseHeaders().get("Location").get(0);
                    webTestClient.get().uri(url)
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .consumeWith(redirectResponse -> {
                                String body = new String(redirectResponse.getResponseBody());
                                assertThat(body.contains(title)).isTrue();
                                assertThat(body.contains(StringEscapeUtils.escapeJava(contents))).isTrue();
                                assertThat(body.contains(coverUrl)).isTrue();
                            });
                });

//        webTestClient.post().uri("/articles/write")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .body(fromFormData("title", title)
//                        .with("coverUrl", coverUrl)
//                        .with("contents", contents))
//                .exchange()
//                .expectStatus().is3xxRedirection()
//                .expectBody()
//                .consumeWith(response -> {
//                    String url = response.getResponseHeaders().get("Location").get(0);
//                    webTestClient.get().uri(url)
//                            .exchange()
//                            .expectStatus().isOk()
//                            .expectBody()
//                            .consumeWith(redirectResponse -> {
//                                String body = new String(redirectResponse.getResponseBody());
//                                assertThat(body.contains(title)).isTrue();
//                                assertThat(body.contains(StringEscapeUtils.escapeJava(contents))).isTrue();
//                                assertThat(body.contains(coverUrl)).isTrue();
//                            });
//                });
    }

    private WebTestClient.ResponseSpec responseSpec(HttpMethod method, String uri) {
        return webTestClient.method(method)
                .uri(uri)
                .exchange();
    }

    private WebTestClient.ResponseSpec responseSpec(HttpMethod method, String uri, MultiValueMap<String, String> form) {

        return webTestClient.method(method)
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(form))
                .exchange();
    }

    @Test
    void findArticleByIdTest() {

        webTestClient.get().uri("/articles/" + article.getId())
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void articleUpdate() {

        webTestClient.put().uri("/articles/" + article.getId())
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
                                String body = new String(redirectResponse.getResponseBody());
                                assertThat(body.contains(title)).isTrue();
                                assertThat(body.contains(StringEscapeUtils.escapeJava(contents))).isTrue();
                                assertThat(body.contains(coverUrl)).isTrue();
                            });
                });
    }

    @Test
    void articleDelete() {

        webTestClient.delete().uri("/articles/" + article.getId())
                .exchange()
                .expectStatus().is3xxRedirection();

        webTestClient.get().uri("/article/" + article.getId())
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @AfterEach
    void tearDown() {
        articleRepository.delete(article);
    }
}
