package techcourse.myblog.presentation.controller;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import techcourse.myblog.application.UserAssembler;
import techcourse.myblog.application.dto.UserRequestDto;
import techcourse.myblog.domain.article.ArticleFeature;
import techcourse.myblog.presentation.controller.common.ControllerTestTemplate;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpMethod.*;
import static techcourse.myblog.utils.ArticleTestObjects.ARTICLE_FEATURE;
import static techcourse.myblog.utils.ArticleTestObjects.UPDATE_ARTICLE_FEATURE;
import static techcourse.myblog.utils.UserTestObjects.READER_DTO;

public class ArticleControllerTests extends ControllerTestTemplate {
    private ArticleFeature articleFeature;
    private String savedArticleUrl;

    @BeforeEach
    protected void setup() {
        super.setup();
        articleFeature = ARTICLE_FEATURE;
        savedArticleUrl = getRedirectUrl(loginAndRequestWithDataWriter(POST, "/articles/write", parseArticle(articleFeature)));
    }

    @Test
    void 로그인상태_게시글_작성_페이지_요청() {
        loginAndRequestWriter(GET, "/articles/writing").isOk();
    }

    @Test
    void 로그아웃상태_게시글_작성_페이지_요청() {
        checkLoginRedirect("/articles/writing");
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("articlesStream")
    void 로그인상태_게시글_작성_테스트(ArticleFeature articleFeature) {
        String redirectUrl = getRedirectUrl(loginAndRequestWithDataWriter(POST, "/articles/write", parseArticle(articleFeature)));
        String responseBody = getResponseBody((loginAndRequestWriter(GET, redirectUrl)));

        ArticleFeature escapedArticle = applyEscapeArticle(articleFeature);
    
        containData(responseBody, escapedArticle);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("articlesStream")
    void 로그아웃상태_게시글_작성_테스트(ArticleFeature articleFeature) {
        String redirectUrl = getRedirectUrl(httpRequestWithData(POST, "/articles/write", parseArticle(articleFeature)));
        assertEquals(redirectUrl, "/login");
    }

    static Stream<ArticleFeature> articlesStream() {
        return Stream.of(
                new ArticleFeature("article_en", "url_en", "contents_en"),
                new ArticleFeature("목적의식 있는 연습을 통한 효과적 학습",
                        "https://t1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/application/user/5tdm/image/7OdaODfUPkDqDYIQKXk_ET3pfKo.jpeg",
                        "나는 우아한형제들에서 우아한테크코스 교육 과정을 진행하고 있다. 우테코를 설계하면서 고민스러웠던 부분 중의 하나는 '선발 과정을 어떻게 하면 의미 있는 시간으로 만들 것인가?'였다.")
        );
    }

    private ArticleFeature applyEscapeArticle(ArticleFeature articleFeature) {
        return new ArticleFeature(articleFeature.getTitle(), articleFeature.getCoverUrl(), StringEscapeUtils.escapeJava(articleFeature.getContents()));
    }

    @Test
    void 로그아웃상태_게시글_페이지_조회_불가_리다이렉트() {
        checkLoginRedirect(savedArticleUrl);
    }

    @Test
    void 로그인상태_게시글_페이지_정상_조회() {
        String responseBody = getResponseBody(loginAndRequestWriter(GET, savedArticleUrl));
    
        containData(responseBody, articleFeature);
    }

    @Test
    void 존재하지_않는_게시글_조회_에러() {
        loginAndRequest(GET, "/articles/0", HttpStatus.NOT_FOUND, savedUserRequestDto)
                .consumeWith(response -> {
                    String result = new String(response.getResponseBody());
                    assertTrue(result.contains(NOT_FOUND_ARTICLE_EXCEPTION_MESSAGE));
                });
    }

    @Test
    void 로그아웃상태_게시글_수정페이지_이동() {
        checkLoginRedirect(savedArticleUrl + "/edit");
    }

    @Test
    void 로그인상태_게시글_수정페이지_이동() {
        loginAndRequestWriter(GET, savedArticleUrl + "/edit").isOk();
    }

    @Test
    void 로그인상태_다른_유저_게시글_수정페이지_이동() {
        UserRequestDto other = READER_DTO;
        userRepository.save(UserAssembler.toEntity(other));

        loginAndRequest(GET, savedArticleUrl + "/edit", HttpStatus.FORBIDDEN, other)
                .consumeWith(response -> {
                    String result = new String(response.getResponseBody());
                    assertTrue(result.contains(MISMATCH_ARTICLE_AUTHOR_EXCEPTION_MESSAGE));
                });
    }

    @Test
    void 로그아웃상태_게시글_수정_요청() {
        ArticleFeature editedArticleFeature = new ArticleFeature("new title", "new url", "new contents");

        String redirectUrl = getRedirectUrl(httpRequestWithData(PUT, savedArticleUrl, parseArticle(editedArticleFeature)));
        assertEquals(redirectUrl, "/login");
    }

    @Test
    void 로그인상태_게시글_수정_요청() {
        ArticleFeature editedArticleFeature = new ArticleFeature("new title", "new url", "new contents");

        String redirectUrl = getRedirectUrl(loginAndRequestWithDataWriter(PUT, savedArticleUrl, parseArticle(editedArticleFeature)));
        String responseBody = getResponseBody(loginAndRequestWriter(GET, redirectUrl));
    
        containData(responseBody, editedArticleFeature);
    }

    @Test
    void 로그인상태_다른_유저_게시글_수정_요청() {
        UserRequestDto other = READER_DTO;
        userRepository.save(UserAssembler.toEntity(other));

        loginAndRequestWithData(PUT, savedArticleUrl, HttpStatus.FORBIDDEN, parseArticle(UPDATE_ARTICLE_FEATURE), other)
                .consumeWith(response -> {
                    String result = new String(response.getResponseBody());
                    assertTrue(result.contains(MISMATCH_ARTICLE_AUTHOR_EXCEPTION_MESSAGE));
                });

        String responseBody = getResponseBody(loginAndRequestWriter(GET, savedArticleUrl));
        containData(responseBody, articleFeature);
    }

    @Test
    void 로그아웃상태_게시글_삭제_요청() {
        String redirectUrl = getRedirectUrl(httpRequest(DELETE, savedArticleUrl));
        assertEquals(redirectUrl, "/login");
    }

    @Test
    void 로그인상태_게시글_삭제_요청() {
        String redirectUrl = getRedirectUrl(loginAndRequestWriter(DELETE, savedArticleUrl));
        assertEquals(redirectUrl, "/");

        loginAndRequest(GET, savedArticleUrl, HttpStatus.NOT_FOUND, savedUserRequestDto)
                .consumeWith(response -> {
                    String result = new String(response.getResponseBody());
                    assertTrue(result.contains(NOT_FOUND_ARTICLE_EXCEPTION_MESSAGE));
                });
    }

    @Test
    void 로그인상태_다른_유저_게시글_삭제_요청() {
        UserRequestDto other = new UserRequestDto("ab", "1@1.com", "1234asdf!A");
        userRepository.save(UserAssembler.toEntity(other));

        loginAndRequest(DELETE, savedArticleUrl, HttpStatus.FORBIDDEN, other)
                .consumeWith(response -> {
                    String result = new String(response.getResponseBody());
                    assertTrue(result.contains(MISMATCH_ARTICLE_AUTHOR_EXCEPTION_MESSAGE));
                });

        String responseBody = getResponseBody(loginAndRequestWriter(GET, savedArticleUrl));
        containData(responseBody, articleFeature);
    }
    
    private void containData(String responseBody, ArticleFeature articleFeature) {
        assertThat(responseBody).contains(articleFeature.getTitle());
        assertThat(responseBody).contains(articleFeature.getCoverUrl());
        assertThat(responseBody).contains(articleFeature.getContents());
    }
    
    private void checkLoginRedirect(String url) {
        String redirectUrl = getRedirectUrl(httpRequest(GET, url));
        assertEquals(redirectUrl, "/login");
    }
}
