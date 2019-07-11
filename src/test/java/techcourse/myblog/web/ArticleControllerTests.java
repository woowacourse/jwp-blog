package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ArticleRepository articleRepository;

    private static List<Article> articles;
    private static boolean isFirstSetUp = true;

    @BeforeEach
    void setUp() {
        if (!isFirstSetUp)
            return;
        articles = Arrays.asList(
                new Article("title_1", "contents_1", "background_1")
                , new Article("title_2", "contents_2", "background_2")
                , new Article("title_3", "contents_3", "background_3"));
        for (Article article : articles) {
            articleRepository.addBlog(article);
        }
        isFirstSetUp = false;
    }

    @Test
    void index() {
        webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void Article_생성_Form_테스트() {
        webTestClient.get()
                .uri("/writing")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void Article_생성_테스트() {
        int expectedIndex = articleRepository.findAll().size();
        String title = "목적의식 있는 연습을 통한 효과적인 학습";
        String coverUrl = "https://t1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/5tdm/image/7OdaODfUPkDqDYIQKXk_ET3pfKo.jpeg";
        String contents = "나는 우아한형제들에서 우아한테크코스 교육 과정을 진행하고 있다. 우테코를 설계하면서 고민스러웠던 부분 중의 하나는 '선발 과정을 어떻게 하면 의미 있는 시간으로 만들 것인가?'였다.";

        webTestClient.post()
                .uri("/articles")
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection();
        Article article = articleRepository.find(expectedIndex);
        assertThat(article.getTitle()).isEqualTo(title);
        assertThat(article.getContents()).isEqualTo(contents);
        assertThat(article.getBackground()).isEqualTo(coverUrl);
    }

    @Test
    void Article_조회_테스트() {
        Article target = articleRepository.find(0);
        webTestClient.get()
                .uri("/articles/0")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(target.getTitle())).isTrue();
                    assertThat(body.contains(target.getContents())).isTrue();
                    assertThat(body.contains(target.getBackground())).isTrue();
                });
    }

    @Test
    void Article_수정_Form_테스트() {
        Article target = articleRepository.find(0);
        webTestClient.get()
                .uri("/articles/0/edit")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    System.out.println(body);
                    assertThat(body.contains(target.getTitle())).isTrue();
                    assertThat(body.contains(target.getContents())).isTrue();
                    assertThat(body.contains(target.getBackground())).isTrue();
                });
    }

    @Test
    void Article_수정_테스트() {
        String postTitle = "1_title";
        String postCoverUrl = "1_coverUrl";
        String postContents = "1_contents";
        webTestClient.put()
                .uri("/articles/0")
                .body(BodyInserters
                        .fromFormData("title", postTitle)
                        .with("coverUrl", postCoverUrl)
                        .with("contents", postContents))
                .exchange()
                .expectStatus().is3xxRedirection();
        Article target = articleRepository.find(0);
        assertThat(target.getTitle()).isEqualTo(postTitle);
        assertThat(target.getContents()).isEqualTo(postContents);
        assertThat(target.getBackground()).isEqualTo(postCoverUrl);
    }

    @Test
    void Article_삭제_테스트() {
        int expectedSize = articleRepository.findAll().size() - 1;
        webTestClient.delete()
                .uri("/articles/0")
                .exchange()
                .expectStatus().is3xxRedirection();
        assertThat(articleRepository.findAll().size()).isEqualTo(expectedSize);
    }
}