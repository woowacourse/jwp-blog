package techcourse.myblog.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;
import techcourse.myblog.domain.Comment;
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

    private UserDto authorDto = savedUserDto;

    private CommentDto commentDto = new CommentDto("comment");
    private CommentDto updateCommentDto = new CommentDto("new comment");
    private String savedArticleUrl;
    private String commentUrl;
    private Comment comment;

    @BeforeEach
    public void setup() {
        super.setup();
        ArticleDto articleDto = new ArticleDto("title", "coverUrl", "contents");
        savedArticleUrl = getRedirectUrl(
                loginAndRequest(authorDto, POST, "/articles/write", parseArticle(articleDto)));
        commentUrl = savedArticleUrl + "/comments";
    }

    @Test
    public void 게시글의_댓글_요청() {
        //댓글 없을 때 댓글 요청
        webTestClient.get().uri(commentUrl)
                .cookie("JSESSIONID", getLoginSessionId(authorDto))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$").isEmpty();

        //댓글 두개 작성
        webTestClient.post().uri(commentUrl)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentDto), CommentDto.class)
                .cookie("JSESSIONID", getLoginSessionId(authorDto))
                .exchange()
                .returnResult(String.class);
        webTestClient.post().uri(commentUrl)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentDto), CommentDto.class)
                .cookie("JSESSIONID", getLoginSessionId(authorDto))
                .exchange()
                .returnResult(String.class);

        //댓글 요청
        webTestClient.get().uri(commentUrl)
                .cookie("JSESSIONID", getLoginSessionId(authorDto))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2);
    }

    @Test
    public void 게시글의_댓글_개수_요청() {
        //댓글 작성 전 0개
        webTestClient.get().uri(commentUrl + "/count")
                .cookie("JSESSIONID", getLoginSessionId(authorDto))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$").isEqualTo(0);

        //댓글 하나 작성
        webTestClient.post().uri(commentUrl)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentDto), CommentDto.class)
                .cookie("JSESSIONID", getLoginSessionId(authorDto))
                .exchange()
                .returnResult(String.class);

        //댓글 작성 후 1개
        webTestClient.get().uri(commentUrl + "/count")
                .cookie("JSESSIONID", getLoginSessionId(authorDto))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$").isEqualTo(1);

    }

    @Test
    public void 로그아웃_상태_댓글작성_리다이렉트() {
        String logoutWriteCommentRedirectUrl = getRedirectUrl(httpRequest(POST, commentUrl, parser(commentDto)));
        assertThat(logoutWriteCommentRedirectUrl.equals("/login"));
    }

    @Test
    public void 로그인_상태_댓글작성_성공() {
        webTestClient.post().uri(commentUrl)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentDto), CommentDto.class)
                .cookie("JSESSIONID", getLoginSessionId(authorDto))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.contents").isEqualTo(commentDto.getContents());
    }

    @Test
    void 로그아웃_상태_댓글삭제_리다이렉트() {
        String logoutDeleteCommentRedirectUrl = getRedirectUrl(httpRequest(DELETE, getCommentUrl()));

        assertThat(logoutDeleteCommentRedirectUrl.equals("/login")).isTrue();
    }

    @Test
    void 작성자_로그인_상태_댓글삭제_성공() {
        webTestClient.delete()
                .uri(getCommentUrl())
                .cookie("JSESSIONID", getLoginSessionId(authorDto))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$").isEqualTo(comment.getId());
    }

    @Test
    void 작성자가_아닌_사용자_로그인_상태_댓글삭제_실패() {
        String otherUserDeleteCommentRedirectUrl = getRedirectUrl(loginAndRequest(otherUserDto, DELETE, getCommentUrl()));

        assertThat(otherUserDeleteCommentRedirectUrl.equals("/")).isTrue();
    }

    @Test
    void 로그아웃_상태_댓글수정_리다이렉트() {
        String logoutUpdateCommentRedirectUrl = getRedirectUrl(httpRequest(PUT, getCommentUrl(), parser(updateCommentDto)));

        assertThat(logoutUpdateCommentRedirectUrl.equals("/login")).isTrue();
    }

    @Test
    void 작성자_로그인_상태_댓글수정_성공() {
        webTestClient.put().uri(getCommentUrl())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(updateCommentDto), CommentDto.class)
                .cookie("JSESSIONID", getLoginSessionId(authorDto))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.contents").isEqualTo(updateCommentDto.getContents());
    }

    @Test
    void 작성자가_아닌_사용자_로그인_상태_댓글수정_실패() {
        String otherUserUpdateCommentRedirectUrl = getRedirectUrl(
                webTestClient.put().uri(getCommentUrl())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .body(Mono.just(updateCommentDto), CommentDto.class)
                        .cookie("JSESSIONID", getLoginSessionId(otherUserDto))
                        .exchange()
                        .expectStatus()
        );

        assertThat(otherUserUpdateCommentRedirectUrl.equals("/")).isTrue();
    }

    private String getCommentUrl() {
        webTestClient.post().uri(commentUrl)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentDto), CommentDto.class)
                .cookie("JSESSIONID", getLoginSessionId(authorDto))
                .exchange();

        comment = commentRepository.findAll().get(0);

        return String.format("/articles/%d/comments/%d", comment.getArticle().getId(), comment.getId());
    }

    private MultiValueMap<String, String> parser(CommentDto commentDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("contents", commentDto.getContents());
        return multiValueMap;
    }
}