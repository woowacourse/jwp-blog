package techcourse.myblog.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.restassured.response.Response;
import techcourse.myblog.controller.test.WebClientGenerator;
import techcourse.myblog.dto.CommentRequest;
import techcourse.myblog.dto.CommentResponse;
import techcourse.myblog.dto.UserDto;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentRestControllerTest extends WebClientGenerator {
    @LocalServerPort
    private int port;

    @Test
    public void Comment_전체_목록_불러오기() {
        Response response = given()
                .queryParam("articleId", 1)
                .expect()
                .statusCode(200)
                .when()
                .get("http://localhost:" + port + "/comments");

        List<CommentResponse> comments = response.getBody()
                .jsonPath()
                .get("");

        assertThat(comments.size()).isEqualTo(1);
    }

    @Test
    public void Comment_작성하기() {
        UserDto userDto = new UserDto("", "luffy@luffy.com", "@Password12");
        MultiValueMap<String, ResponseCookie> loginCookie = getLoginCookie(userDto);

        CommentRequest commentRequest = new CommentRequest(1L, "새로운 댓글입니다.");

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .cookie("JSESSIONID", Objects.requireNonNull(loginCookie.getFirst("JSESSIONID")).getValue())
                .body(commentRequest)
                .expect()
                .statusCode(200)
                .when()
                .post("http://localhost:" + port + "/comments");

        Map<String, String> comment = response.getBody()
                .jsonPath()
                .get("");

        assertThat(comment.get("contents")).isEqualTo(commentRequest.getContents());
    }
}
