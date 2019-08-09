package techcourse.myblog.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import techcourse.myblog.dto.CommentRequest;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

class CommentApiControllerTest extends AbstractControllerTest {
    @LocalServerPort
    private int port;

    @Test
    public void 댓글을_잘_불러오는지_확인한다() {
        when().
                get("http://localhost:" + port + "/articles/1/comments").
        then().
                statusCode(200).
                body(containsString("댓글입니다"));
    }

    @Test
    public void 댓글을_잘_작성하는지_확인한다() {
        CommentRequest commentRequest = new CommentRequest("새로운 댓글입니다");

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                sessionId(extractJSessionId(login(user1))).
                body(commentRequest).
        when().
                post("http://localhost:" + port + "/articles/1/comments").
        then().
                statusCode(200).
                body("contents", equalTo(commentRequest.getContents()));
    }

    @Test
    public void 댓글을_잘_수정하는지_확인한다() {
        CommentRequest commentRequest = new CommentRequest("수정한 댓글입니다");

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                sessionId(extractJSessionId(login(user1))).
                body(commentRequest).
        when().
                put("http://localhost:" + port + "/articles/1/comments/1").
        then().
                statusCode(200).
                body("contents", equalTo(commentRequest.getContents()));
    }

    @Test
    public void 작성자가_아닌_사람은_댓글을_수정할_수_없다() {
        CommentRequest commentRequest = new CommentRequest("수정한 댓글입니다");

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                sessionId(extractJSessionId(login(user2))).
                body(commentRequest).
        when().
                put("http://localhost:" + port + "/articles/1/comments/1").
        then().
                statusCode(302);
    }

    @Test
    public void 댓글을_잘_삭제하는지_확인한다() {
        given().
                sessionId(extractJSessionId(login(user1))).
        when().
                delete("http://localhost:" + port + "/articles/1/comments/3").
        then().
                statusCode(200);
    }

    @Test
    public void 작성자가_아닌_사람은_댓글을_삭제할_수_없다() {
        given().
                sessionId(extractJSessionId(login(user2))).
        when().
                delete("http://localhost:" + port + "/articles/1/comments/1").
        then().
                statusCode(302);
    }
}