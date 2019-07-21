package techcourse.myblog.web;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.repository.ArticleRepository;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(SpringExtension.class)
public class ArticleControllerTests extends WebClientGenerator {
    @Autowired
    private ArticleRepository articleRepository;

    private ArticleDto articleDto = new ArticleDto("title", "coverUrl", "contents");
    private Article saved;

    @BeforeEach
    void setup() {
        if (saved == null) {
            saved = articleRepository.save(articleDto.toArticle());
        }
    }

    @Test
    void articleForm() {
        requestAndExpectStatus(GET, "/articles/writing", new LinkedMultiValueMap<>(), OK);
    }

    @Test
    void create_article() {
        String titleKo = "목적의식 있는 연습을 통한 효과적인 학습";
        String coverUrlKo = "https://t1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/5tdm/image/7OdaODfUPkDqDYIQKXk_ET3pfKo.jpeg";
        String contentsKo = "나는 우아한형제들에서 우아한테크코스 교육 과정을 진행하고 있다. 우테코를 설계하면서 고민스러웠던 부분 중의 하나는 '선발 과정을 어떻게 하면 의미 있는 시간으로 만들 것인가?'였다.";
        Article article = new Article(titleKo, coverUrlKo, contentsKo);
        Article articleApplyEscape = new Article(titleKo, coverUrlKo, StringEscapeUtils.escapeJava(contentsKo));

        requestAndExpectStatus(POST, "/articles/write", parser(article), FOUND)
                .expectBody()
                .consumeWith(response ->
                        bodyCheck(
                                requestAndExpectStatus(GET, getRedirectedUri(response), OK),
                                articleApplyEscape));
    }

    @Test
    void create_article_en() {
        requestAndExpectStatus(POST, "/articles/write", parser(articleDto.toArticle()), FOUND)
                .expectBody()
                .consumeWith(response ->
                        bodyCheck(requestAndExpectStatus(GET, getRedirectedUri(response), OK), articleDto.toArticle()));
    }

    @Test
    void 게시글_페이지_정상_조회() {
        bodyCheck(
                requestAndExpectStatus(GET, "/articles/" + saved.getId(), new LinkedMultiValueMap<>(), OK), saved
        );
    }

    @Test
    void 존재하지_않는_게시글_조회_에러() {
        requestAndExpectStatus(GET, "/articles/0", INTERNAL_SERVER_ERROR);
    }

    @Test
    void 게시글_수정페이지_이동() {
        requestAndExpectStatus(GET, "/articles/" + saved.getId(), OK);
    }

    @Test
    void 게시글_수정() {
        Article article = articleRepository.save(articleDto.toArticle());
        Article editedArticle = new Article("new title", "new url", "new contents");

        requestAndExpectStatus(PUT, "/articles/" + article.getId(), parser(editedArticle), FOUND)
                .expectBody()
                .consumeWith(response ->
                        bodyCheck(requestAndExpectStatus(GET, getRedirectedUri(response), OK), editedArticle));
    }

    @Test
    void 게시글_삭제() {
        Article article = articleRepository.save(articleDto.toArticle());

        requestAndExpectStatus(DELETE, "/articles/" + article.getId(), FOUND);
    }

    private String getRedirectedUri(EntityExchangeResult<byte[]> response) {
        return response.getResponseHeaders().get("Location").get(0);
    }

    private void bodyCheck(WebTestClient.ResponseSpec responseSpec, Article article) {
        responseSpec
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(article.getTitle())).isTrue();
                    assertThat(body.contains(article.getCoverUrl())).isTrue();
                    assertThat(body.contains(article.getContents())).isTrue();
                });
    }

    private MultiValueMap<String, String> parser(Article article) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("title", article.getTitle());
        multiValueMap.add("coverUrl", article.getCoverUrl());
        multiValueMap.add("contents", article.getContents());
        return multiValueMap;
    }
}
