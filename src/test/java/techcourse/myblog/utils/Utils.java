package techcourse.myblog.utils;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.controller.dto.ArticleDto;
import techcourse.myblog.controller.dto.LoginDto;
import techcourse.myblog.controller.dto.RequestCommentDto;
import techcourse.myblog.controller.dto.UserDto;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

public class Utils {
    public static void createUser(WebTestClient webTestClient, UserDto userDto) {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("userName", userDto.getUserName())
                        .with("email", userDto.getEmail())
                        .with("password", userDto.getPassword()))
                .exchange();
    }

    public static String createArticle(ArticleDto articleDto, String cookie, String baseUrl) {
        return given()
                .param("title", articleDto.getTitle())
                .param("coverUrl", articleDto.getCoverUrl())
                .param("contents", articleDto.getContents())
                .cookie(cookie)
                .post(baseUrl + "/articles")
                .getHeader("Location");
    }

    public static String createComment(RequestCommentDto requestCommentDto, String cookie, String baseUrl) {
        return given()
                .param("articleId", requestCommentDto.getArticleId())
                .param("contents", requestCommentDto.getContents())
                .cookie(cookie)
                .post(baseUrl + "/comments")
                .getHeader("Location");
    }

    public static void deleteUser(WebTestClient webTestClient, String cookie) {
        webTestClient.delete().uri("/users")
                .header("Cookie", cookie)
                .exchange();
    }

    public static void deleteArticle(WebTestClient webTestClient, String articleUrl) {
        webTestClient.delete().uri(articleUrl)
                .exchange();
    }

    public static String getLoginCookie(WebTestClient webTestClient, LoginDto loginDto) {
        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("email", loginDto.getEmail())
                        .with("password", loginDto.getPassword()))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }

    public static String getResponseBody(byte[] body) {
        return new String(body, StandardCharsets.UTF_8);
    }

    public static String getRedirectedLocationOf(WebTestClient.ResponseSpec responseSpec) {
        return responseSpec.expectHeader()
                .valueMatches("location", ".*")
                .returnResult(String.class)
                .getResponseHeaders()
                .get("Location").get(0);
    }
}
