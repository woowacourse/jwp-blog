package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;
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
    public void 로그아웃_상태_댓글작성_리다이렉트() {
        String redirectUrl = getRedirectUrl(httpRequestWithData(POST, savedArticleUrl + "/comment", parser(commentDto)));
        assertEquals("/login", redirectUrl);
    }

    @Test
    public void 로그인_상태_댓글작성_성공() {
        webTestClient.post().uri(savedArticleUrl + "/comment")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .cookie("JSESSIONID", getLoginSessionId(savedUserDto))
                .body(Mono.just(COMMENT_DTO), CommentDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.contents").isEqualTo("contents")
                .jsonPath("$.writer.id").isEqualTo(savedUser.getId())
                .jsonPath("$.writer.name").isEqualTo(savedUser.getName())
                .jsonPath("$.writer.email").isEqualTo(savedUser.getEmail())
        ;
    }

    @Test
    void 로그아웃_상태_댓글삭제_리다이렉트() {
        String deleteUrl = getCommentUrl();
        String redirectUrl = getRedirectUrl(httpRequest(DELETE, deleteUrl));
        assertEquals("/login", redirectUrl);
    }

    @Test
    void 작성자_로그인_상태_댓글삭제_성공() {
        webTestClient.delete().uri(getCommentUrl())
                .cookie("JSESSIONID", getLoginSessionId(savedUserDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String result = new String(response.getResponseBody());
                    assertTrue(result.contains(DELETE_SUCCESS_MESSAGE));
                })
        ;
    }

    @Test
    void 작성자가_아닌_사용자_로그인_상태_댓글삭제_실패() {
        UserDto other = READER_DTO;
        userRepository.save(other.toUser());
        webTestClient.delete().uri(getCommentUrl())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .cookie("JSESSIONID", getLoginSessionId(other))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.message").isEqualTo(MISMATCH_COMMENT_AUTHOR_EXCEPTION_MESSAGE)
        ;

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
        webTestClient.put().uri(getCommentUrl())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .cookie("JSESSIONID", getLoginSessionId(savedUserDto))
                .body(Mono.just(UPDATE_COMMENT_DTO), CommentDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.contents").isEqualTo("new-contents")
        ;
    }

    @Test
    void 작성자가_아닌_사용자_로그인_상태_댓글수정_실패() {
        UserDto other = READER_DTO;
        userRepository.save(other.toUser());

        webTestClient.put().uri(getCommentUrl())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .cookie("JSESSIONID", getLoginSessionId(other))
                .body(Mono.just(UPDATE_COMMENT_DTO), CommentDto.class)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.message").isEqualTo(MISMATCH_COMMENT_AUTHOR_EXCEPTION_MESSAGE)
        ;
    }

    private String getCommentUrl() {
        webTestClient.post().uri(savedArticleUrl + "/comment")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .cookie("JSESSIONID", getLoginSessionId(savedUserDto))
                .body(Mono.just(COMMENT_DTO), CommentDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.contents").isEqualTo("contents")
                .jsonPath("$.writer.id").isEqualTo(savedUser.getId())
                .jsonPath("$.writer.name").isEqualTo(savedUser.getName())
                .jsonPath("$.writer.email").isEqualTo(savedUser.getEmail())
        ;

        Comment comment = commentRepository.findAll().get(0);

        return "/articles/" + comment.getArticle().getId() + "/comment/" + comment.getId();
    }

    private MultiValueMap<String, String> parser(CommentDto commentDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("contents", commentDto.getContents());
        return multiValueMap;
    }
}