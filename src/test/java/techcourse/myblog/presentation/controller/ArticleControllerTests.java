package techcourse.myblog.presentation.controller;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.presentation.controller.common.ControllerTestTemplate;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpMethod.*;
import static techcourse.myblog.utils.ArticleTestObjects.ARTICLE_DTO;
import static techcourse.myblog.utils.UserTestObjects.READER_DTO;

//TODO : ResponseEntity로 인한 실패 => 테스트로 바꾸기
public class ArticleControllerTests extends ControllerTestTemplate {
    private ArticleDto articleDto = ARTICLE_DTO;
    private String savedArticleUrl;

    @BeforeEach
    protected void setup() {
        super.setup();
        savedArticleUrl = getRedirectUrl(loginAndRequestWithDataWriter(POST, "/articles/write", parseArticle(articleDto)));
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
    void 로그인상태_게시글_작성_테스트(ArticleDto articleDto) {
        String redirectUrl = getRedirectUrl(loginAndRequestWithDataWriter(POST, "/articles/write", parseArticle(articleDto)));
        String responseBody = getResponseBody((loginAndRequestWriter(GET, redirectUrl)));

        ArticleDto escapedArticle = applyEscapeArticle(articleDto);
    
        containData(responseBody, escapedArticle);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("articlesStream")
    void 로그아웃상태_게시글_작성_테스트(ArticleDto articleDto) {
        String redirectUrl = getRedirectUrl(httpRequestWithData(POST, "/articles/write", parseArticle(articleDto)));
        assertEquals(redirectUrl, "/login");
    }

    static Stream<ArticleDto> articlesStream() {
        return Stream.of(
                new ArticleDto("article_en", "url_en", "contents_en"),
                new ArticleDto("목적의식 있는 연습을 통한 효과적 학습",
                        "https://t1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/application/user/5tdm/image/7OdaODfUPkDqDYIQKXk_ET3pfKo.jpeg",
                        "나는 우아한형제들에서 우아한테크코스 교육 과정을 진행하고 있다. 우테코를 설계하면서 고민스러웠던 부분 중의 하나는 '선발 과정을 어떻게 하면 의미 있는 시간으로 만들 것인가?'였다.")
        );
    }

    private ArticleDto applyEscapeArticle(ArticleDto articleDto) {
        return new ArticleDto(articleDto.getTitle(), articleDto.getCoverUrl(), StringEscapeUtils.escapeJava(articleDto.getContents()));
    }

    @Test
    void 로그아웃상태_게시글_페이지_조회_불가_리다이렉트() {
        checkLoginRedirect(savedArticleUrl);
    }

    @Test
    void 로그인상태_게시글_페이지_정상_조회() {
        String responseBody = getResponseBody(loginAndRequestWriter(GET, savedArticleUrl));
    
        containData(responseBody, articleDto);
    }

    @Test
    void 존재하지_않는_게시글_조회_에러() {
        String redirectUrl = getRedirectUrl(loginAndRequestWriter(GET, "/articles/0"));
        assertEquals(redirectUrl, "/");
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
        UserDto other = new UserDto("ab", "1@1.com", "1234asdf!A");
        userRepository.save(other.toUser());
        String redirectUrl = getRedirectUrl(loginAndRequest(GET, savedArticleUrl + "/edit", other));

        assertEquals(redirectUrl, "/");
    }

    @Test
    void 로그아웃상태_게시글_수정_요청() {
        ArticleDto editedArticleDto = new ArticleDto("new title", "new url", "new contents");

        String redirectUrl = getRedirectUrl(httpRequestWithData(PUT, savedArticleUrl, parseArticle(editedArticleDto)));
        assertEquals(redirectUrl, "/login");
    }

    @Test
    void 로그인상태_게시글_수정_요청() {
        ArticleDto editedArticleDto = new ArticleDto("new title", "new url", "new contents");

        String redirectUrl = getRedirectUrl(loginAndRequestWithDataWriter(PUT, savedArticleUrl, parseArticle(editedArticleDto)));
        String responseBody = getResponseBody(loginAndRequestWriter(GET, redirectUrl));
    
        containData(responseBody, editedArticleDto);
    }

    @Test
    void 로그인상태_다른_유저_게시글_수정_요청() {
        UserDto other = READER_DTO;
        userRepository.save(other.toUser());
        ArticleDto editedArticleDto = new ArticleDto("new title", "new url", "new contents");

        String redirectUrl = getRedirectUrl(loginAndRequestWithData(PUT, savedArticleUrl, parseArticle(editedArticleDto), other));

        assertEquals(redirectUrl, "/");
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

//        String redirectRemovedArticleUrl = getRedirectUrl(loginAndRequestWriter(GET, savedArticleUrl));
        loginAndRequest(GET, savedArticleUrl, HttpStatus.FORBIDDEN, savedUserDto)
                .consumeWith(response -> {
                    String result = new String(response.getResponseBody());
                    assertTrue(result.contains(DELETE_SUCCESS_MESSAGE));
                });
//        assertEquals(redirectRemovedArticleUrl, "/");
    }

    @Test
    void 로그인상태_다른_유저_게시글_삭제_요청() {
        UserDto other = new UserDto("ab", "1@1.com", "1234asdf!A");
        userRepository.save(other.toUser());

        String redirectUrl = getRedirectUrl(loginAndRequest(DELETE, savedArticleUrl, other));
        assertEquals(redirectUrl, "/");

        String responseBody = getResponseBody(loginAndRequestWriter(GET, savedArticleUrl));
        containData(responseBody, articleDto);
    }
    
    private void containData(String responseBody, ArticleDto articleDto) {
        assertThat(responseBody).contains(articleDto.getTitle());
        assertThat(responseBody).contains(articleDto.getCoverUrl());
        assertThat(responseBody).contains(articleDto.getContents());
    }
    
    private void checkLoginRedirect(String url) {
        String redirectUrl = getRedirectUrl(httpRequest(GET, url));
        assertEquals(redirectUrl, "/login");
    }
}
