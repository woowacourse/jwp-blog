package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.dto.CommentEditDto;
import techcourse.myblog.dto.CommentSaveRequestDto;
import techcourse.myblog.dto.UserSaveRequestDto;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.testutil.LoginTestUtil;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class CommentControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CommentService commentService;

    private UserSaveRequestDto userSaveRequestDto;
    private String jSessionId;
    private String articleId;

    @BeforeEach
    void setUp_comment_save() {
        userSaveRequestDto = new UserSaveRequestDto("테스트", "comment@test.com", "password1!");

        LoginTestUtil.signUp(webTestClient, userSaveRequestDto);
        jSessionId = LoginTestUtil.getJSessionId(webTestClient, userSaveRequestDto);

        String[] strings = webTestClient.post().uri("/articles")
                .body(BodyInserters
                        .fromFormData("title", "제목")
                        .with("coverUrl", "주소")
                        .with("contents", "내용"))
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .returnResult(String.class)
                .getResponseHeaders()
                .get("Location").get(0)
                .split("/");
        articleId = strings[strings.length - 1];

        CommentSaveRequestDto commentSaveRequestDto = new CommentSaveRequestDto("댓글", Long.parseLong(articleId));

        webTestClient.post().uri("/comment/writing")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentSaveRequestDto), CommentSaveRequestDto.class)
                .cookie("JSESSIONID", jSessionId)
                .exchange();
    }

    @Test
    void saveComment() {
        CommentSaveRequestDto commentSaveRequestDto = new CommentSaveRequestDto("새 댓글", Long.parseLong(articleId));

        webTestClient.post().uri("/comment/writing")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentSaveRequestDto), CommentSaveRequestDto.class)
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.contents").isEqualTo("새 댓글")
                .jsonPath("$.articleId").isEqualTo(Long.parseLong(articleId))
                .jsonPath("$.userName").isEqualTo(userSaveRequestDto.getName());

    }

    @Test
    void editComment() {
        Comment comment = commentService.findByArticleId(Long.parseLong(articleId)).get(0);
        Long commentId = comment.getId();

        CommentEditDto commentEditDto = new CommentEditDto();
        commentEditDto.setEditedContents("수정된댓글");

        webTestClient.put().uri("/comment/" + commentId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentEditDto), CommentEditDto.class)
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.editedContents").isEqualTo("수정된댓글");
    }

    @Test
    void editComment_다른_user가_수정_요청한_경우() {
        UserSaveRequestDto userSaveRequestDto = new UserSaveRequestDto("사용자", "wrongCommentUser@test.com", "password1!");

        LoginTestUtil.signUp(webTestClient, userSaveRequestDto);
        String jSessionIdByAnotherUser = LoginTestUtil.getJSessionId(webTestClient, userSaveRequestDto);

        Comment comment = commentService.findByArticleId(Long.parseLong(articleId)).get(0);
        Long commentId = comment.getId();

        CommentEditDto commentEditDto = new CommentEditDto();
        commentEditDto.setEditedContents("수정된댓글");

        webTestClient.put().uri("/comment/" + commentId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentEditDto), CommentEditDto.class)
                .cookie("JSESSIONID", jSessionIdByAnotherUser)
                .exchange()
                .expectStatus().isOk();

        LoginTestUtil.deleteUser(webTestClient, userSaveRequestDto);
    }

    @AfterEach
    void tearDown_comment_delete() {
        webTestClient.delete().uri("/articles/" + articleId)
                .cookie("JSESSIONID", jSessionId)
                .exchange();

        LoginTestUtil.deleteUser(webTestClient, userSaveRequestDto);
    }
}