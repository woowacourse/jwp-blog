package techcourse.myblog.utils;

import io.restassured.http.ContentType;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import techcourse.myblog.controller.dto.ArticleDto;
import techcourse.myblog.controller.dto.RequestCommentDto;
import techcourse.myblog.controller.dto.LoginDto;
import techcourse.myblog.controller.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

public class Utils {
    public static String getResponseBody(byte[] body) {
        return new String(body, StandardCharsets.UTF_8);
    }

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

    public static void deleteUser(WebTestClient webTestClient, String cookie) {
        webTestClient.delete().uri("/users")
                .header("Cookie", cookie)
                .exchange();
    }

    public static void createComment(RequestCommentDto requestCommentDto, String cookie, WebTestClient webTestClient) {
        webTestClient.post().uri("/comments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .header("Cookie", cookie)
                .body(Mono.just(requestCommentDto), RequestCommentDto.class)
                .exchange();
    }

    public static String getId(String url) {
        List<String> list = Arrays.asList(url.split("/"));
        return list.get(list.size() - 1);
    }
}
