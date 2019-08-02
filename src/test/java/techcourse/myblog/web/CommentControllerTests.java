package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.Comment;
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

        webTestClient.post().uri("/comment/writing")
                .body(BodyInserters
                        .fromFormData("comment", "댓글")
                        .with("articleId", articleId))
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/articles/.*");
    }

    @Test
    void saveComment() {
        webTestClient.post().uri("/comment/writing")
                .body(BodyInserters
                        .fromFormData("comment", "새 댓글")
                        .with("articleId", articleId))
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/articles/.*");
    }

    @Test
    void editComment() {
        Comment comment = commentService.findByArticleId(Long.parseLong(articleId)).get(0);
        Long commentId = comment.getId();

        webTestClient.put().uri("/comment/" + commentId)
                .body(BodyInserters
                        .fromFormData("editedContents", "수정된댓글"))
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void editComment_다른_user가_수정_요청한_경우() {
        UserSaveRequestDto userSaveRequestDto = new UserSaveRequestDto("사용자", "wrongCommentUser@test.com", "password1!");

        LoginTestUtil.signUp(webTestClient, userSaveRequestDto);
        String jSessionIdByAnotherUser = LoginTestUtil.getJSessionId(webTestClient, userSaveRequestDto);

        Comment comment = commentService.findByArticleId(Long.parseLong(articleId)).get(0);
        Long commentId = comment.getId();

        webTestClient.put().uri("/comment/" + commentId)
                .body(BodyInserters
                        .fromFormData("editedContents", "수정된댓글"))
                .cookie("JSESSIONID", jSessionIdByAnotherUser)
                .exchange()
                .expectStatus().isOk();

        LoginTestUtil.deleteUser(webTestClient, userSaveRequestDto);
    }

    @AfterEach
    void tearDown_comment_delete() {
        webTestClient.delete().uri("/articles/" + articleId)
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection();

        LoginTestUtil.deleteUser(webTestClient, userSaveRequestDto);
    }
}