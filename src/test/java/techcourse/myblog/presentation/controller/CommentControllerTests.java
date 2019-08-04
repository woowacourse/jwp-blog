package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentRepository;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.presentation.controller.common.ControllerTestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpMethod.*;
import static techcourse.myblog.utils.ArticleTestObjects.ARTICLE_DTO;
import static techcourse.myblog.utils.CommentTestObjects.COMMENT_DTO;
import static techcourse.myblog.utils.CommentTestObjects.UPDATE_COMMENT_DTO;
import static techcourse.myblog.utils.UserTestObjects.READER_DTO;

class CommentControllerTests extends ControllerTestTemplate {
    @Autowired
    private CommentRepository commentRepository;

    private String savedArticleUrl;
    private CommentDto commentDto;

    @BeforeEach
    public void setup() {
        super.setup();
        savedArticleUrl = getRedirectUrl(loginAndRequestWithDataWriter(POST, "/articles/write", parseArticle(ARTICLE_DTO)));
        commentDto = COMMENT_DTO;
    }

    @Test
    public void 로그아웃_상태_댓글작성_리다이렉트() {
        String redirectUrl = getRedirectUrl(httpRequestWithData(POST, savedArticleUrl + "/comment", parser(commentDto)));
        assertEquals("/login", redirectUrl);
    }

    @Test
    public void 로그인_상태_댓글작성_성공() {
        String redirectUrl = getRedirectUrl(loginAndRequestWithDataWriter(POST, savedArticleUrl + "/comment", parser(commentDto)));
        String responseBody = getResponseBody(loginAndRequestWriter(GET, redirectUrl));
        assertTrue(responseBody.contains(commentDto.getContents()));
    }

    @Test
    void 로그아웃_상태_댓글삭제_리다이렉트() {
        String deleteUrl = getCommentUrl();
        String redirectUrl = getRedirectUrl(httpRequest(DELETE, deleteUrl));
        assertEquals("/login", redirectUrl);
    }

    @Test
    void 작성자_로그인_상태_댓글삭제_성공() {
        String commentUrl = getCommentUrl();
        String redirectUrl = getRedirectUrl(loginAndRequestWriter(DELETE, commentUrl));
    
        assertEquals(redirectUrl, savedArticleUrl);
    }

    @Test
    void 작성자가_아닌_사용자_로그인_상태_댓글삭제_실패() {
        UserDto other = READER_DTO;
        userRepository.save(other.toUser());
        String commentUrl = getCommentUrl();
        String redirectUrl = getRedirectUrl(loginAndRequest(DELETE, commentUrl, other));
    
        assertEquals("/", redirectUrl);
    }

    @Test
    void 로그아웃_상태_댓글수정_리다이렉트() {
        CommentDto commentDto = UPDATE_COMMENT_DTO;
        String commentUrl = getCommentUrl();
        String redirectUrl = getRedirectUrl(httpRequestWithData(PUT, commentUrl, parser(commentDto)));
    
        assertEquals("/login", redirectUrl);
    }

    @Test
    void 작성자_로그인_상태_댓글수정_성공() {
        CommentDto commentDto = UPDATE_COMMENT_DTO;
        
        String commentUrl = getCommentUrl();
        String redirectUrl = getRedirectUrl(loginAndRequestWithDataWriter(PUT, commentUrl, parser(commentDto)));
        String responseBody = getResponseBody(loginAndRequestWriter(GET, redirectUrl));
    
        assertTrue(responseBody.contains(commentDto.getContents()));
    }

    @Test
    void 작성자가_아닌_사용자_로그인_상태_댓글수정_실패() {
        UserDto other = READER_DTO;
        userRepository.save(other.toUser());
        
        String commentUrl = getCommentUrl();
        String redirectUrl = getRedirectUrl(loginAndRequestWithData(PUT, commentUrl, parser(UPDATE_COMMENT_DTO), other));
    
        assertEquals("/", redirectUrl);
    }

    private String getCommentUrl() {
        loginAndRequestWithDataWriter(POST, savedArticleUrl + "/comment", parser(commentDto));

        Comment comment = commentRepository.findAll().get(0);

        return "/articles/" + comment.getArticle().getId() + "/comment/" + comment.getId();
    }

    private MultiValueMap<String, String> parser(CommentDto commentDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("contents", commentDto.getContents());
        return multiValueMap;
    }
}