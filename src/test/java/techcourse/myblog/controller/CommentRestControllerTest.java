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

        assertThat(comments.size()).isNotEqualTo(0);
    }

    @Test
    public void Comment_작성하기() {
        UserDto userDto = new UserDto("", "luffy@luffy.com", "12345678");
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

    @Test
    public void 로그인한_유저의_본인_Comment_수정하기() {
        UserDto userDto = new UserDto("", "luffy@luffy.com", "12345678");
        MultiValueMap<String, ResponseCookie> loginCookie = getLoginCookie(userDto);

        CommentRequest commentRequest = new CommentRequest(1L, "수정한 댓글입니다.");

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .cookie("JSESSIONID", Objects.requireNonNull(loginCookie.getFirst("JSESSIONID")).getValue())
                .body(commentRequest)
                .expect()
                .statusCode(200)
                .when()
                .put("http://localhost:" + port + "/comments/" + 1);

        Map<String, String> comment = response.getBody()
                .jsonPath()
                .get("");

        assertThat(comment.get("contents")).isEqualTo(commentRequest.getContents());
    }

    @Test
    public void 타인의_Comment_수정하기() {
        UserDto userDto = new UserDto("", "cony@cony.com", "12345678");
        MultiValueMap<String, ResponseCookie> loginCookie = getLoginCookie(userDto);

        CommentRequest commentRequest = new CommentRequest(1L, "수정한 댓글입니다.");

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .cookie("JSESSIONID", Objects.requireNonNull(loginCookie.getFirst("JSESSIONID")).getValue())
                .body(commentRequest)
                .when()
                .put("http://localhost:" + port + "/comments/" + 1);

        Map<String, String> responseBody = response.getBody()
                .jsonPath()
                .get("");

        assertThat(responseBody.get("message")).isEqualTo("자신이 작성한 글만 수정/삭제가 가능합니다.");
    }

    @Test
    public void 로그인한_유저의_본인_Comment_삭제하기() {
        UserDto userDto = new UserDto("", "luffy@luffy.com", "12345678");
        MultiValueMap<String, ResponseCookie> loginCookie = getLoginCookie(userDto);

        Response response = given()
                .cookie("JSESSIONID", Objects.requireNonNull(loginCookie.getFirst("JSESSIONID")).getValue())
                .expect()
                .statusCode(200)
                .when()
                .delete("http://localhost:" + port + "/comments/" + 2);

    }

    @Test
    public void 타인의_Comment_삭제하기() {
        UserDto userDto = new UserDto("", "cony@cony.com", "12345678");
        MultiValueMap<String, ResponseCookie> loginCookie = getLoginCookie(userDto);

        Response response = given()
                .cookie("JSESSIONID", Objects.requireNonNull(loginCookie.getFirst("JSESSIONID")).getValue())
                .when()
                .delete("http://localhost:" + port + "/comments/" + 1);

        Map<String, String> responseBody = response.getBody()
                .jsonPath()
                .get("");

        assertThat(responseBody.get("message")).isEqualTo("자신이 작성한 글만 수정/삭제가 가능합니다.");
    }
}
