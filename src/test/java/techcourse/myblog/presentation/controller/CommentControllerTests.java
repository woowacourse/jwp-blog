package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentRepository;
import techcourse.myblog.presentation.controller.common.ControllerTestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpMethod.*;
import static techcourse.myblog.utils.ArticleTestObjects.ARTICLE_DTO;
import static techcourse.myblog.utils.CommentTestObjects.COMMENT_DTO;
import static techcourse.myblog.utils.CommentTestObjects.UPDATE_COMMENT_DTO;
import static techcourse.myblog.utils.UserTestObjects.READER_DTO;

class CommentControllerTests extends ControllerTestTemplate {
    private static final String MISMATCH_COMMENT_AUTHOR_EXCEPTION_MESSAGE = "댓글 작성자가 아닙니다.";
    private static final String DELETE_SUCCESS_MESSAGE = "삭제가 완료되었습니다.";

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
    void 로그아웃_상태_댓글작성_리다이렉트() {
        String redirectUrl = getRedirectUrl(httpRequestWithData(POST, savedArticleUrl + "/comment", parser(commentDto)));
        assertEquals("/login", redirectUrl);
    }

    @Test
    void 로그인_상태_댓글작성_성공() {
        loginAndRequestWithMonoData(POST, savedArticleUrl + "/comment", HttpStatus.CREATED, COMMENT_DTO, savedUserDto)
                .jsonPath("$.contents").isEqualTo("contents")
                .jsonPath("$.writer.id").isEqualTo(savedUser.getId())
                .jsonPath("$.writer.name").isEqualTo(savedUser.getName())
                .jsonPath("$.writer.email").isEqualTo(savedUser.getEmail());
    }

    @Test
    void 로그아웃_상태_댓글삭제_리다이렉트() {
        String deleteUrl = getCommentUrl();
        String redirectUrl = getRedirectUrl(httpRequest(DELETE, deleteUrl));
        assertEquals("/login", redirectUrl);
    }

    @Test
    void 작성자_로그인_상태_댓글삭제_성공() {
        loginAndRequestWithMonoData(DELETE, getCommentUrl(), HttpStatus.OK, COMMENT_DTO, savedUserDto)
                .consumeWith(response -> {
                    String result = new String(response.getResponseBody());
                    assertTrue(result.contains(DELETE_SUCCESS_MESSAGE));
                });
    }

    @Test
    void 작성자가_아닌_사용자_로그인_상태_댓글삭제_실패() {
        UserDto other = READER_DTO;
        userRepository.save(other.toUser());

        loginAndRequestWithMonoData(DELETE, getCommentUrl(), HttpStatus.FORBIDDEN, COMMENT_DTO, other)
                .jsonPath("$.message").isEqualTo(MISMATCH_COMMENT_AUTHOR_EXCEPTION_MESSAGE);
    }

    @Test
    void 로그아웃_상태_댓글수정_리다이렉트() {
        String commentUrl = getCommentUrl();
        String redirectUrl = getRedirectUrl(httpRequestWithData(PUT, commentUrl, parser(UPDATE_COMMENT_DTO)));
    
        assertEquals("/login", redirectUrl);
    }

    @Test
    void 작성자_로그인_상태_댓글수정_성공() {
        loginAndRequestWithMonoData(PUT, getCommentUrl(), HttpStatus.OK, UPDATE_COMMENT_DTO, savedUserDto)
                .jsonPath("$.contents").isEqualTo("new-contents");
    }

    @Test
    void 작성자가_아닌_사용자_로그인_상태_댓글수정_실패() {
        UserDto other = READER_DTO;
        userRepository.save(other.toUser());

        loginAndRequestWithMonoData(PUT, getCommentUrl(), HttpStatus.FORBIDDEN, UPDATE_COMMENT_DTO, other)
                .jsonPath("$.message").isEqualTo(MISMATCH_COMMENT_AUTHOR_EXCEPTION_MESSAGE);

    }

    private String getCommentUrl() {
        loginAndRequestWithMonoData(POST, savedArticleUrl + "/comment", HttpStatus.CREATED, COMMENT_DTO, savedUserDto);

        Comment comment = commentRepository.findAll().get(0);

        return "/articles/" + comment.getArticle().getId() + "/comment/" + comment.getId();
    }

    private MultiValueMap<String, String> parser(CommentDto commentDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("contents", commentDto.getContents());
        return multiValueMap;
    }
}