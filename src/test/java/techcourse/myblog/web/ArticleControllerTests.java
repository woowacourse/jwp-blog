package techcourse.myblog.web;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.repository.ArticleRepository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ExtendWith(SpringExtension.class)
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
        requestExpect(GET, "/articles/writing").isOk();
    }

    @Test
    void create_article() {
        String titleKo = "목적의식 있는 연습을 통한 효과적인 학습";
        String coverUrlKo = "https://t1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/5tdm/image/7OdaODfUPkDqDYIQKXk_ET3pfKo.jpeg";
        String contentsKo = "나는 우아한형제들에서 우아한테크코스 교육 과정을 진행하고 있다. 우테코를 설계하면서 고민스러웠던 부분 중의 하나는 '선발 과정을 어떻게 하면 의미 있는 시간으로 만들 것인가?'였다.";
        Article article = new Article(titleKo, coverUrlKo, contentsKo);

        requestExpect(POST, "/articles/write", parser(article))
                .isFound()
                .expectBody()
                .consumeWith(response ->
                        bodyCheck(
                                requestExpect(GET, getRedirectedUri(response)).isOk(),
                                applyEscapeArticle(article)));
    }

    @Test
    void create_article_en() {
        requestExpect(POST, "/articles/write", parser(articleDto.toArticle()))
                .isFound()
                .expectBody()
                .consumeWith(response ->
                        bodyCheck(
                                requestExpect(GET, getRedirectedUri(response)).isOk(),
                                applyEscapeArticle(articleDto.toArticle())));
    }

    private Article applyEscapeArticle(Article article) {
        return new Article(article.getTitle(), article.getCoverUrl(), StringEscapeUtils.escapeJava(article.getContents()));
    }

    @Test
    void 게시글_페이지_정상_조회() {
        bodyCheck(
                requestExpect(GET, "/articles/" + savedArticle.getId()).isOk(), savedArticle
        );
    }

    @Test
    void 존재하지_않는_게시글_조회_에러() {
        requestExpect(GET, "/articles/0").isEqualTo(INTERNAL_SERVER_ERROR);
    }

    @Test
    void 게시글_수정페이지_이동() {
        requestExpect(GET, "/articles/" + savedArticle.getId()).isOk();
    }

    @Test
    void 게시글_수정() {
        Article article = articleRepository.save(articleDto.toArticle());
        Article editedArticle = new Article("new title", "new url", "new contents");

        requestExpect(PUT, "/articles/" + article.getId(), parser(editedArticle))
                .isFound()
                .expectBody()
                .consumeWith(response ->
                        bodyCheck(requestExpect(GET, getRedirectedUri(response)).isOk(), editedArticle));
    }

    @Test
    void 게시글_삭제() {
        requestExpect(DELETE, "/articles/" + savedArticle.getId()).isFound();
    }

    private void bodyCheck(WebTestClient.ResponseSpec responseSpec, Article article) {
        List<String> contents = new ArrayList<>();
        contents.add(article.getTitle());
        contents.add(article.getCoverUrl());
        contents.add(article.getContents());

        super.bodyCheck(responseSpec, contents);
    }

    private MultiValueMap<String, String> parser(Article article) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("title", article.getTitle());
        multiValueMap.add("coverUrl", article.getCoverUrl());
        multiValueMap.add("contents", article.getContents());
        return multiValueMap;
    }
}
