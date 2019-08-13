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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpMethod.*;
import static techcourse.myblog.utils.ArticleTestObjects.ARTICLE_DTO;
import static techcourse.myblog.utils.CommentTestObjects.*;
import static techcourse.myblog.utils.UserTestObjects.READER_DTO;

class CommentControllerTests extends ControllerTestTemplate {
    private static final String MISMATCH_COMMENT_AUTHOR_EXCEPTION_MESSAGE = "댓글 작성자가 아닙니다.";
    private static final String DELETE_SUCCESS_MESSAGE = "삭제가 완료되었습니다.";

    @Autowired
    private CommentRepository commentRepository;

    private String savedArticleUrl;
    private CommentDto commentDto;
    private Integer commentCount;

    @BeforeEach
    public void setup() {
        super.setup();
        savedArticleUrl = getRedirectUrl(loginAndRequestWithDataWriter(POST, "/articles/write", parseArticle(ARTICLE_DTO)));
        commentDto = COMMENT_DTO;
        commentCount = 0;
    }

    @Test
    void 댓글_조회() {
        getCommentUrl(COMMENT_DTO);
        getCommentUrl(COMMENT_DTO1);
        getCommentUrl(COMMENT_DTO2);

        loginAndRequest(GET, savedArticleUrl + "/comment", savedUserDto)
                .isOk()
                .expectBody()
                .jsonPath("$[0].contents").isEqualTo("contents")
                .jsonPath("$[0].writer.id").isEqualTo(savedUser.getId())
                .jsonPath("$[0].writer.name").isEqualTo(savedUser.getName())
                .jsonPath("$[0].writer.email").isEqualTo(savedUser.getEmail())

                .jsonPath("$[1].contents").isEqualTo("contents1")
                .jsonPath("$[1].writer.id").isEqualTo(savedUser.getId())
                .jsonPath("$[1].writer.name").isEqualTo(savedUser.getName())
                .jsonPath("$[1].writer.email").isEqualTo(savedUser.getEmail())

                .jsonPath("$[2].contents").isEqualTo("contents2")
                .jsonPath("$[2].writer.id").isEqualTo(savedUser.getId())
                .jsonPath("$[2].writer.name").isEqualTo(savedUser.getName())
                .jsonPath("$[2].writer.email").isEqualTo(savedUser.getEmail())
        ;
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
        String deleteUrl = getCommentUrl(COMMENT_DTO);
        String redirectUrl = getRedirectUrl(httpRequest(DELETE, deleteUrl));
        assertEquals("/login", redirectUrl);
    }

    @Test
    void 작성자_로그인_상태_댓글삭제_성공() {
        getCommentUrl(COMMENT_DTO);
        String commentUrl = getCommentUrl(COMMENT_DTO1);
        getCommentUrl(COMMENT_DTO2);
        loginAndRequestWithMonoData(DELETE, commentUrl, HttpStatus.OK, COMMENT_DTO, savedUserDto)
                .consumeWith(response -> {
                    String result = new String(response.getResponseBody());
                    assertTrue(result.contains(DELETE_SUCCESS_MESSAGE));
                });

        loginAndRequest(GET, savedArticleUrl + "/comment", savedUserDto)
                .isOk()
                .expectBody()
                .jsonPath("$[0].contents").isEqualTo(COMMENT_DTO.getContents())
                .jsonPath("$[0].writer.id").isEqualTo(savedUser.getId())
                .jsonPath("$[0].writer.name").isEqualTo(savedUser.getName())
                .jsonPath("$[0].writer.email").isEqualTo(savedUser.getEmail())

                .jsonPath("$[1].contents").isEqualTo(COMMENT_DTO2.getContents())
                .jsonPath("$[1].writer.id").isEqualTo(savedUser.getId())
                .jsonPath("$[1].writer.name").isEqualTo(savedUser.getName())
                .jsonPath("$[1].writer.email").isEqualTo(savedUser.getEmail())

                .jsonPath("$.size()").isEqualTo(commentCount - 1);

    }

    @Test
    void 작성자가_아닌_사용자_로그인_상태_댓글삭제_실패() {
        UserDto other = READER_DTO;
        userRepository.save(other.toUser());

        loginAndRequestWithMonoData(DELETE, getCommentUrl(COMMENT_DTO), HttpStatus.FORBIDDEN, COMMENT_DTO, other)
                .jsonPath("$.message").isEqualTo(MISMATCH_COMMENT_AUTHOR_EXCEPTION_MESSAGE);
    }

    @Test
    void 로그아웃_상태_댓글수정_리다이렉트() {
        String commentUrl = getCommentUrl(COMMENT_DTO);
        String redirectUrl = getRedirectUrl(httpRequestWithData(PUT, commentUrl, parser(UPDATE_COMMENT_DTO)));

        assertEquals("/login", redirectUrl);
    }

    @Test
    void 작성자_로그인_상태_댓글수정_성공() {
        loginAndRequestWithMonoData(PUT, getCommentUrl(COMMENT_DTO), HttpStatus.OK, UPDATE_COMMENT_DTO, savedUserDto)
                .jsonPath("$.contents").isEqualTo("new-contents");
    }

    @Test
    void 작성자가_아닌_사용자_로그인_상태_댓글수정_실패() {
        UserDto other = READER_DTO;
        userRepository.save(other.toUser());

        loginAndRequestWithMonoData(PUT, getCommentUrl(COMMENT_DTO), HttpStatus.FORBIDDEN, UPDATE_COMMENT_DTO, other)
                .jsonPath("$.message").isEqualTo(MISMATCH_COMMENT_AUTHOR_EXCEPTION_MESSAGE);

    }

    private String getCommentUrl(CommentDto commentDto) {
        loginAndRequestWithMonoData(POST, savedArticleUrl + "/comment", HttpStatus.CREATED, commentDto, savedUserDto);
        List<Comment> comments = commentRepository.findAll();
        Comment comment = comments.get(comments.size() - 1);
        commentCount++;

        return "/articles/" + comment.getArticle().getId() + "/comment/" + comment.getId();
    }

    private MultiValueMap<String, String> parser(CommentDto commentDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("contents", commentDto.getContents());
        return multiValueMap;
    }
}