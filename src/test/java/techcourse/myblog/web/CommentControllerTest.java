package techcourse.myblog.web;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.dto.CommentRequest;

import java.util.Objects;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTest {
    private static int FLAG_NO = 1;
    private User user;

    @Autowired
    private WebTestClient webTestClient;

    private String cookie;

    private Long articleId;
    private Long commentId;
    private String commentContents;

    @BeforeEach
    void setUp() {
        // 회원가입
        user = new User("Jason", FLAG_NO++ + "jason@woowahan.com", "Jason12!@");
        webTestClient.post().uri("/users")
                .body(fromFormData("name", user.getName())
                        .with("email", user.getEmail())
                        .with("password", user.getPassword())
                        .with("reconfirmPassword", user.getPassword()))
                .exchange()
                .expectStatus()
                .isFound();
        commentContents = "test Contents";

        cookie = getCookie(user.getEmail());

        articleId = creatArticleAndReturnArticleId(cookie);
        commentId = creatArticleAndReturnCommentId();
    }

    private Long creatArticleAndReturnArticleId(String cookie) {
        return Long.parseLong(webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", "titleTest")
                        .with("coverUrl", "coverUrlTest")
                        .with("contents", "contentsTest"))
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isFound()
                .returnResult(String.class)
                .getResponseHeaders()
                .getLocation().getPath().split("/")[2]);
    }

    private Long creatArticleAndReturnCommentId() {
        CommentRequest commentRequest = new CommentRequest(articleId, commentContents);
        // 댓글작성
        byte[] responseBody = webTestClient.post().uri("/comments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .header("Cookie", cookie)
                .body(Mono.just(commentRequest), CommentRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.contents").isEqualTo(commentContents)
                .jsonPath("$.article.id").isEqualTo(articleId)
                .jsonPath("$.commenter.email").isEqualTo(user.getEmail())
                .returnResult()
                .getResponseBody();

        JsonElement result = new JsonParser().parse(new String(Objects.requireNonNull(responseBody)));
        return result.getAsJsonObject().get("id").getAsLong();
    }

    @Test
    void 댓글작성자_댓글수정() {
        CommentRequest commentRequest = new CommentRequest(articleId, "미스타꼬");

        webTestClient.put().uri("/comments/" + commentId)
                .header("Cookie", cookie)
                // TODO: 2019-08-04 아래꺼 안 써도 잘 됨
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentRequest), CommentRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.contents").isEqualTo(commentRequest.getContents());
    }

    @Test
    void 댓글수정_작성자가_아닐_때_Index로_이동() {
        CommentRequest commentRequest = new CommentRequest(articleId, "updated comment");

        webTestClient.put().uri("/comments/" + commentId)
                .body(Mono.just(commentRequest), CommentRequest.class)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", ".*/;.*");
    }

    @Test
    void 댓글작성자_댓글삭제() {
        webTestClient.delete().uri("/comments/" + commentId)
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 작성자가_아닐_때_댓글삭제() {
        webTestClient.delete().uri("/comments/" + commentId)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/;.*");

    }

    private String getCookie(String email) {
        return webTestClient.post().uri("/login")
                .body(fromFormData("email", email)
                        .with("password", user.getPassword()))
                .exchange()
                .expectStatus()
                .isFound()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");
    }
}
