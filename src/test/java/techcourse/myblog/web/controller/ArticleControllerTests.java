package techcourse.myblog.web.controller;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.web.controller.common.ControllerTestTemplate;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpMethod.*;

public class ArticleControllerTests extends ControllerTestTemplate {
    private UserDto authorDto = savedUserDto;
    private ArticleDto articleDto = new ArticleDto("title", "coverUrl", "contents");
    private ArticleDto editArticleDto = new ArticleDto("new title", "new url", "new contents");
    private String savedArticleUrl;

    static Stream<ArticleDto> articlesStream() {
        return Stream.of(
                new ArticleDto("article_en", "url_en", "contents_en"),
                new ArticleDto("목적의식 있는 연습을 통한 효과적 학습",
                        "https://t1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/5tdm/image/7OdaODfUPkDqDYIQKXk_ET3pfKo.jpeg",
                        "나는 우아한형제들에서 우아한테크코스 교육 과정을 진행하고 있다. 우테코를 설계하면서 고민스러웠던 부분 중의 하나는 '선발 과정을 어떻게 하면 의미 있는 시간으로 만들 것인가?'였다.")
        );
    }

    @BeforeEach
    protected void setup() {
        super.setup();
        savedArticleUrl = getRedirectUrl(loginAndRequest(authorDto, POST, "/articles/write", parseArticle(articleDto)));
    }

    @Test
    void 로그인상태_게시글_작성_페이지_요청() {
        loginAndRequest(authorDto, GET, "/articles/writing").isOk();
    }

    @Test
    void 로그아웃상태_게시글_작성_페이지_요청_리다이렉트() {
        checkLoginRedirect("/articles/writing");
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("articlesStream")
    void 로그인상태_게시글_작성_테스트(ArticleDto articleDto) {
        String loginWriteArticleRedirectUrl = getRedirectUrl(
                loginAndRequest(authorDto, POST, "/articles/write", parseArticle(articleDto)));
        String articlePage = getResponseBody((loginAndRequest(authorDto, GET, loginWriteArticleRedirectUrl)));

        checkPageContainArticle(applyEscapeArticle(articleDto), articlePage);
    }

    private void checkPageContainArticle(ArticleDto articleDto, String responseBody) {
        assertThat(responseBody).contains(articleDto.getTitle());
        assertThat(responseBody).contains(articleDto.getCoverUrl());
        assertThat(responseBody).contains(articleDto.getContents());
    }

    private ArticleDto applyEscapeArticle(ArticleDto articleDto) {
        return new ArticleDto(articleDto.getTitle(), articleDto.getCoverUrl(), StringEscapeUtils.escapeJava(articleDto.getContents()));
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("articlesStream")
    void 로그아웃상태_게시글_작성_테스트_리다이렉트(ArticleDto articleDto) {
        String redirectUrl = getRedirectUrl(httpRequest(POST, "/articles/write", parseArticle(articleDto)));
        assertEquals(redirectUrl, "/login");
    }

    @Test
    void 로그아웃상태_게시글_페이지_조회_불가_리다이렉트() {
        checkLoginRedirect(savedArticleUrl);
    }

    @Test
    void 로그인상태_게시글_페이지_정상_조회() {
        String articlePage = getResponseBody(loginAndRequest(authorDto, GET, savedArticleUrl));
        checkPageContainArticle(articleDto, articlePage);
    }

    @Test
    void 존재하지_않는_게시글_조회_에러() {
        String notExistArticleRedirectUrl = getRedirectUrl(loginAndRequest(authorDto, GET, "/articles/0"));
        assertEquals(notExistArticleRedirectUrl, "/");
    }

    @Test
    void 로그아웃상태_게시글_수정페이지_요청_리다이렉트() {
        checkLoginRedirect(savedArticleUrl + "/edit");
    }

    @Test
    void 로그인상태_게시글_수정페이지_요청() {
        loginAndRequest(authorDto, GET, savedArticleUrl + "/edit").isOk();
    }

    @Test
    void 로그인상태_다른_유저_게시글_수정페이지_이동() {
        String otherEditArticleRedirectUrl = getRedirectUrl(loginAndRequest(otherUserDto, GET, savedArticleUrl + "/edit"));

        assertEquals(otherEditArticleRedirectUrl, "/");
    }

    @Test
    void 로그아웃상태_게시글_수정_요청_리다이렉트() {
        String logoutEditArticleRedirectUrl = getRedirectUrl(httpRequest(PUT, savedArticleUrl, parseArticle(editArticleDto)));
        assertEquals(logoutEditArticleRedirectUrl, "/login");
    }

    @Test
    void 로그인상태_본인_게시글_수정_요청_성공() {
        String authorEditArticleRedirectUrl = getRedirectUrl(loginAndRequest(authorDto, PUT, savedArticleUrl, parseArticle(editArticleDto)));
        String articlePage = getResponseBody(loginAndRequest(authorDto, GET, authorEditArticleRedirectUrl));

        checkPageContainArticle(editArticleDto, articlePage);
    }

    @Test
    void 로그인상태_다른_유저_게시글_수정_요청_리다이렉트() {
        String otherUserEditArticleRedirectUrl = getRedirectUrl(loginAndRequest(otherUserDto, PUT, savedArticleUrl, parseArticle(editArticleDto)));

        assertEquals(otherUserEditArticleRedirectUrl, "/");
    }

    @Test
    void 로그아웃상태_게시글_삭제_요청_리다이렉트() {
        String logoutDeleteArticleRedirectUrl = getRedirectUrl(httpRequest(DELETE, savedArticleUrl));
        assertEquals(logoutDeleteArticleRedirectUrl, "/login");
    }

    @Test
    void 로그인상태_본인_게시글_삭제_성공() {
        String authorDeleteArticleRedirectUrl = getRedirectUrl(loginAndRequest(authorDto, DELETE, savedArticleUrl));
        assertEquals(authorDeleteArticleRedirectUrl, "/");

        String redirectRemovedArticleUrl = getRedirectUrl(loginAndRequest(authorDto, GET, savedArticleUrl));
        assertEquals(redirectRemovedArticleUrl, "/");
    }

    @Test
    void 로그인상태_다른_유저_게시글_삭제_불가() {
        String otherUserDeleteArticleRedirectUrl = getRedirectUrl(loginAndRequest(otherUserDto, DELETE, savedArticleUrl));
        assertEquals(otherUserDeleteArticleRedirectUrl, "/");

        String articlePage = getResponseBody(loginAndRequest(otherUserDto, GET, savedArticleUrl));
        checkPageContainArticle(articleDto, articlePage);
    }

    private void checkLoginRedirect(String url) {
        String redirectUrl = getRedirectUrl(httpRequest(GET, url));
        assertEquals(redirectUrl, "/login");
    }
}
