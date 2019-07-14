package techcourse.myblog.web;

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

    private String title = "title";
    private String coverUrl = "coverUrl";
    private String contents = "contents";

    @Test
    void index() {
        requestGet("/").expectStatus().isOk();
    }

    @Test
    void articleForm() {
        requestGet("/writing").expectStatus().isOk();
    }

    @Test
    void create_article() {
        String titleKo = "목적의식 있는 연습을 통한 효과적인 학습";
        String coverUrlKo = "https://t1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/5tdm/image/7OdaODfUPkDqDYIQKXk_ET3pfKo.jpeg";
        String contentsKo = "나는 우아한형제들에서 우아한테크코스 교육 과정을 진행하고 있다. 우테코를 설계하면서 고민스러웠던 부분 중의 하나는 선발 과정을 어떻게 하면 의미 있는 시간으로 만들 것인가였다.";
        Article article = new Article(articleRepository.nextId(), titleKo, coverUrlKo, contentsKo);

        requestPost("/write", createArticleForm(article))
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response ->
                    checkContainArticle(requestGet(response.getResponseHeaders().get("Location").get(0)), article)
                );
    }

    @Test
    void create_article_en() {
        Article article = new Article(articleRepository.nextId(), title, coverUrl, contents);

        requestPost("/write", createArticleForm(article))
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response ->
                    checkContainArticle(requestGet(response.getResponseHeaders().get("Location").get(0)), article)
                );
    }

    @Test
    void 게시글_페이지_정상_조회() {
        Article article = insertArticle(title, coverUrl, contents);
        checkContainArticle(requestGet("/articles/" + article.getId()), article);
    }

    @Test
    void 존재하지_않는_게시글_조회_에러() {
        requestGet("/articles/" + articleRepository.nextId())
                .expectStatus().is5xxServerError();
    }

    @Test
    void 게시글_수정페이지_이동() {
        Article article = insertArticle(title, coverUrl, contents);
        requestGet("/articles/" + article.getId() + "/edit")
                .expectStatus().isOk();
    }

    @Test
    void 게시글_수정() {
        Article article = insertArticle(title, coverUrl, contents);
        Article editedArticle = new Article(article.getId(), "new title", coverUrl, contents);

        checkContainArticle(
                requestPut("/articles/" + article.getId(), createArticleForm(editedArticle)),
                editedArticle);
    }

    @Test
    void 게시글_삭제() {
        Article article = insertArticle(title, coverUrl, contents);

        webTestClient.delete()
                .uri("/articles/" + article.getId())
                .exchange()
                .expectStatus().isFound()
        ;
    }

    private Article insertArticle(String title, String url, String contents) {
        Article article = new Article(articleRepository.nextId(), title, url, contents);
        articleRepository.insert(article);
        return article;
    }

    private void checkContainArticle(WebTestClient.ResponseSpec responseSpec, Article article) {
        responseSpec
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(article.getTitle())).isTrue();
                    assertThat(body.contains(article.getCoverUrl())).isTrue();
                    assertThat(body.contains(article.getContents())).isTrue();
                });
    }

    private BodyInserters.FormInserter<String> createArticleForm(Article article) {
        return BodyInserters
                .fromFormData("title", article.getTitle())
                .with("coverUrl", article.getCoverUrl())
                .with("contents", article.getContents());
    }

    private WebTestClient.ResponseSpec requestGet(String uri) {
        return webTestClient.get()
                .uri(uri)
                .exchange()
                ;
    }

    private WebTestClient.ResponseSpec requestPost(String uri, BodyInserters.FormInserter<String> form) {
        return webTestClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .exchange()
                ;
    }

    private WebTestClient.ResponseSpec requestPut(String uri, BodyInserters.FormInserter<String> form) {
        return webTestClient.put()
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .exchange()
                ;
    }
}
