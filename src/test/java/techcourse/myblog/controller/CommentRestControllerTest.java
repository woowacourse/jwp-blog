package techcourse.myblog.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

import io.restassured.response.Response;
import techcourse.myblog.dto.CommentResponse;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentRestControllerTest {
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
}
