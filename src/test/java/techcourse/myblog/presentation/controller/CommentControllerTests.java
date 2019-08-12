package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import techcourse.myblog.application.UserAssembler;
import techcourse.myblog.application.dto.CommentRequestDto;
import techcourse.myblog.application.dto.UserRequestDto;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentRepository;
import techcourse.myblog.presentation.controller.common.ControllerTestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpMethod.*;
import static techcourse.myblog.utils.ArticleTestObjects.ARTICLE_FEATURE;
import static techcourse.myblog.utils.CommentTestObjects.COMMENT_DTO;
import static techcourse.myblog.utils.CommentTestObjects.UPDATE_COMMENT_DTO;
import static techcourse.myblog.utils.UserTestObjects.READER_DTO;

class CommentControllerTests extends ControllerTestTemplate {
    @Autowired
    private CommentRepository commentRepository;

    private String savedArticleUrl;
    private CommentRequestDto commentRequestDto;

    @BeforeEach
    public void setup() {
        super.setup();
        savedArticleUrl = getRedirectUrl(loginAndRequestWithDataWriter(POST, "/articles/write", parseArticle(ARTICLE_FEATURE)));
        commentRequestDto = COMMENT_DTO;
    }

    @Test
    void 로그아웃_상태_댓글작성_리다이렉트() {
        String redirectUrl = getRedirectUrl(httpRequestWithData(POST, savedArticleUrl + "/comment", parser(commentRequestDto)));
        assertEquals("/login", redirectUrl);
    }

    @Test
    void 로그인_상태_댓글작성_성공() {
        loginAndRequestWithMonoData(POST, savedArticleUrl + "/comment", HttpStatus.OK, COMMENT_DTO, savedUserRequestDto)
                .jsonPath("$.contents").isEqualTo(COMMENT_DTO.getContents())
                .jsonPath("$.userId").isEqualTo(savedUser.getId())
                .jsonPath("$.userName").isEqualTo(savedUser.getName());
    }

    @Test
    void 로그아웃_상태_댓글삭제_리다이렉트() {
        String deleteUrl = getCommentUrl();
        String redirectUrl = getRedirectUrl(httpRequest(DELETE, deleteUrl));
        assertEquals("/login", redirectUrl);
    }

    @Test
    void 작성자_로그인_상태_댓글삭제_성공() {
        loginAndRequestWithMonoData(DELETE, getCommentUrl(), HttpStatus.OK, COMMENT_DTO, savedUserRequestDto)
                .consumeWith(response -> {
                    String result = new String(response.getResponseBody());
                    assertTrue(result.contains(DELETE_SUCCESS_MESSAGE));
                });
    }

    @Test
    void 작성자가_아닌_사용자_로그인_상태_댓글삭제_실패() {
        UserRequestDto other = READER_DTO;
        userRepository.save(UserAssembler.toEntity(other));

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
        loginAndRequestWithMonoData(PUT, getCommentUrl(), HttpStatus.OK, UPDATE_COMMENT_DTO, savedUserRequestDto)
                .jsonPath("$.contents").isEqualTo("new-contents");
    }

    @Test
    void 작성자가_아닌_사용자_로그인_상태_댓글수정_실패() {
        UserRequestDto other = READER_DTO;
        userRepository.save(UserAssembler.toEntity(other));

        loginAndRequestWithMonoData(PUT, getCommentUrl(), HttpStatus.FORBIDDEN, UPDATE_COMMENT_DTO, other)
                .jsonPath("$.message").isEqualTo(MISMATCH_COMMENT_AUTHOR_EXCEPTION_MESSAGE);

    }

    private String getCommentUrl() {
        loginAndRequestWithMonoData(POST, savedArticleUrl + "/comment", HttpStatus.OK, COMMENT_DTO, savedUserRequestDto);

        Comment comment = commentRepository.findAll().get(0);

        return "/articles/" + comment.getArticle().getId() + "/comment/" + comment.getId();
    }

    private MultiValueMap<String, String> parser(CommentRequestDto commentRequestDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("contents", commentRequestDto.getContents());
        return multiValueMap;
    }
}