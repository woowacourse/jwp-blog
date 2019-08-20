package techcourse.myblog.web;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import techcourse.myblog.service.dto.ArticleRequest;
import techcourse.myblog.service.dto.CommentRequest;
import techcourse.myblog.service.dto.UserLoginRequest;
import techcourse.myblog.service.dto.UserRequest;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

public class TestRequestHelper {
    private static final Logger log = LoggerFactory.getLogger(TestRequestHelper.class);

    public static ResponseSpec signup(WebTestClient webTestClient, UserRequest userRequest) {
        ResponseSpec rs = webTestClient.post().uri("/users")
                .body(fromFormData("name", userRequest.getName())
                        .with("email", userRequest.getEmail())
                        .with("password", userRequest.getPassword())
                        .with("reconfirmPassword", userRequest.getReconfirmPassword()))
                .exchange();

        rs.expectStatus().isFound();

        return rs;
    }

    public static ResponseSpec login(WebTestClient webTestClient, UserLoginRequest userLoginRequest) {
        ResponseSpec rs = webTestClient.post().uri("/login")
                .body(fromFormData("email", userLoginRequest.getEmail())
                        .with("password", userLoginRequest.getPassword()))
                .exchange();

        rs.expectStatus().isFound();

        return rs;
    }

    public static String getCookie(WebTestClient webTestClient, UserLoginRequest userLoginRequest) {
        return login(webTestClient, userLoginRequest)
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");
    }

    public static Long createArticle(WebTestClient webTestClient, String cookie, ArticleRequest articleRequest) {
        ResponseSpec rs = requestNewArticle(webTestClient, cookie, articleRequest);

        return Long.parseLong(getRedirectUri(rs).split("/")[2]);
    }

    public static String getRedirectUri(ResponseSpec rs) {
        return rs.returnResult(String.class)
                .getResponseHeaders()
                .getLocation()
                .getPath();
    }

    private static ResponseSpec requestNewArticle(WebTestClient webTestClient, String cookie, ArticleRequest articleRequest) {
        ResponseSpec rs = webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", articleRequest.getTitle())
                        .with("coverUrl", articleRequest.getCoverUrl())
                        .with("contents", articleRequest.getContents()))
                .header("Cookie", cookie)
                .exchange();

        rs.expectStatus().isFound();

        return rs;
    }

    public static Long createComment(WebTestClient webTestClient, String cookie, CommentRequest commentRequest) {
        ResponseSpec rs = requestNewComment(webTestClient, cookie, commentRequest);

        String body = new String(
                rs.expectBody()
                        .jsonPath("$.contents").isEqualTo(commentRequest.getContents())
                        .returnResult()
                .getResponseBody());

        log.debug("body: {}", body);
        System.out.println(body);

        JsonElement result = new JsonParser().parse(body);
        return result.getAsJsonObject().get("id").getAsLong();
    }

    public static ResponseSpec requestNewComment(WebTestClient webTestClient, String cookie, CommentRequest commentRequest) {
        ResponseSpec rs = webTestClient.post().uri("/api/comments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .header("Cookie", cookie)
                .body(Mono.just(commentRequest), CommentRequest.class)
                .exchange();

        rs.expectStatus().isOk();

        return rs;
    }
}
