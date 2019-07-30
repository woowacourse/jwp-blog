package techcourse.myblog.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.repository.ArticleRepository;
import techcourse.myblog.domain.repository.CommentRepository;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.web.controller.common.ControllerTestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.*;

class CommentControllerTests extends ControllerTestTemplate {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    private String savedArticleUrl;
    private CommentDto commentDto;

    @BeforeEach
    public void setup() {
        super.setup();
        savedArticleUrl = getRedirectUrl(loginAndRequest(POST, "/articles/write",
                parseArticle(new ArticleDto("title", "url", "content"))));
        commentDto = new CommentDto("comment", null, null);
    }

    @Test
    public void 로그아웃_상태_댓글작성_리다이렉트() {
        String redirectUrl = getRedirectUrl(httpRequest(POST, savedArticleUrl + "/comment", parser(commentDto)));
        assertThat(redirectUrl.equals("/login"));
    }

    @Test
    public void 로그인_상태_댓글작성_성공() {
        String redirectUrl = getRedirectUrl(loginAndRequest(POST, savedArticleUrl + "/comment", parser(commentDto)));
        String responseBody = getResponseBody(loginAndRequest(GET, redirectUrl));

        assertThat(responseBody.contains(commentDto.getContents())).isTrue();
    }

    @Test
    void 로그아웃_상태_댓글삭제_리다이렉트() {
        String deleteUrl = getCommentUrl();
        String redirectUrl = getRedirectUrl(httpRequest(DELETE, deleteUrl));

        assertThat(redirectUrl.equals("/login")).isTrue();
    }

    @Test
    void 작성자_로그인_상태_댓글삭제_성공() {
        String deleteUrl = getCommentUrl();
        String redirectUrl = getRedirectUrl(loginAndRequest(DELETE, deleteUrl));

        assertThat(redirectUrl.equals(savedArticleUrl)).isTrue();
    }

    @Test
    void 작성자가_아닌_사용자_로그인_상태_댓글삭제_실패() {
        UserDto other = new UserDto("ab", "1@1.com", "1234asdf!A");
        userRepository.save(other.toUser());
        String deleteUrl = getCommentUrl();
        String redirectUrl = getRedirectUrl(loginAndRequest(DELETE, deleteUrl, other));

        assertThat(redirectUrl.equals("/")).isTrue();
    }

    @Test
    void 로그아웃_상태_댓글수정_리다이렉트() {
        CommentDto commentDto = new CommentDto("new comment", null, null);
        String modifyUrl = getCommentUrl();
        String redirectUrl = getRedirectUrl(httpRequest(PUT, modifyUrl, parser(commentDto)));

        assertThat(redirectUrl.equals("/login")).isTrue();
    }

    @Test
    void 작성자_로그인_상태_댓글수정_성공() {
        CommentDto commentDto = new CommentDto("new comment", null, null);
        String modifyUrl = getCommentUrl();
        String redirectUrl = getRedirectUrl(loginAndRequest(PUT, modifyUrl, parser(commentDto)));
        String responseBody = getResponseBody(loginAndRequest(GET, redirectUrl));

        assertThat(responseBody.contains(commentDto.getContents())).isTrue();
    }

    @Test
    void 작성자가_아닌_사용자_로그인_상태_댓글수정_실패() {
        CommentDto commentDto = new CommentDto("new comment", null, null);
        UserDto other = new UserDto("ab", "1@1.com", "1234asdf!A");
        userRepository.save(other.toUser());
        String modifyUrl = getCommentUrl();
        String redirectUrl = getRedirectUrl(loginAndRequest(PUT, modifyUrl, parser(commentDto), other));

        assertThat(redirectUrl.equals("/")).isTrue();
    }

    private String getCommentUrl() {
        loginAndRequest(POST, savedArticleUrl + "/comment", parser(commentDto));

        Comment comment = commentRepository.findAll().get(0);

        return "/articles/" + comment.getArticle().getId() + "/comment/" + comment.getId();
    }

    private MultiValueMap<String, String> parser(CommentDto commentDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("contents", commentDto.getContents());
        return multiValueMap;
    }
}