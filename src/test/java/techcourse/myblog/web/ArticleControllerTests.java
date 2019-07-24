package techcourse.myblog.web;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.repository.ArticleRepository;
import techcourse.myblog.dto.ArticleDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class ArticleControllerTests extends ControllerTestTemplate {
    @Autowired
    private ArticleRepository articleRepository;

    private ArticleDto articleDto = new ArticleDto("title", "coverUrl", "contents");
    private Article savedArticle;

    @BeforeEach
    void setup() {
        savedArticle = articleRepository.save(articleDto.toArticle());
    }

    @Test
    void articleForm() {
        httpRequest(GET, "/articles/writing").isOk();
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("articlesStream")
    void 게시글_작성_테스트(Article article) {
        String redirectUrl = requestAndGetRedirectUrl(POST, "/articles/write", parser(article));
        String responseBody = requestAndGetBody(GET, redirectUrl);

        Article escapedArticle = applyEscapeArticle(article);

        assertThat(responseBody).contains(escapedArticle.getTitle());
        assertThat(responseBody).contains(escapedArticle.getCoverUrl());
        assertThat(responseBody).contains(escapedArticle.getContents());
    }

    static Stream<Article> articlesStream() throws Throwable {
        return Stream.of(
                new Article("article_en", "url_en", "contents_en"),
                new Article("목적의식 있는 연습을 통한 효과적 학습",
                        "https://t1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/5tdm/image/7OdaODfUPkDqDYIQKXk_ET3pfKo.jpeg",
                        "나는 우아한형제들에서 우아한테크코스 교육 과정을 진행하고 있다. 우테코를 설계하면서 고민스러웠던 부분 중의 하나는 '선발 과정을 어떻게 하면 의미 있는 시간으로 만들 것인가?'였다.")
        );
    }

    private Article applyEscapeArticle(Article article) {
        return new Article(article.getTitle(), article.getCoverUrl(), StringEscapeUtils.escapeJava(article.getContents()));
    }

    @Test
    void 게시글_페이지_정상_조회() {
        String responseBody = requestAndGetBody(GET, "/articles/" + savedArticle.getId());

        assertThat(responseBody).contains(savedArticle.getTitle());
        assertThat(responseBody).contains(savedArticle.getCoverUrl());
        assertThat(responseBody).contains(savedArticle.getContents());
    }

    @Test
    void 존재하지_않는_게시글_조회_에러() {
        httpRequest(GET, "/articles/0").isEqualTo(INTERNAL_SERVER_ERROR);
    }

    @Test
    void 게시글_수정페이지_이동() {
        httpRequest(GET, "/articles/" + savedArticle.getId()).isOk();
    }

    @Test
    void 게시글_수정() {
        Article editedArticle = new Article("new title", "new url", "new contents");

        String redirectUrl = requestAndGetRedirectUrl(PUT, "/articles/" + savedArticle.getId(), parser(editedArticle));
        String responseBody = requestAndGetBody(GET, redirectUrl);

        assertThat(responseBody).contains(editedArticle.getTitle());
        assertThat(responseBody).contains(editedArticle.getCoverUrl());
        assertThat(responseBody).contains(editedArticle.getContents());
    }

    @Test
    void 게시글_삭제() {
        httpRequest(DELETE, "/articles/" + savedArticle.getId()).isFound();
    }

    private MultiValueMap<String, String> parser(Article article) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("title", article.getTitle());
        multiValueMap.add("coverUrl", article.getCoverUrl());
        multiValueMap.add("contents", article.getContents());
        return multiValueMap;
    }
}
