package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.dto.ArticleRequestDto;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ArticleRepository articleRepository;

    private Article newArticle;

    @BeforeEach
    void setUp() {
        ArticleRequestDto articleRequestDto = new ArticleRequestDto();
        articleRequestDto.setTitle("1");
        articleRequestDto.setCoverUrl("2");
        articleRequestDto.setContents("3");
        newArticle = Article.of(articleRequestDto);
        articleRepository.addArticle(newArticle);

    }

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
    void createArticlePost() {
        Map<String, String> requestMap = new HashMap<>();
        String title = "hello";
        String backgroundURL = "http://asdf.com";
        String content = "helloContent";

        requestMap.put("title", title);
        requestMap.put("backgroundURL", backgroundURL);
        requestMap.put("content", content);
        webTestClient.post()
                .uri("/articles")
                .body(Mono.just(requestMap), Map.class)
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void retrieveArticle() {
        webTestClient.get()
                .uri("/articles/" + newArticle.getId())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void editArticle() {
        webTestClient.get()
                .uri("/articles/" + newArticle.getId() + "/edit")
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    void editArticlePut() {
        String title = "목적의식 있는 연습을 통한 효과적인 학습";
        String coverUrl = "https://t1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/5tdm/image/7OdaODfUPkDqDYIQKXk_ET3pfKo.jpeg";
        String contents = "나는 우아한형제들에서 우아한테크코스 교육 과정을 진행하고 있다. 우테코를 설계하면서 고민스러웠던 부분 중의 하나는 '선발 과정을 어떻게 하면 의미 있는 시간으로 만들 것인가?'였다.";
        String category = "자유";

        ArticleRequestDto articleRequestDto = new ArticleRequestDto();
        articleRequestDto.setTitle("1");
        articleRequestDto.setCoverUrl("2");
        articleRequestDto.setContents("3");
        articleRequestDto.setCategory("4");

        Article article = Article.of(articleRequestDto);
        articleRepository.addArticle(article);

        ArticleRequestDto articleRequestDtoToChange = new ArticleRequestDto();
        articleRequestDtoToChange.setTitle(title);
        articleRequestDtoToChange.setCoverUrl(coverUrl);
        articleRequestDtoToChange.setContents(contents);
        articleRequestDtoToChange.setCategory(category);

        webTestClient.put()
                .uri("/articles/" + newArticle.getId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents)
                        .with("category", category))
                .exchange()
                .expectStatus().isOk();

//        webTestClient.put()
//                .uri("/articles/" + article.getId())
//                .body(Mono.just(articleRequestDtoToChange), ArticleRequestDto.class)
//                .exchange()
//                .expectStatus().isOk();
    }

    @Test
    void deleteArticle() {
        webTestClient.delete()
                .uri("/articles/" + newArticle.getId())
                .exchange()
                .expectStatus().is3xxRedirection();
    }


    @Test
    void create_article() {
        String title = "목적의식 있는 연습을 통한 효과적인 학습";
        String coverUrl = "https://t1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/5tdm/image/7OdaODfUPkDqDYIQKXk_ET3pfKo.jpeg";
        String contents = "나는 우아한형제들에서 우아한테크코스 교육 과정을 진행하고 있다. 우테코를 설계하면서 고민스러웠던 부분 중의 하나는 '선발 과정을 어떻게 하면 의미 있는 시간으로 만들 것인가?'였다.";

        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody().consumeWith(response -> {
            webTestClient.get().uri(response.getResponseHeaders().get("Location").get(0))
                    .exchange()
                    .expectBody().consumeWith(response2 -> {
                String body = new String(response2.getResponseBody());
                System.out.println(body);
                System.out.println(body.contains(title));
                System.out.println(body.contains(coverUrl));
                System.out.println(body.contains(contents));
                assertThat(body.contains(title)).isTrue();
                assertThat(body.contains(coverUrl)).isTrue();
                assertThat(body.contains(contents)).isTrue();
            });
        });
    }
}
