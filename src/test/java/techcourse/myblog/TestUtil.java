package techcourse.myblog;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

public class TestUtil {
    public static void signUp(WebTestClient webTestClient, String email, String name, String password) {
        webTestClient.post().uri("/users")
                .body(BodyInserters.fromFormData("name", name)
                        .with("email", email)
                        .with("password", password))
                .exchange();
    }
    public static String getCookie(WebTestClient webTestClient, String email, String password){
        return  webTestClient.post().uri("login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }
    public static void createArticle(WebTestClient webTestClient, String cookie, String title, String coverUrl, String contents){
          webTestClient.post().uri("/articles")
                .header("cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("title", title)
                        .with("contents", contents)
                        .with("coverUrl", coverUrl))
                .exchange();
    }

    protected String getRedirectUrl(StatusAssertions statusAssertions) {
        return statusAssertions
                .isFound()
                .expectBody()
                .returnResult()
                .getResponseHeaders()
                .getLocation().getPath();
    }



}
