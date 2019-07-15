package techcourse.myblog.web;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.ArticleDto;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.http.HttpMethod.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ArticleRepository articleRepository;

    private ArticleDto articleDto = new ArticleDto("title", "coverUrl", "contents");

    @Test
    void index() {
        requestHttpMethod(GET, "/", null).expectStatus().isOk();
    }

    @Test
    void articleForm() {
        requestHttpMethod(GET, "/writing", null).expectStatus().isOk();
    }

    @Test
    void create_article() {
        String titleKo = "목적의식 있는 연습을 통한 효과적인 학습";
        String coverUrlKo = "https://t1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/5tdm/image/7OdaODfUPkDqDYIQKXk_ET3pfKo.jpeg";
        String contentsKo = "나는 우아한형제들에서 우아한테크코스 교육 과정을 진행하고 있다. 우테코를 설계하면서 고민스러웠던 부분 중의 하나는 '선발 과정을 어떻게 하면 의미 있는 시간으로 만들 것인가?'였다.";
        ArticleDto articleKoDto = new ArticleDto(titleKo, coverUrlKo, contentsKo);
        ArticleDto articleApplyEscape = new ArticleDto(titleKo, coverUrlKo, StringEscapeUtils.escapeJava(contentsKo));

        requestHttpMethod(POST, "/write", createArticleForm(articleKoDto))
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response ->
                        checkContainArticle(
                                requestHttpMethod(GET, response.getResponseHeaders().get("Location").get(0), null),
                                articleApplyEscape));
    }

    @Test
    void create_article_en() {
        requestHttpMethod(POST, "/write", createArticleForm(articleDto))
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response ->
                        checkContainArticle(
                                requestHttpMethod(GET, response.getResponseHeaders().get("Location").get(0), null),
                                articleDto));
    }

    @Test
    void 게시글_페이지_정상_조회() {
        Article article = articleRepository.save(articleDto);
        checkContainArticle(
                requestHttpMethod(GET, "/articles/" + article.getId(), null),
                articleDto);
    }

    @Test
    void 존재하지_않는_게시글_조회_에러() {
        requestHttpMethod(GET, "/articles/" + getNotPresentId(), null)
                .expectStatus().is5xxServerError();
    }

    @Test
    void 게시글_수정페이지_이동() {
        Article article = articleRepository.save(articleDto);
        requestHttpMethod(GET, "/articles/" + article.getId() + "/edit", null)
                .expectStatus().isOk();
    }

    @Test
    void 게시글_수정() {
        Article article = articleRepository.save(articleDto);
        ArticleDto editedArticle = new ArticleDto("new title", "new url", "new contents");

        requestHttpMethod(PUT, "/articles/" + article.getId(), createArticleForm(editedArticle))
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response ->
                        checkContainArticle(
                                requestHttpMethod(GET, response.getResponseHeaders().get("Location").get(0), null),
                                editedArticle));
    }

    @Test
    void 게시글_삭제() {
        Article article = articleRepository.save(articleDto);

        requestHttpMethod(DELETE, "/articles/" + article.getId(), null)
                .expectStatus().isFound();
    }

    private WebTestClient.ResponseSpec requestHttpMethod(HttpMethod method, String uri,
                                                         BodyInserters.FormInserter<String> form) {
        return webTestClient.method(method)
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .exchange()
                ;
    }

    private BodyInserters.FormInserter<String> createArticleForm(ArticleDto articleDto) {
        return BodyInserters
                .fromFormData("title", articleDto.getTitle())
                .with("coverUrl", articleDto.getCoverUrl())
                .with("contents", articleDto.getContents());
    }

    private int getNotPresentId() {
        return 1 + articleRepository.findAll().stream()
                .map(Article::getId)
                .max(Integer::compare)
                .orElse(-1);
    }

    private void checkContainArticle(WebTestClient.ResponseSpec responseSpec, ArticleDto articleDto) {
        responseSpec
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(articleDto.getTitle())).isTrue();
                    assertThat(body.contains(articleDto.getCoverUrl())).isTrue();
                    assertThat(body.contains(articleDto.getContents())).isTrue();
                });
    }
}
